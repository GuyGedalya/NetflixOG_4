#include <gtest/gtest.h>
#include "App.hpp"
#include "HelpCommand.hpp"

// Test suite for HelpCommand
class HelpCommandTest : public ::testing::Test {
protected:
    App app;
};

// Test if the help command outputs the expected list of commands
TEST_F(HelpCommandTest, OutputsExpectedHelp) {

    std::shared_ptr<ICommand> helpCommand = std::make_shared<HelpCommand>();
    std::string output = helpCommand->execute(app);

    // Check if the output matches the expected format
    std::string expectedOutput = OK + "\n\n"  +
		"DELETE,arguments: [userid] [movieid1] [movieid2] ...\n"
		"GET,arguments: [userid] [movieid]\n"
		"PATCH,arguments: [userid] [movieid1] [movieid2] ...\n"
		"POST,arguments: [userid] [movieid1] [movieid2] ...\n"
        "HELP\n";
    EXPECT_EQ(output, expectedOutput);
}
