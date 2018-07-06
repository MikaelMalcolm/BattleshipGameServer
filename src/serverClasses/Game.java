package serverClasses;

import java.io.IOException;
import java.net.SocketException;

public class Game extends Thread {

	public GameNode game;
	private String msg;
	private int turn = 1;

	private String p1ans;
	private String p2ans;

	private boolean gameSet = false;
	private boolean hit;

	private boolean mainPhase = false;
	ReceiveP2 player2Init;

	public Player player1;

	public Player Attacker;
	public Player Defender;

	private int x, y, result; // x and y locations used for BOMBs and RESUs

	String attackerMsg;
	String defenderMsg;


	
	// 7 for miss, 8 for hit

	public Game(GameNode g) {

		this.game = g;
	}

	public void run() {

		while (!(game.playing.equals("finished"))) {

			player2Init = new ReceiveP2(game);
			player2Init.start();

			try {

				msg = game.p1.recvWsMsg();
				

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (msg.substring(0, 4).equals("REDY")) {

				int i;
				int j;

				// HIGH nRISK
				msg = msg.substring(5);
				

				int position = 0;

				for (j = 0; j < 10; j++) {

					for (i = 0; i < 10; i++) {

						game.p1.board[i][j] = Integer.parseInt(msg.substring(
								position, position + 1));

						System.out.print(game.p1.board[i][j] + " ");
						position++;
					}
					//System.out.println("");

				}

				// game.p1.sendWsMsg("HEHE,");

				/**
				 * for(i=0;i<10;i++){
				 * 
				 * for(j=0;j<10;j++){
				 * 
				 * System.out.print(game.p1.board[i][j]+" ");
				 * 
				 * } System.out.println("");
				 * 
				 * }
				 **/

			} else {

				System.out.println("recevied an invalid message!");

			}

			// wait for the thread that is collecting info about player 2 to
			// finish
			try {
				player2Init.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			mainPhase = true;

			try {
				Battle();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			try {
				p1ans = game.p1.recvWsMsg();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				p2ans = game.p2.recvWsMsg();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (((p1ans.equals("AGAN,1")) && (p2ans.equals("AGAN,1")))) {
				game.p1.sendWsMsg("AGAN,1");
				game.p2.sendWsMsg("AGAN,1");

				this.game.playing = "finished";

			} else {

				game.p1.sendWsMsg("AGAN,0");
				game.p2.sendWsMsg("AGAN,0");

			}

		}

		// delete the Game Node here? =P

	}

	public void Battle() throws IOException {

		game.p1.turn = true; // PIvotal moment!!!!!!!!
		game.p2.turn = false;

		while (mainPhase == true) {

			Attacker = game.FindTrue();
			System.out.print("Attacker is: " + Attacker.name);
			Defender = game.FindFalse();
			System.out.print("Defender is: " + Defender.name);
			Attacker.sendWsMsg("BOMB,");
			System.out.println("I am sending a bomb");
			Defender.sendWsMsg("LOLO,");
			System.out.println("I am sending a LOL"); 

			msg = Attacker.recvWsMsg();
			System.out.println(msg);
			
		  if(msg == "" || Attacker.clientSocket.isConnected() == false){
			  
			  
			       Defender.sendWsMsg("WINN,");

		  }else if (msg.substring(0, 4).equals("BOM1")) {

				x = Integer.parseInt(msg.substring(5, 6));
				y = Integer.parseInt(msg.substring(6, 7));

				if (Defender.board[x][y] == 0) {
					result = 7;
					hit = false;
					Defender.board[x][y] = 7;

				} else {
					result = 8;
					hit = true;
					Defender.board[x][y] = 8;
					System.out.println("ABC");

				}

				gameSet = CheckResult(Attacker, Defender);

				if (gameSet == false) {
                    
					
					attackerMsg = "RES1," + Integer.toString(x)
							+ Integer.toString(y) + Integer.toString(result);
					System.out.println("I am sendinjg a result to"
							+ Attacker.name);
					defenderMsg = "RES1," + Integer.toString(x)
							+ Integer.toString(y) + Integer.toString(result);
					System.out.println("I am sendinjg a result to:"
							+ Defender.name);
					
					Attacker.sendWsMsg(attackerMsg);
					Defender.sendWsMsg(defenderMsg);

				} else if (gameSet == true) {

					attackerMsg = "RES1," + Integer.toString(x)
							+ Integer.toString(y) + Integer.toString(result);
					System.out.println("I am sendinjg a result to"
							+ Attacker.name);
					defenderMsg = "RES1," + Integer.toString(x)
							+ Integer.toString(y) + Integer.toString(result);
					System.out.println("I am sendinjg a result to:"
							+ Defender.name);
					Attacker.sendWsMsg(attackerMsg);
					Defender.sendWsMsg(defenderMsg);
					attackerMsg = "WINN,";
					defenderMsg = "LOSE,";
					mainPhase = false;
					Attacker.sendWsMsg(attackerMsg);
					Defender.sendWsMsg(defenderMsg);
					System.out.print(attackerMsg);
					System.out.print(defenderMsg);

				}

			} else if (msg.substring(0, 4).equals("BOM2")) {
				System.out.println(msg);

				x = Integer.parseInt(msg.substring(5, 6));
				y = Integer.parseInt(msg.substring(6, 7));
				int i = 0;
				int j = 0;
				String output = "";

				for (i = x; i < x + 3; i++) {

					for (j = y; j < y + 3; j++) {

						if (Defender.board[i][j] == 0) {
							Defender.board[i][j] = 7;
							output = output.concat(Integer
									.toString(Defender.board[i][j]));

						} else if ((Defender.board[i][j] >= 1)
								&& (Defender.board[i][j] <= 6)) {

							Defender.board[i][j] = 8;
							output = output.concat(Integer
									.toString(Defender.board[i][j]));
						}

					}

				}

				gameSet = CheckResult(Attacker, Defender);

				if (gameSet == false) {

					attackerMsg = "RES2," + Integer.toString(x)
							+ Integer.toString(y) + output;
					System.out.println("I am sendinjg a result to"
							+ Attacker.name);
					defenderMsg = "RES2," + Integer.toString(x)
							+ Integer.toString(y) + output;
					System.out.println("I am sendinjg a result to:"
							+ Defender.name);
					Attacker.sendWsMsg(attackerMsg);
					Defender.sendWsMsg(defenderMsg);

					Attacker.turn = false;
					Defender.turn = true;

				} else if (gameSet == true) {
					attackerMsg = "RES2," + Integer.toString(x)
							+ Integer.toString(y) + output;
					System.out.println("I am sendinjg a result to"
							+ Attacker.name);
					defenderMsg = "RES2," + Integer.toString(x)
							+ Integer.toString(y) + output;
					System.out.println("I am sendinjg a result to:"
							+ Defender.name);

					Attacker.sendWsMsg(attackerMsg);
					Defender.sendWsMsg(defenderMsg);

					attackerMsg = "WINN," + Integer.toString(x)
							+ Integer.toString(y) + Integer.toString(result);
					defenderMsg = "LOSE," + Integer.toString(x)
							+ Integer.toString(y) + Integer.toString(result);
					mainPhase = false;

					Attacker.sendWsMsg(attackerMsg);
					Defender.sendWsMsg(defenderMsg);
					System.out.print(attackerMsg);
					System.out.print(defenderMsg);

				}

			} else if (msg.substring(0, 4).equals("SURR")) {

				attackerMsg = "LOSE," + Integer.toString(x)
						+ Integer.toString(y) + Integer.toString(result);
				defenderMsg = "WINN," + Integer.toString(x)
						+ Integer.toString(y) + Integer.toString(result);
				mainPhase = false;
				Attacker.sendWsMsg(attackerMsg);
				Defender.sendWsMsg(defenderMsg);

				// break?

			} else if (msg.substring(0, 4).equals("MSGS")) {

				Defender.sendWsMsg("MSGS" + msg.substring(5));
			} else {

				System.out
						.println("Excuse me, the client sent and unfamiliar messgae");
			}

			if ((hit == false) && !(msg.substring(0, 4).equals("BOM2"))) {

				Attacker.turn = false;
				Defender.turn = true;
			}
		}

	}

	// function for checking the result of the thing. Feel me?
	
	public boolean CheckResult(Player a, Player d) {

		int i;
		int j;

		boolean conclusion = true;

		for (i = 0; i < 10; i++) {

			for (j = 0; j < 10; j++) {

				if ((d.board[i][j] >= 1) && (d.board[i][j] <= 6)) {

					conclusion = false;
				}
			}

		}

		return conclusion;

	}

}