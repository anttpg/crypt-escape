package com.cryptescape.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door {
	private Interactable leftDoor; // Top if side, left if top
	private Interactable rightDoor; // Bottom if side, right if top
	private Animation leftAnimation;
	private Animation rightAnimation;
	
	private int animationPhase = -1;
	
	public Door(int col, int row, String current, Room p, int c) {
		leftAnimation = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions(current + "Left"));
		rightAnimation = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions(current + "Right"));
		//leftAnimation.setFrameDuration();
		
		if (c == 2 || c == 0) {
			if (row == (Constants.X_TILES / 2)) {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(GameScreen.atlas.findRegion(current));

			} else {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(GameScreen.atlas.findRegion(current));
			}
		}

		else {
			if (col == (Constants.Y_TILES / 2)) {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(GameScreen.atlas.findRegion(current));

			} else {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(GameScreen.atlas.findRegion(current));
			}
		}
	}

	
	public void setMissingDoor(int col, int row, String current, Room p, int c) {
		if (leftDoor == null) {
			if (c == 2 || c == 0) {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(GameScreen.atlas.findRegion(current));

			} else {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(GameScreen.atlas.findRegion(current));

			}
		} 
		
		else {
			if (c == 2 || c == 0) {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(GameScreen.atlas.findRegion(current));
			}

			else {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(GameScreen.atlas.findRegion(current));

			}
		}	
	}
	
	public void draw(SpriteBatch batch) {
		if(animationPhase == -1) {
			
		}
		
		leftDoor.draw(batch);
		rightDoor.draw(batch);
	}
}
