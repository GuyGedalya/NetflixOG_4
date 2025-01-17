const User = require('../models/user');

// Returns userId is exists a match
const getToken = async (UserName, Password) => {
	try {
		// Since UserName is a uniq field, searching for a match
		const user = await User.findOne({ UserName });

		// Need to make user the Password is correct
		if(!user || user.Password !== Password) {
			return null;
		}
		return user._id;
	} catch (error) {
		console.error(error);
		return null;
	}
};
module.exports = {getToken};