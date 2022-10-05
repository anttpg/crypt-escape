package com.cryptescape.game;

import java.util.Random;

public class Puddle extends Interactable {

	public Puddle(int c, int r, String current, Room p) {
		super(c, r, current, p);
		super.createStaticEdge(0);
		
		Random rand = new Random();
		int l = rand.nextInt(3)+1;
		if(GameScreen.atlas.findRegion("puddle" + l) != null) {
			super.setTextureRegion(GameScreen.atlas.findRegion("puddle" + l));
		}	
		else {
			System.out.println("NullPuddleType; Could not find box of type 'puddle" + l + "'"); 
		}
	}
	
}
