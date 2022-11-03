package com.cryptescape.game.rooms;

import java.util.Random;

import com.cryptescape.game.GameScreen;

public class BoxObstacle extends Interactable  {

	public BoxObstacle(int col, int row, String current, Room p) {
		super(col, row, current, p);
		
		Random r = new Random();
		int l = r.nextInt(6)+1;
		if(GameScreen.atlas.findRegion(current + l) != null) {
			super.setTextureRegion(GameScreen.atlas.findRegion(current + l));
			super.checkBounds(current + l);
		}	
		else {
			System.out.println("NullBoxType; Could not find box of type '" + current + "" + l + "'. "
			        + "Make sure all boxes have a boxUnlocked version. "); 
		}
		
		super.createStaticBox();
	}
	
}
