#include <gtest/gtest.h>
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include <memory>
#include <iostream> 
#include <algorithm>

// Test fixture for App
class AppTest : public ::testing::Test {
protected:
    App app;

    std::shared_ptr<User> createUser(int id) {
        return std::make_shared<User>(id);
    }

    std::shared_ptr<Movie> createMovie(int id) {
        return std::make_shared<Movie>(id);
    }
};

// Test adding a user
TEST_F(AppTest, AddUser) {
    std::cout << "Running AddUser test..." << std::endl; 
    auto user = createUser(1);
    app.addUser(user);

    auto foundUser = app.searchUserByID(1);
    ASSERT_NE(foundUser, nullptr);
    EXPECT_EQ(foundUser->getID(), 1);
}

// Test adding a movie
TEST_F(AppTest, AddMovie) {
    std::cout << "Running AddMovie test..." << std::endl; 
    auto movie = createMovie(101);
    app.addMovie(movie);

    auto foundMovie = app.searchMovieByID(101);
    ASSERT_NE(foundMovie, nullptr);
    EXPECT_EQ(foundMovie->getId(), 101);
}

// Test searching for a non-existent user
TEST_F(AppTest, SearchNonExistentUser) {
    std::cout << "Running SearchNonExistentUser test..." << std::endl;
    auto foundUser = app.searchUserByID(999);
    EXPECT_EQ(foundUser, nullptr);
}

// Test searching for a non-existent movie
TEST_F(AppTest, SearchNonExistentMovie) {
    std::cout << "Running SearchNonExistentMovie test..." << std::endl; 
    auto foundMovie = app.searchMovieByID(999);
    EXPECT_EQ(foundMovie, nullptr);
}

// Test User and Movie interaction
TEST(UserMovieTest, UserMovieAssociation) {
    std::cout << "Running UserMovieAssociation test..." << std::endl; 
    auto user = std::make_shared<User>(1);
    auto movie = std::make_shared<Movie>(101);

    user->addMovie(movie);
    movie->addViewer(user);

    // Validate user's movies
    auto userMovies = user->getMovies();
    ASSERT_EQ(userMovies.size(), 1);
    EXPECT_EQ(userMovies[0]->getId(), 101);

    // Validate movie's viewers
    auto movieViewers = movie->getViewers();
    ASSERT_EQ(movieViewers.size(), 1);
    EXPECT_EQ(movieViewers[0]->getID(), 1);
}



