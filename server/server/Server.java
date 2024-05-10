package server;

import java.io.*;
import java.net.*;

/**
 * The Server class represents a multi-threaded server application that listens
 * for incoming client connections on three different ports and handles client
 * requests concurrently.
 * @author Sarat
 */

public class Server {
	// Static fields to keep track of connected clients and socket connections
        private static int noOfClients=0;
	private static Socket clientSocket = null;
	private static Socket clientSocketT1 = null;
	private static Socket clientSocketT2 = null;
        private static ServerSocket serverSocket = null;
        private static ServerSocket serverSocketT1 = null;
        private static ServerSocket serverSocketT2 = null;

    /**
     * The main method initializes the server sockets for each port and continuously
     * listens for incoming client connections.When a client connects, it creates
     * a new thread to handle the client's requests.
     * 
     * @param args Command-line arguments (not used)
     * @throws IOException If an I/O error occurs when opening the server sockets
     */
    public static void main(String[] args) throws IOException {
        serverSocket = null;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(9442);
            serverSocketT1 = new ServerSocket(9443);
            serverSocketT2 = new ServerSocket(9444);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4441.");
            System.exit(-1);
        }


	// Continuously accept client connections
        while (listening){
	    try{
		clientSocket = serverSocket.accept();
		clientSocketT1 = serverSocketT1.accept();
		clientSocketT2 = serverSocketT2.accept();
            	InetAddress clientAddress = clientSocket.getInetAddress();
            	new ThreadServer (clientSocket,clientSocketT1,clientSocketT2).start();
	    } catch (IOException e) {
		    // Handle error if failed to accept client connection
                System.err.println("Error accepting client connection: " + e.getMessage());
            }
	}
        serverSocket.close();
        serverSocketT1.close();
        serverSocketT2.close();
    }

    /**
     * Increment the number of connected clients and print the current count.
     */
    public static void addMe(){
                noOfClients++;
                System.out.println("Current no. of clients connected: "+noOfClients);
        
    }

    /**
     * Decrement the number of connected clients and print the current count.
     */
    public static void removeMe(){
                noOfClients--;
                System.out.println("Current no. of clients connected: "+noOfClients);
    }

    /**
     * Get the IP address of the connected client.
     *
     * @return The IP address of the connected client, or null if no client is
     *         connected
     */
    public static String getClientIP() {
        if (clientSocket != null) {
            return clientSocket.getInetAddress().getHostAddress();
        } else {
            return null;
        }
    }

}

