package com.cryptescape.game;

public class CustomFixtureData {
	private boolean isMovable;
//	private boolean mouseInRange;

	public CustomFixtureData(boolean isMovable) {
		this.isMovable = isMovable;
	}
	
	public void setMovable(boolean s) {
		isMovable = s;
	}
	
	public boolean getMovable() {
		return isMovable;
	}
	
//	public boolean isMouseInRange() {
//		return mouseInRange;
//	}
//	
//	public void setMouseInRange(boolean s) {
//		mouseInRange = s;
//	}
}
