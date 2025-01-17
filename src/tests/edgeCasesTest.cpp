#include <gtest/gtest.h>
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include <memory>
#include <iostream>
#include <limits>

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
// Test adding maximum number of users
TEST_F(AppTest, AddMaxUsers)
{
    for (int i = 0; i < 10000; ++i)
    {
        auto user = createUser(i);
        app.addUser(user);
    }

    auto foundUser = app.searchUserByID(9999);
    ASSERT_NE(foundUser, nullptr);
    EXPECT_EQ(foundUser->getID(), 9999);
}

// Test searching with invalid IDs
TEST_F(AppTest, SearchWithInvalidIDs)
{
    EXPECT_EQ(app.searchUserByID(-1), nullptr);                               // Negative ID
    EXPECT_EQ(app.searchMovieByID(std::numeric_limits<int>::max()), nullptr); // Overflow ID
}

// Test adding movie with extreme ID values
TEST(UserTest, AddExtremeMovieIDs)
{
    auto user = std::make_shared<User>(1);
    auto minMovie = std::make_shared<Movie>(std::numeric_limits<int>::min());
    auto maxMovie = std::make_shared<Movie>(std::numeric_limits<int>::max());

    user->addMovie(minMovie);
    user->addMovie(maxMovie);

    auto userMovies = user->getMovies();
    ASSERT_EQ(userMovies.size(), 2);
    EXPECT_EQ(userMovies[0]->getId(), std::numeric_limits<int>::min());
    EXPECT_EQ(userMovies[1]->getId(), std::numeric_limits<int>::max());
}

// Test adding nullptr to user's movies
TEST(UserTest, AddNullMovie)
{
    auto user = std::make_shared<User>(1);
    // Should safely ignore
    user->addMovie(nullptr);

    auto userMovies = user->getMovies();
    // No movie should be added
    EXPECT_EQ(userMovies.size(), 0);
}

// Test adding user with extreme ID values
TEST(MovieTest, AddExtremeUserIDs)
{
    auto movie = std::make_shared<Movie>(101);
    auto minUser = std::make_shared<User>(std::numeric_limits<int>::min());
    auto maxUser = std::make_shared<User>(std::numeric_limits<int>::max());

    movie->addViewer(minUser);
    movie->addViewer(maxUser);

    auto movieViewers = movie->getViewers();
    ASSERT_EQ(movieViewers.size(), 2);
    EXPECT_EQ(movieViewers[0]->getID(), std::numeric_limits<int>::min());
    EXPECT_EQ(movieViewers[1]->getID(), std::numeric_limits<int>::max());
}

// Test adding nullptr to movie's viewers
TEST(MovieTest, AddNullViewer)
{
    auto movie = std::make_shared<Movie>(101);
    // Should safely ignore
    movie->addViewer(nullptr);

    auto movieViewers = movie->getViewers();
    // No viewer should be added
    EXPECT_EQ(movieViewers.size(), 0);
}

// Test circular references
TEST(IntegrationTest, CircularReferences)
{
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

