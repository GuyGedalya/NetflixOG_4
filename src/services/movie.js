const Movie = require('../models/movie');
const User = require('../models/user');
const Category = require('../models/category')
const CounterServices = require('../services/counter');

const getMovieById = async (id) => {
    try {
        return await Movie.findById(id).populate('Categories', 'name');
    } catch (error) {
        return null;
    }
};

async function getPromotedCategoryNames() {
    try {
        // Fetch all categories where 'promoted' is true
        const promotedCategories = await Category.find({ promoted: true });

        // Extract the 'name' field from each category and return as an array
        const categoryNames = promotedCategories.map(category => category._id);
        return categoryNames;
    } catch (error) {
        console.error('Error fetching promoted categories:', error);
        throw error; // Propagate the error for higher-level handling if needed
    }
}


/**
 * Processes movies by categories and returns up to 20 unseen movies per category for the user.
 * It also includes the last 20 movies the user has seen, shuffled.
 */
async function getUnseenMoviesPerCategory(userId, categories) {
    try {
        // Final result: mapping of categories to movie lists
        const result = {};

        // Fetch the user by their ID and populate their movie list:
        const user = await User.findById(userId).populate('Movies');
        if (!user) throw new Error('User not found');

        // Get the last 20 movies the user has seen (first 20 from the Movies array):
        const lastSeenMovies = user.Movies.slice(0, 20);

        // Shuffle the list of last seen movies randomly shuffle:
        const shuffledSeenMovies = lastSeenMovies.sort(() => Math.random() - 0.5);

        // Get the movie object:
        const shuffledSeenMoviesObject = [];
        for (const id of shuffledSeenMovies) {
            const movie = await getMovieById(id);
            if (movie) {
                shuffledSeenMoviesObject.push(movie);
            }
        }

        // Iterate over each category
        for (const categoryName of categories) {
            // Fetch all movies associated with this category
            const moviesInCategory = await Movie.find({ 'Categories': categoryName }).populate('Categories');
            // List of movies that the user hasn't seen in this category
            const unseenMovies = [];

            // Iterate over the movies in the current category
            for (const movie of moviesInCategory) {
                // Check if the user has watched the movie:
                const hasSeen = user.Movies.some(userMovie => userMovie._id.equals(movie._id));
                if (!hasSeen) {
                    const movieObject = await getMovieById(movie._id);
                    // Add the movie title to the unseen list:
                    unseenMovies.push(movieObject);
                }
            }

            // Randomly select up to 20 unseen movies from the list:
            const selectedMovies = [];
            while (unseenMovies.length > 0 && selectedMovies.length < 20) {
                const randomIndex = Math.floor(Math.random() * unseenMovies.length);
                // Remove the selected movie from the list:
                const selectedMovie = unseenMovies.splice(randomIndex, 1)[0];
                // Add the selected movie to the selected list
                selectedMovies.push(selectedMovie);
            }

            // Finding the category by its name to set the field in result:
            const category = await Category.findById(categoryName);
            // Setting the category field in result with the movies:
            result[category.name] = selectedMovies;
        }

        // Add the shuffled list of last 20 seen movies to the final result
        result.lastSeenMovies = shuffledSeenMoviesObject;

        return result;
    } catch (error) {
        console.error('Error processing unseen movies per category:', error);
        throw error;
    }
}

const createMovie = async (Title, ReleaseDate,Image, Categories) => {
    try {
        // Creating a new movie with given fields
        const MovieNumber = await CounterServices.getNextSequence('movies');
        const movie = new Movie({
            MovieNumber: MovieNumber,
            Title: Title,
            ReleaseDate: ReleaseDate,
			Image: Image,
            Categories: Categories
        });
        return await movie.save();
    } catch (error) {
        console.error('Error creating movie:', error);
        throw error;
    }
};

// Replacing the movie details
const replaceMovie = async (movie, Title, ReleaseDate, Categories) => {
    if (!Categories || Categories.length === 0) {
        throw new Error('Categories cannot be empty.');
    }
    movie.Title = Title;
    movie.ReleaseDate = ReleaseDate;
    movie.Categories = Categories;
    return await movie.save();
}

// This function deletes a movie form the database
const deleteMovie = async (movieId) => {
    // Deletes the movie and returns the deletes movie
    const deletedMovie = await Movie.findByIdAndDelete(movieId);
    // Null if unsuccessful
    return deletedMovie;
}

const getMovieByNumber = async (MovieNumber) => {
    const movie = await Movie.findOne({ MovieNumber: MovieNumber });
    return movie;
};

// Returning an array of movie objects given an array of movie numbers
const movieNumbersToObject = async (movieNumbers) => {
    // For all the movies in the list, convert to movie object
    const moviePromises = movieNumbers.map(async (movieNumber) => {
        return await getMovieByNumber(movieNumber);
    });
    // Waiting for all the promises
    const movies = await Promise.all(moviePromises);
    return movies;
};


async function searchMovies(query) {
    try {
        // Search all the categories the has the query inside them:
        const categories = await Category.find({
            name: { $regex: query, $options: 'i' }
        });

        // Getting its ID:
        const categoryIds = categories.map(category => category._id);

        // Find movies that match the query in Title, ReleaseDate, or Categories:
        const movies = await Movie.find({
            $or: [
                { Title: { $regex: query, $options: 'i' } },
                {
                    // convert ReleaseDate to string and compare using regex:
                    $expr: {
                        $regexMatch: {
                            input: { $toString: "$ReleaseDate" },
                            regex: query
                        }
                    }
                },
                // Search for the relevant categories that match the query in the category ID:
                { Categories: { $in: categoryIds } }
            ]
        });


        if (movies.length > 0) {
            return movies;
        } else {
            return [];
        }
    } catch (error) {
        // If error, return empty array:
        console.error('Error searching movies:', error);
        return [];
    }
}


module.exports = { getMovieById, createMovie, getPromotedCategoryNames, getUnseenMoviesPerCategory, replaceMovie, deleteMovie, movieNumbersToObject, searchMovies };
