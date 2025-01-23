import { useState } from "react";

function AddMovie() {
	// State to store form data
	const [formData, setFormData] = useState({
		Title: "",
		RealeaseDate: "",
		Categories: [],
		Film: "",
		MovieImage: null,
		
	});
	const [inputValue, setInputValue] = useState(""); // ערך זמני לשדה הקלט

	// Handle form input changes
	const handleChange = (e) => {
		const { name, value, files } = e.target;
		if (name === "ProfileImage") {
			setFormData((prev) => ({ ...prev, ProfileImage: files[0] }));
		} else {
			setFormData((prev) => ({ ...prev, [name]: value }));
		}
	};

	// הוספת ערך למערך בלחיצת Enter
	const handleKeyDown = (e) => {
		if (e.key === "Enter" && inputValue.trim() !== "") {
		  e.preventDefault(); // מונע את פעולת ברירת המחדל של Enter
		  setFormData((prev) => ({
			...prev,
			Categories: [...prev.Categories, inputValue.trim()], // הוספת הערך למערך
		  }));
		  setInputValue(""); // איפוס שדה הקלט
		}
	  };
	
	  // הסרת ערך מהמערך
	  const removeOption = (index) => {
		setFormData((prev) => ({
		  ...prev,
		  Categories: prev.Categories.filter((_, i) => i !== index), // סינון הערך לפי אינדקס
		}));
	  };

	  
	const handleSubmit = async (e) => {
		e.preventDefault(); // Prevent page reload
		const reqBody = {
			Title: formData.Title,
			RealeaseDate: formData.RealeaseDate,
			Film: formData.Film,
			Categories: formData.Categories,
			MovieImage: formData.MovieImage
		}
		console.log(reqBody);
		try {
			// Send a POST request to the server
			const response = await fetch("http://localhost:3001/api/movies", {
				method: "POST",
				headers: {
					"Content-Type": "application/json", // Specify JSON format
					Authorization: `Bearer ${sessionStorage.getItem("token")}`
				},
				body: JSON.stringify(reqBody)
			});

			if (response.ok) {
				alert("Category Added!");
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
			<h1>Add Movie</h1>
			<form className="login-form" onSubmit={handleSubmit} >
				<input type="text"
					name="Title"
					placeholder="Title"
					required
					value={formData.Title}
					onChange={handleChange}
				/>
				<input type="date"
					name="RealeaseDate"
					placeholder="Realease Date: YYYY-MM-DD"
					required
					value={formData.RealeaseDate}
					onChange={handleChange}
				/>
				<label>
					Add Categories:
					<input
						type="text"
						value={inputValue}
						onChange={(e) => setInputValue(e.target.value)} // עדכון הערך הזמני
						onKeyDown={handleKeyDown} // האזנה ל-Enter
						placeholder="Type and press Enter"
					/>
				</label>
				<ul>
					{formData.Categories.map((Category, index) => (
						<li key={index}>
							{Category}{" "}
							<button  style={{ marginLeft: "40px" }} onClick={() => removeOption(index)}>Remove</button>
						</li>
					))}
				</ul>
				<input type="text"
					name="Film"
					placeholder="Movie URL"
					required
					value={formData.Film}
					onChange={handleChange}
				/>
				<label>
					Add Movie Image
					<input type="file"
						name="MovieImage"
						accept="image/*"
						onChange={handleChange}
					/>
				</label>
				<button type="submit" >Add</button>
			</form>
		</div>
	);
}
export default AddMovie;