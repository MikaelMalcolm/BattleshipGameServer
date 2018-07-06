package serverClasses;

import java.io.IOException;

public class ReceiveP2 extends Thread {

	public GameNode game;
	String msg;
	boolean valid = false;

	public ReceiveP2(GameNode g) {

		this.game = g;

	}

	public void run() {

		while (valid == false) {

			try {
				msg = game.p2.recvWsMsg();
				System.out.println(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (msg.substring(0, 4).equals("REDY")) {
				int i;
				int j;
				int position = 0;
				msg = msg.substring(5);

				for (j = 0; j < 10; j++) {

					for (i = 0; i < 10; i++) {

						game.p2.board[i][j] = Integer.parseInt(msg.substring(
								position, position + 1));

						System.out.print(game.p2.board[i][j] + " ");
						position++;
					}
					System.out.println("");

				}

			}

			valid = true;
		}

	}

}
