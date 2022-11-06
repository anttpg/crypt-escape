package com.cryptescape.game.rooms;

import java.util.Random;

import com.cryptescape.game.GameScreen;

public class Wall extends Interactable {
	
	public Wall(int col, int row, String current, Room p, int c) {
		super(col, row, current, p);
		
		Random r = new Random();
		int l = r.nextInt(4)+1;

		if(GameScreen.atlas.findRegion(current + l) != null) {

			super.setTextureRegion(GameScreen.atlas.findRegion(current + l));
			super.createStaticEdge(c);
			
		}	
		else {
			System.out.println("NullBoxType; Could not find box of type '" + current + " " + l + "'"); 
		}

	}
	
}
