import './SignIn.css';
import { Link } from 'react-router-dom';
import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';

function SignIn() {
	// State to store form data
	const [formData, setFormData] = useState({
		UserName: "",
		Password: ""
	});
	// Handle form input changes
	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData((prev) => ({ ...prev, [name]: value }));
	};

	const navigate = useNavigate();
	
	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload
		const reqBody = {
			UserName: formData.UserName,
			Password: formData.Password
		}

		try {
			// Send a POST request to the server
			const response = await fetch("http://localhost:3001/api/tokens", {
				method: "POST",
				headers: {
					"Content-Type": "application/json" // Specify JSON format
				},
				body: JSON.stringify(reqBody)
			});

			if (response.ok) {
				const data = await response.json();
				sessionStorage.setItem("token", data.token);
				sessionStorage.setItem("user" ,JSON.stringify(data.user));
				alert("Login successful!");
				navigate('/home')
			} else {
				// Parse the error message from the server response
				const errorData = await response.json();
				alert(`Error: ${errorData.error}`); // Show the error as an alert
			}
		} catch (error) {
			console.error("Error:", error);
			alert("An error occurred. Please try again later.");
		}
	};

	return (
		<>
			<div className="logo-container">
				<img src="/images/favicon.ico" alt="NOG Logo" className="logo"></img>
			</div>
			<div className="text-container1">
				<h1>Sign In</h1>
				<form className="login-form" onSubmit={handleSubmit}>
					<input type='text'
						name="UserName" 
						placeholder="UserName" 
						required 
						value={formData.UserName} 
						onChange={handleChange}/>
					<input type="password" 
						id="password" 
						name="Password" 
						placeholder="Password" 
						required 
						value={formData.Password} 
						onChange={handleChange} />
					<button type="submit" >Sign In</button>
				</form>
				<p>New to NOG? <Link to="/signUp">Sign up now.</Link></p>
			</div>
		</>
	);
}

export default SignIn;
