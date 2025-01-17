const tokenService = require('../services/token');

//this function gets UserName and Password and returns the userId if exists
const getToken = async (req, res) => {
	const userId = await tokenService.getToken(req.body.UserName, req.body.Password);
	// If we didn't such user
	if(!userId) {
		return res.status(404).json({error: 'User or Password not found!'});
	}
	res.status(200).json({userId: userId});
};
module.exports = {getToken};