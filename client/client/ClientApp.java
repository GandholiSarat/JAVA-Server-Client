package client;
import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 * This class represents a client application that communicates with a server, it includes functionality for establishing connections, sending and receiving files,
 * and handling communication with the server.
 *
 * @author Sarat
 */

public class ClientApp {
    private static Socket socket; // Socket for main communication
    private static Socket socketT1; // Socket for file transfer (sending)
    private static Socket socketT2; // Socket for file transfer (receiving)
    private static String fileName; // To store the name of the file being sent or received
    public static int port; //Port number
    public static String ip; //IP 

    /**
     * Main method to initiate the client application.
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // Loop to continuously prompt user for server IP and port until successful connection
        while (true) {
            try {
                System.out.println("Enter Server IP: ");
                ip = scanner.nextLine();
                System.out.println("Enter Server port: ");
                port = Integer.parseInt(scanner.nextLine()); // Parse port input as an integer

                connectToServer(ip, port);
                break; // If connection is successful, break out of the input loop
            } catch (IOException e) {
                // Handle IO Exceptions
                System.err.println("IO Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                // Handle Number Format Exceptions
                System.err.println("Invalid port number: " + e.getMessage());
            } catch (Throwable t) {
                // Handle any other exception
                System.err.println("Error: " + t.getMessage());
            }
        }

        boolean firstAttempt = true; // Flag to track if it's the first attempt
        // Loop to attempt reconnection in case of connection loss
        while (true) {
            try {
                if (!firstAttempt) { // Skip prompting for input on subsequent attempts
                    Thread.sleep(3000); // Wait for 3 seconds before attempting to reconnect
                    connectToServer(ip, port);
                }
                communicateWithServer();
            } catch (IOException e) {
                // Handle IO Exceptions
                System.err.println("IO Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                // Handle Number Format Exceptions
                System.err.println("Invalid port number: " + e.getMessage());
            } catch (Throwable t) {
                // Handle any other exception
                System.err.println("Error: " + t.getMessage());
            } finally {
                firstAttempt = false; // Set the flag to false after the first attempt
            }
        }
    }

    /**
     * Method to establish connections to the server.
     * @param ip Server IP address.
     * @param port Server port number.
     * @throws IOException if an I/O error occurs when creating the socket.
     */
    private static void connectToServer(String ip , int port) throws IOException {
        socket = new Socket(ip, port); // Main communication socket
        socketT1 = new Socket(ip, port+1); // Socket for file transfer (sending)
        socketT2 = new Socket(ip, port+2); // Socket for file transfer (receiving)
    }

    /**
     * Method to handle communication with the server.
     * @throws IOException if an I/O error occurs.
     */
    private static void communicateWithServer() throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String fromServer;
        String fromUser;

        // Continuous communication loop with the server
        while (true) {
            System.out.println("Server: ");
            while (!(fromServer = in.readLine()).equals("-EOF-")) {
                System.out.println(fromServer);
                if (fromServer.contains("File Recieving:")) {
                    fileName = fromServer.substring(fromServer.indexOf(':') + 1);
                    PrintWriter fwriter = new PrintWriter(socketT1.getOutputStream(), true);
                	 if (fromServer.contains("Invalid Choice.")) continue;
                    sendFile(fileName, fwriter);
                    continue;
                } else if (fromServer.contains("File Sending from Server:")) {
                    fileName = fromServer.substring(fromServer.indexOf(':') + 1);
                    BufferedReader freader = new BufferedReader(new InputStreamReader(socketT2.getInputStream()));
                	 if (fromServer.contains("Invalid Choice.")) continue;
                    receiveFile(fileName, freader);
                    continue;
                } else if (fromServer.contains("File not found: ")) {
			continue;
                } else if (fromServer.contains("Invalid Choice.")) {
			continue;
		}
                if (fromServer.contains("Bye")) {
                    out.close();
                    in.close();
                    stdIn.close();
                    socket.close();
                    System.exit(0);
                }
            }
            fromUser = stdIn.readLine();
            if (fromUser != null) {
                System.out.println("Client: " + fromUser);
                if (fromUser.contains("en=") || fromUser.contains("de=")) {
                        fileName = fromUser.substring(fromUser.indexOf('=') + 1);
			File file = new File(fileName);
            		if (!file.exists()) {
            		System.out.println( "File not found on client: " + fileName + "\n Try again\n");
                        out.println("cc= ");
			break;
            		}

			 
		}
                out.println(fromUser);
            }
        }
    }

    /**
     * Method to receive a file from the server.
     * @param fileName Name of the file to be received.
     * @param reader BufferedReader to read data from the socket.
     * @return Status message indicating the success or failure of the file reception.
     */
    public static String receiveFile(String fileName, BufferedReader reader) {
        try {
            PrintWriter fileWriter = new PrintWriter(new FileWriter(fileName));
            String line;
            while ((line = reader.readLine()) != null) {
                fileWriter.println(line);
            }
            fileWriter.close();
            reader.close();
            return "File received and saved as " + fileName + ".";
        } catch (IOException ex) {
            System.err.println("Error receiving file from client: " + ex.getMessage());
            return "File receive failed!";
        }
    }

    /**
     * Method to send a file to the server.
     * @param fileName Name of the file to be sent.
     * @param writer PrintWriter to write data to the socket.
     * @return Status message indicating the success or failure of the file sending.
     */
    public static String sendFile(String fileName, PrintWriter writer) {
        try {
	    File file = new File(fileName);
            if (!file.exists()) {
            	return "File not found: " + fileName;
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
            String line;
            while ((line = fileReader.readLine()) != null) {
                writer.println(line);
            }
            fileReader.close();
            writer.close();
            return "File " + fileName + " sent to client.";
        } catch (IOException e) {
            System.err.println("Error occurred while sending file: " + e.getMessage());
            return "File send failed!";
        }
    }
}
