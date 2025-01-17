#include <gtest/gtest.h>
#include "CommandParser.hpp"
#include "PostCommand.hpp"
#include "PatchCommand.hpp"
#include "DeleteCommand.hpp"
#include "GetCommand.hpp"
#include "HelpCommand.hpp"

// Test for POST
TEST(CommandParserTest, PostCommand_ValidID)
{
    CommandParser parser("POST 123 456");
    auto command = parser.execute();
    ASSERT_NE(command, nullptr);
    EXPECT_NE(dynamic_cast<PostCommand*>(command.get()), nullptr);
}

TEST(CommandParserTest, PostCommand_InvalidID)
{
    CommandParser parser("POST 1");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

// Test for PATCH
TEST(CommandParserTest, PatchCommand_ValidID)
{
    CommandParser parser("PATCH 123 456");
    auto command = parser.execute();
    ASSERT_NE(command, nullptr);
    EXPECT_NE(dynamic_cast<PatchCommand*>(command.get()), nullptr);
}

TEST(CommandParserTest, PatchCommand_InvalidID)
{
    CommandParser parser("PATCH 1");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

//Test for DELETE
TEST(CommandParserTest, DeleteCommand_ValidIDs)
{
    CommandParser parser("DELETE 123 456");
    auto command = parser.execute();
    ASSERT_NE(command, nullptr);
    EXPECT_NE(dynamic_cast<DeleteCommand*>(command.get()), nullptr);
}

TEST(CommandParserTest, DeleteCommand_InvalidIDs)
{
    CommandParser parser("DELETE 123");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

// Test for GET
TEST(CommandParserTest, GetCommand_ValidIDs)
{
    CommandParser parser("GET 123 456");
    auto command = parser.execute();
    ASSERT_NE(command, nullptr);
    EXPECT_NE(dynamic_cast<GetCommand*>(command.get()), nullptr);
}

TEST(CommandParserTest, GetCommand_InvalidIDs)
{
    CommandParser parser("GET 123 456 789");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

// Test for HELP
TEST(CommandParserTest, HelpCommand_NoIDs)
{
    CommandParser parser("HELP");
    auto command = parser.execute();
    ASSERT_NE(command, nullptr);
    EXPECT_NE(dynamic_cast<HelpCommand*>(command.get()), nullptr);
}

TEST(CommandParserTest, HelpCommand_WithIDs)
{
    CommandParser parser("HELP 123");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

// Test for invalid commands:
TEST(CommandParserTest, InvalidCommand)
{
    CommandParser parser("unknown 123");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand1)
{
    CommandParser parser("GET g 2 3");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}


TEST(CommandParserTest, InvalidCommand2)
{
    CommandParser parser("PATCH g t 3");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand3)
{
    CommandParser parser("Get pnina rozenbloom");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand4)
{
    CommandParser parser("get a life");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand5)
{
    CommandParser parser("kill me");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand6)
{
    CommandParser parser("Patch 1 3 t");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand7)
{
    CommandParser parser("Get 1 t ");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand8)
{
    CommandParser parser("Post 1 t ");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand9)
{
    CommandParser parser(" Post 1 t ");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand10)
{
    CommandParser parser("Post1 2 t ");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}

TEST(CommandParserTest, InvalidCommand11)
{
    CommandParser parser("Post 2t 3");
    auto command = parser.execute();
    EXPECT_EQ(command, nullptr);
}