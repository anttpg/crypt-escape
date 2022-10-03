package com.cryptescape.game;

import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.graphics.Texture;
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
	private float[] roomLocation = new float[2]; // room [x, y] real relative locations
	
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
		roomLocation[0] = Constants.HEIGHT * (Constants.Y_MAPSIZE - l[0]);
		roomLocation[1] = Constants.WIDTH * (l[1]);	
		
		setSeed(s);
		setRoomType(rt);
		
		//roomTypes are {"open", "blocked", "bN3", "bS3", "bW1", "bE1", "bW3", "bE3"}
		if(getRoomType().equals("open")) doors = new boolean[] {true,true,true,true};
		else if(getRoomType().equals("blocked")) doors = new boolean[] {false,false,false,false};
		else if(getRoomType().equals("bN3")) doors = new boolean[] {false,false,true,false};
		else if(getRoomType().equals("bS3")) doors = new boolean[] {true,false,false,false};
		else if(getRoomType().equals("bW1")) doors = new boolean[] {true,true,true,false};
		else if(getRoomType().equals("bE1")) doors = new boolean[] {true,false,true,true};
		else if(getRoomType().equals("bW3")) doors = new boolean[] {false,true,false,false};
		else if(getRoomType().equals("bE3")) doors = new boolean[] {false,false,false,true};
		
		String current = new String();
		for(int col = 0; col < Constants.Y_MAPSIZE; col++) {
			for(int row = 0; row < Constants.X_MAPSIZE; row++) { 
				current = getSeed()[col][row];
				
				//Checking if the current item should be a static object
				for(String w : Constants.WALLTYPES) { //Of type wall
					if( current.equals(w) ) createStaticEdge(col, row); 
				} 
				
				for(String d : Constants.DOORTYPES) { //Of type Door
					if( current.equals(d) ) createStaticEdge(col, row);
				}
				
				if( current.equals("box") ) { //figure out how to work this.
					iItems.add(new Interactable(createStaticBox(col, row))); 
				} 
				
				if ( current.equals("blocked") ) {

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
	
	
	public void drawRoom() {
		return;
	}
	
	
	private Vector2 getItemLocation(int col, int row) {
		float colZ = 0f;
		float rowZ = 0f;
		
		if(col != 0 ) {
			colZ = (Constants.HEIGHT/col);
		}
		if(row != 0 ) {
			rowZ = (Constants.WIDTH/row);
		}
			return new Vector2(roomLocation[0] + rowZ, roomLocation[1] + colZ);
		
	}
	
	
	private void createStaticEdge(int col, int row) {
		BodyDef bodyDef = new BodyDef();  
		bodyDef.position.set(getItemLocation(col,row)); //Set its position 
		Body bd = GameScreen.world.createBody(bodyDef);  
		
		EdgeShape edge = new EdgeShape(); //Walls/Doors dont need to be a full box
		
		// SETTING THE POINTS AS OFFSET DISTANCE FROM CENTER
		edge.set(-Constants.TILESIZE / 2f, 0, Constants.TILESIZE / 2f, 0);
		bd.createFixture(edge, 0.0f);
		edge.dispose();
	}
	
	
	private Fixture createStaticBox(int col, int row) {
		BodyDef bodyDef = new BodyDef();  
		bodyDef.position.set(getItemLocation(col,row)); //Set its position 
		Body bd = GameScreen.world.createBody(bodyDef);  
		
		PolygonShape box = new PolygonShape();  // Create a polygon shape 

		box.setAsBox(-Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
		Fixture f = bd.createFixture(box, 0.0f);
		box.dispose();
		return f;
	}

	public String getRoomType() {
		return roomType;
	}

	public void setRoomType(String roomType) {
		this.roomType = roomType;
	}

	public String[][] getSeed() {
		return seed;
	}

	public void setSeed(String[][] seed) {
		this.seed = seed;
	}
	
	
}
