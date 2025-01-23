const express = require('express');
var router = express.Router();
const upload = require('../middlewares/upload');
const movieController = require('../controllers/movie');
const middleware = require('../middlewares/authentication');

// Some functions need authentication
router.route('/')
	.get(middleware.authenticateToken, middleware.getUser, movieController.returnMovies)
	.post(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin,upload.fields([{ name: 'MovieImage', maxCount: 1 }, {name: 'Film', maxCount: 1 }]), movieController.createMovie);

router.route('/categories')
	.get(movieController.getCategories)

router.route('/:id')
	.get(movieController.getMovie)
	.put(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin,upload.fields([{ name: 'MovieImage', maxCount: 1 }, {name: 'Film', maxCount: 1 }]), movieController.replaceMovie)
	.delete(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, movieController.deleteMovie);

router.route('/:id/recommend')
	.get(middleware.authenticateToken, middleware.getUser, movieController.recommendMovies)
	.post(middleware.authenticateToken, middleware.getUser, movieController.addMovieToUser);

router.route('/search/:query')
	.get(movieController.searchMovies);

module.exports = router;