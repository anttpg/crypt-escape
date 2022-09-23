package com.cryptescape.game;

import com.badlogic.gdx.Gdx;

public class Constants {
	
	//Constants
	public static float FRAME_SPEED = 1/60f;
	public static final int ROOMY = Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/3);
	public static final int ROOMX = (int)((4/3.0) * ROOMY); 
	public static final int X_TILES = 24;
	public static final int Y_TILES = 18;
	public static final int TILESIZE = ROOMX/X_TILES;
	
	//Aspect ratio should always be 4:3. Remember this is in 'Meters', not pixels
	public static final float VIEWPORT_WIDTH = 12f;
	public static final float VIEWPORT_HEIGHT = 9f;
}