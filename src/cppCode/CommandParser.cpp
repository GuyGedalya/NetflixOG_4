#include <iostream>
#include <string>
#include "CommandParser.hpp"
#include "ICommand.hpp"
#include "GetCommand.hpp"
#include "HelpCommand.hpp"
#include "PostCommand.hpp"
#include "PatchCommand.hpp"
#include "DeleteCommand.hpp"
#include <memory>
#include <sstream>
#include <set>
#include <vector>

std::set<std::string> validCommands = {"POST", "PATCH", "GET", "HELP", "DELETE"};

CommandParser::CommandParser(const std::string &input) : commandString(input)
{
    // Setting the commandString to the string it received
    // Seperate the command by spaces:
    std::istringstream stream(commandString);
    std::string part;

    // Building a vector of words:
    while (stream >> part)
    {
        commandParts.push_back(part);
    }
}

std::unique_ptr<ICommand> CommandParser::execute()
{
    // Check if the command is empty:
    if (commandParts.empty())
    {
        return nullptr;
    }

    // Get the first word of the command and check if it valid witht the set:
    std::string commandType = commandParts[0];

    if (validCommands.find(commandType) != validCommands.end())
    {
        if (commandType == "POST")
        {
            return postBuilder();
        }
        else if (commandType == "PATCH")
        {
            return patchBuilder();
        }
        else if (commandType == "GET")
        {
            return getBuilder();
        }
        else if (commandType == "DELETE")
        {
            return deleteBuilder();
        }
        else
        {
            return helpBuilder();
        }
    }
    else
    {
        return nullptr;
    }
}

std::unique_ptr<ICommand> CommandParser::deleteBuilder()
{
    // Check if it has at least 2 arguments:
    if (commandParts.size() - 1 < 2)
    {
        return nullptr;
    }
    // Check if the first argument (the user ID) is type Int:
    else if (!isIntNumber(commandParts[1]))
    {
        return nullptr;
    }
    else
    {
        int userID = std::stoi(commandParts[1]);
        // Setting counter to 2 to begin iterating on the vector:
        int counter = 2;
        std::vector<int> movieIds;

        // Loop:
        // The loop iterate the vector and for every argument check if it Int, if not return nullptr, else add it:
        while (counter <= commandParts.size() - 1)
        {
            if (!isIntNumber(commandParts[counter]))
            {
                return nullptr;
            }
            else
            {
                movieIds.push_back(std::stoi(commandParts[counter]));
            }
            counter++;
        }
        // Return the patch command class:
        return std::make_unique<DeleteCommand>(userID, movieIds);
    }
}
std::unique_ptr<ICommand> CommandParser::helpBuilder()
{
    // Check if it contains only one arguement:
    if (commandParts.size() - 1 != 0)
    {
        return nullptr;
    }
    // Creating a help class:
    return std::make_unique<HelpCommand>();
}

std::unique_ptr<ICommand> CommandParser::getBuilder()
{
    // Check if it has 2 arguments:
    if (commandParts.size() - 1 != 2)
    {
        return nullptr;
    }
    // Check if the first word is an Int for the user ID:
    else if (!isIntNumber(commandParts[1]))
    {
        return nullptr;
    }
    else
    {
        // Set the user ID to the first argument:
        int userID = std::stoi(commandParts[1]);
        int movieId;

        // Check if the second argument is also Int for the movie ID:
        if (!isIntNumber(commandParts[2]))
        {
            return nullptr;
        }
        else
        {
            // Set the movie ID:
            movieId = std::stoi(commandParts[2]);
        }
        // Return the Get command:
        return std::make_unique<GetCommand>(userID, movieId);
    }
}

std::unique_ptr<ICommand> CommandParser::patchBuilder()
{
    // Check if it has at least 2 arguments:
    if (commandParts.size() - 1 < 2)
    {
        return nullptr;
    }
    // Check if the first argument (the user ID) is type Int:
    else if (!isIntNumber(commandParts[1]))
    {
        return nullptr;
    }
    else
    {
        int userID = std::stoi(commandParts[1]);
        // Setting counter to 2 to begin iterating on the vector:
        int counter = 2;
        std::vector<int> movieIds;

        // Loop:
        // The loop iterate the vector and for every argument check if it Int, if not return nullptr, else add it:
        while (counter <= commandParts.size() - 1)
        {
            if (!isIntNumber(commandParts[counter]))
            {
                return nullptr;
            }
            else
            {
                movieIds.push_back(std::stoi(commandParts[counter]));
            }
            counter++;
        }
        // Return the patch command class:
        return std::make_unique<PatchCommand>(userID, movieIds);
    }
}

std::unique_ptr<ICommand> CommandParser::postBuilder()
{
    // Check if tit has at least two arguments:
    if (commandParts.size() - 1 < 2)
    {
        return nullptr;
    }
    // Check if the first argument (the user ID) is type Int:
    else if (!isIntNumber(commandParts[1]))
    {
        return nullptr;
    }
    else
    {
        int userID = std::stoi(commandParts[1]);
        // Setting counter to 2 to begin iterating on the vector:
        int counter = 2;
        std::vector<int> movieIds;
        // Loop:
        // The loop iterate the vector and for every argument check if it Int, if not return nullptr, else add it:
        while (counter <= commandParts.size() - 1)
        {
            if (!isIntNumber(commandParts[counter]))
            {
                return nullptr;
            }
            else
            {
                movieIds.push_back(std::stoi(commandParts[counter]));
            }
            counter++;
        }
        // Return the post command class:
        return std::make_unique<PostCommand>(userID, movieIds);
    }
}

// A function the check if a string represnt int:
bool CommandParser::isIntNumber(const std::string &str)
{
    if (str.empty()) 
        return false;
    
    size_t start = 0;
    if (str[0] == '-') 
        return false;
    
    for (size_t i = start; i < str.size(); ++i)
    {
        if (!std::isdigit(str[i]))
            return false;
    }
    
    return true;
}

