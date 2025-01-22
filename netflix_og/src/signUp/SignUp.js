import './SignUp.css';
import React, { useState } from "react";
import { useNavigate } from 'react-router-dom';

function SignUp() {
	// State to store form data
	const [formData, setFormData] = useState({
		UserName: "",
		Email: "",
		Password: "",
		Phone: "",
		ProfileImage: null,
	});
	// Handle form input changes
	const handleChange = (e) => {
		const { name, value, files } = e.target;
		if (name === "ProfileImage") {
			setFormData((prev) => ({ ...prev, ProfileImage: files[0] }));
		} else {
			setFormData((prev) => ({ ...prev, [name]: value }));
		}
	};

	const navigate = useNavigate();
	// Handle form submission
	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload

		// Prepare form data to send to the server
		const formData1 = new FormData();
		formData1.append('UserName', formData.UserName);
		formData1.append('Email', formData.Email);
		formData1.append('Password', formData.Password);
		formData1.append('Phone', formData.Phone);
		formData1.append('ProfileImage', formData.ProfileImage);

		try {
			// Send a POST request to the server
			const response = await fetch("http://localhost:3001/api/users", {
				method: "POST",
				body: formData1
			});
			if (response.ok) {
				alert("Sign up successful! You can now log in to your account.");
				navigate('/signIn')
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
			<div className="text-container">
				<h1>Sign Up</h1>
				<form className="login-form" onSubmit={handleSubmit}>
					<input type="text"
						name="UserName"
						placeholder="UserName"
						required
						value={formData.UserName} onChange={handleChange} />
					<input type="email"
						name="Email"
						placeholder="Email"
						required
						value={formData.Email} onChange={handleChange} />
					<input type="password"
						name="Password"
						placeholder="Password"
						required
						pattern="(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}"
						title="Password must be at least 8 characters. Password must include at least one letter and one number."
						value={formData.Password} onChange={handleChange} />
					<input type="tel"
						name="Phone"
						placeholder="Phone"
						required
						pattern="^05\d{8}$"
						title="Phone number must start with 05 and be 10 digits."
						value={formData.Phone} onChange={handleChange} />
					<input type="file"
						name="ProfileImage"
						accept="image/*"
						onChange={handleChange} />
					<button type="submit" >Sign Up</button>
				</form>
			</div>
		</>
	);
}

export default SignUp;