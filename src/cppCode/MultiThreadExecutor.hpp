#ifndef MULTITHREAD_EXECUTOR_HPP
#define MULTITHREAD_EXECUTOR_HPP

#include <functional>
#include <thread>
#include "IExecutor.hpp"

class MultiThreadExecutor : public IExecutor {
public:
    void submit(std::function<void()> task) override;
};

#endif // MULTITHREAD_EXECUTOR_HPP
