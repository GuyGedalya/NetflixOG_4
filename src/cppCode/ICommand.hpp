#ifndef ICOMMAND_HPP
#define ICOMMAND_HPP

#include <string>

const std::string NOT_FOUND = "404 Not Found";
const std::string OK  = "200 Ok";
const std::string CREATED = "201 Created";
const std::string NO_CONTENT = "204 No Content";

// Forward declaration of App class
class App;

// This is the Icommand interface. All commands will inherit from this class
class ICommand
{
public:
	// Shows that the class is pure abstract
	virtual std::string execute(App &app) = 0;
};

#endif