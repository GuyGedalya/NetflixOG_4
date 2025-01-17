#include <gtest/gtest.h>
#include <filesystem>
#include "App.hpp"
#include "DataManager.hpp"
#include <fstream>

class DataManagerTest : public ::testing::Test
{
protected:
    // Relative path:
    std::string testDirectory = "/data";

	void SetUp() override{
        std::filesystem::remove_all(testDirectory);
        std::filesystem::create_directory(testDirectory);
    }

    void TearDown() override{
    }
};

TEST_F(DataManagerTest, UserFileCreation)
{
	App app;
    auto user = std::make_shared<User>(1);
    app.addUser(user);

    std::string userFile = testDirectory + "/user_1.txt";

    // Check if the file exists:
    ASSERT_TRUE(std::filesystem::exists(userFile)) << "User file was not created.";

    // Open file for reading:
    std::ifstream file(userFile);
    ASSERT_TRUE(file.is_open()) << "Failed to open user file.";

    // Check first row - "user ID: <ID>"
    std::string line;
    ASSERT_TRUE(std::getline(file, line)) << "File is empty or failed to read first line.";
    ASSERT_EQ(line, "user ID: 1") << "First line in file does not match the expected format.";

    // Check second row - "Related:"
    ASSERT_TRUE(std::getline(file, line)) << "File does not contain a second line.";
    ASSERT_EQ(line, "Related:") << "Second line in file does not match the expected format.";

    file.close();
}

TEST_F(DataManagerTest, MovieFileCreation)
{
	App app;
    auto movie = std::make_shared<Movie>(101);
    app.addMovie(movie);
    // file path
    std::string movieFile = testDirectory + "/movie_101.txt";

    // Check for existing:
    ASSERT_TRUE(std::filesystem::exists(movieFile)) << "Movie file was not created.";

    // Open for reading
    std::ifstream file(movieFile);
    ASSERT_TRUE(file.is_open()) << "Failed to open movie file.";

    // Check first row- "movie ID: <ID>"
    std::string line;
    ASSERT_TRUE(std::getline(file, line)) << "File is empty or failed to read first line.";
    ASSERT_EQ(line, "movie ID: 101") << "First line in file does not match the expected format.";

    // Check second row- "Related:"
    ASSERT_TRUE(std::getline(file, line)) << "File does not contain a second line.";
    ASSERT_EQ(line, "Related:") << "Second line in file does not match the expected format.";
    file.close();
}

TEST_F(DataManagerTest, MultipleUsersCreation)
{
	App app;
    for (int i = 1; i <= 5; ++i)
    {
        auto user = std::make_shared<User>(i);
        app.addUser(user);

        // Path:
        std::string userFile = testDirectory + "/user_" + std::to_string(i) + ".txt";

        // Check for existing:
        ASSERT_TRUE(std::filesystem::exists(userFile)) << "User file " << userFile << " was not created.";

        // Open for reading:
        std::ifstream file(userFile);
        ASSERT_TRUE(file.is_open()) << "Failed to open user file " << userFile;

        // First row checking - "user ID: <ID>"
        std::string line;
        ASSERT_TRUE(std::getline(file, line)) << "File " << userFile << " is empty or failed to read first line.";
        ASSERT_EQ(line, "user ID: " + std::to_string(i)) << "First line in file " << userFile << " does not match the expected format.";

        // Second row checking - "Related:"
        ASSERT_TRUE(std::getline(file, line)) << "File " << userFile << " does not contain a second line.";
        ASSERT_EQ(line, "Related:") << "Second line in file " << userFile << " does not match the expected format.";

        file.close();
    }
}

TEST_F(DataManagerTest, MultipleMoviesCreation)
{
	App app;
    for (int i = 101; i <= 105; ++i)
    {
        auto movie = std::make_shared<Movie>(i);
        app.addMovie(movie);

        // Path:
        std::string movieFile = testDirectory + "/movie_" + std::to_string(i) + ".txt";

        // Check if the file exists:
        ASSERT_TRUE(std::filesystem::exists(movieFile)) << "Movie file " << movieFile << " was not created.";

        // Open for reading:
        std::ifstream file(movieFile);
        ASSERT_TRUE(file.is_open()) << "Failed to open movie file " << movieFile;

        // First row checking - "movie ID: <ID>"
        std::string line;
        ASSERT_TRUE(std::getline(file, line)) << "File " << movieFile << " is empty or failed to read first line.";
        ASSERT_EQ(line, "movie ID: " + std::to_string(i)) << "First line in file " << movieFile << " does not match the expected format.";

        // Second row checking - "Related:"
        ASSERT_TRUE(std::getline(file, line)) << "File " << movieFile << " does not contain a second line.";
        ASSERT_EQ(line, "Related:") << "Second line in file " << movieFile << " does not match the expected format.";

        file.close();
    }
}
