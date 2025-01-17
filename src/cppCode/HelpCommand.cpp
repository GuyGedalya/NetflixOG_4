#include "HelpCommand.hpp"
#include <iostream>

HelpCommand::HelpCommand() {}

//Function for printing the commands:
std::string HelpCommand::execute(App& app) {
	std::string output;
	output = OK + "\n\n"  + "DELETE,arguments: [userid] [movieid1] [movieid2] ...\n"
		     "GET,arguments: [userid] [movieid]\n"
		     "PATCH,arguments: [userid] [movieid1] [movieid2] ...\n"
		     "POST,arguments: [userid] [movieid1] [movieid2] ...\n"
             "HELP\n";
	return output; 
}
