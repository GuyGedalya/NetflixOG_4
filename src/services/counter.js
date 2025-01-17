const Counter = require('../models/counter');

// Returns the value of the counter after incrementing
const getNextSequence = async (counterName) => {
    try {
        const counter = await Counter.findOneAndUpdate(
            { _id: counterName },
            { $inc: { seq: 1 } },
            { new: true, upsert: true } // upsert create if object doesn't exist
        );
        return counter.seq;
    } catch (error) {
        console.error(`Error getting next sequence for ${counterName}:`, error);
        throw error;
    }
};

module.exports = {getNextSequence};
