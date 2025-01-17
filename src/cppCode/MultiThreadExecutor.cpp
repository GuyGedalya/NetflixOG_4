#include <functional>
#include <thread>
#include "MultiThreadExecutor.hpp"   

void MultiThreadExecutor::submit(std::function<void()> task) {
    // Creating new tread for the task:
    std::thread([task]() {
        // Executing the task in different thread:
        task();  
    }).detach();
}
