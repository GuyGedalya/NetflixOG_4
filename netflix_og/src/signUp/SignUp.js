import React, { useState } from "react";

function SignUp(){
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
	
	// Handle form submission
	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload
	
		// Prepare form data to send to the server
		const data = {
			UserName: formData.UserName,
			Email: formData.Email,
			Password: formData.Password,
			Phone: formData.Phone
		}

//		if (formData.ProfileImage) {
//		  data.append("ProfileImage", formData.ProfileImage);
//		}
		try {
			// Send a POST request to the server
			const response = await fetch("http://localhost:3001/api/users", {
			  method: "POST",
			  headers: {
				"Content-Type": "application/json",
			  },
			  body: JSON.stringify(data),
			});
			
			if (response.ok) {
			  alert("Sign up successful!");
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
	return(
		<> 
			<div className="logo-container">
				<img src="/images/favicon.ico" alt="NOG Logo" className="logo"></img>
			</div>
			<div className="container">
				<h1>Sign Up</h1>
				<form className="login-form" onSubmit={handleSubmit}>
					<input type="text" 
						name="UserName" 
						placeholder="UserName" 
						required
						value={formData.UserName} onChange={handleChange}	/>
					<input type="email" 
						name="Email" 
						placeholder="Email" 
						required 
						value={formData.Email} onChange={handleChange}/>
					<input type="password" 
						name="Password" 
						placeholder="Password" 
						required
						value={formData.Password} onChange={handleChange}/>
					<input type="tel" 
						name="Phone" 
						placeholder="+50" 
						required 
         				pattern="^05\d{8}$"
						value={formData.Phone} onChange={handleChange}/>
					<input type="file" 
						name="ProfileImage" 
						accept="image/*"
						value={formData.ProfileImage} onChange={handleChange}/>
					<button type="submit" >Sign Up</button>
				</form>
			</div>
	  </>
	);
}




export default SignUp;