const mongoose = require('mongoose');
const Schema = mongoose.Schema;
const Category = new Schema({
    name: {
        type: String,
        require: true,
		unique: true
    },
    promoted: {
        type: Boolean,
        require: true
    }
},
{ versionKey: false });
module.exports = mongoose.model('Category', Category);