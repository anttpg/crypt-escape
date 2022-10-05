package com.cryptescape.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Room {
	
	private boolean discovered;
	private int[] relativeLocation = new int[2]; // [Col, Row] on the map
	private float[] roomCorner = new float[2]; // room [y, x] real relative locations 
	private float[] roomTop = new float[2];
	
	private String roomType; 
	private boolean[] doors;
	
	//Seed is the string version of all the objects
	private String[][] seed;
	
	//iItems stores all the interactable objects
	private ArrayList<Interactable> iItems = new ArrayList<Interactable>();
	/**
	* Defines a Room object, where L is the relative [Y,X] position of the room on the map (IE: [2,1] for 2 rows down, 1 col over).
	* S is the String seed of what is within the room, RT is the room type, and d is the usable doors (IE: [T,T,F,T])
	* 
	*/
	public Room(int[] l, String[][] s, String rt) {	
		// Get relative x/y location and calculate real coords based on that.
		relativeLocation = l;
		roomCorner[0] = Constants.CAMERA_HEIGHT * (Constants.Y_MAPSIZE - l[0]) + Constants.Y_BUFFER;
		roomCorner[1] = Constants.CAMERA_WIDTH * (l[1]) + Constants.X_BUFFER;
		
		seed = s;
		roomType = rt;
		
		//roomTypes are {"open", "blocked", "bN3", "bS3", "bW1", "bE1", "bW3", "bE3"}
		if(getRoomType().equals("open")) doors = new boolean[] {true,true,true,true};
		else if(getRoomType().equals("blocked")) doors = new boolean[] {false,false,false,false};
		else if(getRoomType().equals("bN3")) doors = new boolean[] {false,false,true,false};
		else if(getRoomType().equals("bS3")) doors = new boolean[] {true,false,false,false};
		else if(getRoomType().equals("bW1")) doors = new boolean[] {true,true,true,false};
		else if(getRoomType().equals("bE1")) doors = new boolean[] {true,false,true,true};
		else if(getRoomType().equals("bW3")) doors = new boolean[] {false,true,false,false};
		else if(getRoomType().equals("bE3")) doors = new boolean[] {false,false,false,true};
		
		
		roomTop[0] = roomCorner[0] + (Constants.TILESIZE * xLen);
		roomTop[1] = roomCorner[1] + (Constants.TILESIZE * yLen);
		String current = new String();
		
		
		int counter = 0;
		for(int col = 0; col < Constants.Y_TILES; col++) {
			for(int row = 0; row < Constants.X_TILES; row++) { 
				current = seed[col][row];
				
				counter = 0;
				//Checking if the current item should be a static object
				for(String w : Constants.WALLTYPES) { //Of type wall
					if( current.equals(w) ) iItems.add(new Wall(col, row, current, this, counter));
					counter++;
				} 
				
				counter = 0;
				for(String d : Constants.DOORTYPES) { //Of type Door
					if( current.equals(d) ) iItems.add(new Door(col, row, current, this, counter));
					counter++;
				}
				
				if( current.equals("box") ) { //Of Type Box
					//iItems.add(new Door(col, row, "northDoor", this, 0));
				} 
				
				if ( current.equals("blocked") ) {
					//doSOmething later
				}
				
				if ( current.equals("empty") ) {
					//doNothing
				} 
				
			}
		}
	}
	
	public void discover() {
		discovered = true;
	}
	
	public void draw(SpriteBatch batch) {
		//Render background first, then
		batch.draw(GameScreen.BACKGROUND, roomCorner[1], roomCorner[0], Constants.CAMERA_WIDTH - Constants.X_BUFFER*2, Constants.CAMERA_HEIGHT - Constants.Y_BUFFER*2);
		for(Interactable i : iItems) {
			i.draw(batch);
		}
	}
	
	//Debug functions
	@SuppressWarnings("unused")
	public void debugItem(Fixture f, int col, int row) {
		System.out.println("Position of item at col  " + col + "  and row  " + row + "  : " + f.getBody().getPosition());
	}
	
	public void debugRoomPosition() {
		System.out.println("END OF Room col: " + relativeLocation[0] + "    and row: " + relativeLocation[1]);
		System.out.println("Y meters: " + roomCorner[0] + "   X meters: " + roomCorner[1] + "\n");
	}
	
	public void debugRoomSeed() {
		System.out.println(" \nStart of template: " + roomType);
		for(int yn = 0; yn < seed.length; yn++) {
			System.out.print("Col: " + yn);
			for(int xn = 0; xn < seed[yn].length; xn++) {
				System.out.print(" "+ seed[yn][xn]);
			}
			System.out.println("");
		}
	}


	public String getRoomType() {
		return roomType;
	}

	public String[][] getSeed() {
		return seed;
	}

	public float[] getRoomLocation() {
		return roomCorner;
	}
	
}
