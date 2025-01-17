#ifndef GETCOMMAND_HPP
#define GETCOMMAND_HPP

#include "ICommand.hpp"
#include "App.hpp"
#include "Movie.hpp" 
#include "User.hpp"  
#include <iostream>
#include <map>
#include <unordered_set>
#include <vector>
#include <algorithm>
#include <memory> 
#include <string>


class GetCommand : public ICommand {
public:
    // Constructor:
    GetCommand(int userId, int movieId);

    std::string execute(App& app) override;

private:
    int userId;               
    int recommandMovieId;     

	// Helper function to find similarity between our user and the other users
    std::map<int, int> findingSimilarity(App& app);

	// Helper function to remove every user who didn't watch the movie to recommand by
    void removeUnwatched(App& app, std::map<int, int>& similarityMap);

	// Helper function to map between a movie anf it's relevence
    std::map<int, int> movieByRelevence(App& app, std::map<int, int>& similarityMap);

	// Removing all the movies the user already watched
    void removeAlreadyWatched(App& app, std::map<int, int>& map);

	// Printing the recommendation by decending relevence
    std::string TopRelevantMovies(const std::map<int, int>& MovieRelevenceMap);
};

#endif 
