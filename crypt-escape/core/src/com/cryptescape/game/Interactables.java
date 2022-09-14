package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Interactables {
	
	public boolean visible;
	public int[] room = new int[2]; // [roomX,roomY]
	public double[] pos = new double[2]; // [x, y]
	public int spritePos = -1;
	public Texture spriteMap;
	public TextureRegion spriteRegion;
	int height;
	int width;
	
	public Interactables(boolean v, int[] r, int[] position) {
		pos[0] = position[0];
		pos[1] = position[1];
		visible = v;
		room[0] = r[0];
		room[1] = r[1];
		
		
	}
	

}
