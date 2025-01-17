#ifndef POSTCOMMAND_HPP
#define POSTCOMMAND_HPP

#include "ICommand.hpp"
#include "App.hpp"
#include <vector>

class PostCommand : public ICommand {
	public:
		PostCommand(int userId, const std::vector<int>& movieIds);

		std::string execute(App& app) override;
	
	private:
    	int userId; 
    	// Lists of movies related to the user:
    	std::vector<int> movieIds; 
};


#endif