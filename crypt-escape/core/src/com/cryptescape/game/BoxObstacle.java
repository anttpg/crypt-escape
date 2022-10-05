package com.cryptescape.game;

public class BoxObstacle extends Interactable {

	public BoxObstacle(int col, int row, String current, Room p) {
		super(col, row, current, p);
		super.createStaticBox();
	}
	
}
