
import { useState } from "react";
function UpdateCategory() {
	const [formData, setFormData] = useState({
		id: "",
		name: "",
		promoted: false
	});
	// Handle form input changes
	const handleChange = (e) => {
		const { name, type, value, checked } = e.target;
		setFormData((prev) => ({
			...prev,
			[name]: type === "checkbox" ? checked : value, // Handle checkbox separately
		}));
	};

	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload
		const reqBody = {
			name: formData.name,
			promoted: formData.promoted
		}
		try {
			// Send a POST request to the server
			const response = await fetch(`http://localhost:3001/api/categories/${formData.id}`, {
				method: "PATCH",
				headers: {
					"Content-Type": "application/json", // Specify JSON format
					Authorization: `Bearer ${sessionStorage.getItem("token")}`
				},
				body: JSON.stringify(reqBody)
			});

			if (response.ok) {
				alert("Category Updated!");
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
			<h1>Update Category</h1>
			<form className="login-form" onSubmit={handleSubmit} >
				<input type="text"
					name="id"
					placeholder="Category Id"
					required
					value={formData.id}
					onChange={handleChange}
				/>
				<input type="text"
					name="name"
					placeholder="Category Name"
					required
					value={formData.name}
					onChange={handleChange}
				/>
				<label>
					Promoted Category?
					<input type="checkbox" name="promoted"
					value={formData.promoted}
					onChange={handleChange} />

				</label>
				<button type="submit" >Update</button>
			</form>
		</div>
	);
}
export default UpdateCategory;