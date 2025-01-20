const userService = require('../services/user');
const createUser = async (req, res) => {
    try {
        // Setting the variable:
        const { Email, UserName, Password, Phone } = req.body;
		const profileImagePath = req.files && req.files.ProfileImage ? req.files.ProfileImage[0].path : null;
        // Creating user using services method:
        const user = await userService.createUser(Email, UserName, Password, Phone, profileImagePath);
		res.setHeader('Location', `/api/users/${user._id}`);
        return res.status(201).json();
    } catch (error) {
        if (error.status === 400) {
            return res.status(400).json({ error: error.message });
        }
        res.status(500).json({ error: 'An error occurred while creating the user.', error: error.message });
    }
};

const getUser = async (req, res) => {
    try {
        // Try to find the user by its ID:
        const user = await userService.getUserById(req.params.id);
        if (!user) {
            return res.status(404).json({ error: 'User not found' });
        }
        res.json(user);
    }
    catch(error) {
        if (error.status === 400){
            return res.status(400).json({ error: 'User not found' });

        }
        
    }


};
module.exports = { createUser, getUser };