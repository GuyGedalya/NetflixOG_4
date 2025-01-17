#include <iostream>
#include <sys/socket.h>
#include <stdio.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <unistd.h>
#include <string.h>
#include <thread>
#include "CommandParser.hpp"
#include "App.hpp"

#include "IExecutor.hpp"
#include "ThreadPool.hpp"

App app;

void handleClient(int clientSocket)
{
    // Creating buffer for reading the message:
    char buffer[1024];
    std::string request;

    // Reading the requests:
    int bytesReceived;
    memset(buffer, 0, sizeof(buffer));
    if (bytesReceived = recv(clientSocket, buffer, sizeof(buffer) - 1, 0) > 0)
    {
        request = std::string(buffer);
    }
    else
    {
        return;
    }

    // Creating a parser:
    CommandParser parser(request);

    std::unique_ptr<ICommand> command = parser.execute();

    // In case of invalid command:
    if (command == nullptr)
    {
        std::string response = "400 Bad Request";
        send(clientSocket, response.c_str(), response.size(), 0);
        // Closing the socket:
        close(clientSocket);
    }
    else
    {
        // Returning the output of the command:
        std::string response = command->execute(app);
        send(clientSocket, response.c_str(), response.size(), 0);
        // Closing the socket:
        close(clientSocket);
    }
}

// Starting a server:
void startServer(int port)
{

    std::unique_ptr<IExecutor> threadManager = std::make_unique<ThreadPool>(3);
    // Creating TCP socket:
    int serverSocket = socket(AF_INET, SOCK_STREAM, 0);

    // Creating struct for communication:
    struct sockaddr_in sin;
    memset(&sin, 0, sizeof(sin));
    // Setting IP format to IPv4:
    sin.sin_family = AF_INET;
    sin.sin_addr.s_addr = INADDR_ANY;
    sin.sin_port = htons(port);

    // Binding the server to the socket:
    bind(serverSocket, (struct sockaddr *)&sin, sizeof(sin));
    // Start listening to the clients:
    listen(serverSocket, 5);
    // Server loop:
    while (true)
    {
        int clientSocket = accept(serverSocket, nullptr, nullptr);
        if (clientSocket >= 0)
        {
            threadManager->submit([clientSocket]()
                                  { handleClient(clientSocket); });
        }
    }

    close(serverSocket);
}

int main(int argc, char *argv[])
{
    try
    {
        int port = std::stoi(argv[1]);
        app.loadData();
        startServer(port);
    }
    catch (const std::invalid_argument &e)
    {
        std::cerr << "Invalid argument for port: " << e.what() << std::endl;
        return 1;
    }
    catch (const std::out_of_range &e)
    {
        std::cerr << "Port number out of range: " << e.what() << std::endl;
        return 1;
    }
    catch (...)
    {

        std::cerr << "Unknown error occurred while parsing port." << std::endl;
        return 1;
    }

    return 0;
}