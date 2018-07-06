package serverClasses;

public class Node {

	public Node nextLink; // pointer to the next Node of the Linked
	// every user that connects will be given an ID

	boolean playing; // if the user is playing or not

	Player player;

	// Link constructor
	public Node(Player p) {
		this.player = p;

	}

}
