cmake_minimum_required(VERSION 3.1)

project(threads)

# To disable runtime checks, remove "-fsanitize=address" below
set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -pthread -fsanitize=address -std=c++1y -Wall -Wextra")

add_executable(threads main.cpp)
