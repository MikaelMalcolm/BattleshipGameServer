package serverClasses;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Dictionary;
import java.util.LinkedList;

public class BattleServer {

	// private static final String = null;

	public static void main(String args[]) throws NumberFormatException,
			IOException {

		LinkList onlineUsers = new LinkList();
		GameLinkList games = new GameLinkList();
        
		games.insert(); // initialize dat
		
		int port = 0;

		port = Integer.valueOf(args[0]);

		int userID = 0; // latest userID
		ServerSocket serverSocket = null;
		// Socket client;

		try {
			serverSocket = new ServerSocket(port);
			
			//
			System.out.println("Battleship server started");
		} catch (IOException e) {
			System.err.println("Could not listen on this port");
		}
	//	new GameStartThread(games, onlineUsers).start();

		while (true) {

			userID += 1;
			new UserThread(serverSocket.accept(), onlineUsers, userID, games)
					.start();

		}

	}

}
