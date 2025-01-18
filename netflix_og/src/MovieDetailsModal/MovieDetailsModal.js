import React, { useState, useEffect } from 'react';
import { Modal } from 'react-bootstrap';
import './MovieDetailsModal.css';

const MovieDetailsModal = ({ show, onHide, movieId }) => {
  const [movie, setMovie] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    if (movieId) {
      fetch(`http://localhost:3001/api/movies/${movieId}`)
        .then((response) => {
          if (!response.ok) {
            throw new Error('Failed to fetch movie details');
          }
          return response.json();
        })
        .then((data) => setMovie(data))
        .catch((err) => setError(err.message));
    }
  }, [movieId]);

  return (
    <Modal show={show} onHide={onHide} className="movie-modal" fullscreen>
      <Modal.Body>
        {error ? (
          <p>{error}</p>
        ) : movie ? (
          <div className="modal-content-container">
            {/* Video section */}
            <div className="video-container">
              <video className="movie-video" controls>
                <source src="https://www.example.com/video.mp4" type="video/mp4" />
                Your browser does not support the video tag.
              </video>
            </div>

            {/* Movie details section */}
            <div className="movie-details">
              <p>
                <strong>Title:</strong> {movie.Title}
              </p>
              <p>
                <strong>Release Date:</strong>{' '}
                {new Date(movie.ReleaseDate).toLocaleDateString()}
              </p>
              <p>
                <strong>Categories:</strong>{' '}
                {movie.Categories.map((cat) => cat.name).join(', ')}
              </p>
            </div>
          </div>
        ) : (
          <p>Loading...</p>
        )}
      </Modal.Body>
    </Modal>
  );
};

export default MovieDetailsModal;
