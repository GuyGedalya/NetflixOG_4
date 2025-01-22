const jwt = require('jsonwebtoken');
const userService = require('../services/user');

const secretKey = 'BestKeyEver'; // Replace with your actual secret key

const authenticateToken = (req, res, next) => {
	const token = req.header('Authorization')?.split(' ')[1];
	if (!token) {
		return res.status(401).json({ message: 'Access Denied' });
	}

	try {
		const verified = jwt.verify(token, secretKey);
		req.userItems = verified;
		next();
	} catch (err) {
		res.status(400).json({ message: 'Invalid Token' });
	}
};

// Gets the user who's ID was in the header
const getUser = async (req, res, next) => {
	try {
		const user = await userService.getUserById(req.userItems.id);
		if (!user) {
			return res.status(404).json({ error: 'Not a valid user token' });
		}
		req.user = user;
		next();
	} catch (error) {
		return res.status(500).json({ error: "Error finding user" });
	}
};

const checkAdmin = (req, res, next) => {
	if (!req.user.Admin) {
		return res.status(403).json({ message: 'Access Denied' });
	}
	next();
};

const signToken = (user) => {
	const payload = { id: user._id, admin: user.Admin };
	const token = jwt.sign(payload, secretKey);
	return token;
};

module.exports = { authenticateToken, signToken, getUser, checkAdmin };