package com.cryptescape.game;

import java.util.Random;

public class BoxObstacle extends Interactable  {

	public BoxObstacle(int col, int row, String current, Room p) {
		super(col, row, current, p);
		super.createStaticBox();
		Random r = new Random();
		int l = r.nextInt(6)+1;
		if(GameScreen.atlas.findRegion("box" + l) != null) {
			super.setTextureRegion(GameScreen.atlas.findRegion("box" + l));
		}	
		else {
			System.out.println("NullBoxType; Could not find box of type 'box" + l + "'"); 
		}
	}
	
}
