#include "ThreadPool.hpp"
#include <iostream>
#include <functional>
#include <unistd.h>

ThreadPool::ThreadPool(size_t numThreads) : stop(false)
{
    for (size_t i = 0; i < numThreads; i++)
    {
        workers.emplace_back([this]()
                             { workerFunction(); });
    }
}

ThreadPool::~ThreadPool()
{
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        stop = true;
    }
    condition.notify_all();  
    for (auto& worker : workers) {
        worker.join(); 
    }
}

void ThreadPool::submit(std::function<void()> task) {
    // Add a task to the queue
    {
        std::unique_lock<std::mutex> lock(queueMutex);
        tasks.push(task);
    }
    // Notify one thread that a task is available
    condition.notify_one();  
}

void ThreadPool::workerFunction() {
    while (true) {
        std::function<void()> task;
        {
            std::unique_lock<std::mutex> lock(queueMutex);
            // Wait until there are tasks or stop flag is set
            condition.wait(lock, [this] { return stop || !tasks.empty(); });
            if (stop && tasks.empty()) {
                return;  // Exit if stop is true and no tasks are left
            }
            task = tasks.front();  // Get the task from the front of the queue
            tasks.pop();  // Remove the task from the queue
        }
        task();  // Execute the task
    }
}
