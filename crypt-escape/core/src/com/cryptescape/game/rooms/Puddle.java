package com.cryptescape.game.rooms;

import java.util.Random;

import com.cryptescape.game.GameScreen;

public class Puddle extends Interactable {

	public Puddle(int c, int r, String name, Room p) {
		super(c, r, name, p);
//		super.createStaticEdge(0);
		
		Random rand = new Random();
		int l = rand.nextInt(2)+1;
		if(GameScreen.atlas.findRegion("puddle" + l) != null) {
			super.setTextureRegion(GameScreen.atlas.findRegion("puddle" + l));
		}	
		else {
			System.out.println("NullPuddleType; Could not find box of type 'puddle" + l + "'"); 
		}
	}
	
}
