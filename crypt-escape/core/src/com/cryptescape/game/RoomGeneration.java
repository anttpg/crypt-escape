package com.cryptescape.game;

import java.util.ArrayList;
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
		String[][] seed = createEmptyNxN(Constants.Y_TILES, Constants.X_TILES);
		
		double[] p2 = new double[] {75, 5, 2, 2, 2, 2, 6, 6}; // Probability of room type
		String[] key2 = new String[] {"open", "blocked", "bN3", "bE3", "bS3", "bW3", "bN1", "bE1", "bS1", "bW1", "hallNS", "hallEW", "cb"}; 
		// Type names, open means all 4 doors are usable [T,T,T,T]. Blocked is the opposite [F,F,F,F]
		// b stands for blocked, and then follows the direction and number of doors blocked
		// EX: bN3 means the northmost 3 doors of that room are blocked. (the east, west, and north doors [F,F,T,F]
		// hallNS is a north -> south hallway. East and west blocked
		
		ArrayList<String[][]> pregenTemplate = new ArrayList<String[][]>();
		RandomCollection<Integer> roomTypeGen = new RandomCollection<Integer>();
		for(int i = 0; i < p.length; i++) {
			roomTypeGen.add(p2[i], i);
		}
		
			
		pregenTemplate.add(GameScreen.clone2dArray(seed)); //open
		pregenTemplate.add(createNxN(seed, new boolean[][] {{true,true,true}, {true,true,true}, {true,true,true}})); //blocked
		repeat(createNxN(seed, new boolean[][] {{true,true,true}, {true,true,true}, {false,false,false}}), pregenTemplate); // 3 doors blocked
		repeat(createNxN(seed, new boolean[][] {{true,true,true}, {false,false,false}, {false,false,false}}), pregenTemplate); // 1 door blocked
		pregenTemplate.add(createNxN(seed, new boolean[][] {{true,false,true}, {true,false,true}, {true,false,true}})); //hall north -> south
		pregenTemplate.add(createNxN(seed, new boolean[][] {{true,true,true}, {false,false,false}, {true,true,true}})); //hall east -> west
		pregenTemplate.add(removeUselessWalls(addBlock3x3(seed, 1, 1))); // cb | Center blocked
		
		final ArrayList<String[][]> TEMPLATE = new ArrayList<String[][]>(Collections.unmodifiableList(pregenTemplate)); //makes it unmodable
		generateRooms(TEMPLATE, key2, roomTypeGen, roomItemGen);

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
				String[][] seed = GameScreen.clone2dArray(TEMPLATE.get(index)); 
				
//				System.out.println(index); //TRYING TO DEBUG
//				for(String t : key2) System.out.println(t);  
//				printSeedArray(seed, roomType);			
				for(int y = 1; y < Constants.Y_TILES-1; y++) { //Loop through and fill the seed (Excluding boundaries)
					for(int x = 1; x < Constants.X_TILES-1; x++) {
						if(seed[y][x].equals("empty")) {
							seed[y][x] = roomItemGen.next();
						}
						
						if(x == (Constants.X_TILES/2) || x == (Constants.X_TILES/2)-1) { //Exceptions are, if in front of door
							if(y == 1 || y == Constants.Y_TILES-2) {
								seed[y][x] = "empty";
							}
						}
						
						if(y == (Constants.Y_TILES/2) || y == (Constants.Y_TILES/2)-1) { //Exceptions are, if in front of door
							if(x == 1 || x == Constants.X_TILES-2) {
								seed[y][x] = "empty";
							}
						}
						
					}
				}
				GameScreen.rooms.get(col).add(new Room(new int[] {col+1, row}, seed.clone(), roomType));
			}
		}
		
		GameScreen.player.changeRoom(GameScreen.rooms.get(3).get(0));
	}
	
	
	public static void repeat(String[][] s, ArrayList<String[][]> pregenTemplate) {
		pregenTemplate.add(GameScreen.clone2dArray(s));
		pregenTemplate.add(rotateArray(s, 1));
		pregenTemplate.add(rotateArray(s, 2));
		pregenTemplate.add(rotateArray(s, 3));
	}
	
	/**
	 * Combinds two arrays, so long as original + offset > toCombind.length for both axis.
	 */
	private static String[][] combindArray(String[][] original, String[][] toCombind, int colStart, int rowStart) {
		try {
			for(int col = colStart; col < toCombind.length + rowStart; col++) {
				for(int row = rowStart; row < toCombind[col].length + colStart; row++) {
					original[col][row] = new String(toCombind[col-colStart][row-rowStart]);
				}
			}
		} 
		catch(Exception ArrayIndexOutOfBoundsException) {
			System.out.println("Error: the size of Original + Offset vs toCombind is out of bounds. \n Original:" 
		+ original.length + "   toCombind: " + toCombind.length + "     col: " + colStart + "   row: "+ rowStart);			
		}
		return original;
	}
	
	/**
	 * @param map; an 2d NxN map of boolean values. True means that area will be blocked
	 * @param original: the seed to apply it to.
	 */
	private static String[][] createNxN(String[][] original, boolean[][] map) {
		for(int col = 0; col < map.length; col++) {
			for(int row = 0; row < map[col].length; row++) {
				if(map[col][row]) {
					if(map.length == 3) {
						original = addBlock3x3(original, col, row);
					}
				}
			}
		}
		return removeUselessWalls(original);
	}
	
	/**
	 * Adds a new blocked area to the array, based on an 3x3 grid
	 * 0:0 is the top left corner, 2:2 the bottom right.
	 */
	private static String[][] addBlock3x3(String[][] original, int col, int row) {
		int height = (Constants.Y_TILES)/3;
		int width = (Constants.X_TILES)/3;
		String[][] combind = createEmptyNxN(height, width);
		
		int colStart = (col * Constants.Y_TILES)/3;
		int rowStart = (row * Constants.X_TILES)/3;
		return combindArray(original, combind, colStart, rowStart);
	}
	
	
	
	/**
	 * Creates an empty NxN room, COLxROW.
	 * Keep col & row > 5 to prevent weird bugs
	 */
	private static String[][] createEmptyNxN(int col, int row) {
		String[][] s = new String[col][row];
		for(int y = 0; y < col; y++) { //Loop through and fill the boundaries
			for(int x = 0; x < row; x++) {
				s[y][x] = "empty";
				
				//NORTH-SOUTH
				if(y == 0) { //SOUTH FACING
					if(x == (row/2) || x == (row/2)-1) // If doorway
						s[y][x] = "southDoor";
					else  // regular wall
						s[y][x] = "southWall";		
				}
				else if(y == col-1) { // NORTH FACING
					if(x == (row/2) || x == (row/2)-1) 
						s[y][x] = "northDoor";
					else  
 						s[y][x] = "northWall";
				}
				
				//EAST-WEST
				else if(x == 0) { //WEST FACING
					if(y == (col/2) || y == (col/2)-1) // If doorway
						s[y][x] = "westDoor";
					else  // regular wall
						s[y][x] = "westWall";
					
				}
				else if(x == row-1) { //EAST FACING
					if(y == (col/2) || y == (col/2)-1) 
						s[y][x] = "eastDoor";
					else  
 						s[y][x] = "eastWall";
				}
				

				if((x == 0 || x == (row-1)) && (y == 0 || y == col-1)) { //Corners
					s[y][x] = "blocked";
				}
			}
		}
		return s;
	}
	
	/**
	 * 
	 * @param matrix
	 * @return matrix rotated 90 degrees clockwise
	 */
	
	private static String[][] rotateClockWise90(String[][] matrix) {
		 int sizeY = matrix.length;
		 int sizeX = matrix[1].length;
		 String[][] ret = GameScreen.clone2dArray(matrix);

		 for (int i = 1; i < sizeY-1; ++i) 
		  for (int j = 1; j < sizeX-1; ++j) 
		   ret[i][j] = matrix[sizeX - j - 1][i]; //***
		 
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
	
	/**
	 *  Removes the inaccessable walls from the array 
	 */
	
	/** Removes walls that cannot be seen or accessed for performance increases */
	private static String[][] removeUselessWalls(String[][] r) {
		for(int y = 0; y < r.length; y++) {
			for(int x = 0; x < r[y].length; x++) {
				if(x != 0 && !(x+1 > r[y].length)) {
					if(r[y][x-1].equals("blocked") && r[y][x+1].equals("blocked")) {
						r[y][x] = "blocked";
					}
				}
				if(y != 0 && !(y+1 > r.length)) {
					if(r[y-1][x].equals("blocked") && r[y+1][x].equals("blocked")) {
						r[y][x] = "blocked";
					}
				}
			}
		}
		return r;
	}
	

	
	private static void testMethods() {
		
	}
}
