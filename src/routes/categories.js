const express = require('express');
var router = express.Router();
const categoriesController = require('../controllers/category');
const middleware = require('../middlewares/authentication');

router.route('/')
    .get(categoriesController.getCategories)
    .post(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, categoriesController.createCategory);
router.route('/:id')
    .get(categoriesController.getCategory)
    .patch(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, categoriesController.updateCategory)
    .delete(middleware.authenticateToken, middleware.getUser,middleware.checkAdmin, categoriesController.deleteCategory);
module.exports = router;