package com.cryptescape.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RoomGeneration {
	
	
	public static void generateTemplates() {
		// ROOM GENERATION BELOW
		int[] p = new int[] {92, 4, 3, 1}; // Probability of a interactable type
		String[] key = new String[] {"empty", "box", "puddle", "bat"}; //The cooresponding type
		
		RandomCollection<String> roomItemGen = new RandomCollection<String>();
		for(int i = 0; i < p.length; i++) {
			roomItemGen.add(p[i], key[i]);
		}
		
		// use ##.next() to get where in key[i] to use
		String[][] seed = createEmptyNxN(Constants.Y_TILES, Constants.X_TILES, false, false);
		
		// Probability of room type
		double[] p2 = new double[] {
				100,10,1,  
				1,1,1,1,
				1,1,1,1,
				1,1,1,1,
				1,1,1,1,
				1,1,1,1,
				5,4,10,10,
				10,10,1,1}; 
		
		String[] key2 = new String[] {
				"open",  "cb", "blocked", 
				"aN3", "aE3", "aS3", "aW3", 
				"bN3", "bE3", "bS3", "bW3", 
				"aN1", "aE1", "aS1", "aW1", 
				"bN1", "bE1", "bS1", "bW1",
				"cN1", "cE1", "cS1", "cW1",
				"NE2", "SE2", "SW2", "NW2",
				"hallNS", "hallEW", "trapNS", "trapEW"}; 
		
		// Type names, open means all 4 doors are usable [T,T,T,T]. Blocked is the opposite [F,F,F,F]
		// a stands for a-series (for multiple  skins), and then follows the direction and number of doors blocked
		// EX: bN3 means the northmost 3 doors of that room are blocked. (the east, west, and north doors [F,F,T,F]), skin2
		// hallNS is a north -> south hallway. East and west blocked
		
		ArrayList<String[][]> pregenTemplate = new ArrayList<String[][]>();
		RandomCollection<Integer> roomTypeGen = new RandomCollection<Integer>();
		for(int i = 0; i < key2.length; i++) {
			roomTypeGen.add(p2[i], i);
		}
		
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
		generateRooms(TEMPLATE, key2, roomTypeGen, roomItemGen);
		
		for(int col = 0; col < GameScreen.rooms.size(); col++) {
			for(int row = 0; row < GameScreen.rooms.get(col).size(); row++) {
				GameScreen.rooms.get(col).get(row).determinePartners();
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
	
	
	public static void generateRooms(ArrayList<String[][]> TEMPLATE, String[] key2,
			RandomCollection<Integer> roomTypeGen, RandomCollection<String> roomItemGen) {
		//Called once all the templates are generated, create and fill all the rooms 
		
		String item;
		String roomType;
		int index;
		for(int col = 0; col < Constants.NUM_OF_ROOMS_Y; col++) {
			GameScreen.rooms.add(new ArrayList<Room>());  //instantiate all columns in the 2d array
			
			for(int row = 0; row < Constants.NUM_OF_ROOMS_X; row++) {
				//For each room in an NxN grid, that will make up the playfield...
				//DETERMINE: Room type, and what its filled with.
				index = roomTypeGen.next();
				roomType = key2[index];
				String[][] seed = clone2dArray(TEMPLATE.get(index)); 
				
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
				GameScreen.rooms.get(col).add(new Room(new int[] {col+1, row}, seed.clone(), roomType));
				
			}
		}
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
				if(row == 0 || row == Constants.X_TILES || col == 0 || col == Constants.Y_TILES) {
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
