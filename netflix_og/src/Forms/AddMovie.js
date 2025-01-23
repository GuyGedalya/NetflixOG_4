
function AddMovie() {
	return (
		<div className="text-container">
			<h1>Add Movie</h1>
			<form className="login-form" >
				<input type="text"
					name="Title"
					placeholder="Title"
					required
				/>
				<input type="date"
					name="RealeaseDate"
					placeholder="Realease Date: YYYY-MM-DD"
					required
				/>
				<label>
				<input type="text"
					name="Film"
					placeholder="Movie URL"
					required
				/>
				Movie Image
				<input type="file"
					name="MovieImage"
					accept="image/*"
				/>
				</label>
				<button type="submit" >Add</button>
			</form>
		</div>
	);
}
export default AddMovie;