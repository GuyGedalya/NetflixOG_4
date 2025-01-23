
function DeleteMovie() {
	return (
		<div className="text-container">
			<h1>Delete Movie</h1>
			<form className="login-form" >
				<input type="text"
					name="id"
					placeholder="Movie Id"
					required
				/>
				<button type="submit" >Delete</button>
			</form>
		</div>
	);
}
export default DeleteMovie;