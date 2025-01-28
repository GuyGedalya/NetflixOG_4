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
			<div className="container-fluid">
				<div className="row1">
					<div className="col-3">
						<LeftManagerMenu onMenuClick={handleMenuClick} />
					</div>
					<div className="col-9 text-container10">
						{currentComponent}
					</div>
				</div>
			</div>
		</>
	);
}

export default Manage;
