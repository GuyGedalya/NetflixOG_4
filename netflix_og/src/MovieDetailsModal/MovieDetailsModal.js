import React, { useEffect, useState } from 'react';
import { Modal, Button } from 'react-bootstrap';
import './MovieDetailsModal.css';

const MovieDetailsModal = ({ show, onHide, movieId }) => {
  const [movie, setMovie] = useState(null);
  const [relatedMovies, setRelatedMovies] = useState([]); // State for additional movies
  const [error, setError] = useState(null);
  const [showVideo, setShowVideo] = useState(false); // State to toggle between image and video

  useEffect(() => {
    if (movieId) {
      setMovie(null);
      setError(null);
      setShowVideo(false); // Reset video state when modal opens

      // Fetch the current movie details
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

  useEffect(() => {
    const fetchMovies = async () => {
      if (!movieId) return; // Return early if movieId is not defined
  
      try {
        const response = await fetch(`http://localhost:3001/api/movies/${movieId}/recommend`, {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${sessionStorage.getItem("token")}`,
          },
        });
  
        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`Failed to fetch movies: ${response.status} - ${errorText}`);
        }
        const data = await response.json();
        setRelatedMovies(data); // Save movies to state
      } catch (err) {
        console.error('Error fetching movies:', err);
      }
    };
  
    fetchMovies();
  }, [movieId]); // Re-run when movieId changes
  
  // Function to handle POST request and show video
  const handleWatchNow = async () => {
    try {
        // Perform the POST request
      const response = await fetch(`http://localhost:3001/api/movies/${movieId}/recommend`, {
        method: "POST",
        headers: {
          Authorization: `Bearer ${sessionStorage.getItem("token")}`,
        },
      });

      if (!response.ok) {
        throw new Error("Failed to send POST request");
      }
      // Show the video after successful POST request
      setShowVideo(true); 
    } catch (error) {
      alert("Error: " + error.message);
    }
  };

  return (
    <Modal show={show} onHide={onHide} className="movie-modal" fullscreen>
      <Button
        variant="secondary"
        onClick={() => {
          onHide();
          setShowVideo(false);
        }}
        className="close-button"
      >
        X
      </Button>

      <Modal.Body>
        {error ? (
          <p>{error}</p>
        ) : movie ? (
          <div className="modal-content-container">
            {/* Video/Image section */}
            <div className="video-container">
              {showVideo ? (
                <video className="movie-video" controls autoPlay>
                  <source src={`http://localhost:3001/${movie.Film}`} type="video/mp4" />
                  Your browser does not support the video tag.
                </video>
              ) : (
                <>
                  {movie.Image ? (
                    <img
                    style={{width:'60%'}}
                      src={`http://localhost:3001/${movie.Image}`}
                      alt={movie.Title}
                      className="movie-image"
                    />
                  ) : (
                    <p>No image available</p> // If the image is missing, display a message
                  )}
                  <Button
                    className="watch-now-button"
                    onClick={handleWatchNow} // Call the POST request and show video
                  >
                    Watch Now
                  </Button>
                </>
              )}
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
                {movie.Categories && movie.Categories.length > 0 ? (
                  movie.Categories.map((cat) => cat.name).join(', ')
                ) : (
                  <span>No categories available</span>
                )}
              </p>
            </div>

            {/* Related movies section */}
            <div className="related-movies">
              <h4>Related Movies</h4>
              <div className="related-movies-list"style={{
                  display: 'flex',
                  flexWrap: 'wrap',
                  gap: '10px',
                }}>
                {relatedMovies && relatedMovies.length > 0 ? (
                  relatedMovies.map((relatedMovie) => (
                    <div key={relatedMovie._id} className="related-movie-item"style={{ flex: '0 0 20%', paddingBottom: '10px' }}>
                      {relatedMovie.Image ? (
                        <img
                        style={{width:'100%'}}
                          src={`http://localhost:3001/${relatedMovie.Image}`}
                          alt={relatedMovie.Title}
                          className="related-movie-image"
                        />
                      ) : (
                        <p>No image available</p>
                      )}
                      <p className="related-movie-title">{relatedMovie.Title}</p>
                    </div>
                  ))
                ) : (
                  <p>No related movies found.</p>
                )}
              </div>
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
