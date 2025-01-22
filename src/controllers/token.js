const tokenService = require('../services/token');
const signServies = require('../middlewares/authentication');

//this function gets UserName and Password and returns the userId if exists
const getToken = async (req, res) => {
	try {
		const user = await tokenService.getToken(req.body.UserName, req.body.Password);
		// If we didn't such user
		if (!user) {
			return res.status(404).json({ error: 'User or Password not found!' });
		}
		const token = signServies.signToken(user);
		return res.status(200).json({ token: token, user: user });
	} catch (error) {
		return res.status(500).json({ error: 'Internal Server Error' });
	}
};
module.exports = { getToken };