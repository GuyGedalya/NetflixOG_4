const User = require('../models/user');
const CounterServices = require('../services/counter');
const mongoose = require('mongoose');


const createUser = async (Email, UserName, Password, Phone, ProfileImage) => {
  try {
    // If details missing:
    if (!Email || !UserName || !Password || !Phone) {
      throw new Error('Missing required fields: Email, UserName, Password, or Phone');
    }

    // Setting the user new serial number:
    const UserNumber = await CounterServices.getNextSequence('users');

    // Creating new User:
    const user = new User({
      UserNumber: UserNumber,
      Email: Email,
      UserName: UserName,
      Password: Password,
      Phone: Phone,
      ProfileImage: ProfileImage
    });

    return await user.save();
  } catch (error) {
    // Check if the error is a missing field issue
    if (error.message.startsWith('Missing required fields')) {
      throw { status: 400, message: error.message };
    }
    // Check if the error is a duplicate info:
    if (error.message && error.message.includes('E11000 duplicate key error')) {
      const originalMessage = error.message;

	  const duplicateFieldMessage = originalMessage.split('dup key: ')[1];

      const customMessage = 'The specified field already exists. Please choose another one.';

      // Combine both messages
      const combinedMessage = `${customMessage} Original error: ${duplicateFieldMessage}`;

      // Send the response
      throw { status: 400, message: combinedMessage };
    }
    throw new Error(`Error creating user: ${error.message}`);
  }
};

const getUserById = async (id) => {
  if (!mongoose.Types.ObjectId.isValid(id)) {
    // Invalid ID:
    throw { status: 400, message: 'Invalid ObjectId format' };  
  }
  return await User.findById(id).populate('Movies', 'Title');;
};

// Checks if a user has watched the movie
const hasWatched = async (user, movieId) => {
  // If the list is empty
  if (user.Movies.length === 0) {
    return false;
  }

  // Looking for the IDs in the movie list
  for (let i = 0; i < user.Movies.length; i++) {
    if (user.Movies[i].equals(movieId)) {
      return true; // We found a match
    }
  }

  return false; // User didn't watch the movie
};

// Returns an array of users who watched the movie with the given movieId
const findUsersWhoWatched = async (movieId) => {
  try {
    const users = await User.find({ Movies: movieId });
    return users;
  } catch (error) {
    throw error;
  }
};

const deleteMovieFromWatched = async (userId, movieId) => {
  try {
    const updatedUser = await User.findByIdAndUpdate(userId, { $pull: { Movies: movieId } }, { new: true });
    return updatedUser;
  } catch (error) {
    return null;
  }
};

module.exports = { createUser: createUser, getUserById: getUserById, hasWatched, findUsersWhoWatched, deleteMovieFromWatched }