#include <gtest/gtest.h>
#include "PostCommand.hpp"
#include "App.hpp"

class PostCommandTest: public ::testing::Test{

};

// Checking that Post command fails if user in app
TEST_F(PostCommandTest, Check_Fail_If_User_Exist) {
	App app;
	// Adding user 1 to the app before the post operation
	std::shared_ptr<User> user = std::make_shared<User>(1);
	app.addUser(user);

	std::shared_ptr<ICommand> command = std::make_shared<PostCommand>(1, std::vector<int>{100});
	std::string output = command->execute(app);
	std::string notFound = "404 Not Found";
	// Expecting 404 if user already in the app
	EXPECT_STREQ(output.c_str(), notFound.c_str());
}

// Check if user and movie added when not in the app
TEST_F(PostCommandTest, Add_User_And_Movie_When_Not_Exist) {
	App app;
    // Adding movie's and to user that doesn't exist
    std::vector<int> movieIds = {100, 101};
    std::shared_ptr<ICommand> command = std::make_shared<PostCommand>(1, movieIds);

    // Running the command to add the movies 100 and 101 to user 1
    std::string output = command->execute(app);

    // Making sure user 1 exists 
    auto user = app.searchUserByID(1);
    ASSERT_NE(user, nullptr);  

    // Making sure both movies exists
    auto movie1 = app.searchMovieByID(100);
    auto movie2 = app.searchMovieByID(101);
    // Check movie 100
    ASSERT_NE(movie1, nullptr);  
    // Check movie 101
    ASSERT_NE(movie2, nullptr); 
	std::string created = "201 Created";
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}

TEST_F(PostCommandTest, Add_Movie_To_User) {
	App app;

    std::vector<int> movieIds = {200};
    std::shared_ptr<ICommand> command = std::make_shared<PostCommand>(2, movieIds);

    // Runing Add movie command
    std::string output = command->execute(app);
	std::shared_ptr<User> user = app.searchUserByID(2);
	// Check the the user is indeed added
	ASSERT_NE(user, nullptr);

    // Making sure user 2 indeed got the movie added
    auto movie = app.searchMovieByID(200);
    ASSERT_NE(movie, nullptr);
    // Movie added to the user watch list
    ASSERT_EQ(user->getMovies().size(), 1); 
    // User added to the movie's viewers list 
    ASSERT_EQ(movie->getViewers().size(), 1);  
	std::string created = "201 Created";
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}

TEST_F(PostCommandTest, Add_Multiple_Movies_To_User) {
	App app;
    // Adding 3 movies
    std::vector<int> movieIds = {500, 501, 502};  
    std::shared_ptr<ICommand> command = std::make_shared<PostCommand>(2, movieIds);

    // Run command to add multiple movies
    std::string output = command->execute(app);

    // Ensure all movies are added to the user
    auto user = app.searchUserByID(2);
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
	std::string created = "201 Created";
	// Expecting 201 if executed successfully
	EXPECT_STREQ(output.c_str(), created.c_str());
}
