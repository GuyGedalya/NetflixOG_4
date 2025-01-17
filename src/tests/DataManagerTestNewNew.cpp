#include <gtest/gtest.h>
#include "App.hpp"
#include "DataManager.hpp"
#include "PostCommand.hpp"
#include "ICommand.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include <filesystem>
#include <fstream>

// Test fixture
class DataManagerTest : public ::testing::Test
{
protected:
    const std::string testDir = "/data";

    void SetUp() override
    {
        std::filesystem::remove_all(testDir);
        std::filesystem::create_directory(testDir);
    }

    void TearDown() override
    {
    }

    // Function to read the file:
    std::string readFile(const std::string &filePath)
    {
        std::ifstream file(filePath);
        if (!file.is_open())
        {
            return "";
        }
        return std::string((std::istreambuf_iterator<char>(file)),
                           std::istreambuf_iterator<char>());
    }
};

// Creating unique files:
TEST_F(DataManagerTest, CreateFilesForInstances)
{
    User user(1);
    Movie movie(101);

    // Check if the file exists:
    EXPECT_TRUE(std::filesystem::exists(testDir + "/user_1.txt"));
    EXPECT_TRUE(std::filesystem::exists(testDir + "/movie_101.txt"));

    // Check the file text:
    std::string userContent = readFile(testDir + "/user_1.txt");
    EXPECT_NE(userContent.find("user ID: 1"), std::string::npos);
    EXPECT_NE(userContent.find("Related:"), std::string::npos);

    std::string movieContent = readFile(testDir + "/movie_101.txt");
    EXPECT_NE(movieContent.find("movie ID: 101"), std::string::npos);
    EXPECT_NE(movieContent.find("Related:"), std::string::npos);
}

// Check file update
TEST_F(DataManagerTest, UpdateUserFile)
{
    App app;
    User user(1);
    Movie movie(101);

    std::unique_ptr<ICommand> command = std::make_unique<PostCommand>(user.getID(), std::vector<int>{movie.getId()});
    command->execute(app);

    // Check user file:
    std::string userContent = readFile(testDir + "/user_1.txt");
    EXPECT_NE(userContent.find("user ID: 1"), std::string::npos);
    EXPECT_NE(userContent.find("Related: 101"), std::string::npos);

    // Check movie file:
    std::string movieContent = readFile(testDir + "/movie_101.txt");
    EXPECT_NE(movieContent.find("movie ID: 101"), std::string::npos);
    EXPECT_NE(movieContent.find("Related: 1"), std::string::npos);
}

// Check file update:
TEST_F(DataManagerTest, UpdateMovieFile)
{
    App app;
    User user(1);
    Movie movie(101);

    std::unique_ptr<ICommand> command = std::make_unique<PostCommand>(user.getID(), std::vector<int>{movie.getId()});
    command->execute(app);

    // Check movie file:
    std::string movieContent = readFile(testDir + "/movie_101.txt");
    EXPECT_NE(movieContent.find("movie ID: 101"), std::string::npos);
    EXPECT_NE(movieContent.find("Related: 1"), std::string::npos);

    // Check user file:
    std::string userContent = readFile(testDir + "/user_1.txt");
    EXPECT_NE(userContent.find("user ID: 1"), std::string::npos);
    EXPECT_NE(userContent.find("Related: 101"), std::string::npos);
}

// Adding more than one movie:
TEST_F(DataManagerTest, AddMultipleMoviesToUser)
{
    App app;
    User user(1);
    Movie movie1(101);
    Movie movie2(102);

    std::unique_ptr<ICommand> command = std::make_unique<PostCommand>(user.getID(), std::vector<int>{movie1.getId(), movie2.getId()});
    command->execute(app);

    // Check user file:
    std::string userContent = readFile(testDir + "/user_1.txt");
    EXPECT_NE(userContent.find("user ID: 1"), std::string::npos);
    EXPECT_NE(userContent.find("Related: 101 102"), std::string::npos);

    // Check movie file:
    std::string movie1Content = readFile(testDir + "/movie_101.txt");
    EXPECT_NE(movie1Content.find("movie ID: 101"), std::string::npos);
    EXPECT_NE(movie1Content.find("Related: 1"), std::string::npos);

    std::string movie2Content = readFile(testDir + "/movie_102.txt");
    EXPECT_NE(movie2Content.find("movie ID: 102"), std::string::npos);
    EXPECT_NE(movie2Content.find("Related: 1"), std::string::npos);
}

// Adding more than one viewer:
TEST_F(DataManagerTest, AddMultipleViewersToMovie)
{
    App app;
    User user1(1);
    User user2(2);
    Movie movie(101);

    std::unique_ptr<ICommand> command1 = std::make_unique<PostCommand>(user1.getID(), std::vector<int>{movie.getId()});
    command1->execute(app);

    std::unique_ptr<ICommand> command2 = std::make_unique<PostCommand>(user2.getID(), std::vector<int>{movie.getId()});
    command2->execute(app);

    // Check the movie file:
    std::string movieContent = readFile(testDir + "/movie_101.txt");
    EXPECT_NE(movieContent.find("movie ID: 101"), std::string::npos);
    EXPECT_NE(movieContent.find("Related: 1 2"), std::string::npos);

    // Check the user files:
    std::string user1Content = readFile(testDir + "/user_1.txt");
    EXPECT_NE(user1Content.find("user ID: 1"), std::string::npos);
    EXPECT_NE(user1Content.find("Related: 101"), std::string::npos);

    std::string user2Content = readFile(testDir + "/user_2.txt");
    EXPECT_NE(user2Content.find("user ID: 2"), std::string::npos);
    EXPECT_NE(user2Content.find("Related: 101"), std::string::npos);
}
