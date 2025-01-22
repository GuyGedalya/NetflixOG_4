
function AddCategory() {
	return (
		<div className="text-container">
			<h1>Add Category</h1>
			<form className="login-form" >
				<input type="text"
					name="name"
					placeholder="Category Name"
					required
				/>
				<label>
					<input type="checkbox" name="promoted" required />
					Promoted Category?
				</label>
				<button type="submit" >Add</button>
			</form>
		</div>
	);
}
export default AddCategory;