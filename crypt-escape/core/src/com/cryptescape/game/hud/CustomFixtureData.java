package com.cryptescape.game.hud;

public class CustomFixtureData {
	private boolean isMovable;
//	private boolean mouseInRange;
	
	/**
	 * Used for determining if a object can be moved
	 * @param isMovable
	 */
	public CustomFixtureData(boolean isMovable) {
		this.isMovable = isMovable;
	}
	
	 /**
     * Used for determining if a object should be saved from deletion
     * @param deleteOnClose
     */
//	 public CustomFixtureData(boolean isMovable) {
//	        this.isMovable = isMovable;
//	 }
	
	public void setMovable(boolean s) {
		isMovable = s;
	}
	
	public boolean getMovable() {
		return isMovable;
	}
	
//	public boolean interact() {
//		return mouseInRange;
//	}
//	
//	public void setMouseInRange(boolean s) {
//		mouseInRange = s;
//	}
}
