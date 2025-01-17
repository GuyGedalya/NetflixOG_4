#ifndef DELETECOMMAND_HPP
#define DELETECOMMAND_HPP

#include <vector>
#include <string>
#include "ICommand.hpp"
#include "User.hpp"
#include "Movie.hpp"

class DeleteCommand : public ICommand
{
public:
    DeleteCommand(int userId, const std::vector<int> &movieIds);

    std::string execute(App &app) override;

private:
    int userId;
    std::vector<int> movieIdsToDelete;
    bool movieExistsInUser(std::shared_ptr<User> user);
    void deleteMovieFromFile(const std::string &filePath);
    void removeUserFromMovieFile(int movieId);


    };

#endif // DELETECOMMAND_HPP