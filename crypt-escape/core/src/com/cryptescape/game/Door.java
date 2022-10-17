package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Door {
	private Interactable leftDoor; // Top if side, left if top
	private Interactable rightDoor; // Bottom if side, right if top
	private Animation<TextureRegion> leftAnimation;
	private Animation<TextureRegion> rightAnimation;
	
	private int animationPhase = 0;
	private float timer;
	private float relativeTime;
	private final float ANIMATION_SPEED = Constants.FRAME_SPEED * 20;
	
	public Door(int col, int row, String current, Room p, int c) {
		leftAnimation = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions(current + "Left"));
		rightAnimation = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions(current + "Right"));
		leftAnimation.setFrameDuration(ANIMATION_SPEED);
		rightAnimation.setFrameDuration(ANIMATION_SPEED);
		
		if (c == 2 || c == 0) {
			if (row == (Constants.X_TILES / 2)) {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(rightAnimation.getKeyFrame(0f));

			} else {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(leftAnimation.getKeyFrame(0f));
			}
		}

		else {
			if (col == (Constants.Y_TILES / 2)) {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(leftAnimation.getKeyFrame(0f));

			} else {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(rightAnimation.getKeyFrame(0f));
			}
		}
	}

	
	public void setMissingDoor(int col, int row, String current, Room p, int c) {
		if (leftDoor != null) {
			if (c == 2 || c == 0) {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(rightAnimation.getKeyFrame(0f));

			} else {
				rightDoor = new Interactable(col, row, current, p);
				rightDoor.createStaticEdge(c);
				rightDoor.setTextureRegion(rightAnimation.getKeyFrame(0f));

			}
		} 
		
		else {
			if (c == 2 || c == 0) {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(leftAnimation.getKeyFrame(0f));
			}

			else {
				leftDoor = new Interactable(col, row, current, p);
				leftDoor.createStaticEdge(c);
				leftDoor.setTextureRegion(leftAnimation.getKeyFrame(0f));

			}
		}	
	}
	
	public void draw(SpriteBatch batch) {
		if(animationPhase == 0) {
			timer += Gdx.graphics.getDeltaTime();
			if(timer > 3f) {
				timer = 0;
				animationPhase = 1;
			}
		}
		if(animationPhase == 1) {
			timer += Gdx.graphics.getDeltaTime();
			rightDoor.setTextureRegion(rightAnimation.getKeyFrame(timer));
			leftDoor.setTextureRegion(leftAnimation.getKeyFrame(timer));
			if(timer > rightAnimation.getAnimationDuration()) {
				animationPhase = 2;
				timer = 0;
			}
		}
		else if(animationPhase == 2) {
			timer += Gdx.graphics.getDeltaTime();
			if (timer > 1.5f) {
				relativeTime = (rightAnimation.getAnimationDuration() - (timer-1.5f));
				rightDoor.setTextureRegion(rightAnimation.getKeyFrame(relativeTime));
				leftDoor.setTextureRegion(leftAnimation.getKeyFrame(relativeTime));
				if(timer > rightAnimation.getAnimationDuration()+1.5f) {
					animationPhase = 0;
					timer = 0;
				}
			}
		}
		
		leftDoor.draw(batch);
		rightDoor.draw(batch);
	}
}
