#include "DeleteCommand.hpp"
#include <filesystem>
#include <string>
#include "App.hpp"
#include "User.hpp"
#include "Movie.hpp"
#include <fstream>
#include <sstream>
#include <iostream>
#include <algorithm>

DeleteCommand::DeleteCommand(int userId, const std::vector<int> &movieIds)
    : userId(userId), movieIdsToDelete(movieIds)
{
}

std::string DeleteCommand::execute(App &app)
{
    app.lock();
    std::shared_ptr<User> user = app.searchUserByID(userId);
    app.unlock();
    if (user == nullptr)
    {
        return NOT_FOUND;
    }
    else if (!movieExistsInUser(user))
    {
        return NOT_FOUND;
    }
    else
    {
        std::string filename = "user_" + std::to_string(userId) + ".txt";

        user->lock();
        for (int movieId : movieIdsToDelete)
        {
            app.lock();
            std::shared_ptr<Movie> movie = app.searchMovieByID(movieId);
            app.unlock();
            movie->lock();
        }
        deleteMovieFromFile(filename);
        for (int movieId : movieIdsToDelete)
        {
            app.lock();
            std::shared_ptr<Movie> movie = app.searchMovieByID(movieId);
            app.unlock();
            movie->unlock();
        }
        user->unlock();
        // Loop through moviesToDelete and call removeMovie
        for (int movieId : movieIdsToDelete)
        {
            // Find the movie object by ID
            std::shared_ptr<Movie> movie = app.searchMovieByID(movieId);
            if (movie != nullptr)
            {

                user->lock();
                user->removeMovie(movie);
                user->unlock();

                movie->lock();
                movie->removeViewer(user);
                movie->unlock();
            }
        }
    }
    return NO_CONTENT;
}

void DeleteCommand::deleteMovieFromFile(const std::string &filePath)
{
    // Setting the path:
    std::string filename = "/data/" + filePath;

    // Open the file for read:
    std::ifstream inputFile(filename);

    // Set a vector for the line in the file and copying the lines to it:
    std::string line;
    std::vector<std::string> fileLines;
    while (std::getline(inputFile, line))
    {
        fileLines.push_back(line);
    }
    inputFile.close();

    // Extract the second line where the Related movie is in:
    std::string secondLine = fileLines[1];

    // Remove the Related from the line:
    secondLine = secondLine.substr(9);

    // Dividing into ints:
    std::istringstream ss(secondLine);
    std::vector<int> relatedMovieIds;
    int movieId;
    while (ss >> movieId)
    {
        relatedMovieIds.push_back(movieId);
    }

    // Erasing the requested movies from the line:
    for (int movieIdToDelete : movieIdsToDelete)
    {
        relatedMovieIds.erase(std::remove(relatedMovieIds.begin(), relatedMovieIds.end(), movieIdToDelete), relatedMovieIds.end());
    }

    // Opening again the file to rewrite the correct data:
    std::ofstream outputFile(filename);

    // Rewriting the update data:
    // The first line:
    outputFile << fileLines[0] << std::endl;

    // Adding a Related prefix to the second line:
    outputFile << "Related: ";

    // Writing the update vector of movies:
    for (size_t i = 0; i < relatedMovieIds.size(); ++i)
    {
        outputFile << relatedMovieIds[i];
        if (i != relatedMovieIds.size() - 1)
        {
            // Seperated by space " ":
            outputFile << " ";
        }
    }
    outputFile.close();

    // Now we also need to delete the user from the movie files that are related to these movies
    for (int movieIdToDelete : movieIdsToDelete)
    {
        // Call removeUserFromMovieFile to delete the user from each movie's file
        removeUserFromMovieFile(movieIdToDelete);
    }
    return;
}

void DeleteCommand::removeUserFromMovieFile(int movieId)
{
    // Building the path:
    std::string filename = "/data/movie_" + std::to_string(movieId) + ".txt";

    // Opening the file for reading:
    std::ifstream inputFile(filename);

    // Read the file contant to a data struct:
    std::string line;
    std::vector<std::string> fileLines;
    while (std::getline(inputFile, line))
    {
        fileLines.push_back(line);
    }
    inputFile.close();

    // Getting the second line:
    std::string secondLine = fileLines[1];

    // Remove the "Related: ":

    secondLine = secondLine.substr(9);

    // Seperating the line by spaces " ":
    std::istringstream ss(secondLine);

    // Setting a vector of user Ids:
    std::vector<int> usersList;
    int userId;
    while (ss >> userId)
    {
        usersList.push_back(userId);
    }

    // Erase the relevant userID:
    usersList.erase(std::remove(usersList.begin(), usersList.end(), this->userId), usersList.end());

    // Rewriting to the file:
    std::ofstream outputFile(filename);

    // First row rewriting:
    outputFile << fileLines[0] << std::endl;
    outputFile << "Related: ";

    // Rewriting the users:
    for (size_t i = 0; i < usersList.size(); ++i)
    {
        outputFile << usersList[i];

        outputFile << " ";
    }

    outputFile.close();
    return;
}

bool DeleteCommand ::movieExistsInUser(std::shared_ptr<User> user)
{

    user->lock();
    const std::vector<std::shared_ptr<Movie>> &userMovies = user->getMovies();
    user->unlock();

    for (int movieIdToDelete : movieIdsToDelete)
    {
        bool movieFound = false;

        for (const auto &movie : userMovies)
        {
            if (movie->getId() == movieIdToDelete)
            {
                movieFound = true;
                break;
            }
        }

        if (!movieFound)
        {
            return false;
        }
    }
    return true;
}
