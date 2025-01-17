#include "User.hpp"
#include "Movie.hpp"
#include "DataManager.hpp"
#include <vector>
#include <iostream>
#include <algorithm>

// Constructor:
User::User(int ID) : ID(ID)
{
    dataInterface = std::make_shared<DataManager>("user", ID);
}

// Function to add a movie to the list:
void User::addMovie(std::shared_ptr<Movie> movie)
{
    if (!movie)
    {
        return;
    }

    // Check if the movie already in:
    for (const auto &existingMovie : movies)
    {
        if (existingMovie == movie)
        {
            return;
        }
    }

    // Adding movie to the list:
    movies.push_back(movie);

    // Update the file:
    if (dataInterface)
    {
        dataInterface->update(ID, "user", movie->getId());
    }
}

// Function to restore the data from the Movie:
void User::restoreMovie(std::shared_ptr<Movie> movie)
{
    if (!movie)
    {
        return;
    }

    // Simply push the movie into the 'movies' vector
    movies.push_back(movie);
}

int User::getID() const
{
    return ID;
}

const std::vector<std::shared_ptr<Movie>> &User::getMovies() const
{
    return movies;
}


// Function to remove a movie from the lists of movies:

void User::removeMovie(std::shared_ptr<Movie> movie)
{
    // Creating an iterator to look for the movie:
    auto it = std::find_if(movies.begin(), movies.end(), [movie](const std::shared_ptr<Movie> &m)
                           { return m->getId() == movie->getId(); });
    if (it != movies.end())
    {
        // If found - remove movie from the list:
        movies.erase(it); 
    }
}

// Lock the mutex manually
void User::lock()
{
    mtx.lock();
}

// Unlock the mutex manually
void User::unlock()
{
    mtx.unlock();
}

// Try to lock the mutex (non-blocking)
bool User::tryLock()
{
    return mtx.try_lock(); 
}

