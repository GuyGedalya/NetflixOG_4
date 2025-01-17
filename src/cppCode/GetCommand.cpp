#include "GetCommand.hpp"

GetCommand::GetCommand(int userId, int movieId)
    : userId(userId), recommandMovieId(movieId) {}

std::string GetCommand::execute(App &app)
{
    app.lock();
	auto thisUser = app.searchUserByID(userId);
    app.unlock();

	// If user doesn't exist, return NOT FOUND
	if (!thisUser) { 
		return NOT_FOUND;
	}
    // Calculate similarity between users
    std::map<int, int> similarityMap = findingSimilarity(app);
	
    // Exit if no similarity data
    if (similarityMap.empty()){
		std::string output = OK + "\n\n";
        return output;
	}
    // Remove users who haven't watched the specific movie:
    removeUnwatched(app, similarityMap);
    // Calculate movie relevance:
    std::map<int, int> movieRelevenceMap = movieByRelevence(app, similarityMap);
    // Filter out movies already watched by the user:
    removeAlreadyWatched(app, movieRelevenceMap);
    // Print the top relevant movies:
    std::string recommendatins = TopRelevantMovies(movieRelevenceMap);
	std::string output = OK + "\n\n" + recommendatins;

	return output; 
}

std::map<int, int> GetCommand::findingSimilarity(App &app)
{
    // Map to store similarity scores for each user
    std::map<int, int> similarityMap;
    // Find the current user
    app.lock();
    auto thisUser = app.searchUserByID(userId);
    app.unlock();


    // Locking the source:
    thisUser->lock();
    std::unordered_set<std::shared_ptr<Movie>> thisUserMovies(
        // Get movies watched by this user
        thisUser->getMovies().begin(), thisUser->getMovies().end());
    // Releasing the mutex:
    thisUser->unlock();


    // Get all users in the app
    const std::vector<std::shared_ptr<User>> appUsers = app.getUsers();
    for (const auto &otherUser : appUsers)
    {
        // Skip if comparing the same user
        if (otherUser->getID() == userId)
            continue;

        // Counter for similarity
        int similarCnt = 0;

        // Movies watched by another user

        otherUser->lock();
        std::unordered_set<std::shared_ptr<Movie>> otherUserMovies(
            otherUser->getMovies().begin(), otherUser->getMovies().end());
        otherUser->unlock();


        for (const auto &movieWatched : thisUserMovies)
        {
            if (otherUserMovies.find(movieWatched) != otherUserMovies.end())
            {
                // Increment counter if both users watched the same movie
                similarCnt++;
            }
        }
        // Store similarity score
        similarityMap[otherUser->getID()] = similarCnt;
    }
    // Return similarity map
    return similarityMap;
}

void GetCommand::removeUnwatched(App &app, std::map<int, int> &similarityMap)
{
    for (auto it = similarityMap.begin(); it != similarityMap.end();)
    {
        // Find the user in the app
        app.lock();
        auto user = app.searchUserByID(it->first);
        // Find the specific movie
        auto movieToFind = app.searchMovieByID(recommandMovieId);
        app.unlock();

        user->lock();
        if (!user || !movieToFind ||
            std::find(user->getMovies().begin(), user->getMovies().end(), movieToFind) == user->getMovies().end())
        {
            // Remove user if they haven't watched the movie
            it = similarityMap.erase(it);
        }
        else
        {
            // Move to the next user
            ++it;
        }
        user->unlock();
    }
}

std::map<int, int> GetCommand::movieByRelevence(App &app, std::map<int, int> &similarityMap)
{
    // Map to store movie relevance scores
    std::map<int, int> relevenceMap;

    for (auto it = similarityMap.begin(); it != similarityMap.end(); ++it)
    {
        // Find the user in the app
        app.lock();
        auto user = app.searchUserByID(it->first);
        app.unlock();

        if (!user)
            // Skip if the user does not exist
            continue;

        user->lock();
        for (const auto &movie : user->getMovies())
        {
            if (movie->getId() == recommandMovieId)
                // Skip the recommended movie itself
                continue;
            // Increment the relevance score
            relevenceMap[movie->getId()] += it->second;
        }
        user->unlock();
    }
    return relevenceMap;
}

void GetCommand::removeAlreadyWatched(App &app, std::map<int, int> &map)
{
    // Find the current user
    app.lock();
    auto user = app.searchUserByID(userId);
    app.unlock();
    // Exit if the user does not exist
    if (!user)
        return;

    std::unordered_set<int> watchedMovieIds;

    user->lock();
    for (const auto &movie : user->getMovies())
    {
        // Add watched movie IDs to the set
        watchedMovieIds.insert(movie->getId());
    }
    user->unlock();
    
    for (auto it = map.begin(); it != map.end();)
    {
        if (watchedMovieIds.count(it->first))
        {
            // Remove movies already watched
            it = map.erase(it);
        }
        else
        {
            // Move to the next movie
            ++it;
        }
    }
}

std::string GetCommand::TopRelevantMovies(const std::map<int, int> &MovieRelevenceMap)
{
    // Exit if no relevant movies found
    if (MovieRelevenceMap.empty())
        return "";
    // Convert map to vector for sorting
    std::vector<std::pair<int, int>> movieVector(MovieRelevenceMap.begin(), MovieRelevenceMap.end());

    std::sort(movieVector.begin(), movieVector.end(),
              [](const std::pair<int, int> &a, const std::pair<int, int> &b)
              {
                  // Sort by relevance score descending
                  if (a.second != b.second)
                      return a.second > b.second;
                  // Sort by movie ID ascending for ties
                  return a.first < b.first;
              });

    int count = 0;
	std::string output;
    for (const auto &movie : movieVector)
    {
        // Limit to top 10 movies
        if (count == 10)
            break;

        if (count > 0)
            // Add space between movie IDs
            output = output + " ";

        output = output + std::to_string(movie.first);
        // Increment the counter
        count++;
    }
	return output;
}
