
function UpdateMovie() {
	return (
		<div className="text-container">
			<h1>Update Movie</h1>
			<form className="login-form" >
				<input type="text"
					name="id"
					placeholder="Movie Id"
					required
				/>
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
					Movie Image
					<input type="file"
						name="MovieImage"
						accept="image/*"
					/>
				</label>
				<button type="submit" >Update</button>
			</form>
		</div>
	);
}
export default UpdateMovie;