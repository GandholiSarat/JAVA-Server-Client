package server.filetransfer;

import java.io.*;
import java.net.*;

/**
 * This class handles file transfer operations between a server and a client.
 * @author Sarat
 */
public class FileTransfer {
    
    /**
     * Receives a file from the client and saves it on the server.
     * 
     * @param fileName The name of the file to save.
     * @param reader   BufferedReader connected to the client's input stream.
     * @return A message indicating the status of the file receiving process.
     */
    public String receiveFile(String fileName, BufferedReader reader) {
        try {
            PrintWriter fileWriter = new PrintWriter(new FileWriter(fileName)); // Create a PrintWriter to write to the file

            String line;
            while ((line = reader.readLine()) != null) {
                fileWriter.println(line); // Write each line received from the client to the file
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
     * Sends a file to the client.
     * 
     * @param fileName The name of the file to send.
     * @param writer   PrintWriter connected to the client's output stream.
     * @return A message indicating the status of the file sending process.
     */
    public String sendFile(String fileName, PrintWriter writer) {
        try {
	    File file = new File(fileName);
            if (!file.exists()) {
                return "File not found: " + fileName;
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName)); // Open the file for reading
            String line;
            while ((line = fileReader.readLine()) != null) {
                writer.println(line); // Send each line from the file to the client
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

