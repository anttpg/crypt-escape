package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Room {
	
	public boolean discovered;
	public boolean[] doors; //sets which doors are useable
	public int[] roomLocation = new int[2]; // [roomX,roomY] on the map
	public String[][] seed; 
	public String roomType; 
	
	public Texture spriteMap;
	public TextureRegion spriteRegion;
	
	public Room(int[] r, boolean[] d, String[][] s, String rt) {
		roomLocation = r;
		doors = d;
		seed = s;
		roomType = rt;
		
		
		discovered = false;
		
	}
	
	public void discover() {
		discovered = true;
	}
	
	public void drawRoom() {
		return;
	}
	
	
}
