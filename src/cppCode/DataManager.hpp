#ifndef DATA_MANAGER_HPP
#define DATA_MANAGER_HPP

#include "DataInterface.hpp"
#include <string>
#include <fstream>
#include <filesystem>

class DataManager : public DataInterface {
private:
    // Creating data directory:
    std::string directory = "/data"; 

    void appendToFile(const std::string& fileName, int relatedId);

public:
    explicit DataManager(const std::string& type, int id);

    void update(int id, const std::string& type, int relatedId) override;
    void deleteInstance(int id, const std::string& fileName) override;
};

#endif // DATA_MANAGER_HPP
