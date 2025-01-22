import "./manage.css";
import LeftManagerMenu from "../Components/LeftManageMenu";
import AddCategory from "../Forms/AddCategory";
import { useState } from "react";
function Manage() {
	const [currentComponent, setCurrentComponent] = useState(<AddCategory />);
	const handleMenuClick = (component) => {
		setCurrentComponent(component);
	};
	return (
		<>
			<div class="container-fluid">
				<div class="row">
					<div class="col-3">
						<LeftManagerMenu onMenuClick={handleMenuClick} />
					</div>
					<div class="col-9 text-container">
						{currentComponent}
					</div>
				</div>
			</div>
		</>
	);
}

export default Manage;
