cmake_minimum_required(VERSION 3.10)

project(NETFLUX)

set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)


# Add include directories
# include_directories(/cppCode)

file(GLOB CPP_FILES *.cpp)

add_executable(runProject ${CPP_FILES})
