const mongoose = require('mongoose');

const counterSchema = new mongoose.Schema({
    _id: { type: String, required: true }, // counter name ( "users" or "movies")
    seq: { type: Number, required: true } // Current value of the counter
});

module.exports = mongoose.model('Counter', counterSchema);