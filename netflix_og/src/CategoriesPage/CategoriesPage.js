import React, { useEffect, useState } from 'react';
import './CategoriesPage.css';
import MovieDetailsModal from '../MovieDetailsModal/MovieDetailsModal';
import SearchResultsModal from '../SearchResultsModal/SearchResultsModal';

import Header from '../Components/upperMenuHeader';



function CategoriesPage() {
    const [categories, setCategories] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [selectedMovieId, setSelectedMovieId] = useState(null);
    const [results, setResults] = useState([]);
    const [showSearchModal, setShowSearchModal] = useState(false);
  

    useEffect(() => {
        fetch('http://localhost:3001/api/movies', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                Authorization: `Bearer ${sessionStorage.getItem("token")}`
            },
        })
            .then((response) => response.json())
            .then((data) => {
                setCategories(data);
            })
            .catch((error) => alert('Error fetching movies: ' + error.message));    }, []);

    const handleMovieClick = (movieId) => {
        setSelectedMovieId(movieId);
        setShowModal(true);
    };

    return (
        <div>
            <Header setResults = {setResults} setShowSearchModal = {setShowSearchModal}/>
            <section className="categories1">
                {Object.keys(categories).map((categoryName) => (
                    <div key={categoryName} className="category">
                        <h2>{categoryName} Movies</h2>
                        <div className="row">
                            {categories[categoryName].map((movie) => (
                                <img
                                    key={movie._id}
                                    src={movie.Image || '/images/favicon.ico'}
                                    alt={movie.Title}
                                    className="movie-poster"
                                    onClick={() => handleMovieClick(movie._id)}
                                />
                            ))}
                        </div>
                    </div>
                ))}
            </section>

            <footer>
                <p>&copy; 2025 NOG. All rights reserved.</p>
            </footer>

            <MovieDetailsModal
                show={showModal}
                onHide={() => setShowModal(false)}
                movieId={selectedMovieId}
            />
            <SearchResultsModal
                show={showSearchModal}
                onClose={() => setShowSearchModal(false)}
                results={results}
                onMovieClick={handleMovieClick}
            />
        </div>
    );
}

export default CategoriesPage;
