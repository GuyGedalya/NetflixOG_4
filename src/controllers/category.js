const categoryService = require('../services/category');

const createCategory = async (req, res) => {
    try {
        const { name, promoted } = req.body;
        // Check for validity:
        if (!name) {
            return res.status(400).json({ error: 'Name is required.' });
        }
        if (typeof name !== 'string') {
            return res.status(400).json({ error: 'Name should be a string.' });
        }
        if (promoted === undefined) {
            return res.status(400).json({ error: 'Promoted is required.' });
        }
        if (typeof promoted !== 'boolean') {
            return res.status(400).json({ error: 'Promoted should be a boolean.' });
        }
        // Try to find a category, if find exit:
        const category = await categoryService.findByName(name);
        if (category) {
            return res.status(400).json({ error: 'Category already exists' });
        }
        const newCategory = await categoryService.createCategory(name, promoted);
		// Set the Location header to point to the new resource
		res.setHeader('Location', `/api/categories/${newCategory._id}`);
        res.status(201).json();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getCategories = async (req, res) => {
    try {
        // Try to get all the categories:
        const categories = await categoryService.getCategories();
        res.json(categories);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

const getCategory = async (req, res) => {
    try {
        //Try to find a category base of the id given in the req:
        const category = await categoryService.getCategoryById(req.params.id);
        // If not exists:
        if (!category) {
            return res.status(404).json({ error: 'Category not found' });
        }
        res.json(category);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};
const updateCategory = async (req, res) => {
    try {
        // Get the argument from req:
        const { name, promoted } = req.body;
        // Updating:
        const updatedCategory = await categoryService.updateCategory(req.params.id, name, promoted);
        // If couldnt find the requested category:
        if (!updatedCategory) {
            return res.status(404).json({ error: 'Category not found' });
        }
        res.status(204).end();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

const deleteCategory = async (req, res) => {
    try {
        // First, try to find the category to delete:
        const result = await categoryService.deleteCategory(req.params.id);
        // If couldnt find:
        if (!result) {
            return res.status(404).json({ error: 'Category not found' });
        }
        // Else return 204 status:
        res.status(204).send();
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

module.exports = { createCategory: createCategory, getCategories, getCategory, updateCategory: updateCategory, deleteCategory };