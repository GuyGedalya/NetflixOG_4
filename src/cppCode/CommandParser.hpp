#ifndef CommandParser_hpp
#define CommandParser_hpp

#include <string>
#include <vector>
#include <memory>
#include <string>

#include "ICommand.hpp"

class CommandParser
{
private:
    const std::string commandString;
    std::vector<std::string> commandParts;

public:
    CommandParser(const std::string &input);

    std::unique_ptr<ICommand> execute();

    std::string getCommand();

private:
    std::unique_ptr<ICommand> postBuilder();
    std::unique_ptr<ICommand> patchBuilder();
    std::unique_ptr<ICommand> getBuilder();
    std::unique_ptr<ICommand> helpBuilder();
    std::unique_ptr<ICommand> deleteBuilder();

    bool isIntNumber(const std::string &str);
};

#endif
