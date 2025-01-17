#include <gtest/gtest.h>
#include "PatchCommand.hpp"
#include "App.hpp"

class PatchCommandTest: public ::testing::Test{

};

// Checking that Patch command fails if user isn't in app
TEST_F(PatchCommandTest, Check_Fail_If_User_Not_Exist) {
	App app;
	// User 1 doesn't exist in the app
	std::shared_ptr<ICommand> command = std::make_shared<PatchCommand>(1, std::vector<int>{100});
	std::string output = command->execute(app);
	std::string notFound = NOT_FOUND;
	// Expecting 404 if user not in the app
	EXPECT_STREQ(output.c_str(), notFound.c_str());
}

// Check if non existent movie added when User is in the app
TEST_F(PatchCommandTest, Add_Movie_When_Not_Exist) {
	App app;
	std::shared_ptr<User> user = std::make_shared<User>(1);
	app.addUser(user);
    // Adding movie's and to user that doesn't exist
    std::vector<int> movieIds = {100, 101};
    std::shared_ptr<ICommand> command = std::make_shared<PatchCommand>(1, movieIds);

    // Running the command to add the movies 100 and 101 to user 1
    std::string output = command->execute(app);

    // Making sure user 1 exists 
    user = app.searchUserByID(1);
    ASSERT_NE(user, nullptr);  

    // Making sure both movies exists
    auto movie1 = app.searchMovieByID(100);
    auto movie2 = app.searchMovieByID(101);
    // Check movie 100
    ASSERT_NE(movie1, nullptr);  
    // Check movie 101
    ASSERT_NE(movie2, nullptr); 
	std::string created = NO_CONTENT;
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}

TEST_F(PatchCommandTest, Add_Movie_To_User) {
	App app;
	std::shared_ptr<User> user = std::make_shared<User>(2);
	app.addUser(user);

    std::vector<int> movieIds = {200};
    std::shared_ptr<ICommand> command = std::make_shared<PatchCommand>(2, movieIds);

    // Running Add movie command
    std::string output = command->execute(app);

    // Making sure user 2 indeed got the movie added
    auto movie = app.searchMovieByID(200);
    ASSERT_NE(movie, nullptr);
    // Movie added to the user watch list
    ASSERT_EQ(user->getMovies().size(), 1); 
    // User added to the movie's viewers list 
    ASSERT_EQ(movie->getViewers().size(), 1);  
	std::string created = NO_CONTENT;
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}

TEST_F(PatchCommandTest, Add_Multiple_Movies_To_User) {
	App app;
    // Adding 3 movies
    std::vector<int> movieIds = {500, 501, 502};
	std::shared_ptr<User> user = std::make_shared<User>(2);
	app.addUser(user);
    std::shared_ptr<ICommand> command = std::make_shared<PatchCommand>(2, movieIds);

    // Run command to add multiple movies
    std::string output = command->execute(app);

    // Ensure all movies are added to the user
    user = app.searchUserByID(2);
    ASSERT_NE(user, nullptr);
     // 3 movies should be added

    ASSERT_EQ(user->getMovies().size(), 3); 
    // Ensure each movie has the user in the viewers list
    for (int movieId : movieIds) {
        auto movie = app.searchMovieByID(movieId);
        ASSERT_NE(movie, nullptr);
        // User should be in the viewers list
        ASSERT_EQ(movie->getViewers().size(), 1);  
    }
	std::string created = NO_CONTENT;
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}

// We want to insure that movies are added once
TEST_F(PatchCommandTest, Existing_Movie_Added_twice) {
	App app;
    // Adding movie 500 to user 2 before the command
	std::shared_ptr<Movie> movie = std::make_shared<Movie>(500);
	app.addMovie(movie);
    std::vector<int> movieIds = {500};
	std::shared_ptr<User> user = std::make_shared<User>(2);
	app.addUser(user);

	// Add the movie to the user's list
    user->addMovie(movie);
    // Add the user as a viewer of the movie
    movie->addViewer(user);

    std::shared_ptr<ICommand> command = std::make_shared<PatchCommand>(2, movieIds);

    // Run command to add existing movie
    std::string output = command->execute(app);

    // User should exist
    user = app.searchUserByID(2);
    ASSERT_NE(user, nullptr);

	// Movie added only ones
    ASSERT_EQ(user->getMovies().size(), 1);
	// User added to movie only ones
	ASSERT_EQ(movie->getViewers().size(), 1);
	std::string created = NO_CONTENT;
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}
