const movieService = require('../services/movie');
const categoryService = require('../services/category');
const connectionService = require('../services/connection');
const userService = require('../services/user');


const getMovie = async (req, res) => {
	try {
		// Try to find the movie by its ID:
		const movie = await movieService.getMovieById(req.params.id);
		// If not found:
		if (!movie) {
			return res.status(404).json({ error: 'Movie not found!' });
		}
		res.status(200).json(movie);
	} catch (error) {
		res.status(500).json({ message: 'An error occurred while fetching the movie.' });
	}
};

const createMovie = async (req, res) => {
	try {
		const MovieImagePath = req.files && req.files.MovieImage ? req.files.MovieImage[0].path : null;
		const MovieVideoPath = req.files && req.files.Film ? req.files.Film[0].path : null;
		// Checking that all the fields arrived
		if (!req.body.Title || !req.body.ReleaseDate || !req.body.Categories || MovieImagePath == null || MovieVideoPath == null) {
			return res.status(400).json({ error: 'Missing required fields.' });
		}
		const categories = await categoryService.getCategoriesIdsByNames(JSON.parse(req.body.Categories));
		if (categories == []) {
			res.status(400).json({ error: "Please provide existing categories." });
		}
		// Creating movie
		const movie = await movieService.createMovie(req.body.Title, req.body.ReleaseDate, MovieImagePath, categories.map(cat => cat._id), MovieVideoPath);
		if (movie) {
			// Set the Location header to point to the new resource
			res.setHeader('Location', `/api/movies/${movie._id}`);
			res.status(201).json();
		}
	} catch (error) {
		if (error.message.includes("does not exist")) {
			return res.status(400).json({ error: error.message });
		}
		console.error(error);
		res.status(500).json({ error: 'An error occurred while creating the movie.' });
	}
};

async function returnMovies(req, res) {
	try {
		// Step 1: Get promoted category names:
		const promotedCategories = await movieService.getPromotedCategoryNames();

		// Step 2: Get unseen movies per category:
		const userId = req.user._id;
		const moviesByCategory = await movieService.getUnseenMoviesPerCategory(userId, promotedCategories);

		// Send the response to the client
		res.status(200).json(moviesByCategory);
	} catch (error) {
		console.error('Error in returnMovies:', error);
		res.status(500).json({ error: 'Failed to process movies' });
	}
};

// Replace movieID details
const replaceMovie = async (req, res) => {
	if (!req.body.Title || !req.body.ReleaseDate || !req.body.Categories || !req.body.Image || !req.body.Film) {
		return res.status(400).json({ message: 'Missing required fields: Title, ReleaseDate, or Categories.' });
	}
	try {
		// Getting the movie
		const movie = await movieService.getMovieById(req.params.id);
		if (!movie) {
			return res.status(404).json({ error: 'Movie not found!' });
		}
		// Getting a list of category IDs
		const categories = await categoryService.getCategoriesIdsByNames(req.body.Categories);
		if (!categories) {
			res.status(400).json({ error: "Please use existing categories." });
		}
		const MovieImagePath = req.files && req.files.MovieImage ? req.files.MovieImage[0].path : null;
		// Changing movie details
		const updatedMovie = await movieService.replaceMovie(movie, req.body.Title, req.body.ReleaseDate, MovieImagePath, categories.map(cat => cat._id), req.body.Film);
		if (!updatedMovie) {
			res.status(400).json({ error: "Failed to update movie. Please check the field formats." });
		}
		return res.status(200).json();
	} catch (error) {
		if (error.message.includes("does not exist")) {
			return res.status(400).json({ error: error.message });
		}
		console.error(error);
		res.status(500).json({ error: 'An error occurred while fetching the movie.' });
	}
};

const addMovieToUser = async (req, res) => {
	const user = req.user;
	try {

		// Getting the movie
		const movie = await movieService.getMovieById(req.params.id);
		if (!movie) {
			return res.status(404).json({ error: 'Movie not found!' });
		}
		if (await userService.hasWatched(user, movie._id)) {
			return res.status(204).json();
		}
		// Trying to connect to recommendation server
		let message = `POST ${user.UserNumber} ${movie.MovieNumber}`;
		let response = await connectionService.sendAndReceive(message);
		// If the user is new
		if (response === '201 Created') {
			// Adding the movie to the user's list
			user.Movies.unshift(movie._id);
			await user.save();
			return res.status(204).json();
			// Trying to add if the user isn't new	
		} else {
			message = `PATCH ${user.UserNumber} ${movie.MovieNumber}`;
			response = await connectionService.sendAndReceive(message);
			if (response === '204 No Content') {
				// Adding the movie to the user's list
				user.Movies.unshift(movie._id);
				await user.save();
				return res.status(204).json();
			}
		}
		// Both operations failed	
		res.status(400).json({ error: 'An error accrued adding movie' });
	} catch (error) {
		return res.status(500).json({ error: error.message });
	}
};

const deleteMovie = async (req, res) => {
	try {
		const movieid = req.params.id;
		// Checking if movie exists before deleting
		let movie = await movieService.getMovieById(movieid);
		if (!movie) {
			return res.status(404).json({ error: 'Movie not found!' });
		}
		movie = await movieService.deleteMovie(movieid);
		if (!movie) {
			return res.status(500).json({ error: 'An error accrued while deleting the movie!' });
		}
		// Getting all the users who watched the movie
		const MovieNumber = movie.MovieNumber;
		const users = await userService.findUsersWhoWatched(movieid);
		let errorFlag = false;
		// Deleting the movie from the users watch list in the cpp server
		users.forEach(async user => {
			// Another try catch block to handle errors from the cpp server
			try {
				const message = `DELETE ${user.UserNumber} ${MovieNumber}`;
				const response = await connectionService.sendAndReceive(message);
				// If cpp server didn't succeed
				if (response !== '204 No Content') {
					errorFlag = true;
				}
				const updatedUser = await userService.deleteMovieFromWatched(user._id, movie._id);
				if (!updatedUser) {
					errorFlag = true;
				}
			} catch (cppError) {
				return res.status(500).json({ error: cppError.message });
			}
		});

		// If some movies didn't weren't deleted
		if (errorFlag === true) {
			return res.status(500).json({ error: 'An error accrued while deleting movie from users watch list' });
		}
		return res.status(204).json();
	} catch (error) {
		return res.status(500).json({ error: error.message });
	}
}


const recommendMovies = async (req, res) => {
	try {
		// Checking that movie exists
		const movie = await movieService.getMovieById(req.params.id);
		if (!movie) {
			return res.status(404).json({ error: 'Movie not found!' });
		}
		const user = req.user;
		// Sending message to the cpp server to get a recommendation
		const message = `GET ${user.UserNumber} ${movie.MovieNumber}`;
		const response = await connectionService.sendAndReceive(message);

		// Parsing the response
		const lines = response.split('\n');
		const movieNumbersLine = lines[2]
		const movieNumbers = movieNumbersLine.split(' ');

		// Getting an array of movies
		let recommended = await movieService.movieNumbersToObject(movieNumbers);
		if (recommended[0] === null) {
			recommended = [];
		}
		return res.status(200).json(recommended);

	} catch (error) {
		res.status(500).json({ message: 'An error occurred while fetching the movie.' });
	}
}

async function searchMovies(req, res) {
	const query = req.params.query;
	try {
		// Get the movies from the services:
		const movies = await movieService.searchMovies(query);
		if (movies.length > 0) {
			// Send found movies as JSON response
			res.json(movies);
		} else {
			res.status(404).json({ error: 'No movies found matching the query' });
		}
	} catch (error) {
		res.status(500).json({ error: 'Error searching for movies', error });
	}
}


module.exports = { getMovie, createMovie, replaceMovie, returnMovies, addMovieToUser, deleteMovie, recommendMovies, searchMovies };
