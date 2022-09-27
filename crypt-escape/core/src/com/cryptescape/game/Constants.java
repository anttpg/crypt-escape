package com.cryptescape.game;

import com.badlogic.gdx.Gdx;

public class Constants {
	
	
	//Constants
	//Aspect ratio should always be 4:3. Remember this is in 'Meters', not pixels	
	public static float FRAME_SPEED = 1/60f;
	
	public static final int Y_MAPSIZE = Menu.getSize()[0];
	public static final int X_MAPSIZE = Menu.getSize()[1];

			
	public static final float WIDTH = 12f;
	public static final float HEIGHT = 9f;
	
	public static final int X_TILES = 24;
	public static final int Y_TILES = 18;
	public static final float ROOM_Y = HEIGHT - (HEIGHT/3);
	public static final float ROOM_X = (int)((4/3.0) * ROOM_Y); 
	public static final float TILESIZE = ROOM_X/X_TILES;
	
	public static String[][] template = new String[X_TILES][Y_TILES];
	
	
	
	

}