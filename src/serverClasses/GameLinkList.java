package serverClasses;

public class GameLinkList {
	private GameNode first;
	protected int nextType = 0;

	// LinkList constructor
	public GameLinkList() {
		first = null;
	}

	// Inserts a new Link at the first of the list
	public void insert() {
		GameNode link = new GameNode();
		link.nextLink = first;
		first = link;
	}
     
	public void add(Player p) {
		if(first.p1 == null){
			
			first.p1 = p;
			
		}else if(first.p2 == null){
			
			first.p2 = p;
		}else{
			
			System.out.println("Both are set, noob!");
			
		}
		
	}

	public void setPlay() {
	    first.playing = "playing";
	
		
	}

	
	
	
	
	// Deletes the link at the first of the list
	public String delete(int deleteID) {
		GameNode tempLink = first;
        String statusMessage = "There are no users to remove";
		
		if (tempLink == null) {
			statusMessage = "There is nothing in the dictionary.";
		} else if (tempLink.p1.userID == deleteID) {
			first = tempLink.nextLink;
			statusMessage = Integer.toString(tempLink.p1.userID) + " has been deleted from the dictionary.";
		} else {
			while (tempLink.nextLink != null) {
				if (tempLink.nextLink.p1.userID == deleteID) {
					statusMessage = Integer.toString(tempLink.nextLink.p1.userID) + " has been deleted from the dictionary.";
					break;
				}
				tempLink = tempLink.nextLink;
			}
		}
		return statusMessage;
	}

	// Find the word from the link list
	public GameNode search(){
		// int key = -1;
		
		GameNode tempLink = first;

		while (tempLink != null) {
			if (tempLink.playing == "waiting" ) {
				
				break;
			}
			tempLink = tempLink.nextLink;
		}
		
		return tempLink;
	}

	
	
	public GameNode searchID(int ID){
		// int key = -1;
		
		GameNode tempLink = first;

		while (tempLink != null) {
			if ((tempLink.p1.userID == ID) || (tempLink.p2.userID == ID) ) {
				
				break;
			}
			tempLink = tempLink.nextLink;
		}
		return tempLink;
	}

	
	
	

	
	// Construct display list
 public String makeList(int ID) {
	String list = "List : ";
		GameNode tempLink = first;

	while (tempLink != null) {
		if (tempLink.p1.userID == ID) {
				list = list + Integer.toString(tempLink.p1.userID) + " ";
				
			}
		tempLink = tempLink.nextLink;
	}
		return list;
	}
	
	
}
