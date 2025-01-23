import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const token = sessionStorage.getItem('token'); 
    if (!token) { // If there is no token, redirect to login
        return <Navigate to="/logIn" />; 
    }
    return children; 
};

const ManagerProtectedRoute = ({ children }) => {
	const user = JSON.parse(sessionStorage.getItem("user"));
	if (!user.Admin) { // If there is no token, redirect to login
		return <Navigate to="/logIn" />;
	}
	return children;
};

export {ProtectedRoute, ManagerProtectedRoute};
