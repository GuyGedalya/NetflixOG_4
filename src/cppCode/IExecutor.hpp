#ifndef IEXECUTOR_HPP
#define IEXECUTOR_HPP


#include <functional> 
#include <vector>
#include <thread>
#include <mutex>
#include <queue>
#include <condition_variable>

class IExecutor {
public:
    virtual ~IExecutor() {}
    // Sending a task to complete:
    virtual void submit(std::function<void()> task) = 0;  
};

#endif // IEXECUTOR_HPP