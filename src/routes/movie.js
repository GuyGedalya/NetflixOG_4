const express = require('express');
var router = express.Router();
const movieController = require('../controllers/movie');
const middleware = require('../controllers/authentication');

// Some functions need authentication
router.route('/')
	.get(middleware.verifyUserId, middleware.getUser, movieController.returnMovies)
	.post(middleware.verifyUserId, middleware.getUser, movieController.createMovie);

router.route('/:id')
	.get(movieController.getMovie)
	.put(middleware.verifyUserId, middleware.getUser, movieController.replaceMovie)
	.delete(middleware.verifyUserId, middleware.getUser, movieController.deleteMovie);

router.route('/:id/recommend')
	.get(middleware.verifyUserId, middleware.getUser, movieController.recommendMovies)
	.post(middleware.verifyUserId, middleware.getUser, movieController.addMovieToUser);

router.route('/search/:query')
	.get(movieController.searchMovies);

module.exports = router;