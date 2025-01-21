const express = require('express');
const upload = require('../middlewares/upload');
var router = express.Router();
const userController = require('../controllers/user');
router.route('/')
    .post(upload.fields([{ name: 'ProfileImage', maxCount: 1 }]), userController.createUser);
router.route('/:id')
    .get(userController.getUser)
module.exports = router;