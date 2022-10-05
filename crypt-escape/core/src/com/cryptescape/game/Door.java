package com.cryptescape.game;

public class Door extends Interactable {
	
	public Door(int col, int row, String current, Room p, int c) {
		super(col, row, current, p);
		super.createStaticEdge(c);
		super.setTextureRegion(GameScreen.atlas.findRegion(Constants.DOORTYPES[c]));
	}

}
