package com.cryptescape.game;

public class Wall extends Interactable {
	
	public Wall(int col, int row, String current, Room p, int c) {
		super(col, row, current, p);
		super.createStaticEdge(c);
		super.setTextureRegion(GameScreen.atlas.findRegion(Constants.WALLTYPES[c]));
	}
	
}
