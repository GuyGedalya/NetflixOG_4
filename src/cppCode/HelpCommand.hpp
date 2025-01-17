#ifndef HELPCOMMAND_HPP
#define HELPCOMMAND_HPP

#include "ICommand.hpp"
#include "App.hpp"
#include <iostream>

class HelpCommand : public ICommand
{
public:
    HelpCommand();
    std::string execute(App &app) override;
};

#endif // HELPCOMMAND_HPP
