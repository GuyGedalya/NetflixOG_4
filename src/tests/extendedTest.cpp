#include <gtest/gtest.h>
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include <memory>
#include <iostream>

// Test fixture for App
class AppTest : public ::testing::Test
{
protected:
    App app;

    std::shared_ptr<User> createUser(int id)
    {
        return std::make_shared<User>(id);
    }

    std::shared_ptr<Movie> createMovie(int id)
    {
        return std::make_shared<Movie>(id);
    }
};

// Test adding multiple users with the same ID
TEST_F(AppTest, AddDuplicateUsers)
{
    auto user1 = createUser(1);
    auto user2 = createUser(1);

    app.addUser(user1);
    // Adding a user with the same ID should not cause issues
    app.addUser(user2);

    auto foundUser = app.searchUserByID(1);
    ASSERT_NE(foundUser, nullptr);
    // Should return the first user added
    EXPECT_EQ(foundUser->getID(), 1);
}

// Test adding multiple movies with the same ID
TEST_F(AppTest, AddDuplicateMovies)
{
    auto movie1 = createMovie(101);
    auto movie2 = createMovie(101);

    app.addMovie(movie1);
    // Adding a movie with the same ID should not cause issues
    app.addMovie(movie2);

    auto foundMovie = app.searchMovieByID(101);
    ASSERT_NE(foundMovie, nullptr);
    // Should return the first movie added
    EXPECT_EQ(foundMovie->getId(), 101);
}

// Test removing users (not implemented yet)
TEST_F(AppTest, SearchAfterAddingAndRemovingUser)
{
    auto user = createUser(2);
    app.addUser(user);

    auto foundUser = app.searchUserByID(2);
    ASSERT_NE(foundUser, nullptr);
}

// Test adding the same movie multiple times to a user
TEST(UserTest, AddDuplicateMovies)
{
    auto user = std::make_shared<User>(1);
    auto movie = std::make_shared<Movie>(101);

    // Duplicate add
    user->addMovie(movie);
    user->addMovie(movie);

    // Ensure no duplicates
    auto userMovies = user->getMovies();
    ASSERT_EQ(userMovies.size(), 1);
    EXPECT_EQ(userMovies[0]->getId(), 101);
}

// Test adding the same user multiple times to a movie
TEST(MovieTest, AddDuplicateViewers)
{
    auto movie = std::make_shared<Movie>(101);
    auto user = std::make_shared<User>(1);
    // Duplicate add
    movie->addViewer(user);
    movie->addViewer(user);

    auto movieViewers = movie->getViewers();
    ASSERT_EQ(movieViewers.size(), 1);
    // Ensure no duplicates
    EXPECT_EQ(movieViewers[0]->getID(), 1);
}

// Test complex user and movie interactions
TEST(IntegrationTest, MultipleUsersAndMovies)
{
    auto user1 = std::make_shared<User>(1);
    auto user2 = std::make_shared<User>(2);
    auto movie1 = std::make_shared<Movie>(101);
    auto movie2 = std::make_shared<Movie>(102);

    user1->addMovie(movie1);
    user2->addMovie(movie2);
    movie1->addViewer(user1);
    movie2->addViewer(user2);

    // Validate user1
    auto user1Movies = user1->getMovies();
    ASSERT_EQ(user1Movies.size(), 1);
    EXPECT_EQ(user1Movies[0]->getId(), 101);

    // Validate movie1 viewers
    auto movie1Viewers = movie1->getViewers();
    ASSERT_EQ(movie1Viewers.size(), 1);
    EXPECT_EQ(movie1Viewers[0]->getID(), 1);

    // Validate user2
    auto user2Movies = user2->getMovies();
    ASSERT_EQ(user2Movies.size(), 1);
    EXPECT_EQ(user2Movies[0]->getId(), 102);

    // Validate movie2 viewers
    auto movie2Viewers = movie2->getViewers();
    ASSERT_EQ(movie2Viewers.size(), 1);
    EXPECT_EQ(movie2Viewers[0]->getID(), 2);
}
