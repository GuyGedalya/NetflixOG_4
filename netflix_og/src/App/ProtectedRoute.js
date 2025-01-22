import React from 'react';
import { Navigate } from 'react-router-dom';

const ProtectedRoute = ({ children }) => {
    const token = sessionStorage.getItem('token'); 
    if (!token) { // If there is no token, redirect to login
        return <Navigate to="/logIn" />; 
    }
    return children; 
};

export default ProtectedRoute;
