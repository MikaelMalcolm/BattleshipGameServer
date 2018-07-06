package serverClasses;

import java.io.BufferedReader; 
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.apache.commons.codec.binary.Base64;

public class UserThread extends Thread {

	Socket clientSocket = null;
	String cAddress;
	int CPortNumber;
	boolean status = false; // player status - 'false' for not playing, 'true'
							// for playing

	LinkList onlineUsers;
	int userID;

	String memberNodeInfo;

	String initialAction;

	Node opponent;

	Player gamer;
	
	MessageDigest wsKeySha1;  //the key that will be sent back to the client during the WebSocket
	

	GameLinkList games; // reference to the linked list of all the current games
	
	String webSocketAccept; // the accept thing we will send back to server.
	
	
	char[] cbuf= new char[100];
    
	GameNode game;
	
	public UserThread(Socket cSocket, LinkList onlineU, int id, GameLinkList g)
			throws IOException {

		this.gamer = new Player(cSocket, id);

		this.onlineUsers = onlineU;

		this.games = g;
		
		try{
		this.wsKeySha1 = MessageDigest.getInstance("SHA-1");
		}catch(NoSuchAlgorithmException e){
			 System.out.println("No such luck my friend.");
			
		}

	}

	public void run() {

		this.onlineUsers.insert(gamer);

		System.out.println(this.onlineUsers.makeList(gamer.userID));

		// function that will listen for requests, whether it is to join a game
		// or to disconnect
		String connectVal = " ";
		String webSocketKey = "";
		
		//WebSocket Handshake yo
		
		while ((gamer.clientSocket.isClosed() == false) && (connectVal.equals("") == false)) {
			
			try {
				connectVal = gamer.in.readLine();
				System.out.println(connectVal);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if((connectVal.length() > 19) && (connectVal.substring(0,19).equals("Sec-WebSocket-Key: "))){
				
				webSocketKey = connectVal.substring(18);
				System.out.println("I got the KEY:" + webSocketKey);
			   webSocketKey = webSocketKey + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
			   webSocketKey = webSocketKey.substring(1);
			   
			   System.out.println(webSocketKey);
			   
			   
			   try {
				wsKeySha1.update(webSocketKey.getBytes("iso-8859-1"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			   
			   byte[] output = wsKeySha1.digest();
			   
			   byte[] encodedBytes = Base64.encodeBase64(output);
			   
			   webSocketAccept = new String(encodedBytes);
			   
			   System.out.println("WebSocket Accept:" + webSocketAccept);
			   
			   
			}
			
		
		}
		
		
		gamer.out.println("HTTP/1.1 101 Switching Protocols \r\nUpgrade: WebSocket\r\nConnection: Upgrade\r\nSec-WebSocket-Accept:" + webSocketAccept +"\r\n");
     
		gamer.out.flush();
		//End of handshake. Should be connected by this point
		

		while (gamer.clientSocket.isClosed() == false) {

			try {
				 // initialAction = gamer.in.readLine();
               // gamer.in.read(cbuf,0,100);
                
				//initialAction = new String(cbuf);
                
                initialAction = gamer.recvWsMsg();
                gamer.sendWsMsg("You");
                
				
                System.out.println(initialAction);
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(!initialAction.equals(null)){
				if (initialAction.equals("QUIT")) {
	
					break;
				} else if (initialAction.substring(0, 4).equals("PLAY")) {
	
					gamer.name = initialAction.substring(5);
	
					this.games.add(this.gamer);
					
					System.out.println("Player ID" + Integer.toString(gamer.userID)
							+ "has been added to the game queue");
					
					game = games.search(); //get the first game node in the linked list
					
					//split into seperate function
					if (game.playing.equals("waiting")
							&& (game.p1 != null && game.p2 != null)) {
						game.playing = "playing";
					
						
						System.out.println("I got here");
						
						
		                game.p1.sendWsMsg("PLAY," + game.p2.name);
		                game.p2.sendWsMsg("PLAY," + game.p1.name);
		                
						// create the game object thread
						new Game(game).start();

						System.out.println(Integer.toString(game.p1.userID) + "and"
								+ Integer.toString(game.p2.userID)
								+ "have started a game");

						games.insert();
						
					}
				
					
	          
	                 
					while (!(this.games.searchID(this.gamer.userID).playing)
							.equals("finished")) { // stop the thred here until
													// their game is finished
							//System.out.println("Awwwwwww yeeeeahhhh!!!");
	
					}
	
				
	
			      }
			}
			
	}
	

 }
}