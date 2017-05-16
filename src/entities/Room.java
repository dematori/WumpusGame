package entities;

/*
 * Room Class: This class handles the Room Object
 * This object has two values:
 * 		roomNumber: the room number that corresponds with this room
 * 		occupant: the occupant of the room
 */
public class Room {
	
	private int roomNumber;
	private String occupant;
	
	/*
	 * Default constructor that creates an empty room with no room number
	 */
	public Room(){
		this.occupant = "";
	}
	
	/*
	 * Overloaded constructor that creates a room with a room number and an occupant
	 */
	public Room (int roomNum, String occupant) {
		this.roomNumber = roomNum;
		this.occupant = occupant;
	}
	
	/*
	 * Get Method: retrieves the occupant of the room
	 * Returns a string of the occupant name.
	 */
	public String getRoomOccupant() {
		return this.occupant;
	}
	
	/*
	 * Set Method: changes the occupant of the room
	 */
	public void setRoom(String newOccupant){
		this.occupant = newOccupant;
	}
	
	/*
	 * Checks if the room is empty by seeing if there is an occupant in the room.
	 */
	public boolean isEmptyRoom(){
		if(!this.occupant.isEmpty()){
			return false;
		}
		return true;
	}
	
	/*
	 * Prints the warning message for the user if there is a hazard/exit in the room
	 */
	public void printStatus(){
		if(this.occupant.equals("pitfall")){
			System.out.println("You hear an echo...Watch your footing!");
		} else if (this.occupant.equals("wumpus")){
			System.out.println("You can smell it's breath...Better run!");
		} else if (this.occupant.equals("exit")){
			System.out.println("You can feel a breeze...You're almost there!");
		}
	}
	
	/*
	 * Prints the current occupant in the room for the map generation
	 * Unique format: [##|s] -> ## and s represent the room number and occupant symbol respectively
	 */
	public void printRoom(){
		String symbol;
		if(this.occupant.equals("pitfall")){
			symbol = "F";
		} else if (this.occupant.equals("bats")){
			symbol = "B";
		} else if (this.occupant.equals("wumpus")){
			symbol = "W";
		} else if (this.occupant.equals("player")){
			symbol = "P";
		} else if (this.occupant.equals("exit")){
			symbol = "E";
		} else {
			symbol = " ";
		}
		System.out.printf("[%2d|%s]", this.roomNumber, symbol);
	}
}
