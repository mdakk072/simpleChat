// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

package client;

import ocsf.client.*;
import common.*;
import java.io.*;

/**
 * This class overrides some of the methods defined in the abstract
 * superclass in order to give more functionality to the client.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;
 * @author Fran&ccedil;ois B&eacute;langer
 * @version July 2000
 */
public class ChatClient extends AbstractClient
{
  //Instance variables **********************************************
  
  /**
   * The interface type variable.  It allows the implementation of 
   * the display method in the client.
   */
  ChatIF clientUI; 
  String loginId;
  

  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the chat client.
   *
   * @param host The server to connect to.
   * @param port The port number to connect on.
   * @param clientUI The interface type variable.
   */
  
  public ChatClient(String loginId , String host, int port, ChatIF clientUI) 
    throws IOException 
  {
    super(host, port); //Call the superclass constructor
    this.clientUI = clientUI;
	 this.loginId = loginId;
	


    try{openConnection();}
    catch(Exception ex) {
    System.out.println("Cannot open connection.  Awaiting command.");}
    if(isConnected())
	sendToServer("#login "+loginId);


  }

  
  //Instance methods ************************************************
    
  /**
   * This method handles all data that comes in from the server.
   *
   * @param msg The message from the server.
   */
  public void handleMessageFromServer(Object msg) 
  {
    clientUI.display(msg.toString());
  }

  /**
   * This method handles all data coming from the UI            
   *
   * @param message The message from the UI.    
   */
  public void handleMessageFromClientUI(String message){
	  
	  

	  
	  if(message.charAt(0) == '#') {
		  try {
				commands(message);} 
		  catch(IOException e) {
		  }
	  }
		
	  
  else {
    try
    {
      sendToServer(message);
    }
    catch(IOException e)
    {
      clientUI.display
        ("Could not send message to server.  Terminating client.");
      quit();
    }}
  }
  
  private void commands(String message)  throws IOException {
	  
	  String[] messageargs= message.split(" ", 2);

	  //create string array to handle setHost and setPort
	 if(messageargs[0].equals("#quit")) {
		 System.out.println("Connection Closed.");
	  	   System.exit(0);
	 }
	 
	 
	 else if(messageargs[0].equals("#logoff")) {
		 closeConnection();
	 System.out.println("Connection closed");}
	 
	 
	 else if(messageargs[0].equals("#sethost")) {
		 if(!isConnected()) {
				setHost(messageargs[1]);
		  		System.out.println("Host set to: "+messageargs[1]);

		  	}
			else{
				 System.out.println("vous devez vous déconnceter pour changer de host");
	 }}
	 else if(messageargs[0].equals("#setport")) {
		 if(!isConnected()) {
		  		setPort(Integer.parseInt(messageargs[1]));
		  		System.out.println("Port set to: "+messageargs[1]);
		  	}
			else{
				 System.out.println("vous devez vous deconnceter pour changer de port");
			}}
		 
		 else if (message.startsWith("#login")) {
			 
				if(!isConnected()) {
					openConnection();
					sendToServer(message);
			  	}
				else{
					 System.out.println("il faut etre Déconnceté pour se login");}
		 
		 }
		
		 else if(message.equals("#gethost")) {
	  clientUI.display("Host: "+ getHost());}
		 else if(message.equals("#getport")) {
	  clientUI.display("Port: "+ getPort());}
		 else {	   System.out.println("Commande non valide "); 
}
	
		   
	
  }
  
  /**
   * This method terminates the client.
   */
  public void quit()
  {
    try
    {
      closeConnection();
    }
    catch(IOException e) {}
    System.exit(0);
  }
  protected void connectionException(Exception exception) {
	  System.out.println("WARNING - The server has stopped listening for connections\n"
	  		+ "SERVER SHUTTING DOWN! DISCONNECTING!\n");
	  connectionClosed();
  }
 
}
//End of ChatClient class
