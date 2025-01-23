import React from 'react';
import { BrowserRouter as Router, Routes, Route, useParams, useNavigate } from 'react-router-dom';
import HomePageUnregistered from '../home/HomePageUnregistered';
import SignIn from '../signIn/SignIn';
import MovieDetailsModal from '../MovieDetailsModal/MovieDetailsModal';
import HomePage from '../HomePage/HomePage'
import CategoriesPage from '../CategoriesPage/CategoriesPage';
import SignUp from '../signUp/SignUp';
import Manage from '../Manager/Manage';

import './App.css';
import {ProtectedRoute, ManagerProtectedRoute} from './ProtectedRoute';

function App() {
	return (

		<Router>
			<div className="App">
				<Routes>
					<Route path="/" element={<HomePageUnregistered />} />
					<Route path="/logIn" element={<SignIn />} />
					<Route path="/signUp" element={<SignUp />} />
					<Route path="/manage" element={<ManagerProtectedRoute><Manage /></ManagerProtectedRoute>} />
					<Route path="/home" element={<ProtectedRoute> <HomePage /> </ProtectedRoute>} />
					<Route path="/categories" element={<ProtectedRoute> <CategoriesPage /> </ProtectedRoute>} />
					<Route path="api/movies/:id" element={<ProtectedRoute><MovieDetailsHandler /> </ProtectedRoute>} />
				</Routes>
			</div>
		</Router>

	);
}


const MovieDetailsHandler = () => {
	const { id } = useParams();
	const navigate = useNavigate();

	return (
		<MovieDetailsModal
			show={true}
			onHide={() => navigate(-1)}
			movieId={id}
		/>
	);
};

export default App;
