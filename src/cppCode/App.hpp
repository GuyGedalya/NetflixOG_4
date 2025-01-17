#ifndef APP_HPP
#define APP_HPP
#include <iostream>
#include <vector>
#include <memory>
#include "User.hpp"
#include "Movie.hpp"
#include "ICommand.hpp"
#include <mutex>

class App
{
private:
    std::vector<std::shared_ptr<User>> users;
    std::vector<std::shared_ptr<Movie>> movies;
    std::pair<std::string, int> parseFileName(const std::string &fileName);
    std::mutex mtx;

public:
    App();
    ~App() = default;

    App(const App &) = default;
    App &operator=(const App &) = default;
    App(App &&) = default;
    App &operator=(App &&) = default;

	// Adds user to the app
    void addUser(std::shared_ptr<User> user);
	// Adds a movie to the app
    void addMovie(std::shared_ptr<Movie> movie);
	// Finds a movie given an ID
    const std::shared_ptr<Movie> searchMovieByID(int searchId) const;
	// Finds a user given an ID
    const std::shared_ptr<User> searchUserByID(int searchId) const;
	// Returns all the users of the app
    const std::vector<std::shared_ptr<User>> &getUsers() const;
	// Returns all the movies in the app
    const std::vector<std::shared_ptr<Movie>> &getMovies() const;
	// Loads all the app data back to the app
    void loadData();
	// Function to extract the related ID to add to the object:
    std::vector<std::string> extractRelatedIDs(const std::string &filePath);
    
    void lock();
    void unlock();
    bool tryLock();

};

#endif // APP_HPP
