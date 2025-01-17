const express = require('express');
var router = express.Router();
const categoriesController = require('../controllers/category');
const middleware = require('../controllers/authentication');

router.route('/')
    .get(categoriesController.getCategories)
    .post(middleware.verifyUserId, middleware.getUser, categoriesController.createCategory);
router.route('/:id')
    .get(categoriesController.getCategory)
    .patch(middleware.verifyUserId, middleware.getUser, categoriesController.updateCategory)
    .delete(middleware.verifyUserId, middleware.getUser, categoriesController.deleteCategory);
module.exports = router;