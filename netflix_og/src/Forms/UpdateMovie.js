import { useState } from "react";

function UpdateMovie() {
	// State to store form data
	const [formData, setFormData] = useState({
		id: "",
		Title: "",
		ReleaseDate: "",
		Categories: [],
		Film: '',
		MovieImage: '',

	});
	const [inputValue, setInputValue] = useState(""); // For the input field

	// Handle form input changes
	const handleChange = (e) => {
		const { name, value, files } = e.target;
		if (name === "MovieImage") {
			setFormData((prev) => ({ ...prev, MovieImage: files[0] }));
		} else if (name === "Film") {
			setFormData((prev) => ({ ...prev, Film: files[0] }));
		} else {
			setFormData((prev) => ({ ...prev, [name]: value }));
		}
	};
	// Adding Category to the list when pressing Enter
	const handleKeyDown = (e) => {
		if (e.key === "Enter" && inputValue.trim() !== "") {
			e.preventDefault(); // Override the default Enter key behavior
			setFormData((prev) => ({
				...prev,
				Categories: [...prev.Categories, inputValue.trim()], // Adding the new value to the array
			}));
			setInputValue(""); // Clear the input field
		}
	};

	// Removing a Category from the list
	const removeOption = (index) => {
		setFormData((prev) => ({
			...prev,
			Categories: prev.Categories.filter((_, i) => i !== index), // Remove the value at the specified index
		}));
	};

	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload
		if (formData.Categories.length === 0) {
			alert("Please add at least one category before submitting.");
			return;
		}
		// Prepare form data to send to the server
		const reqBody = new FormData();
		reqBody.append('Title', formData.Title);
		reqBody.append('ReleaseDate', formData.ReleaseDate);
		reqBody.append('Film', formData.Film);
		reqBody.append('Categories', JSON.stringify(formData.Categories));
		reqBody.append('MovieImage', formData.MovieImage);;
		try {
			// Send a POST request to the server
			const response = await fetch(`http://localhost:3001/api/movies/${formData.id}`, {
				method: "PUT",
				headers: {
					Authorization: `Bearer ${sessionStorage.getItem("token")}`
				},
				body: reqBody
			});
			if (response.ok) {
				alert("Movie Updated!");
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
		<div className="text-container">
			<h1>Update Movie</h1>
			<form className="login-form" onSubmit={handleSubmit} >
				<input type="text"
					name="id"
					placeholder="Movie Id"
					required
					value={formData.id}
					onChange={handleChange}
				/>
				<input type="text"
					name="Title"
					placeholder="Title"
					required
					value={formData.Title}
					onChange={handleChange}
				/>
				<label> Add Release Date:
				</label>
				<input type="date"
					name="ReleaseDate"
					placeholder="Release Date: YYYY-MM-DD"
					required
					value={formData.ReleaseDate}
					onChange={handleChange}
				/>
				<label>
					Add Categories:

				</label>
				<input
					type="text"
					value={inputValue}
					onChange={(e) => setInputValue(e.target.value)} // Update the input value
					onKeyDown={handleKeyDown} // Listen for the Enter key
					placeholder="Type and press Enter"
				/>

				<ul>
					{formData.Categories.map((Category, index) => (
						<li key={index}>
							{Category}{" "}
							<button style={{ marginLeft: "40px" }} onClick={() => removeOption(index)}>Remove</button>
						</li>
					))}
				</ul>
				<label>
					Add video
				</label>
				<input type="file"
					name="Film"
					accept="video/*"
					required
					onChange={handleChange}
				/>
				<label>
					Add Movie Image
				</label>
				<input type="file"
					name="MovieImage"
					accept="image/*"
					required
					onChange={handleChange}
				/>
				<button type="submit" >Update</button>
			</form>
		</div>
	);
}
export default UpdateMovie;