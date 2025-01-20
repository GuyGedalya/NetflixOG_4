const userService = require('../services/user');
const createUser = async (req, res) => {
    try {
		console.log(req.body);
        // Setting the variable:
        const { Email, UserName, Password, Phone, ProfileImage } = req.body;
        // Creating user using services method:
        const user = await userService.createUser(Email, UserName, Password, Phone, ProfileImage);
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