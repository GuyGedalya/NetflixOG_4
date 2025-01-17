#include <gtest/gtest.h>
#include <sys/socket.h>
#include <arpa/inet.h>

#include "ICommand.hpp"
#define BAD_REQUEST "400 Bad Request"


class ServerIntegrationTest : public ::testing::Test {
	protected:
		const char* server_ip = "172.17.0.3"; // Change Later

		const int server_port = 12345; // Change Later
		int sock;
		void SetUp() override {
			// Starting socket
			sock = socket(AF_INET, SOCK_STREAM, 0);
    		if (sock < 0) {
        		FAIL() << "Error creating socket: " << strerror(errno);
    		}
			// Filling socket information
			struct sockaddr_in sin;
    		memset(&sin, 0, sizeof(sin));
    		sin.sin_family = AF_INET;
    		sin.sin_addr.s_addr = inet_addr(server_ip);
    		sin.sin_port = htons(server_port);

			// Connecting to the server
			if (connect(sock, (struct sockaddr *) &sin, sizeof(sin)) < 0) {
        		FAIL() << "Error connecting to server: " << strerror(errno);
    		}
		}

		void TearDown() override {
    		if (sock >= 0) {
        		close(sock); // Make sure to close the socket
    		}
		}

		std::string sendAndReceive(const char* message) {
			// Sending the Msg to the sever
			
			int data_len = strlen(message);
			int sent_bytes = send(sock, message, data_len, 0);
			if (sent_bytes < 0) {
				throw std::runtime_error("Error: failed sending to the server: " + std::string(strerror(errno)));
			}
			// Getting the answer
			char buffer[4096];
			int read_bytes = recv(sock, buffer, sizeof(buffer) - 1, 0);
			if (read_bytes <= 0) {
        		throw std::runtime_error("Error: failed to receive from the server: " + std::string(strerror(errno)));
			}
			// Turning it to a string
			buffer[read_bytes] = '\0';
			return std::string(buffer);
		}
};

// Testing Sending a simple msg
TEST_F(ServerIntegrationTest, TestConnectionAndResponse){
	std::string output = sendAndReceive("HELP\n");
	std::string expectedOutput = OK + "\n\n"  +
		"DELETE,arguments: [userid] [movieid1] [movieid2] ...\n"
		"GET,arguments: [userid] [movieid]\n"
		"PATCH,arguments: [userid] [movieid1] [movieid2] ...\n"
		"POST,arguments: [userid] [movieid1] [movieid2] ...\n"
        "HELP\n";
	EXPECT_EQ(output, expectedOutput);
}

// Checking if server sends invalid input mgs
TEST_F(ServerIntegrationTest, TestInvalidMsg1){
	std::string output = sendAndReceive("Pnina Rozenbloom New\n");
	std::string expectedOutput = BAD_REQUEST;
	EXPECT_EQ(output, expectedOutput);
}

// Checking if server sends invalid input mgs
TEST_F(ServerIntegrationTest, TestInvalidMsg2){
	std::string output = sendAndReceive("GET 1 PNINA\n");
	std::string expectedOutput = BAD_REQUEST;
	EXPECT_EQ(output, expectedOutput);
}

// Checking if server sends invalid input mgs
TEST_F(ServerIntegrationTest, TestLogicallyInvalidMgs){
	std::string output = sendAndReceive("GET 1 100\n");
	std::string expectedOutput = NOT_FOUND;
	EXPECT_EQ(output, expectedOutput);
}

// Checking if server sends invalid input mgs
TEST_F(ServerIntegrationTest, TestValidPostMsg){
	std::string output = sendAndReceive("POST 1 100\n");
	std::string expectedOutput = CREATED;
	EXPECT_EQ(output, expectedOutput);
}

TEST_F(ServerIntegrationTest, TestPostAddsUserAndMovies) {
    // Adding user 1
    std::string output = sendAndReceive("POST 3 100 \n");
    std::string expectedOutput = CREATED; // Should return 200 created

    EXPECT_EQ(output, expectedOutput);

	// Adding user 2
    output = sendAndReceive("POST 4 100 200\n");
    expectedOutput = CREATED; // Should return 200 created

    EXPECT_EQ(output, expectedOutput);

    // Asking for a recommendation 
    output = sendAndReceive("GET 3 100\n");
    expectedOutput = std::string(OK) + "\n\n200\n";
    EXPECT_EQ(output, expectedOutput);
}

TEST_F(ServerIntegrationTest, TestPatchUpdatesMoviesForExistingUser) {
    // Add user 5
    sendAndReceive("POST 5 100 200\n");
    // Add user 6
    sendAndReceive("POST 6 100 200\n");

    // Update user 2
    std::string output = sendAndReceive("PATCH 6 300 400\n");
    std::string expectedOutput = NO_CONTENT; 
    EXPECT_EQ(output, expectedOutput);

    // Make sure all data is added by checking if recommendations are right
    output = sendAndReceive("GET 5 100\n");
	std::string expectedGetOutput = std::string(OK) + "\n\n300 400\n";
    EXPECT_EQ(output, expectedGetOutput);
}

TEST_F(ServerIntegrationTest, TestPatchFailsForNonExistingUser) {
    // Patching non existent user
    std::string output = sendAndReceive("PATCH 999 300 400\n");
    std::string expectedOutput = NOT_FOUND;

    EXPECT_EQ(output, expectedOutput);
}

TEST_F(ServerIntegrationTest, TestDeleteRemovesMoviesFromUser) {
    // Adding user
    sendAndReceive("POST 7 700 800\n");
	// Add user 2
    sendAndReceive("POST 8 700 800\n");

    std::string output = sendAndReceive("DELETE 7 800\n");
    std::string expectedOutput = NO_CONTENT; // 201 Created

    EXPECT_EQ(output, expectedOutput);

    // If 200 indeed got deleted then it should get recommended
    std::string getOutput = sendAndReceive("GET 7 700\n");
    std::string expectedGetOutput = std::string(OK) + "\n\n800\n";
    EXPECT_EQ(getOutput, expectedGetOutput);
}

TEST_F(ServerIntegrationTest, ValidMsgAfterInvalidOne) {
	// Sending an invalid msg
	std::string output = sendAndReceive("Pnina Rozenbloom New\n");
	std::string expectedOutput = BAD_REQUEST;
	EXPECT_EQ(output, expectedOutput);

	// Sending a valid messages after
	output = sendAndReceive("POST 9 900 \n");
    expectedOutput = CREATED; // Should return 200 created

    EXPECT_EQ(output, expectedOutput);

	// Adding user 2
    output = sendAndReceive("POST 10 900 200\n");
    expectedOutput = CREATED; // Should return 200 created

    EXPECT_EQ(output, expectedOutput);

    // Asking for a recommendation to see if information got added
    std::string getOutput = sendAndReceive("GET 9 900\n");
    std::string expectedGetOutput = std::string(OK) + "\n\n200\n";
    EXPECT_EQ(getOutput, expectedGetOutput);
}