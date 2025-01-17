const net = require('net');

const sendAndReceive = async (message) => {
    return new Promise((resolve, reject) => {
        const client = new net.Socket();
        
        // Connect to the server
        client.connect(process.env.SERVER_PORT, 'cpp-server', () => {
            // Send the POST request (in the expected format)
            client.write(message + '\n'); // Include a newline to signal the end of the message
        });

        // Handle incoming data from the server
        client.on('data', (data) => {
            // Resolve the promise with the response from the server
            resolve(data.toString());
            client.destroy(); // Close the connection after receiving the data
        });

        // Handle errors
        client.on('error', (err) => {
            reject(err);
        });

        // Handle connection closure
        client.on('close', () => {
        });
    });
};

module.exports = {sendAndReceive}