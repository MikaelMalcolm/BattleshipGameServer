package serverClasses;

public class GameNode {

	Player p1;
	Player p2;

	public GameNode nextLink; // pointer to the next GameNode of the Linked

	String playing; // if the user is playing or not

	// Link constructor
	public GameNode() {

		this.playing = "waiting";

	}

	public Player FindFalse() {

		Player notTurn;

		if (this.p1.turn == false) {

			notTurn = this.p1;

		} else if (this.p2.turn == false) {

			notTurn = this.p2;
		} else {

			notTurn = null;
			System.out.println("None of them are false");
		}

		return notTurn;
	}

	public Player FindTrue() {

		Player Turn;

		if (this.p1.turn == true) {

			Turn = this.p1;

		} else if (this.p2.turn == true) {

			Turn = this.p2;
		} else {

			System.out.println("None of them are true");
			Turn = null;

		}

		return Turn;
	}
}
