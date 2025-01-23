const Category = require('../models/category');
const mongoose = require('mongoose');

const createCategory = async (name, promoted) => {
    try {
        // Creating new category:
        const category = new Category({ name: name, promoted: promoted });
        // Saving:
        return await category.save();
    } catch (error) {
        throw new Error(`Error creating category: ${error.message}`);
    }
};

const findByName = async (name) => {
    try {
        // Find category using its name:
        return await Category.findOne({ name });
    } catch (error) {
        throw new Error(`Error finding category by name: ${error.message}`);
    }
};

const getCategoryById = async (id) => {
    try {
        // Check for ID validity:
        if (!mongoose.Types.ObjectId.isValid(id)) {
            return null;
        }
        // Try to find the category by its ID:
        return await Category.findById(id);
    } catch (error) {
        throw new Error(`Error finding category by ID: ${error.message}`);
    }
};
const getCategories = async () => {
    try {
        // Get all the categories:
        return await Category.find({});
    } catch (error) {
        throw new Error(`Error fetching categories: ${error.message}`);
    }
};

const updateCategory = async (id, name, promoted) => {
    try {
        // First try to find the category, if not return null:
        const category = await getCategoryById(id);
        if (!category) return null;
        // Update only fields that exist in Category:
        if (name !== undefined) {
            category.name = name;
        }
        if (promoted !== undefined) {
            category.promoted = promoted;
        }
        await category.save();
        return category;
    } catch (error) {
        throw new Error(`Error updating category: ${error.message}`);
    }
};
const deleteCategory = async (id) => {
    try {
        // Find the category, else reutrn null:
        const category = await getCategoryById(id);
        if (!category) return null;
        // Deleting:
        await Category.findByIdAndDelete(id);
        return category;
    } catch (error) {
        throw new Error(`Error deleting category: ${error.message}`);
    }
};

// Creating a list of category IDs from a list of category names
const getCategoriesIdsByNames = async (categoryNames) => {
    try {
        const categories = [];
        // Creating categories if they don't exist
        for (const name of categoryNames) {
            let category = await Category.findOne({ name });
            if (!category) {
                throw new Error(`Category "${name}" does not exist.`);
            }
            categories.push(category);
        }
        return categories;
    } catch (error) {
        throw new Error(`Error fetching categories by names: ${error.message}`);
    }
};

module.exports = { createCategory: createCategory, getCategoryById, getCategories, updateCategory, deleteCategory, getCategoriesIdsByNames, findByName }