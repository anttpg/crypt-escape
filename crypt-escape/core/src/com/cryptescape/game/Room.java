package com.cryptescape.game;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Room {
	
	private boolean discovered;
	private int[] roomLocation = new int[2]; // [roomX,roomY] on the map
	private String[][] seed; 
	private String roomType; 
	private ArrayList<Interactable> interactables = new ArrayList<Interactable>();
	
	public Room(int[] l, String[][] s, String rt, Room base) {
		roomLocation = l;
		seed = s;
		roomType = rt;
		
		for(int row = 0; row < seed.length; row++) {
			for(int col = 0; col < seed[row].length; row++) {
				boolean i = false; 
				
				if( seed[row][col].equals("wall") ) {
					i = true;
				} else if( seed[row][col].equals("door") ) {
					i = true;
				} else if( seed[row][col].equals("box") ) {
					i = true;
				} 
				
				if(i) {
					// Create our body definition
					BodyDef groundBodyDef = new BodyDef();  
					// Set its world position
					groundBodyDef.position.set(new Vector2(0, 10));  
					
					// Create a body from the definition and add it to the world
					Body groundBody = GameScreen.world.createBody(groundBodyDef);  

					// Create a polygon shape
					PolygonShape groundBox = new PolygonShape();  
					// Set the polygon shape as a box which is twice the size of our view port and 20 high
					// (setAsBox takes half-width and half-height as arguments)
					groundBox.setAsBox(GameScreen.camera.viewportWidth, 10.0f);
					// Create a fixture from our polygon shape and add it to our ground body  
					groundBody.createFixture(groundBox, 0.0f);
					// Clean up after ourselves
					groundBox.dispose();
				}
				
				
				else {
					if( seed[row][col].equals("empty") ) {
						
					} else if( seed[row][col].equals("blocked")) {
						
					}
				}
			}
		}
		
		int i = Constants.X_TILES;
		discovered = false;
		
	}
	
	public void discover() {
		discovered = true;
	}
	
	
	
	public void drawRoom() {
		return;
	}
	
	
}
