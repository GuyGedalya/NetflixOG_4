import React, { useEffect, useState } from 'react';
import './HomePage.css';
import MovieDetailsModal from '../MovieDetailsModal/MovieDetailsModal';
import SearchResultsModal from '../SearchResultsModal/SearchResultsModal';

import Header from '../Header/header';


function HomePage() {
    const [categories, setCategories] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [selectedMovieId, setSelectedMovieId] = useState(null);
    const [randomMovie, setRandomMovie] = useState(null);
    const [results, setResults] = useState([]);
    const [showSearchModal, setShowSearchModal] = useState(false);

    useEffect(() => {
        fetch('http://localhost:3001/api/movies', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'user-id': `${localStorage.getItem("token")}`,
            },
        })
            .then((response) => response.json())
            .then((data) => {
                setCategories(data);
                selectRandomMovie(data);
            })
            .catch((error) => console.error('Error fetching movies:', error));
    }, []);

    const handleMovieClick = (movieId) => {
        setSelectedMovieId(movieId);
        setShowModal(true);
    };

    const selectRandomMovie = (data) => {
        const categoryKeys = Object.keys(data).filter(
            (category) => category !== 'lastSeenMovies'
        );
        if (categoryKeys.length > 0) {
            const randomCategory =
                categoryKeys[Math.floor(Math.random() * categoryKeys.length)];
            const movies = data[randomCategory];

            if (movies && movies.length > 0) {
                const randomMovie = movies[Math.floor(Math.random() * movies.length)];
                setRandomMovie(randomMovie);
            }
        }
    };

    return (
        <div>
            <Header setResults = {setResults} setShowSearchModal = {setShowSearchModal}/>
            <div className="hero-section">
                <div className="video-container">
                    {randomMovie ? (
                        <video autoPlay muted loop>
                            <source src={randomMovie.Film} type="video/mp4" />
                            Your browser does not support the video tag.
                        </video>
                    ) : (
                        <p>Loading random movie...</p>
                    )}
                </div>
            </div>

            <section className="categories">
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

export default HomePage;
