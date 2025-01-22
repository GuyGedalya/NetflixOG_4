import "./leftMenu.css";
import AddMovie from "../Forms/AddMovie";
import DeleteMovie from "../Forms/DeleteMovie";
import AddCategory from "../Forms/AddCategory";
import DeleteCategory from "../Forms/DeleteCategory";
import UpdateCategory from "../Forms/UpdateCategory";


function LeftManageMenu( {onMenuClick} ) {
	return (
		<ul className="leftMenu">
			<li><button onClick={() => onMenuClick(<AddCategory />)}>Add Category</button></li>
			<li><button onClick={() => onMenuClick(<UpdateCategory />)}>Edit Category</button></li>
			<li><button onClick={() => onMenuClick(<DeleteCategory />)}>Delete Category</button></li>
			<li><button onClick={() => onMenuClick(<AddMovie />)}>Add Movie</button></li>
			<li><button onClick={() => onMenuClick(<UpdateCategory/>)} >Edit Movie</button></li>
			<li><button onClick={() => onMenuClick(<DeleteMovie />)}>Delete Movie</button></li>
		</ul>

	);
}

export default LeftManageMenu;