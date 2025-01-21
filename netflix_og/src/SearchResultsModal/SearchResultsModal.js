import React from 'react';
import './SearchResultsModal.css'; 

function SearchResultsModal({ show, onClose, results, onMovieClick }) {
    if (!show) return null; 
    console.log(results)
    return (
        <div className="modal-overlay">
            <div className="modal-content">
                <button className="close-modal" onClick={onClose}>
                    ✖
                </button>
                <h2>Search Results</h2>
                {results === null ? (
                    <p className="no-results-message">No Movies Matched Your Query...</p>
                ) : (
                    <div className="search-results-row">
                        {results.map((movie) => (
                            <img
                                key={movie._id}
                                src={movie.Image || '/images/favicon.ico.png'} 
                                alt={movie.Title}
                                className="search-result-poster"
                                onClick={() => onMovieClick(movie._id)} 
                            />
                        ))}
                    </div>
                )}
            </div>
        </div>
    );
}

export default SearchResultsModal;
