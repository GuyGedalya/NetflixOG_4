import { useState } from "react";

function DeleteMovie() {
	const [formData, setFormData] = useState({
		id: ""
	});

	// Handle form input changes
	const handleChange = (e) => {
		const { name, value } = e.target;
		setFormData((prev) => ({
			...prev,
			[name]: value,
		}));
	};


	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload
		try {
			// Send a POST request to the server
			const response = await fetch(`http://localhost:3001/api/movies/${formData.id}`, {
				method: "DELETE",
				headers: {
					"Content-Type": "application/json", // Specify JSON format
					Authorization: `Bearer ${sessionStorage.getItem("token")}`
				}
			});

			if (response.ok) {
				alert("Movie Deleted!");
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
		<div className="text-container10">
			<h1>Delete Movie</h1>
			<form className="login-form" onSubmit={handleSubmit} >
				<input type="text"
					name="id"
					placeholder="Movie Id"
					required
					value={formData.id}
					onChange={handleChange}
				/>
				<button type="submit" >Delete</button>
			</form>
		</div>
	);
}
export default DeleteMovie;