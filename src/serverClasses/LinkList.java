package serverClasses;

class LinkList {
	private Node first;
	protected int nextType = 0;

	// LinkList constructor
	public LinkList() {
		first = null;
	}

	// Inserts a new Link at the first of the list
	public void insert(Player p) {
		Node link = new Node(p);
		link.nextLink = first;
		first = link;
	}

	// Deletes the link at the first of the list
	public String delete(int deleteID) {
		Node tempLink = first;
		String statusMessage = "There are no users to remove";

		if (tempLink == null) {
			statusMessage = "There is nothing in the dictionary.";
		} else if (tempLink.player.userID == deleteID) {
			first = tempLink.nextLink;
			statusMessage = Integer.toString(tempLink.player.userID)
					+ " has been deleted from the dictionary.";
		} else {
			while (tempLink.nextLink != null) {
				if (tempLink.nextLink.player.userID == deleteID) {
					statusMessage = Integer
							.toString(tempLink.nextLink.player.userID)
							+ " has been deleted from the dictionary.";
					break;
				}
				tempLink = tempLink.nextLink;
			}
		}
		return statusMessage;
	}

	// Find the word from the link list
	public Node search() {
		// int key = -1;

		Node tempLink = first;

		while (tempLink != null) {
			if (tempLink.playing == false) {

				break;
			}
			tempLink = tempLink.nextLink;
		}
		return tempLink;
	}

	public Node searchID(int ID) {
		// int key = -1;

		Node tempLink = first;

		while (tempLink != null) {
			if (tempLink.player.userID == ID) {

				break;
			}
			tempLink = tempLink.nextLink;
		}
		return tempLink;
	}

	// Construct display list
	public String makeList(int ID) {
		String list = "List : ";
		Node tempLink = first;

		while (tempLink != null) {
			if (tempLink.player.userID == ID) {
				list = list + Integer.toString(tempLink.player.userID) + " ";
				list = list + tempLink.player.cAddress + " ";
			}
			tempLink = tempLink.nextLink;
		}
		return list;
	}

}
