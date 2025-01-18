import React from 'react';
import { BrowserRouter as Router, Routes, Route, useParams, useNavigate } from 'react-router-dom';
import HomePageUnregistered from '../home/HomePageUnregistered';
import SignIn from '../signIn/SignIn';
import MovieDetailsModal from '../MovieDetailsModal/MovieDetailsModal';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <Routes>
          <Route path="/" element={<HomePageUnregistered />} />
          <Route path="/signin" element={<SignIn />} />
          <Route path="api/movies/:id" element={<MovieDetailsHandler />} />
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
