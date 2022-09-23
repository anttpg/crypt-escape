package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Interactable {
	
	public boolean visible;
	public int[] room = new int[2]; // [roomX,roomY]
	public double[] pos = new double[2]; // [x, y]
	public int spritePos = -1;
	public Texture spriteMap;
	public TextureRegion spriteRegion;
	int height;
	int width;
	
	public Interactable(boolean v, int[] r, int[] position, Texture mapPath) {
		pos[0] = position[0];
		pos[1] = position[1];
		visible = v;
		room[0] = r[0];
		room[1] = r[1];
		spriteMap = mapPath;
		spriteRegion = new TextureRegion(spriteMap, 0,0,32,32);
		height = spriteMap.getHeight()/32;
		width = spriteMap.getWidth()/32;
	}
}
