const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const middleware = require('../middlewares/authentication');

// Some functions need authentication
router.route('/')
	.get(middleware.authenticateToken, middleware.getUser, movieController.returnMovies)
	.post(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, movieController.createMovie);

router.route('/:id')
	.get(movieController.getMovie)
	.put(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, movieController.replaceMovie)
	.delete(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, movieController.deleteMovie);

router.route('/:id/recommend')
	.get(middleware.authenticateToken, middleware.getUser, movieController.recommendMovies)
	.post(middleware.authenticateToken, middleware.getUser, movieController.addMovieToUser);

router.route('/search/:query')
	.get(movieController.searchMovies);

module.exports = router;