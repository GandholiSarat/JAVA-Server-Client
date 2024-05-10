package server;

import java.io.*;
import java.net.*;
import java.util.*;
import server.fileencryption.FileEncryption;
import server.goodthoughts.GoodThoughtsProvider;
import server.passwordgenerator.PasswordGenerator;
import server.internetsearch.GoogleSearch;
import server.filetransfer.FileTransfer;
import server.currencyconverter.CurrencyConverter;
import server.nlp.TextAnalysisService;

/**
 * ThreadServer class handles client connections and provides various services.
 * It extends Thread to handle multiple clients concurrently.
 * @author Sarat
 */

public class ThreadServer  extends Thread {
    private Socket socket = null;
    private Socket socketT1 = null;
    private Socket socketT2 = null;
    private Random rd;
    private int port=8441;

    /**
     * Constructor for ThreadServer.
     * @param socket Client socket
     * @param socketT1 Socket for file transfer
     * @param socketT2 Socket for file transfer
     */

    public ThreadServer (Socket socket,Socket socketT1,Socket socketT2) {
        super("ThreadServer");
        rd = new Random();
        this.socket = socket;
        this.socketT1 = socketT1;
        this.socketT2 = socketT2;
    }

    /**
     * Run method for handling client communication.
     */
    public void run() {
        Server.addMe();
        System.out.println("Now connected to: "+socket.getInetAddress());
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(
                                    new InputStreamReader(socket.getInputStream()));

            String inputLine, outputLine="--";
            StringBuffer output = new StringBuffer();
            output.append("You are now connected to Sarat's Server...\n");
		output.append(menu());
                output.append("\n\n-EOF-");

                out.println(output.toString());
            while ((inputLine = in.readLine()) != null) {
                        

          		String i = inputLine.toLowerCase().trim();
          		StringBuffer o = new StringBuffer();
                	if(inputLine.startsWith("en")){
    				if (inputLine.equals("en") || inputLine.equals("en=")) {
                        		out.println("File not provided. Please specify the file.");
                        		out.println("-EOF-");
					continue;
				}
                        	String fileName = inputLine.substring(inputLine.indexOf('=')+1);
				out.println("File Recieving:" + fileName );
            			BufferedReader freader = new BufferedReader( new InputStreamReader(socketT1.getInputStream()));
				FileTransfer fileTransfer = new FileTransfer();
				String result = fileTransfer.receiveFile(fileName,freader);
                        	out.println("Server: " + result);
                        	outputLine = processInput(inputLine);
                        	out.println(outputLine);
            			PrintWriter fwriter = new PrintWriter(socketT2.getOutputStream(), true);
    				result = fileTransfer.sendFile(fileName+"_encrypted" ,fwriter);
                        	out.println("Server: "+ result + "\n\n" + menu());
                        	out.println("-EOF-");
				continue;

			}
			if(inputLine.startsWith("de")){
    				if (inputLine.equals("de") || inputLine.equals("de=")) {
                        		out.println("File not provided. Please specify the file.");
                        		out.println("-EOF-");
					continue;
				}
                        	String fileName = inputLine.substring(inputLine.indexOf('=')+1);
				out.println("File Recieving:" + fileName );
            			BufferedReader freader = new BufferedReader( new InputStreamReader(socketT1.getInputStream()));
				FileTransfer fileTransfer = new FileTransfer();
				String result = fileTransfer.receiveFile(fileName,freader);
                        	out.println("Server: " + result);
                        	outputLine = processInput(inputLine);
                        	out.println(outputLine);
            			PrintWriter fwriter = new PrintWriter(socketT2.getOutputStream(), true);
    				result = fileTransfer.sendFile(fileName+"_decrypted" ,fwriter);
                        	out.println("Server: "+ result + "\n\n" + menu());
                        	out.println("-EOF-");
				continue;

			}
			/*if(inputLine.startsWith("fnf")){
                        	out.println(menu());
                        	out.println("-EOF-");
				continue;}*/
                        outputLine = processInput(inputLine);
                        out.println(outputLine);
                        out.println("-EOF-");
                        if (outputLine.contains("Bye")){
                            out.close();
                            in.close();
                            socket.close();
                            socketT1.close();
                            socketT2.close();
                            break;
                        }
                }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
                try{
                    socket.close();
                    socketT1.close();
                    socketT2.close();
                    Server.removeMe();
                }catch(Exception e){System.out.println("Problem while closing socket..."+e);}
        }
  }

  /**
     * Generates the menu for available services.
     * @return Menu string
     */
  private String menu(){
	  return (" Available services:" + System.lineSeparator() +
                        "1. Encrypt File - en" + System.lineSeparator() +
                        "2. Decrypt File - de" + System.lineSeparator() +
                        "3. Good Thought - gt " + System.lineSeparator() +
                        "4. Password Generator - pg" + System.lineSeparator() +
                        "5. Google Search - gs" + System.lineSeparator() +
                        "6. Currency Converter - cc" + System.lineSeparator() +
                        "7. NLP - nlp" + System.lineSeparator() +
                        "8. Exit - exit" + System.lineSeparator() + 
			"type 'menu' to get Menu again" + System.lineSeparator() +
			"type 'help' to get usage " + System.lineSeparator());
  }
   /**
     * Generates the usage for available services.
     * @return Help string
     */
  private String help(){
          StringBuffer output = new StringBuffer();
          output.append("\n\n");
          output.append("Here are the following command you can use:\n");
          output.append("en=<filename>\n");
          output.append("de=<filename>\n");
          output.append("gt\n");
          output.append("pg=<lenght>\n");
          output.append("gs=<query>\n");
          output.append("cc=<from>,<to>,<amount>\n");
          output.append("nlp=<string>\n");
          output.append("exit to quit\n");
          return output.toString();

  }

  /**
     * Processes the client input and generates appropriate responses.
     * @param inputLine Client input
     * @return Response string
     */
  private String processInput(String inputLine){
          String input = inputLine.toLowerCase().trim();
          StringBuffer output = new StringBuffer();
                if(inputLine.startsWith("help")){
                        output.append(help());
                        return output.toString();
                }else if(inputLine.startsWith("menu")){
			output.append(menu());
                        return output.toString();
                }else if(inputLine.startsWith("en")){
    			if (inputLine.equals("en") || inputLine.equals("en=")) {
    				output.append("File not provided. Please specify the file.");
                        	output.append(help());
    				return output.toString();
			}
                       	String fileName = inputLine.substring(inputLine.indexOf('=')+1);
			FileEncryption fileEncryption = new FileEncryption();
                        output.append("File Sending from Server:"+ fileEncryption.encryptFile(fileName));
			
                        return output.toString();
                }else if(inputLine.startsWith("de")){
    			if (inputLine.equals("de") || inputLine.equals("de=")) {
    				output.append("File not provided. Please specify the file.");
                        	output.append(help());
    				return output.toString();
			}
			FileEncryption fileEncryption = new FileEncryption();
                        output.append("File Sending from Server:" + fileEncryption.decryptFile(inputLine.substring(inputLine.indexOf('=')+1)));
                        return output.toString();
                }else if(inputLine.startsWith("gt")){
			GoodThoughtsProvider g= new GoodThoughtsProvider();
                        output.append(g.getRandomGoodThought());
			output.append("\n\n" + menu());
                        return output.toString();
		} else if (inputLine.startsWith("pg")) {
    			String lenString = inputLine.substring(inputLine.indexOf('=') + 1).trim();
    			if (inputLine.equals("pg")) {
    				output.append("Length not provided. Please specify the length.");
			}else if (!lenString.isEmpty()) {
        			int len = Integer.parseInt(lenString);
        			PasswordGenerator passwordGenerator = new PasswordGenerator();
				String pass = passwordGenerator.generatePassword(len);
        			output.append("Generated Password: " + pass);
    			} else {
        			output.append("Length not provided. Please specify the length.");
    			}
			output.append("\n\n" + menu());
    			return output.toString();
                }else if(inputLine.startsWith("gs")){
    			if (inputLine.equals("gs") || inputLine.equals("gs=")) {
    				output.append("Query not provided. Please specify the Query.");
    				return output.toString();
			}
    			String query = inputLine.substring(inputLine.indexOf('=') + 1);
			GoogleSearch googleSearch = new GoogleSearch();
			output.append("Google Search Results:\n" + googleSearch.search(query) );
			output.append("\n\n\n" + menu());
    			return output.toString();
                }else if(inputLine.startsWith("cc")){
    			if (inputLine.equals("cc") || inputLine.equals("cc=")) {
    				output.append("Input not full. Please give full Query.");
    				return output.toString();
			}
			String query = inputLine.substring(inputLine.indexOf('=') + 1);
    			String[] words = query.split(",");
			String fromC = words[0].toUpperCase();
			String toC = words[1].toUpperCase();
    			double amount = Double.parseDouble(words[2]);
			output.append(fromC + "  to  "+ toC + " is: ");
			try{
				CurrencyConverter currencyConverter = new CurrencyConverter();
				output.append(currencyConverter.convertCurrency(fromC,toC,amount));
			} catch (IOException e){ output.append("Error in IO \n");
			} catch ( CurrencyConverter.CurrencyConversionException e){ output.append("Error in convertion\n");}
			output.append("\n\n\n" + menu());
    			return output.toString();

                }else if(inputLine.startsWith("nlp")){
    			if (inputLine.equals("nlp") || inputLine.equals("nlp=")) {
    				output.append("String not provided. Please specify the String.");
    				return output.toString();
			}
    			String query = inputLine.substring(inputLine.indexOf('=') + 1);
			try {
                     		TextAnalysisService textAnalysisService = new TextAnalysisService();
                     		output.append(textAnalysisService.nlp(query));
                  } catch (IOException e) {
                     output.append("Error in creating object disconnect and reconnect\n");
                  }
			output.append("\n\n\n" + menu());
    			return output.toString();
                }else if(inputLine.startsWith("exit")){
                        return "Bye and Have a good day ";
                }else {
                        return "Sorry client, I did not understand your request...Try again...";
                }
  }
  
}
