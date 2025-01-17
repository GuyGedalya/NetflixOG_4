#include <gtest/gtest.h>
#include <filesystem>
#include <fstream>
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include "DataManager.hpp"

// Function for printing the file:
void printFileContent(const std::string &filePath)
{
    std::ifstream file(filePath);
    if (!file.is_open())
    {
        std::cerr << "Failed to open file: " << filePath << std::endl;
        return;
    }

    std::cout << "Content of " << filePath << ":\n";
    std::string line;
    while (std::getline(file, line))
    {
        std::cout << line << "\n";
    }
    file.close();
}

// Test fixture
class LoadDataTest : public ::testing::Test
{
protected:
    App app;
    // path for the data:
    const std::string testDir = "/data";

    void SetUp() override
    {
        std::filesystem::remove_all(testDir);
        std::filesystem::create_directory(testDir);
    }

    void TearDown() override
    {
        std::filesystem::remove_all(testDir);
    }

    // Function for creating a file:
    void createTestFile(const std::string &fileName, const std::string &content)
    {
        std::ofstream file(testDir + "/" + fileName);
        file << content;
        file.close();
    }
};

// Basic data loading:
TEST_F(LoadDataTest, BasicDataLoading)
{
    createTestFile("user_1.txt", "user ID: 1\nRelated: 101");
    createTestFile("movie_101.txt", "movie ID: 101\nRelated: 1");


    app.loadData();

    EXPECT_EQ(app.getUsers().size(), 1);
    EXPECT_EQ(app.getMovies().size(), 1);

    auto user = app.searchUserByID(1);
    ASSERT_NE(user, nullptr);
    EXPECT_EQ(user->getMovies().size(), 1);
    EXPECT_EQ(user->getMovies()[0]->getId(), 101);

    auto movie = app.searchMovieByID(101);
    ASSERT_NE(movie, nullptr);
    EXPECT_EQ(movie->getViewers().size(), 1);
    EXPECT_EQ(movie->getViewers()[0]->getID(), 1);
}

// Duplicate creating avoidance:
TEST_F(LoadDataTest, AvoidsCreatingDuplicateObjects)
{
    createTestFile("user_1.txt", "user ID: 1\nRelated: 101");
    createTestFile("movie_101.txt", "movie ID: 101\nRelated: 1");


    app.loadData();

    EXPECT_EQ(app.getUsers().size(), 1);
    EXPECT_EQ(app.getMovies().size(), 1);

    auto user = app.searchUserByID(1);
    ASSERT_NE(user, nullptr);
    EXPECT_EQ(user->getMovies().size(), 1);
    EXPECT_EQ(user->getMovies()[0]->getId(), 101);

    auto movie = app.searchMovieByID(101);
    ASSERT_NE(movie, nullptr);
    EXPECT_EQ(movie->getViewers().size(), 1);
    EXPECT_EQ(movie->getViewers()[0]->getID(), 1);

    EXPECT_EQ(user->getMovies()[0], movie);
    EXPECT_EQ(movie->getViewers()[0], user);
}

// More than one file test:
TEST_F(LoadDataTest, HandlesMultipleFiles)
{
    createTestFile("user_1.txt", "user ID: 1\nRelated: 101 102");
    createTestFile("user_2.txt", "user ID: 2\nRelated: 103");
    createTestFile("movie_101.txt", "movie ID: 101\nRelated: 1");
    createTestFile("movie_102.txt", "movie ID: 102\nRelated: 1");
    createTestFile("movie_103.txt", "movie ID: 103\nRelated: 2");

    app.loadData();

    EXPECT_EQ(app.getUsers().size(), 2);
    EXPECT_EQ(app.getMovies().size(), 3);

    auto user1 = app.searchUserByID(1);
    ASSERT_NE(user1, nullptr);
    EXPECT_EQ(user1->getMovies().size(), 2);

    auto user2 = app.searchUserByID(2);
    ASSERT_NE(user2, nullptr);
    EXPECT_EQ(user2->getMovies().size(), 1);

    auto movie101 = app.searchMovieByID(101);
    ASSERT_NE(movie101, nullptr);
    EXPECT_EQ(movie101->getViewers().size(), 1);

    auto movie102 = app.searchMovieByID(102);
    ASSERT_NE(movie102, nullptr);
    EXPECT_EQ(movie102->getViewers().size(), 1);

    auto movie103 = app.searchMovieByID(103);
    ASSERT_NE(movie103, nullptr);
    EXPECT_EQ(movie103->getViewers().size(), 1);
}
