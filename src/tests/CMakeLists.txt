cmake_minimum_required(VERSION 3.10)

project(MyExample)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)


include(FetchContent)
FetchContent_Declare(
    googletest
    URL https://github.com/google/googletest/archive/refs/tags/release-1.11.0.zip
)

# For Windows: Prevent overriding the parent project's compiler/linker settings
set(gtest_force_shared_crt ON CACHE BOOL "" FORCE)

FetchContent_MakeAvailable(googletest)

# Add include directories
include_directories(../cppCode) # To get to src

file(GLOB TEST_FILES ../tests/*.cpp)
list(REMOVE_ITEM TEST_FILES ../tests/ServerIntegrationTest.cpp)

add_executable(runTests 
${TEST_FILES}
../cppCode/App.cpp 
../cppCode/PostCommand.cpp
../cppCode/PatchCommand.cpp
../cppCode/User.cpp 
../cppCode/Movie.cpp 
../cppCode/GetCommand.cpp 
../cppCode/HelpCommand.cpp
../cppCode/DataManager.cpp
../cppCode/DeleteCommand.cpp
../cppCode/CommandParser.cpp
../cppCode/IExecutor.hpp 
../cppCode/ICommand.hpp
../cppCode/MultiThreadExecutor.cpp)

target_link_libraries(runTests gtest_main)
