#ifndef PATCHCOMMAND_HPP
#define PATCHCOMMAND_HPP

#include "ICommand.hpp"
#include "App.hpp"
#include <vector>

class PatchCommand : public ICommand {
	public:
		PatchCommand(int userId, const std::vector<int>& movieIds);

		std::string execute(App& app) override;
	private:
		int userId;
		std::vector<int> movieIds;
};

#endif