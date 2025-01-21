import React, { useEffect, useState } from 'react';
import './HomePage.css';
import MovieDetailsModal from '../MovieDetailsModal/MovieDetailsModal';
import SearchResultsModal from '../SearchResultsModal/SearchResultsModal';
import { useNavigate } from 'react-router-dom';



function HomePage() {
    const [categories, setCategories] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [selectedMovieId, setSelectedMovieId] = useState(null);
    const [randomMovie, setRandomMovie] = useState(null);
    const [isOpen, setIsOpen] = useState(false);
    const [searchText, setSearchText] = useState('');
    const [results, setResults] = useState([]);
    const [showSearchModal, setShowSearchModal] = useState(false);
    const navigate = useNavigate();



    const handleSearch = async () => {
        if (searchText.trim() === '') {
            alert('Titles, People, Genres');
            return;
        }

        try {
            console.log(`http://localhost:3001/api/movies/search/${encodeURIComponent(searchText)}`)
            const response = await fetch(`http://localhost:3001/api/movies/search/${encodeURIComponent(searchText)}`);
            let data;
            if (!response.ok) {
                data = null;
            } else {
                data = await response.json();
            }
            setResults(data);
            console.log(data);
            setShowSearchModal(true);
        } catch (error) {
            console.error('Failed to fetch search results:', error);
            setResults([]);
        }
    };


    useEffect(() => {
        fetch('http://localhost:3001/api/movies', {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
                'user-id': '678c1fdbb298510871824c64',
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
        console.log(categoryKeys);
        if (categoryKeys.length > 0) {
            const randomCategory =
                categoryKeys[Math.floor(Math.random() * categoryKeys.length)];
            console.log(randomCategory);
            const movies = data[randomCategory];
            console.log(movies);

            if (movies && movies.length > 0) {
                const randomMovie = movies[Math.floor(Math.random() * movies.length)];
                console.log(randomMovie)
                setRandomMovie(randomMovie);
            }
        }
    };

    return (
        <div>
            <header>
                <a href="#" className="logo">
                    <img src="/images/favicon.ico" alt="Project Logo" className="logo-image" />
                </a>
                <nav>
                    <button onClick={() => (window.location.href = '/home')} className='header button'>Home</button>
                    <button onClick={() => navigate('/categories')} className='header button'>Categories</button>
                    {!isOpen ? (<button onClick={() => setIsOpen(true)} className='header button'>Search</button>) :
                        (<div className="search-bar">
                            <input
                                type="text"
                                className="search-input"
                                placeholder="Titles, People, Genres"
                                value={searchText}
                                onChange={(e) => setSearchText(e.target.value)}
                                onKeyDown={(e) => {
                                    if (e.key === 'Enter') {
                                        handleSearch();
                                    }
                                }}
                                autoFocus
                            />
                            <button
                                onClick={() => {
                                    setIsOpen(false);
                                    setSearchText('');
                                }}
                                className="close-button"
                            >
                                ✖
                            </button>
                        </div>
                        )}
                </nav>
            </header>

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
