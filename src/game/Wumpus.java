package game;

import java.util.*;
import entities.*;

public class Wumpus {
	// Maximum number of rooms on inner or outer circle (total number of rooms is twice this value)
	private static final int ROOMCOUNT = 10;
	// List of all the rooms
	private static Room[] roomList;
	// Starting room number for player
	private static int playerStartRoom;
	// Starting room number for wumpus
	private static int wumpusStartRoom;
	// Starting room number for bats
	private static int batStartRoom;
	
	/*
	 * Main method to execute the map generation and play the game.
	 */
	public static void main(String[] args) {
		generateMap();
		play();
	}
	
	/*
	 * Game Method: executes the game after the map has been generated
	 */
	public static void play(){
		Scanner user = new Scanner(System.in);
		int currentRoom = playerStartRoom;
		int[] NPCRooms = {wumpusStartRoom, batStartRoom};
		boolean exit = false;
		char displayMap;
		System.out.println("Would you like to display a comprehensive map? (Y|N)");
		do{
			displayMap = user.next().charAt(0);
		} while(displayMap!='Y' && displayMap!='y' && displayMap!='N' && displayMap!='n');
		while(!exit){
			if(displayMap == 'Y' || displayMap == 'y'){
				printMap();
			}
			int[] adjacentRooms = findAdjacentRooms(currentRoom);
			for(int i = 0; i < 3; i++){
				roomList[adjacentRooms[i]].printStatus();
			}
			String adjList = "";
			for(int i = 0; i < 3; i++){
				adjList += adjacentRooms[i];
				if(i != 2){
					adjList += ", ";
				}
			}
			System.out.println("You are currently in room " + currentRoom);
			System.out.println("You can move to the following rooms: " + adjList);
			System.out.println("Which room would you like to move to?");
			int move = user.nextInt();
			while(!inList(adjacentRooms, move)){
				System.out.println("Invalid Move: Please select one of the following rooms: "+ adjList);
				move = user.nextInt();
			}
			exit = checkForEnd(move);
			if(!exit){
				currentRoom = checkForBats(currentRoom, move);
				NPCRooms = moveNPCs(NPCRooms);
			}
		}
		printMap();
	}
	
	/*
	 * Non-playable characters (NPC) are moved after each player turn
	 */
	public static int[] moveNPCs(int[] NPCs){
		//System.out.println("WUMPUS");
		NPCs[0] = moveHelper(NPCs[0], "wumpus");
		//System.out.println("BATS");
		NPCs[1] = moveHelper(NPCs[1], "bats");
		return NPCs;
	}
	
	/*
	 * Moving the NPC based on movement where bats and wumpus cannot move into
	 * pre-occupied so a wumpus cannot walk into a player or bats cannot fly into 
	 * a player. The direction for each movable NPC is randomly determined and then 
	 * checked if the next room is available.
	 */
	public static int moveHelper(int location, String NPCName){
		int[] possibleRooms= findAdjacentRooms(location);
		ArrayList<Integer> DirectionalIndices = new ArrayList<Integer>();
		DirectionalIndices.add(0);
		DirectionalIndices.add(1);
		DirectionalIndices.add(2);
		int direction = (int) (Math.random() * DirectionalIndices.size());
		boolean moved = false;
		while(!moved){
			if(DirectionalIndices.isEmpty()){
				moved = true;
			}else if(!DirectionalIndices.isEmpty()){
				if(roomList[possibleRooms[DirectionalIndices.get(direction)]].isEmptyRoom()){
					moveEntity(location, possibleRooms[DirectionalIndices.get(direction)], "bats");
					location = possibleRooms[DirectionalIndices.get(direction)];
					moved = true;
				} else {
					//System.out.println("Room: " + possibleRooms[DirectionalIndices.get(direction)] + " is not empty");
					DirectionalIndices.remove(DirectionalIndices.get(direction));
					direction = (int) (Math.random() * DirectionalIndices.size());
				}
			}
		}
		return location;
	}
	
	/*
	 * Checking for end-game conditions (death or exit)
	 */
	public static boolean checkForEnd(int current){
		if(roomList[current].getRoomOccupant().equals("pitfall")){
			System.out.println("You seem to have been falling for quite a while now...You begin to wonder where the bottom is...");
			return true;
		} else if(roomList[current].getRoomOccupant().equals("wumpus")){
			System.out.println("NOMNOMNOMNOM... YOU'RE DEAD BY THE HUNGRY WUMPUS :(");
			return true;
		} else if(roomList[current].getRoomOccupant().equals("exit")){
			System.out.println("Yay! You have successfully escaped the wumpus cave.");
			return true;
		}
		return false;
	}
	
	/*
	 * Checking for bats that cause the player to return to the previous room
	 */
	public static int checkForBats(int current, int next){
		if(roomList[next].getRoomOccupant().equals("bats")){
			System.out.println("AHHH! You ran into bats and ran back to the room you came from.");
			return current;
		}
		moveEntity(current, next, "player");
		return next;
	}
	
	/*
	 * Moving the entity to the next room and emptying the previous one
	 */
	public static void moveEntity(int current, int next, String entity){
		roomList[next].setRoom(entity);
		roomList[current].setRoom("");
	}
	
	/*
	 * Checking if an integer exists in the int array
	 */
	public static boolean inList(int[] tempList, int key){
		for(int test: tempList){
			if(test == key){
				return true;
			}
		}
		return false;
	}
	
	/*
	 * Finding the adjacent room on the map according to which room is selected
	 */
	public static int[] findAdjacentRooms(int roomNumber){
		//System.out.println("\tCurrently looking at room: " + roomNumber);
		int[] adjacentRooms = new int[3];
		if(roomNumber < ROOMCOUNT){
			if(roomNumber == 0){
				adjacentRooms[0] = ROOMCOUNT-1;
			} else {
				adjacentRooms[0] = roomNumber - 1;
			}
			adjacentRooms[1] = roomNumber + 10;
			if(roomNumber == 9){
				adjacentRooms[2] = 0;
			} else {
				adjacentRooms[2] = roomNumber + 1;
			}
		} else {
			if(roomNumber == 10){
				adjacentRooms[0] = 2*ROOMCOUNT-1;
			} else {
				adjacentRooms[0] = roomNumber - 1;
			}
			adjacentRooms[1] = roomNumber - 10;
			if(roomNumber == 19){
				adjacentRooms[2] = ROOMCOUNT;
			} else {
				adjacentRooms[2] = roomNumber + 1;
			}
		}
		/*
		System.out.print("\t\tAdjacent rooms: ");
		for(int i = 0; i < 3; i++){
			System.out.print(adjacentRooms[i]);
			if(i != 2){
				System.out.print(", ");
			}
		}
		System.out.println();
		*/
		return adjacentRooms;
	}
	
	/*
	 * Printing a visual based map that allows the user to see the connections between the rooms
	 * as well as see which entity is in the room
	 */
	public static void printMap(){
		//System.out.println("Printing Visual Map...");
		int totalRoomCount = 2 * ROOMCOUNT;
		for(int i = 0; i < totalRoomCount; i++){
			roomList[i].printRoom();
			System.out.print("---");
			if(i == 9 || i == 19){
				System.out.println();
				if(i==9){
					for(int j = 0; j < ROOMCOUNT; j++){
						System.out.print("   |     ");
					}
					System.out.println();
				}
			}
		}
	}
	
	/*
	 * Generates the map according to the ROOMCOUNT constant defined at the top of the program
	 * Inner loop will exist from 0 <= roomNumber < ROOMCOUNT
	 * Outer loop will exist from ROOMCOUNT <= roomNumber < 2*ROOMCOUNT
	 */
	public static void generateMap(){
		//System.out.println("Generating Map...");
		int totalRoomCount = 2 * ROOMCOUNT;
		roomList = new Room[totalRoomCount];
		int pitfallRoom = (int) (Math.random() * 20);
		int exitRoom;
		do{
			batStartRoom = (int) (Math.random() * 20);
		} while(batStartRoom == pitfallRoom);
		do{
			wumpusStartRoom = (int) (Math.random() * 20);
		} while(batStartRoom == wumpusStartRoom || pitfallRoom == wumpusStartRoom);
		do{
			playerStartRoom = (int) (Math.random() * 20);
		} while(playerStartRoom == batStartRoom || playerStartRoom == wumpusStartRoom || playerStartRoom == pitfallRoom);
		do{
			exitRoom = (int) (Math.random() * 20);
		} while(exitRoom == playerStartRoom || exitRoom == batStartRoom || exitRoom == wumpusStartRoom || exitRoom == pitfallRoom);
		for(int i = 0; i < totalRoomCount; i++){
			//System.out.println("\tCreating Room " + (i+1) + "...");
			if(i == pitfallRoom){
				//System.out.println("\t\tCreating pitfall...");
				roomList[i] = new Room(i, "pitfall");
			} else if(i == batStartRoom){
				//System.out.println("\t\tAdding bats...");
				roomList[i] = new Room(i, "bats");
			} else if(i == wumpusStartRoom){
				//System.out.println("\t\tAdding wumpus...");
				roomList[i] = new Room(i, "wumpus");
			} else if(i == playerStartRoom){
				roomList[i] = new Room(i, "player");
			} else if(i == exitRoom){
				roomList[i] = new Room(i, "exit");
			} else {
				roomList[i] = new Room(i, "");
			}
		}
	}
}
