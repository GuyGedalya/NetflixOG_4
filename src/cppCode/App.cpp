#include "App.hpp"
#include <algorithm>
#include <unordered_map>
#include <iostream>
#include <string>
#include <memory>
#include <functional>
#include <sstream>
#include <filesystem>
#include <fstream>
#include <iostream>
#include <iterator>
#include "ICommand.hpp"
#include "GetCommand.hpp"
#include "HelpCommand.hpp"
#include "PostCommand.hpp"

// Constructor
App::App() = default;

// Add User
void App::addUser(std::shared_ptr<User> user)
{
    users.push_back(user);
}

// Add Movie
void App::addMovie(std::shared_ptr<Movie> movie)
{
    movies.push_back(movie);
}

// Search Movie by ID
const std::shared_ptr<Movie> App::searchMovieByID(int searchId) const
{
    // Creating iterator:
    auto it = std::find_if(movies.begin(), movies.end(), [searchId](const std::shared_ptr<Movie> &movie)
                           { return movie->getId() == searchId; });

    if (it != movies.end())
    {
        return *it;
    }

    return nullptr;
}

// Search User by ID
const std::shared_ptr<User> App::searchUserByID(int searchId) const
{
    // creating iterator:
    auto it = std::find_if(users.begin(), users.end(), [searchId](const std::shared_ptr<User> &user)
                           { return user->getID() == searchId; });

    if (it != users.end())
    {
        return *it;
    }

    return nullptr;
}
const std::vector<std::shared_ptr<User>> &App::getUsers() const
{
    return users;
}

const std::vector<std::shared_ptr<Movie>> &App::getMovies() const
{
    return movies;
}

void App::loadData()
{
    const std::string dataDir = "/data";

    // Lists if user or movie and their ID:
    std::unordered_map<int, std::shared_ptr<User>> usersMap;
    std::unordered_map<int, std::shared_ptr<Movie>> moviesMap;

    // Loop to parse the file name and create the object:
    for (const auto &entry : std::filesystem::directory_iterator(dataDir))
    {
        const std::string fileName = entry.path().filename().string();
        // parsing:
        auto [type, id] = parseFileName(fileName);

        if (type == "user")
        {
            // Search for the user by its ID:
            auto user = searchUserByID(id);
            // If doesnt exists, create one:
            if (!user)
            {
                user = std::make_shared<User>(id);
                addUser(user);
            }
            usersMap[id] = user;
        }
        else if (type == "movie")
        {
            // Search for the movie by its ID:
            auto movie = searchMovieByID(id);
            if (!movie)
            {
                movie = std::make_shared<Movie>(id);
                addMovie(movie);
            }
            moviesMap[id] = movie;
        }
    }

    // Adding the relevant user of movie to the specific Object theyre related to:
    for (const auto &entry : std::filesystem::directory_iterator(dataDir))
    {
        const std::string fileName = entry.path().filename().string();
        auto [type, id] = parseFileName(fileName);

        if (type == "user")
        {
            // Getting the user ID from the map:
            auto user = usersMap[id];

            // Getting the relevant movies:
            auto relatedIDs = extractRelatedIDs(entry.path().string());
            for (const auto &idStr : relatedIDs)
            {
                // Convert from string to int:
                int movieId = std::stoi(idStr);
                // Retrieve the movie from the map:
                auto movie = moviesMap[movieId];

                // Check if the movie is already in the user's movie list:
                auto it = std::find(user->getMovies().begin(), user->getMovies().end(), movie);

                // If the movie is not already in the list, add it:
                if (it == user->getMovies().end())
                {
                    // Add the movie to the user's list
                    user->restoreMovie(movie);
                }
            }
        }
        else if (type == "movie")
        {
            // Getting the movie ID from the map:
            auto movie = moviesMap[id];

            // Getting the relevant users:
            auto relatedIDs = extractRelatedIDs(entry.path().string());

            for (const auto &idStr : relatedIDs)
            {
                // Convert from string to int:

                int userId = std::stoi(idStr);
                // Retrieve the user from the map:

                auto user = usersMap[userId];
                // Check if the user is already in the movie's viewers list
                auto it = std::find(movie->getViewers().begin(), movie->getViewers().end(), user);

                // If the user is not already in the list, add them:
                if (it == movie->getViewers().end())
                {
                    // Add the user to the movie's viewers
                    movie->restoreViewer(user);
                }
            }
        }
    }
}

// Function to parse the file name to help creating the Object:
std::pair<std::string, int> App::parseFileName(const std::string &fileName)
{
    std::string prefix;
    int id = -1;

    // Looking for the seperating '_'
    size_t underscorePos = fileName.find('_');
    size_t dotPos = fileName.find('.');

    if (underscorePos != std::string::npos && dotPos != std::string::npos)
    {
        // Getting the prefix:
        prefix = fileName.substr(0, underscorePos);
        // Getting the ID:
        std::string idStr = fileName.substr(underscorePos + 1, dotPos - underscorePos - 1);

        try
        {
            // Converting to int:
            id = std::stoi(idStr);
        }
        catch (const std::exception &e)
        {
        }
    }
    return {prefix, id};
}

// Function to extract the related ID to add to the object:
std::vector<std::string> App::extractRelatedIDs(const std::string &filePath)
{
    std::ifstream file(filePath);
    std::vector<std::string> relatedIDs;
    std::string line;

    while (std::getline(file, line))
    {
        // Check if the line starts with "Related:"
        if (line.rfind("Related:", 0) == 0)
        {
            // Extract the part after "Related: ":
            std::istringstream iss(line.substr(9));
            std::string id;
            while (iss >> id)
            {
                // Split the IDs
                relatedIDs.push_back(id);
            }
        }
    }
    return relatedIDs;
}

// Lock the mutex manually
void App::lock()
{
    mtx.lock();
}

// Unlock the mutex manually
void App::unlock()
{
    mtx.unlock();
}

// Try to lock the mutex (non-blocking)
bool App::tryLock()
{
    return mtx.try_lock(); 
}