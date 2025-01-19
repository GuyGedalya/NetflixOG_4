const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const User = new Schema({
	UserNumber: {
		type: Number,
		required: true,
		unique: true
	},
    Email: {
        type: String,
        required: true,
        unique: true,
        // Most email addresses are case-insensitive.
        lowercase: true
    },
	UserName: {
		type: String,
		required: true,
		unique: true
	},
    Password: {
        type: String,
        required: true
    },
    Phone: {
        type: String,
        required: true, 
        unique: true,
        // Regex validation for phone number format:
        match: [/^05\d{8}$/, 'Please provide a valid phone number. format: 05********'] 
    },
    ProfileImage: { 
        type: String,
        required: false, 
        // Ensure it's a valid URL
        match: [/^(http|https):\/\/[^\s]+$/, 'Please provide a valid image URL.'] 
    },
	Movies: [{
        type: mongoose.Schema.Types.ObjectId,
        ref: 'Movie'
    }]
}, { versionKey: false,
	toJSON: {
		transform: function(doc, ret) {
		  // Hiding UserNumber from view
		  delete ret.UserNumber;
		  return ret;
		}
	  }
 });

module.exports = mongoose.model('User', User);