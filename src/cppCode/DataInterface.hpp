#ifndef DATA_INTERFACE_HPP
#define DATA_INTERFACE_HPP

#include <string>

class DataInterface {
public:
    virtual ~DataInterface() = default;

    // Update the data of the object:
    virtual void update(int id, const std::string& type, int relatedId) = 0;

    // Deleting:
    virtual void deleteInstance(int id, const std::string& fileName) = 0;
};

#endif // DATA_INTERFACE_HPP
