#include "DataManager.hpp"
#include <iostream>

DataManager::DataManager(const std::string& type, int id) {

    // Building path: /data/<type>_<id>.txt

    if (!std::filesystem::exists(directory)) {
        std::filesystem::create_directory(directory);
    }

    std::string filePath = directory + "/" + type + "_" + std::to_string(id) + ".txt";

    // Creating the file if not exists:
    if (!std::filesystem::exists(filePath)) {
        std::ofstream file(filePath);
        if (file.is_open()) {
            file << type << " ID: " << id << "\n";
            file << "Related:";
            file.close();
        }
    }
}

// Function to append the ID to the file:
void DataManager::appendToFile(const std::string& fileName, int relatedId) {
    std::ofstream file(directory + "/" + fileName, std::ios::app);
    if (file.is_open()) {
        file << " " << relatedId;
    }
}

//Function to update the file:
void DataManager::update(int id, const std::string& type, int relatedId) {
    std::string fileName = type + "_" + std::to_string(id) + ".txt";

    // Adding new ID:
    appendToFile(fileName, relatedId);
}

void DataManager::deleteInstance(int id, const std::string& fileName) {
    std::string filePath = directory + "/" + fileName;

    // Cleaning the file:
    std::ofstream file(filePath, std::ios::trunc);
    if (file.is_open()) {
        file.close();
    }
}
