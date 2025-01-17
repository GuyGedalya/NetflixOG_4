#ifndef USER_HPP
#define USER_HPP

#include "DataInterface.hpp"
#include <memory>
#include <string>
#include <vector>
#include <mutex>

class Movie;

class User
{
private:
    int ID;
    // data interface for the file managment:
    std::shared_ptr<DataInterface> dataInterface;
    std::vector<std::shared_ptr<Movie>> movies;
    std::mutex mtx;

public:
    explicit User(int ID);

    void addMovie(std::shared_ptr<Movie> movie);
    int getID() const;
    const std::vector<std::shared_ptr<Movie>> &getMovies() const;
    void restoreMovie(std::shared_ptr<Movie> movie);
    void removeMovie(std::shared_ptr<Movie> movie);


    void lock();
    void unlock();
    bool tryLock();
};

#endif // USER_HPP
