const express = require('express');
var router = express.Router();
const tokenController = require('../controllers/token');
// Routing to the getToken function in the controllers
router.route('/')
	.post(tokenController.getToken);
module.exports = router;