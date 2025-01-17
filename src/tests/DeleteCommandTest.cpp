#include <gtest/gtest.h>
#include <fstream>
#include <sstream>
#include <vector>
#include <memory>
#include "DeleteCommand.hpp"
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include <filesystem>
#include <iostream>

// Helper to check if the movie deleted from the user file:
bool checkMovieDeleted(int userId, int movieId)
{
    std::ifstream file("/data/user_" + std::to_string(userId) + ".txt");
    if (!file.is_open())
    {

        std::cout << "couldnt open the file" << std::endl;
        return false;
    }

    std::string line;
    // Skip the user ID line
    std::getline(file, line);   
    // Read the "Related: " line          
    std::getline(file, line);   
    // Extract the movie IDs          
    std::stringstream ss(line.substr(9)); 
    int id;
    while (ss >> id)
    {
        if (id == movieId)
        {
            return false;
        }
    }
    return true;
}

// Helper to check if the user deleted from the movie file:
bool checkUserDeletedFromMovie(int movieId, int userId)
{
    std::ifstream file("/data/movie_" + std::to_string(movieId) + ".txt");
    if (!file.is_open())
        return false;

    std::string line;
      // Skip the movie ID line
    std::getline(file, line);           
    // Read the "Related: " line
    std::getline(file, line);             
    // Extract the user IDs
    std::stringstream ss(line.substr(9)); 
    int id;
    while (ss >> id)
    {
        if (id == userId)
        {
            return false;
        }
    }
    return true;
}
TEST(DeleteCommandTest, ClearDataFolder)
{
    // Data Folder
    std::string dataDir = "/data";

    // Checking if dir exists
    if (std::filesystem::exists(dataDir))
    {
        // Delete all the files in the folder
        for (const auto& entry : std::filesystem::directory_iterator(dataDir))
        {
            try
            {
                std::filesystem::remove(entry.path());
            }
            catch (const std::exception& e)
            {
                std::cerr << "Error deleting file: " << entry.path() << " - " << e.what() << std::endl;
            }
        }
    }
    else
    {
        std::cerr << "Data folder does not exist!" << std::endl;
    }

    // Make sure the folder is empty
    bool isEmpty = true;
    for (const auto& entry : std::filesystem::directory_iterator(dataDir))
    {
        isEmpty = false;
        break;
    }
    EXPECT_TRUE(isEmpty);
}

TEST(DeleteCommandTest, DeleteSingleMovie)
{
    App app; // Create App object

    // Create user and movie objects
    auto user = std::make_shared<User>(123);
    auto movie1 = std::make_shared<Movie>(101);
    auto movie2 = std::make_shared<Movie>(102);
    auto movie3 = std::make_shared<Movie>(103);

    // Add user and movies using App
    app.addUser(user);
    app.addMovie(movie1);
    app.addMovie(movie2);
    app.addMovie(movie3);

    // Link movies to user
    user->addMovie(movie1);
    user->addMovie(movie2);
    user->addMovie(movie3);

    // Link user to movies
    movie1->addViewer(user);
    movie2->addViewer(user);
    movie3->addViewer(user);

    std::vector<int> moviesToDelete = {102};
    DeleteCommand deleteCommand(user->getID(), moviesToDelete);
    deleteCommand.execute(app);


    EXPECT_TRUE(checkMovieDeleted(user->getID(), 102));  
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 101)); 
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 103)); 

    EXPECT_TRUE(checkUserDeletedFromMovie(102, user->getID()));  
    EXPECT_FALSE(checkUserDeletedFromMovie(101, user->getID())); 
    EXPECT_FALSE(checkUserDeletedFromMovie(103, user->getID())); 
}

TEST(DeleteCommandTest, DeleteMultipleMovies) {
    App app;  

    // Create user and movie objects
    auto user = std::make_shared<User>(124);
    auto movie1 = std::make_shared<Movie>(101);
    auto movie2 = std::make_shared<Movie>(102);
    auto movie3 = std::make_shared<Movie>(103);
    auto movie4 = std::make_shared<Movie>(104);

    // Add user and movies using App
    app.addUser(user);
    app.addMovie(movie1);
    app.addMovie(movie2);
    app.addMovie(movie3);
    app.addMovie(movie4);

    // Link movies to user
    user->addMovie(movie1);
    user->addMovie(movie2);
    user->addMovie(movie3);
    user->addMovie(movie4);

    // Link user to movies
    movie1->addViewer(user);
    movie2->addViewer(user);
    movie3->addViewer(user);
    movie4->addViewer(user);

    std::vector<int> moviesToDelete = {102, 104};
    DeleteCommand deleteCommand(user->getID(), moviesToDelete);
    deleteCommand.execute(app);  

    EXPECT_TRUE(checkMovieDeleted(user->getID(), 102)); 
    EXPECT_TRUE(checkMovieDeleted(user->getID(), 104)); 
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 101)); 
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 103)); 

    EXPECT_TRUE(checkUserDeletedFromMovie(102, user->getID())); 
    EXPECT_FALSE(checkUserDeletedFromMovie(101, user->getID())); 
    EXPECT_FALSE(checkUserDeletedFromMovie(103, user->getID())); 
    EXPECT_TRUE(checkUserDeletedFromMovie(104, user->getID())); 
}

TEST(DeleteCommandTest, DeleteMovieNotInList) {
    App app;  

    // Create user and movie objects
    auto user = std::make_shared<User>(125);
    auto movie1 = std::make_shared<Movie>(101);
    auto movie2 = std::make_shared<Movie>(102);
    auto movie3 = std::make_shared<Movie>(103);

    // Add user and movies using App
    app.addUser(user);
    app.addMovie(movie1);
    app.addMovie(movie2);
    app.addMovie(movie3);

    // Link movies to user
    user->addMovie(movie1);
    user->addMovie(movie2);
    user->addMovie(movie3);

    // Link user to movies
    movie1->addViewer(user);
    movie2->addViewer(user);
    movie3->addViewer(user);

    // Movie that doesn't exist:
    std::vector<int> moviesToDelete = {105}; 
    DeleteCommand deleteCommand(user->getID(), moviesToDelete);
    deleteCommand.execute(app);  

    EXPECT_TRUE(checkMovieDeleted(user->getID(), 105)); 
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 101)); 
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 102)); 
    EXPECT_FALSE(checkMovieDeleted(user->getID(), 103)); 

    EXPECT_FALSE(checkUserDeletedFromMovie(105, user->getID())); 
    EXPECT_FALSE(checkUserDeletedFromMovie(101, user->getID())); 
    EXPECT_FALSE(checkUserDeletedFromMovie(102, user->getID())); 
    EXPECT_FALSE(checkUserDeletedFromMovie(103, user->getID())); 
}

TEST(DeleteCommandTest, DeleteUserNotFound) {
    App app;  
    // Non-existing user ID:
    int userId = 999;  
    std::vector<int> moviesToDelete = {101, 102};

    DeleteCommand deleteCommand(userId, moviesToDelete);
    deleteCommand.execute(app);  

    // This user ID should not have a file:
    EXPECT_FALSE(std::ifstream("/data/user_" + std::to_string(userId) + ".txt").is_open());
}

TEST(DeleteCommandTest, DeleteSingleMovieSequentiallyFromMultipleUsers)
{
    App app;

    // Create users and a shared movie
    auto user1 = std::make_shared<User>(126);
    auto user2 = std::make_shared<User>(127);
    auto movie = std::make_shared<Movie>(101);

    // Add users and movie to the app
    app.addUser(user1);
    app.addUser(user2);
    app.addMovie(movie);

    // Link movie to users
    user1->addMovie(movie);
    user2->addMovie(movie);

    // Link users to movie
    movie->addViewer(user1);
    movie->addViewer(user2);

    // User1 deletes the movie
    std::vector<int> moviesToDelete = {101};
    DeleteCommand deleteCommand1(user1->getID(), moviesToDelete);
    deleteCommand1.execute(app);

    // Verify movie is deleted only for user1
    EXPECT_TRUE(checkMovieDeleted(user1->getID(), 101));
    EXPECT_EQ(user1->getMovies().size(), 0);
    EXPECT_EQ(user2->getMovies().size(), 1);

    // User2 deletes the movie
    DeleteCommand deleteCommand2(user2->getID(), moviesToDelete);
    deleteCommand2.execute(app);

    // Verify movie is deleted for user2
    EXPECT_TRUE(checkMovieDeleted(user2->getID(), 101));
    EXPECT_EQ(user2->getMovies().size(), 0);

    // Verify the movie still exists but has no viewers
    EXPECT_EQ(movie->getViewers().size(), 0);
}

TEST(DeleteCommandTest, DeleteMultipleMoviesSequentiallyFromMultipleUsers)
{
    App app;

    // Create users and movies
    auto user1 = std::make_shared<User>(128);
    auto user2 = std::make_shared<User>(129);
    auto movie1 = std::make_shared<Movie>(101);
    auto movie2 = std::make_shared<Movie>(102);

    // Add users and movies to the app
    app.addUser(user1);
    app.addUser(user2);
    app.addMovie(movie1);
    app.addMovie(movie2);

    // Link movies to users
    user1->addMovie(movie1);
    user1->addMovie(movie2);
    user2->addMovie(movie1);
    user2->addMovie(movie2);

    // User1 deletes the movies
    std::vector<int> moviesToDelete1 = {101, 102};
    DeleteCommand deleteCommand1(user1->getID(), moviesToDelete1);
    deleteCommand1.execute(app);

    // Verify movies are deleted only for user1
    EXPECT_TRUE(checkMovieDeleted(user1->getID(), 101));
    EXPECT_TRUE(checkMovieDeleted(user1->getID(), 102));
    EXPECT_EQ(user1->getMovies().size(), 0);
    EXPECT_EQ(user2->getMovies().size(), 2);

    // User2 deletes the movies
    std::vector<int> moviesToDelete2 = {101, 102};
    DeleteCommand deleteCommand2(user2->getID(), moviesToDelete2);
    deleteCommand2.execute(app);

    // Verify movies are deleted for user2
    EXPECT_TRUE(checkMovieDeleted(user2->getID(), 101));
    EXPECT_TRUE(checkMovieDeleted(user2->getID(), 102));
    EXPECT_EQ(user2->getMovies().size(), 0);

    // Verify the movies still exist but have no viewers
    EXPECT_EQ(movie1->getViewers().size(), 0);
    EXPECT_EQ(movie2->getViewers().size(), 0);
}

TEST(DeleteCommandTest, DeleteMultipleMoviesFromEachUser)
{
    App app;

    // Create users and movies
    auto user1 = std::make_shared<User>(130);
    auto user2 = std::make_shared<User>(131);
    auto movie1 = std::make_shared<Movie>(103);
    auto movie2 = std::make_shared<Movie>(104);
    auto movie3 = std::make_shared<Movie>(105);

    // Add users and movies to the app
    app.addUser(user1);
    app.addUser(user2);
    app.addMovie(movie1);
    app.addMovie(movie2);
    app.addMovie(movie3);

    // Link movies to users
    user1->addMovie(movie1);
    movie1->addViewer(user1);
    user1->addMovie(movie2);
    movie2->addViewer(user1);
    user1->addMovie(movie3);
    movie3->addViewer(user1);
    user2->addMovie(movie1);
    movie1->addViewer(user2);
    user2->addMovie(movie2);
    movie2->addViewer(user2);
    user2->addMovie(movie3);
    movie3->addViewer(user2);

    // // User1 deletes movies
    std::vector<int> moviesToDelete1 = {103, 104};
    DeleteCommand deleteCommand1(user1->getID(), moviesToDelete1);
    deleteCommand1.execute(app);

    // Verify movies are deleted for user1
    EXPECT_TRUE(checkMovieDeleted(user1->getID(), 103));
    EXPECT_TRUE(checkMovieDeleted(user1->getID(), 104));
    EXPECT_EQ(user1->getMovies().size(), 1);
    EXPECT_EQ(user2->getMovies().size(), 3);

    // User2 deletes movies
    std::vector<int> moviesToDelete2 = {103, 104, 105};
    DeleteCommand deleteCommand2(user2->getID(), moviesToDelete2);
    deleteCommand2.execute(app);

    // Verify movies are deleted for user2
    EXPECT_TRUE(checkMovieDeleted(user2->getID(), 103));
    EXPECT_TRUE(checkMovieDeleted(user2->getID(), 104));
    EXPECT_TRUE(checkMovieDeleted(user2->getID(), 105));
    EXPECT_EQ(user2->getMovies().size(), 0);

    // Verify only movie3 is left for user1
    EXPECT_EQ(user1->getMovies().size(), 1);
    EXPECT_EQ(user1->getMovies()[0]->getId(), 105);

    // Verify viewer lists of movies
    EXPECT_EQ(movie1->getViewers().size(), 0);
    EXPECT_EQ(movie2->getViewers().size(), 0);
    EXPECT_EQ(movie3->getViewers().size(), 1);
}

TEST(DeleteCommandTest, CheckUserExists) {
    App app;  
    // Non-existing user ID:
    auto user1 = std::make_shared<User>(140);
    app.addUser(user1);
    int userId = 140;
    auto movie1 = std::make_shared<Movie>(103);
    app.addMovie(movie1);
    user1->addMovie(movie1);
    movie1->addViewer(user1);
    std::vector<int> moviesToDelete1 = {103};

    DeleteCommand deleteCommand(userId, moviesToDelete1);
    deleteCommand.execute(app);  

    EXPECT_TRUE(app.searchUserByID(140) != nullptr);
    
}