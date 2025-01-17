#include <gtest/gtest.h>
#include <sstream>
#include <iostream>
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include "GetCommand.hpp"
#include "PostCommand.hpp"
#include <algorithm>


class GetTest : public ::testing::Test {
protected:
    App app;

    void SetUp() override {
        // Add example users and movies to the app for testing
		std::vector<int> movies = {100, 101, 102, 103};
        auto cmd = std::make_shared<PostCommand>(1, movies);
		cmd->execute(app);

		movies = {101, 102, 104, 105};
        cmd = std::make_shared<PostCommand>(2, movies);
		cmd->execute(app);

		movies = {100, 104, 105};
        cmd = std::make_shared<PostCommand>(3, movies);
		cmd->execute(app);
    }

};

// Test 1: Recommend movies based on user similarity
TEST_F(GetTest, RecommendMoviesBasedOnSimilarity) {
	auto cmd = std::make_shared<GetCommand>(1, 104);
    std::string output = cmd->execute(app); 

    // Expected output: movies recommended based on similarity
    // Recommendation should print movie IDs line by line
    std::string expected = OK + "\n\n105"; 
    EXPECT_EQ(output, expected);
}

// Test 2: Limit recommendations to 10 movies
TEST_F(GetTest, RecommendLimitToTenMovies) {
    // Add many users and movies to test the limit
	std::vector<int> movies;
     // Creating a vector with 200-220 movie ids
    for (int i = 200; i <= 220; ++i) {
        movies.push_back(i);
    }
    for (int i = 4; i <= 20; ++i) {
        auto cmd = std::make_shared<PostCommand>(i, movies);
        cmd->execute(app);
    }

	auto cmd = std::make_shared<GetCommand>(1, 104);
    std::string output = cmd->execute(app);

	size_t delimiterPos = output.find("\n\n");
	std::string leftPart = output.substr(0, delimiterPos);
    std::string rightPart = output.substr(delimiterPos + 2); // +2 to skip "\n\n"

	EXPECT_EQ(leftPart, OK);

    // Count number of lines in output
    std::istringstream stream(rightPart);
    int spaceCount = std::count(std::istreambuf_iterator<char>(stream), std::istreambuf_iterator<char>(), ' ');
    ASSERT_LE(spaceCount, 9);
}

// Test 3: Exclude already watched movies
TEST_F(GetTest, ExcludeAlreadyWatchedMovies) {
    // User 1 already watched 100, 101, 102, 103
	auto cmd = std::make_shared<GetCommand>(1, 101); 
    std::string output = cmd->execute(app); 

    // Ensure output does not contain movies already watched
    std::vector<int> alreadyWatched = {100, 101, 102, 103};

	size_t delimiterPos = output.find("\n\n");
	std::string leftPart = output.substr(0, delimiterPos);
    std::string rightPart = output.substr(delimiterPos + 2); // +2 to skip "\n\n"

	EXPECT_EQ(leftPart, OK);

    std::istringstream stream(rightPart); // This is the recommendations

    int movieId;
    while (stream >> movieId) {
        ASSERT_TRUE(std::find(alreadyWatched.begin(), alreadyWatched.end(), movieId) == alreadyWatched.end());
    }
}

// Test 4: No recommendations for a user with no similarity
TEST_F(GetTest, NoRecommendationsForNoSimilarity) {
	std::vector<int> watched = {300};
     // Unique movie
	auto cmdAdd = std::make_shared<PostCommand>(4, watched);
    cmdAdd->execute(app);

	auto cmdRcm = std::make_shared<GetCommand>(4, 300);
    
    // User 4 has no similar users
    std::string output = cmdRcm->execute(app); 
    
	std::string expected = OK + "\n\n";
	EXPECT_EQ(output, expected);
}

// Should return Not Found is user doesn't exist
TEST_F(GetTest, NonExistentUser) {

	auto cmdRcm = std::make_shared<GetCommand>(4, 300);
    
    // User 4 has no similar users
    std::string output = cmdRcm->execute(app); 	
	std::string expected = NOT_FOUND;
	EXPECT_EQ(output, expected);
}


class HemiTestSetup : public ::testing::Test {
protected:
	App app;

    void SetUp() override {
        // Add example users and movies to the app for testing
		std::vector<int> movies = {100, 101, 102, 103};
        auto cmd = std::make_shared<PostCommand>(1, movies);
		cmd->execute(app);

		movies = {101, 102, 104, 105, 106};
        cmd = std::make_shared<PostCommand>(2, movies);
		cmd->execute(app);

		movies = {100, 104, 105, 107, 108};
        cmd = std::make_shared<PostCommand>(3, movies);
		cmd->execute(app);

		
		movies = {101, 105, 106, 107, 109, 110};
        cmd = std::make_shared<PostCommand>(4, movies);
		cmd->execute(app);

		movies = {100, 102, 103, 105, 108, 111};
        cmd = std::make_shared<PostCommand>(5, movies);
		cmd->execute(app);

		
		movies = {100,103,104, 110, 111, 112, 113};
        cmd = std::make_shared<PostCommand>(6, movies);
		cmd->execute(app);

		movies = {102, 105, 106, 107, 108, 109, 110};
        cmd = std::make_shared<PostCommand>(7, movies);
		cmd->execute(app);

		
		movies = {101, 104, 105, 106, 109, 111, 114};
        cmd = std::make_shared<PostCommand>(8, movies);
		cmd->execute(app);

		movies = {100, 103, 105, 107, 112, 113, 115};
        cmd = std::make_shared<PostCommand>(9, movies);
		cmd->execute(app);
		
		movies = {100, 102, 105, 106, 107, 109, 110, 116};
        cmd = std::make_shared<PostCommand>(10, movies);
		cmd->execute(app);
    }

};


TEST_F(HemiTestSetup, exampleFromHomework) {
	// Like the example
	auto cmd = std::make_shared<GetCommand>(1, 104);

   
    // Print recommendations for user 1 and movie 104
    std::string output = cmd->execute(app);
    

    // Expected output: movies recommended based on similarity
    // Recommendation should print movie IDs line by line
    std::string expected = OK + "\n\n105 106 111 110 112 113 107 108 109 114"; 
    EXPECT_EQ(output, expected);
}


