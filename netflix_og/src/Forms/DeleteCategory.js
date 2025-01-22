
function DeleteCategory() {
	return (
		<div className="text-container">
			<h1>Delete Category</h1>
			<form className="login-form" >
				<input type="text"
					name="id"
					placeholder="Category Id"
					required
				/>
				<button type="submit" >Delete</button>
			</form>
		</div>
	);
}
export default DeleteCategory;