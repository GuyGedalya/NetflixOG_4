#include "PostCommand.hpp"

PostCommand::PostCommand(int userId, const std::vector<int> &movieIds)
	: userId(userId), movieIds(movieIds) {}

std::string PostCommand::execute(App &app){
	// If the user exists 
    app.lock();
	auto user = app.searchUserByID(userId);
    app.unlock();
	if (user){
		// Can't add an already existing user
		return NOT_FOUND;
	}
	// Create a new user
    user = std::make_shared<User>(userId);
    // Add the new user to the App
    app.lock();
    app.addUser(user);
    app.unlock();
	// Iterate through the list of movie IDs
    for (int movieId : movieIds)
    {
        // Search for a movie by ID in the App
        app.lock();
        auto movie = app.searchMovieByID(movieId);
        app.unlock();
        // If the movie does not exist
        if (!movie)
        {
            // Create a new movie
            movie = std::make_shared<Movie>(movieId);
            // Add the new movie to the App
            app.lock();
            app.addMovie(movie);
            app.unlock();
        }

        // Associate the user with the movie and vice versa
        // Add the movie to the user's list
        user->lock();
        user->addMovie(movie);
        user->unlock();

        // Add the user as a viewer of the movie
        movie->lock();
        movie->addViewer(user);
        movie->unlock();
    }
	return CREATED;
}