package com.cryptescape.game.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.cryptescape.game.Constants;
import com.cryptescape.game.Filters;
import com.cryptescape.game.GameScreen;

public class RoomGeneration {
	
	public static Room startingRoom;
	private static int startX;
	private static int startY;
	
	private static ArrayList<double[]> doorOptions;
	private static ArrayList<boolean[][]> doorEquivilence;
	private static double[] roomProbs;
	private static RandomCollection<Integer> roomDoorNumGenerator;
	private static String[][] generalTypes;
	private static String[][][] skinTypes;
	private static double[][][] skinProbability;
	private static int stoppingPoint = 0;
	
	public static void generateTemplates() {
	    Filters.setupFilters(); // Defines filter values
	    
		// ROOM GENERATION BELOW
		int[] p = new int[] {92, 3, 3, 2}; // Probability of a interactable type
		String[] key = new String[] {"empty", "box", "boxUnlocked", "puddle"}; //The cooresponding type
		
		RandomCollection<String> roomItemGen = new RandomCollection<String>();
		for(int i = 0; i < p.length; i++) {
			roomItemGen.add(p[i], key[i]);
		}
		
		// use ##.next() to get where in key[i] to use
		String[][] seed = createEmptyNxN(Constants.Y_TILES, Constants.X_TILES, false, false);
		
		
		
		String[] keyBefore = new String[] {
				"open",  "openCB", "blocked", 
				"aN3", "aE3", "aS3", "aW3", 
				"bN3", "bE3", "bS3", "bW3", 
				"aN1", "aE1", "aS1", "aW1", 
				"bN1", "bE1", "bS1", "bW1",
				"cN1", "cE1", "cS1", "cW1",
				"NE2", "SE2", "SW2", "NW2",
				"hallNS", "hallEW", "trapNS", "trapEW"}; 
		

		ArrayList<String> key2 = new ArrayList<String>();
		for(String s : keyBefore) 
			key2.add(s);
		
		
		
		// Type names, open means all 4 doors are usable [T,T,T,T]. Blocked is the opposite [F,F,F,F]
		// a stands for a-series (for multiple  skins), and then follows the direction and number of doors blocked
		// EX: bN3 means the northmost 3 doors of that room are blocked. (the east, west, and north doors [F,F,T,F]), skin2
		// hallNS is a north -> south hallway. East and west blocked
		ArrayList<String[][]> pregenTemplate = new ArrayList<String[][]>();
		
		
		//FORMAT: 3x3 bool Array. True means blocked in that section.	
		pregenTemplate.add(clone2dArray(seed)); //open
		
		pregenTemplate.add(addBlock3x3(seed, 1, 1)); // cb | Center blocked
		
		pregenTemplate.add(createNxN(seed, new boolean[][] {
			{true,true,true}, 
			{true,true,true}, 
			{true,true,true}})); //blocked
		
		repeat(createNxN(seed, new boolean[][] {
			{true,true,true}, 
			{true,true,true}, 
			{false,false,false}}), pregenTemplate); // 3 doors blocked
		repeat(createNxN(seed, new boolean[][] {
			{true,true,true}, 
			{true,false,true}, 
			{true,false,true}}), pregenTemplate); // 3 doors blocked (alternate B)
		
		repeat(createNxN(seed, new boolean[][] {
			{true,true,true},
			{false,false,false},
			{false,false,false}}), pregenTemplate); // 1 door blocked
		repeat(createNxN(seed, new boolean[][] {
			{true,true,true},
			{false,false,false},
			{true,false,true}}), pregenTemplate); // 1 door blocked (alternate B)
		repeat(createNxN(seed, new boolean[][] {
			{true,true,true},
			{false,true,false},
			{false,false,false}}), pregenTemplate); // 1 door blocked (alternate C)
		
		repeat(createNxN(seed, new boolean[][] {
			{true,false,true},
			{true,false,false},
			{true,true,true}}), pregenTemplate); // 2 doors blocked (L TURN)
		
		
		pregenTemplate.add(createNxN(seed, new boolean[][] {
			{true,false,true},
			{true,false,true},
			{true,false,true}})); //hall north -> south
		
		pregenTemplate.add(createNxN(seed, new boolean[][] {
			{true,true,true},
			{false,false,false},
			{true,true,true}})); //hall east -> west
		
		pregenTemplate.add(createNxN(seed, new boolean[][] {
			{true,false,true},
			{true,true,true},
			{true,false,true}})); //trap north-south
		
		pregenTemplate.add(createNxN(seed, new boolean[][] {
			{true,true,true},
			{false,true,false},
			{true,true,true}})); //trap east-west
		
		
		
		for(int i = 0; i < pregenTemplate.size(); i++)
			pregenTemplate.set(i, formatWalls(pregenTemplate.get(i)));
		final ArrayList<String[][]> TEMPLATE = new ArrayList<String[][]>(Collections.unmodifiableList(pregenTemplate)); //makes it unmodable
		
		
		
        Random r = new Random();
        startX = r.nextInt(Constants.NUM_OF_ROOMS_X - 2) + 1;  //Finds the players starting room
        startY = r.nextInt(Constants.NUM_OF_ROOMS_Y - 2) + 1;
        
		generateRooms(TEMPLATE, key2, roomItemGen); //Fills all rooms
		
        GameScreen.player.setStartingRoom(GameScreen.rooms.get(startY).get(startX)); //Sets player starting room
        GameScreen.player.setPos(
        		GameScreen.rooms.get(startY).get(startX).getRoomLocation()[1] + Constants.CAMERA_WIDTH / 2.99f, 
        		GameScreen.rooms.get(startY).get(startX).getRoomLocation()[0] + Constants.CAMERA_HEIGHT / 1.96f);
		
        
        
		for(int col = 0; col < GameScreen.rooms.size(); col++) {
			for(int row = 0; row < GameScreen.rooms.get(col).size(); row++) {
				GameScreen.rooms.get(col).get(row).determinePartners(); //Finds out which rooms are connected
			}
		}
	}
	
	
	
	
	public static void debugTemplates(ArrayList<String[][]> pregenTemplate) {
		for(int i = 0; i < pregenTemplate.size(); i++) {
			System.out.println("BEFORE REMOVING WALLS: ");
			printSeedArray(pregenTemplate.get(i), Integer.toString(i));
			
			System.out.println("AFTER REMOVING WALLS: ");
			printSeedArray(pregenTemplate.get(i), Integer.toString(i) + ": ");
		}
	}
	
	
	
	private static void setupDoorProbabilities() {
		roomProbs = new double[] {10, 12, 30, 43, 28}; //0 doors, 1 door, 2 doors, 3 doors, 4 doors.
		
		doorOptions = new ArrayList<double[]>();
		doorOptions.add(new double[] {100} ); //Only one way
		doorOptions.add(new double[] {25,25,25,25} );  //Which doors will be blocked
		doorOptions.add(new double[] {10,10,10,10,10,10} );  //Hall, corners
		doorOptions.add(new double[] {25,25,25,25} );  //T junction, hall Ts
		doorOptions.add(new double[] {100} );  //Only one way
		
		doorEquivilence = new ArrayList<boolean[][]>();
		doorEquivilence.add(new boolean[][] {{false, false, false, false}}); //blockec
		doorEquivilence.add(new boolean[][] {{false, false, true, false}, {false, false, false, true}, {true, false, false, false}, {false, true, false, false}}); //one door
		doorEquivilence.add(new boolean[][] {{true, true, false, false}, {false, true, true, false}, {false, false, true, true}, {true, false, false, true}, {true, false, true, false}, {false, true, false, true} }); //two doors
		doorEquivilence.add(new boolean[][] {{false, true, true, true}, {true, false, true, true}, {true, true, false, true}, {true, true, true, false}}); //three doors open
		doorEquivilence.add(new boolean[][] {{true, true, true, true}}); //all open
		
		
		roomDoorNumGenerator = new RandomCollection<Integer>();
		for(int i = 0; i < roomProbs.length; i++) {
			roomDoorNumGenerator.add(roomProbs[i], i);
		}
		
		generalTypes = new String[][] {
			{ "blocked" },
			{ "N3", "E3", "S3", "W3" },
			{ "NE2", "SE2", "SW2", "NW2", "NS2", "EW2" } ,
			{ "N1", "E1", "S1", "W1" }, // 3 doors open
			{ "open" } }; 
			
		skinTypes = new String[][][] {
			{ {"blocked"} }, 
			{ {"aN3", "bN3"}, {"aE3", "bE3"}, {"aS3", "bS3"}, {"aW3","bW3"} }, //1 door open
			{ {"NE2"}, {"SE2"}, {"SW2"}, {"NW2"}, {"hallNS", "trapNS"}, {"hallEW", "trapEW"} },
			{ {"aN1", "bN1", "cN1"}, {"aE1", "bE1", "cE1"}, {"aS1", "bS1", "cS1"}, {"aW1", "bW1", "cW1"} },
			{ {"open",  "openCB"} } };
			
		// Probability of room type
		skinProbability = new double[][][] {
			{ {100} }, 
			{ {40, 60}, {40, 60}, {40, 60}, {40, 60} }, 
			{ {100}, {100}, {100}, {100}, {75, 25}, {75, 25} },
			{ {20, 60, 20}, {20, 60, 20}, {20, 60, 20}, {20, 60, 20} },
			{ {40, 60} } };	
			
	}
	
	
	
	public static String determineRoomType(int col, int row) {
		stoppingPoint++;
		
		String[] neighborDoors = new String[] {null, null, null, null};

		if(col == 0)
			neighborDoors[0] = "false";
		else if(GameScreen.rooms.get(col-1).get(row).getDoors().get(2) != null)
			neighborDoors[0] = "true";
			
		if(row == 0)
			neighborDoors[3] = "false";
		else if(GameScreen.rooms.get(col).get(row-1).getDoors().get(1) != null)
			neighborDoors[3] = "true";
		
		if(row == Constants.NUM_OF_ROOMS_X - 1)
			neighborDoors[1] = "false";
		
		if(col == Constants.NUM_OF_ROOMS_Y - 1)
			neighborDoors[2] = "false";
		
		
		
		int roomNumDoors = roomDoorNumGenerator.next();
		RandomCollection<Integer> doorGen = new RandomCollection<Integer>();
		for(int i = 0; i < doorOptions.get(roomNumDoors).length; i++) {
			doorGen.add(doorOptions.get(roomNumDoors)[i], i);
		}
		
		String best = "WuckyErrorThisShouldntBePossible";
		float bestScore = 0f;
		int totalTries = 0; //Just brute force guesses for a bit, otherwise give up and use the best solution.
		float numCorrect = 0f;
		int finalIndex = 0;
		int trueConnected = 0;
		
		
		while(totalTries < 3) {
			numCorrect = 0;
			trueConnected = 0;
			
			//Generate current guess
			int index = doorGen.next();
			boolean[] guess = doorEquivilence.get(roomNumDoors)[index];

			
			for(int i = 0; i < 4; i++) {
				if((neighborDoors[i] != null && (Boolean.valueOf(neighborDoors[i]) && guess[i]))) {
					numCorrect += 1;
					trueConnected++;
				}
				
				if((neighborDoors[i] != null && (Boolean.valueOf(neighborDoors[i]) == guess[i]))) 
					numCorrect += 0.9f;
					
				if(neighborDoors[i] == null && (guess[i]))
					numCorrect += 0.75;	
			}
			
			if(numCorrect >= bestScore) {
				finalIndex = index;
				bestScore = numCorrect;
				best = generalTypes[roomNumDoors][index];
			}
			totalTries++;
		}
		
		//If nowhere connected, try once recursivly.
		if(trueConnected == 0 && stoppingPoint < 2)
			return determineRoomType(col, row);
		
		
		//Now determine the skin for that room
		RandomCollection<String> roomSkinGenerator = new RandomCollection<String>();
		for(int skinprob = 0; skinprob < skinProbability[roomNumDoors][finalIndex].length; skinprob++) {
			roomSkinGenerator.add(skinProbability[roomNumDoors][finalIndex][skinprob], skinTypes[roomNumDoors][finalIndex][skinprob]);
		}	
		
		stoppingPoint = 0;
		return roomSkinGenerator.next();
	}
	
	
	
	
	public static void generateRooms(ArrayList<String[][]> TEMPLATE, ArrayList<String> key2, RandomCollection<String> roomItemGen) {
		//Called once all the templates are generated, create and fill all the rooms 
		setupDoorProbabilities();
		
		String item;
		String roomType;
		int index;
		for(int col = 0; col < Constants.NUM_OF_ROOMS_Y; col++) {
			GameScreen.rooms.add(new ArrayList<Room>());  //instantiate all columns in the 2d array
			
			for(int row = 0; row < Constants.NUM_OF_ROOMS_X; row++) {
				//For each room in an NxN grid, that will make up the playfield...
				//DETERMINE: Room type, and what its filled with.
				roomType = determineRoomType(col, row);
				String[][] seed;
				
				if(col == startY && row == startX) {
					seed = clone2dArray(TEMPLATE.get(0)); 
				}
				
				else {
					seed = clone2dArray(TEMPLATE.get(key2.indexOf(roomType))); 
				}
				
				
				
				
//				System.out.println(index); //TRYING TO DEBUG
//				for(String t : key2) System.out.println(t);  
//				printSeedArray(seed, roomType);			
				for(int y = 1; y < Constants.Y_TILES-1; y++) { //Loop through and fill the seed (Excluding boundaries)
					for(int x = 1; x < Constants.X_TILES-1; x++) {
						if(seed[y][x].equals("empty")) {
							seed[y][x] = roomItemGen.next();
						}
						
						if(!seed[y][x].equals("blocked")) { //Exceptions are, if in front of door
							if(x == (Constants.X_TILES/2) || x == (Constants.X_TILES/2)-1) { //North-South
								if(y == 1 || y == Constants.Y_TILES-2) {
									seed[y][x] = "empty";
								}
							}
							
							if(y == (Constants.Y_TILES/2) || y == (Constants.Y_TILES/2)-1) { //East-West
								if(x == 1 || x == Constants.X_TILES-2) {
									seed[y][x] = "empty";
								}
							}
						}			
					}
				}
				
                if(col == startY && row == startX) {
                    //seed = clone2dArray(TEMPLATE.get(0)); 
                    addItemsToStartingRoom(seed);
                }
				
				
				GameScreen.rooms.get(col).add(new Room(new int[] {col+1, row}, seed.clone(), roomType));
				
			}
		}
	}
	
	
	private static void addItemsToStartingRoom(String[][] seed) {
	    //Y, X starting from 0, 0
	    seed[7][10] = "boxUnlocked";
        seed[9][12] = "haystack";
        seed[9][14] = "table";
        seed[9][15] = "empty";
        
    }




    public static void repeat(String[][] s, ArrayList<String[][]> pregenTemplate) {
		pregenTemplate.add(clone2dArray(s));
		pregenTemplate.add(rotateArray(s, 1));
		pregenTemplate.add(rotateArray(s, 2));
		pregenTemplate.add(rotateArray(s, 3));
	}
	
	/**
	 * Combinds two arrays, so long as original + offset > toCombind.length for both axis.
	 */
	private static String[][] combindArray(String[][] original, String[][] toCombind, int colStart, int rowStart) {
		int colCount = 0;
		int rowCount = 0;
		try {
			for(int col = colStart; col < toCombind.length + colStart; col++) {
				colCount = col;
				for(int row = rowStart; row < toCombind[col-colStart].length + rowStart; row++) {
					rowCount = row;
					original[col][row] = new String(toCombind[col-colStart][row-rowStart]);
				}
			}
		}
		catch(ArrayIndexOutOfBoundsException e) {
			System.out.println("Error: the size of Original + Offset vs toCombind is out of bounds. \n Original: " 
		+ original.length + "   toCombind: " + toCombind.length + "     col: " + colStart + "   row: "+ rowStart);			
		}
		return original;
	}
	
	/**
	 * map; an 2d NxN map of boolean values. True means that area will be blocked
	 * original: the seed to apply it to.
	 */
	private static String[][] createNxN(String[][] original, boolean[][] map) {
		original = createEmptyNxN(original.length, original[0].length, false, false);
		
		for(int col = 0; col < map.length; col++) { //Block additions
			for(int row = 0; row < map[col].length; row++) {
				if(map[col][row]) {
					if(map.length == 3) {
						original = addBlock3x3(original, col, row);
					}
					else {
						//SomethingElse
					}
				}
			}
		}
		
		String[][] temp = createEmptyNxN(original.length, original[0].length, false, false);
		for(int col = 0; col < original.length; col++) { //Edges redone to override blocks
			for(int row = 0; row < original[col].length; row++) {
				if(row == 0 || row == Constants.X_TILES-1 || col == 0 || col == Constants.Y_TILES-1) {
					original[col][row] = temp[col][row];
				}
			}
		}
		
		
		return original;
	}
	
	/**
	 * Adds a new blocked area to the array, based on an 3x3 grid
	 * 0:0 is the top left corner, 2:2 the bottom right.
	 */
	private static String[][] addBlock3x3(String[][] original, int col, int row) {
		int height = (Constants.Y_TILES)/3;
		int width = (Constants.X_TILES)/3;
		String[][] combind = createEmptyNxN(height, width, true, true);
		
		int colStart = (col * Constants.Y_TILES)/3;
		int rowStart = (row * Constants.X_TILES)/3;
		return combindArray(original, combind, colStart, rowStart);
	}
	
	
	
	/**
	 * Creates an empty NxN room, COLxROW.
	 * Keep col & row > 5 to prevent weird bugs
	 * If block is TRUE, all squares inside will be blocked. Otherwise they will be empty.
	 */
	private static String[][] createEmptyNxN(int col, int row, boolean block, boolean isGrid) {
		String[][] s = new String[col][row];
		for(int y = 0; y < col; y++) { //Loop through and fill the boundaries
			for(int x = 0; x < row; x++) {
				
				if(block) s[y][x] = "blocked";
				else s[y][x] = "empty";
				
				//NORTH-SOUTH
				if(y == 0) { //SOUTH FACING
					if((x == (row/2) || x == (row/2)-1) && !isGrid) // If doorway
						s[y][x] = "northDoor";
					else  // regular wall
						s[y][x] = "northWall";		
				}
				else if(y == col-1) { // NORTH FACING
					if((x == (row/2) || x == (row/2)-1) && !isGrid)  //isGrid just checks if the square is empty. If so, no doors are generated.
						s[y][x] = "southDoor";
					else  
 						s[y][x] = "southWall";
				}
				
				//EAST-WEST
				else if(x == 0) { //WEST FACING
					if((y == (col/2) || y == (col/2)-1) && !isGrid) // If doorway
						s[y][x] = "westDoor";
					else  // regular wall
						s[y][x] = "westWall";
					
				}
				else if(x == row-1) { //EAST FACING
					if((y == (col/2) || y == (col/2)-1) && !isGrid) 
						s[y][x] = "eastDoor";
					else  
 						s[y][x] = "eastWall";
				}
				

				if((x == 0) && (y == 0 || y == col-1) && !isGrid) { //Corners (Minus blocked segments)
					s[y][x] = "westWall";
				}
				if((x == (row-1)) && (y == 0 || y == col-1) && !isGrid) { //Corners (Minus blocked segments)
					s[y][x] = "eastWall";
				}
				
			}
		}
		return s;
	}
	
	/**
	 * 
	 * matrix
	 * matrix rotated 90 degrees clockwise
	 */
	
	private static String[][] rotateClockWise90(String[][] matrix) {
		int sizeY = matrix.length;
		int sizeX = matrix[1].length;
		boolean switchNext = false;
		String[][] ret = clone2dArray(matrix);

		for (int i = 1; i < sizeY - 1; ++i) {
			for (int j = 1; j < sizeX - 1; ++j) {
				ret[i][j] = matrix[sizeX - j - 1][i]; //Rotates array minus edges
				
				//Just checks to see if wall side need to switch TODO
//				for(String w : Constants.WALLTYPES) {
//					if(switchNext) { 
//						switchNext = false;
//						ret[i][j] = w;
//					}
//					if(ret[i][j].equals(w)) switchNext = true;
//				}
			}
		}
		return ret;
	}
	
	
	
	/**
	 * Rotates an array clockwise n times. If n is negative, rotates
	 * counterclockwise. Only tested for -3= < n <= 3
	 */
	private static String[][] rotateArray(String[][] matrix, int times){
		if(times < 0) times += 4;
		for(int i = 0; i < times; i++) {
			matrix = rotateClockWise90(matrix);
		}
		return matrix;	
	}
	
	
	/** Removes walls that cannot be seen or accessed for performance, and fixes wall orientation*/
	private static String[][] formatWalls(String[][] r) {
		boolean flip = true;
		boolean isWall = false;
		for(int y = 0; y < r.length; y++) {
			for(int x = 0; x < r[y].length; x++) {
				
				if(x != 0 && (x+1 < r[y].length)) { //Prevents out of bounds x
					if(inaccessable(r[y][x-1]) && inaccessable(r[y][x+1])) {  //Checks x sides
						
						if(y != 0 && (y+1 < r.length)) { //Prevents out of bounds y
							if(inaccessable(r[y-1][x]) && inaccessable(r[y+1][x])) {  //Checks y sides
								r[y][x] = "blocked";
							}
						}
					}
				}
				
				if (x != 0 && (x + 1 < r[y].length)) {
					if (y != 0 && (y + 1 < r.length)) {
						for (String w : Constants.WALLTYPES) {
							if (r[y][x].equals(w)) {
								isWall = true;
							}
							
							if (x == 0 && !inaccessable(r[y][x+1]))
								r[y][x] = "westWall"; // checks for x=0 edge cases
							if (x + 1 == r[y].length && !inaccessable(r[y][x - 1]))
								r[y][x] = "eastWall";

							if (y == 0 && !inaccessable(r[y+1][x]))
								r[y][x] = "southWall"; // checks for y=0 edge cases
							if (y + 1 == r.length && !inaccessable(r[y - 1][x]))
								r[y][x] = "northWall";

							// -1 INSTEAD OF +1 FOR N/S BECAUSE Y GOES DOWN.
							if (!inaccessable(r[y - 1][x]))
								if (!r[y][x].equals("southWall"))
									if (isWall)
										r[y][x] = "southWall";

							if (!inaccessable(r[y + 1][x]))
								if (!r[y][x].equals("northWall"))
									if (isWall)
										r[y][x] = "northWall";

							if (!inaccessable(r[y][x - 1]))
								if (!r[y][x].equals("eastWall"))
									if (isWall)
										r[y][x] = "eastWall";

							if (!inaccessable(r[y][x + 1]))
								if (!r[y][x].equals("westWall"))
									if (isWall)
										r[y][x] = "westWall";

							isWall = false;
						}
					}
				}
				
				if(x == 0 && inaccessable(r[y][x+1])) r[y][x] = "blocked";  //checks for x=0 edge cases
				if(x+1 == r[y].length && inaccessable(r[y][x-1])) r[y][x] = "blocked"; 
				//+1 to account for X_TILES being x+1 since x starts at 0
				
				if(y == 0 && inaccessable(r[y+1][x])) r[y][x] = "blocked";  //checks for y=0 edge cases
				if(y+1 == r.length && inaccessable(r[y-1][x])) r[y][x] = "blocked";
				
				
				
				for(String w : Constants.WALLTYPES) { //Wall direction edge cases
					if(r[y][x].equals(w)) {		
						if(x == 0 && !inaccessable(r[y][x+1])) r[y][x] = "westWall";  //checks for x=0 edge cases
						if(x+1 == r[y].length && !inaccessable(r[y][x-1])) r[y][x] = "eastWall";
								
						// -1 INSTEAD OF +1 FOR N/S BECAUSE Y GOES DOWN.
						if(y == 0 && !inaccessable(r[y+1][x])) r[y][x] = "northWall";  //checks for y=0 edge cases
						if(y+1 == r.length && !inaccessable(r[y-1][x])) r[y][x] = "southWall";
					}
				}
			}
		}
		return r;
	}
	
	private static boolean inaccessable(String side) {
		if(side.equals("blocked")){
			return true;
		}
		for(String d : Constants.DOORTYPES) {
			if(side.equals(d)) return true;
		}
		for(String w : Constants.WALLTYPES) {
			if(side.equals(w)) return true;
		}
		
		return false;
	}
	
	public static void printSeedArray(String[][] org, String rt) {
		System.out.println("Type of room should be: " + rt);
		int count = 10;
		for(String[] s : org) {
			System.out.print("Col " + count + ":   ");
			count++;
			for(String s2 : s) {
				for(String w : Constants.WALLTYPES)
					if(s2.equals(w))
						s2 = s2.toUpperCase();
				if(s2.length() > 5)
					System.out.print(s2.substring(0,5) + " ");
				else
					System.out.print(s2);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static String[][] clone2dArray(String[][] original) {
		return Arrays.stream(original).map(String[]::clone).toArray(String[][]::new);
	}
	
}
