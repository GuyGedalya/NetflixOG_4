const userService = require('../services/user');

// Gets ID from the header and attaches it the the request
const verifyUserId = (req, res, next) => {
	try {
		const userId = req.headers['user-id']; 
		if (!userId) {
			return res.status(400).json({ error: 'User ID is required in the header.' });
		}
		req.userId = userId; 
		next(); // Move to the next function in line
	} catch (error) {
		return res.status(500).json({error: "Error fetching user token"});
	}
};

// Gets the user who's ID was in the header
const getUser = async (req, res, next) => {
	try {
		const user = await userService.getUserById(req.userId);
		if (!user) {
			return res.status(404).json({ error: 'Not a valid user token' });
		}
		req.user= user;
		next();
	}catch (error) {
		return res.status(500).json({error: "Error finding user"});
	}
};

module.exports = { verifyUserId, getUser };