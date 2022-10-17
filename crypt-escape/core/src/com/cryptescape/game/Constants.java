package com.cryptescape.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Constants {
	
	
	//Constants
	//Aspect ratio should always be 4:3. Remember this is in 'Meters', not pixels	
	public static float FRAME_SPEED = 1/60f;
	
	//Y by X room array
	public static final int NUM_OF_ROOMS_Y = Menu.getSize()[0]; 
	public static final int NUM_OF_ROOMS_X = Menu.getSize()[1];

	//Camera size
	public static final float CAMERA_WIDTH = 24f;
	public static final float CAMERA_HEIGHT = 18f;
	public static final float VIEWPORT_WIDTH = CAMERA_WIDTH / 3;
	public static final float VIEWPORT_HEIGHT = CAMERA_HEIGHT / 3;
	
	//Room constants
	public static final float Y_ROOM_METERS = CAMERA_HEIGHT - (CAMERA_HEIGHT/7.0f); //pixel x/y of each room
	public static final float X_ROOM_METERS = Y_ROOM_METERS; 
	public static final float X_BUFFER = (CAMERA_WIDTH - X_ROOM_METERS)/2; //Border buffer
	public static final float Y_BUFFER = (CAMERA_HEIGHT - Y_ROOM_METERS)/2;
	
	public static final int X_TILES = 24; //num of tiles in each room, must be divisible by 3
	public static final int Y_TILES = 24;
	public static final float TILESIZE = X_ROOM_METERS/X_TILES;
	public static final String[] WALLTYPES = new String[] {"northWall", "eastWall", "southWall", "westWall"};
	public static final String[] DOORTYPES = new String[] {"northDoor", "eastDoor", "southDoor", "westDoor"};	
	
	public static float[][] edgeSizes = new float[][] { //v1X, v1Y, v2X, v2Y edge points>> into NESW
		{-Constants.TILESIZE / 2f, -Constants.TILESIZE/2f, Constants.TILESIZE / 2f, -Constants.TILESIZE/2f}, 
		{-Constants.TILESIZE/2f, -Constants.TILESIZE / 2f, -Constants.TILESIZE/2f, Constants.TILESIZE / 2f},
		{-Constants.TILESIZE / 2f, Constants.TILESIZE/2f, Constants.TILESIZE / 2f, Constants.TILESIZE/2f},
		{Constants.TILESIZE/2f, -Constants.TILESIZE / 2f, Constants.TILESIZE/2f, Constants.TILESIZE / 2f}}; 
	
	
	
	//MAKE FINAL LATER TODO
	//public static final ArrayList<String[][]> TEMPLATE = new ArrayList<String[][]>(GameScreen.getImmutableList());


}