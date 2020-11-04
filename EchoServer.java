// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 

import java.io.*;

import common.ChatIF;
import ocsf.server.*;

/**
 * This class overrides some of the methods in the abstract 
 * superclass in order to give more functionality to the server.
 *
 * @author Dr Timothy C. Lethbridge
 * @author Dr Robert Lagani&egrave;re
 * @author Fran&ccedil;ois B&eacute;langer
 * @author Paul Holden
 * @version July 2000
 */
public class EchoServer extends AbstractServer 
{
  //Class variables *************************************************
  
  /**
   * The default port to listen on.
   */
  final public static int DEFAULT_PORT = 5555;
  
  //Constructors ****************************************************
  
  /**
   * Constructs an instance of the echo server.
   *
   * @param port The port number to connect on.
   */
  public EchoServer(int port) 
  {
    super(port);
  }

  ChatIF serverUI;
  
  public EchoServer(int port,ChatIF serverUI) 
  {
    super(port);
    this.serverUI = serverUI;
  }

  
  //Instance methods ************************************************
  
  /**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   */
  
  
  public void handleMessageFromClient(Object msg, ConnectionToClient client){
	  
	  System.out.println("Message received: " + msg + " from " + client.getInfo("loginId"));
	  
	  
	  
	  if((boolean) client.getInfo("loginMessage")){	
		String[] messageArgs = ((String)msg).split(" ", 2);
		client.setInfo("loginMessage", false);
			client.setInfo("loginId", messageArgs[1]);
			System.out.println(client.getInfo("loginId")+" has logged on.");
			this.sendToAllClients(client.getInfo("loginId")+" has logged on.");

		}
	
	
	  else{

			if(((String)msg).equals("#login")){
				
					try {
						client.sendToClient("erreur vous etes deja connecté ");
						client.close();
					} catch (IOException e) {
						System.out.println(e);
					}
					

				}
				
			
		    this.sendToAllClients((String)client.getInfo("loginId")+ ">"+ msg);}
		}
	  
	  

	  
    
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
   */
  boolean serverstart;
  protected void serverStarted()
  {
	  serverstart= true;
    System.out.println
      ("Server listening for connections on port " + getPort());
  }
  
  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */
  protected void serverStopped()
  {   serverstart=false;
    System.out.println
      ("Server has stopped listening for connections.");
  }
  
  protected void clientConnected(ConnectionToClient client) {
	  System.out.println("A new client is attempting to connect to the server."); 
	  client.setInfo("loginMessage", true);

}
  synchronized protected void clientException(ConnectionToClient client, Throwable exception) {
	  clientDisconnected(client);
  }
  
  synchronized protected void clientDisconnected( ConnectionToClient client) {
	  
	  System.out.println(client.getInfo("loginId")+ " has disconnected.");
	  this.sendToAllClients(client.getInfo("loginId")+ " has disconnected.");}

  
  //Class methods ***************************************************
  
  /**
   * This method is responsible for the creation of 
   * the server instance (there is no UI in this phase).
   *
   * @param args[0] The port number to listen on.  Defaults to 5555 
   *          if no argument is entered.
   */
  public static void main(String[] args) 
  {
    int port = 0; //Port to listen on

    try
    {
      port = Integer.parseInt(args[0]); //Get port from command line
    }
    catch(Throwable t)
    {
      port = DEFAULT_PORT; //Set port to 5555
    }
	
    EchoServer sv = new EchoServer(port);
    
    try 
    {
      sv.listen(); //Start listening for connections
    } 
    catch (Exception ex) 
    {
      System.out.println("ERROR - Could not listen for clients!");
    }
  }


public void handleMessageFromServerUI(String message) {
	
		  if(message.charAt(0) == '#'){
				try{
					servCommands(message);
				}
				catch(IOException e){
				}
			}
		  else{
			  serverUI.display("SERVER MESSAGE>" + message);
			  sendToAllClients("SERVER MESSAGE>" + message); }
		  }


private void servCommands(String message) throws IOException{
	
	
	String[] messageargs= message.split(" ", 2);
	  message=messageargs[0];
	  if(message.equals("#quit")) {
	   System.exit(0);}
	  	  
	  else if(message.equals("#stop"))  {
       stopListening();
       }
	  else if (message.equals("#close")) {
		  		close();
		  		}
	  else if(message.equals("#setport")) {
			 if(!serverstart) {
			  		setPort(Integer.parseInt(messageargs[1]));
			  		System.out.println("port set to: " +messageargs[1]);
			  	}
				else{
					 System.out.println("le serveur doit etre fermé pour changer de port");
				}}
	  else if(message.equals("#start")) {
		  if(!isListening()) {
				listen();
		  	}
			else{
				 System.out.println("le serveur est deja started");
			}
		  
	  }
		  
		 
	  else if(message.equals("#getport")) {
		  serverUI.display("Port: "+ getPort());}
			 else {	  System.out.println("commande non valide"); 
		  	
	  }
}
	  
		

 
}
//End of EchoServer class
