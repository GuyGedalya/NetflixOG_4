#include "Movie.hpp"
#include "User.hpp"
#include "DataManager.hpp"
#include <iostream>
#include <vector>
#include <algorithm>

// Constructor:
Movie::Movie(int id) : id(id) {
    dataInterface = std::make_shared<DataManager>("movie", id);
}

// Function to add a viewer to the movie list:
void Movie::addViewer(std::shared_ptr<User> user) {
    if (!user) {
        return;
    }

    // Check if the user already in:
    for (const auto& existingUser : users) {
        if (existingUser == user) {
            return; 
        }
    }

    users.push_back(user);

    // Updating the file of the movie:
    if (dataInterface) {
        dataInterface->update(id, "movie", user->getID());
    }
}

// Restore the data after closing:
void Movie::restoreViewer(std::shared_ptr<User> user) {
    if (!user) {
        return;
    }

    // Simply push the user into the 'users' vector:
    users.push_back(user);
}

int Movie::getId() const {
    return id;
}
const std::vector<std::shared_ptr<User>>& Movie::getViewers() const {
    return users;
}

// Function to remove a viewer from the list of viewer:
void Movie::removeViewer(std::shared_ptr<User> user) {
    // Creating an iterator to find for the viewer:
    auto it = std::find_if(users.begin(), users.end(),
                           [&user](const std::shared_ptr<User>& u) { return u->getID() == user->getID(); });
    // If found - remove it:
    if (it != users.end()) {
        //Remove user from list:
        users.erase(it);  
    }
}

// Lock the mutex manually
void Movie::lock()
{
    mtx.lock();
}

// Unlock the mutex manually
void Movie::unlock()
{
    mtx.unlock();
}

// Try to lock the mutex (non-blocking)
bool Movie::tryLock()
{
    return mtx.try_lock(); 
}