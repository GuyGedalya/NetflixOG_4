
function UpdateCategory() {
	return (
		<div className="text-container">
			<h1>Update Category</h1>
			<form className="login-form" >
				<input type="text"
					name="id"
					placeholder="Category Id"
					required
				/>
				<input type="text"
					name="name"
					placeholder="Category Name"
					required
				/>
				<label>
					<input type="checkbox" name="promoted" required />
					Promoted Category?
				</label>
				<button type="submit" >Update</button>
			</form>
		</div>
	);
}
export default UpdateCategory;