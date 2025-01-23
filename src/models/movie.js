const mongoose = require('mongoose');
const Schema = mongoose.Schema;

// The is the movie scheme, it has a title, date and category
const Movie = new Schema({
	MovieNumber: {
		type: Number,
		required: true,
		unique: true
	},
	Title: {
		type: String, 
		required: true 
	},
	ReleaseDate: {
		type: Date, 
		required: true 
	},
	Image: { 
        type: String,
        required: true
    }, 
	Film: { 
        type: String,
        required: true, 
        // Ensure it's a valid URL
        match: [/^(http|https):\/\/[^\s]+$/, 'Please provide a valid image URL.'] 
    }, 
	Categories: [{ 
		type: mongoose.Schema.Types.ObjectId, 
		ref: 'Category', 
		required: true 
	}] 
  }, { versionKey: false,
	toJSON: {
		transform: function(doc, ret) {
		  // Hiding UserNumber from view
		  delete ret.MovieNumber;
		  return ret;
		}
	  }
   });

  module.exports = mongoose.model('Movie', Movie);