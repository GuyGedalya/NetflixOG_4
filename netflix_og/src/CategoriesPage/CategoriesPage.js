import React, { useEffect, useState } from 'react';
import './CategoriesPage.css';
import MovieDetailsModal from '../MovieDetailsModal/MovieDetailsModal';
import SearchResultsModal from '../SearchResultsModal/SearchResultsModal';
import { useNavigate } from 'react-router-dom';



function CategoriesPage() {
    const [categories, setCategories] = useState({});
    const [showModal, setShowModal] = useState(false);
    const [selectedMovieId, setSelectedMovieId] = useState(null);
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
            const response = await fetch(`http://localhost:3001/api/movies/search/${encodeURIComponent(searchText)}`);
            let data;
            if (!response.ok) {
                data = null;
            } else {
                data = await response.json();
            }
            setResults(data);
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
                'user-id': `${localStorage.getItem('token')}`,
            },
        })
            .then((response) => response.json())
            .then((data) => {
                setCategories(data);
            })
            .catch((error) => console.error('Error fetching movies:', error));
    }, []);

    const handleMovieClick = (movieId) => {
        setSelectedMovieId(movieId);
        setShowModal(true);
    };

    return (
        <div>
            <header>
                <a href="/home" className="logo">
                    <img src="/images/favicon.ico" alt="Project Logo" className="logo-image" />
                </a>
                <nav>
                    <button onClick={() => navigate('/home')} className='header button'>Home</button>
                    <button onClick={() => (window.location.href = '/categories')} className='header button'>Categories</button>

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
