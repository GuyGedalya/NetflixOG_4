#ifndef MOVIE_HPP
#define MOVIE_HPP

#include "DataInterface.hpp"
#include <memory>
#include <string>
#include <vector>
#include <mutex>

class User;

class Movie
{
private:
    int id;
    // Interface for data managment:
    std::shared_ptr<DataInterface> dataInterface;
    std::vector<std::shared_ptr<User>> users;
    std::mutex mtx;

public:
    explicit Movie(int id);
    void addViewer(std::shared_ptr<User> user);
    int getId() const;
    const std::vector<std::shared_ptr<User>> &getViewers() const;
    void restoreViewer(std::shared_ptr<User> user);
    void removeViewer(std::shared_ptr<User> user);

    void lock();
    void unlock();
    bool tryLock();
};

#endif // MOVIE_HPP
