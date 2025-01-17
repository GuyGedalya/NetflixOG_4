#ifndef THREADPOOL_HPP
#define THREADPOOL_HPP

#include <vector>
#include <queue>
#include <thread>
#include <mutex>
#include <condition_variable>
#include <atomic>
#include <functional>
#include "IExecutor.hpp"

class ThreadPool : public IExecutor
{
private:
    std::vector<std::thread> workers;
    std::queue<std::function<void()>> tasks;
    std::mutex queueMutex;
    std::condition_variable condition;
    std::atomic<bool> stop;

public:
    // Constructor to initialize the thread pool
    ThreadPool(size_t numThreads);

    // Destructor to join threads and clean up
    ~ThreadPool();

    void submit(std::function<void()> task);

private:
    void workerFunction();
};

#endif // THREADPOOL_HPP
