package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Door  extends Interactable {
	private Animation<TextureRegion> animation;
	private Door partner = null;
	
	private boolean TEMPVARIABLEhasAnimation = false;
	
	private int animationPhase = 0;
	private float timer;
	private float relativeTime;
	private final String type;
	private final float ANIMATION_SPEED = Constants.FRAME_SPEED * 20;

	
	public Door(int col, int row, String current, Room p, int c) {
		super(col, row, current, p);
		type = current;
		

		animation = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions(current));
		animation.setFrameDuration(ANIMATION_SPEED);
		
		if(animation.getKeyFrames().length == 0) { 
			super.setTextureRegion(GameScreen.atlas.findRegion(current));
		}
		else {
			super.setTextureRegion(animation.getKeyFrame(0f));
			TEMPVARIABLEhasAnimation = true ;
		}
			
		
		super.createStaticEdge(c);
		super.createInteractionRadius(Constants.TILESIZE, Constants.TILESIZE);
	}
	
	
	public void draw(SpriteBatch batch) {
		if(TEMPVARIABLEhasAnimation) {
			if(animationPhase == 0) {
				timer += Gdx.graphics.getDeltaTime();
				if(timer > 3f) {
					timer = 0;
					animationPhase = 1;
				}
			}
			if(animationPhase == 1) {
				timer += Gdx.graphics.getDeltaTime();
				super.setTextureRegion(animation.getKeyFrame(timer));
				if(timer > animation.getAnimationDuration()) {
					animationPhase = 2;
					timer = 0;
				}
			}
			else if(animationPhase == 2) {
				timer += Gdx.graphics.getDeltaTime();
				if (timer > 1.5f) {
					relativeTime = (animation.getAnimationDuration() - (timer-1.5f));
					super.setTextureRegion(animation.getKeyFrame(relativeTime));
					if(timer > animation.getAnimationDuration()+1.5f) {
						animationPhase = 0;
						timer = 0;
					}
				}
			}
		}
		super.draw(batch);
	}
	

	public void setPartner(Door p) {
		partner = p;
	}
	
	/**
	 * Gets the partner Door for this item, if Null no such door exists.
	 */
	public Door getPartner() {
		return partner;
	}


	/**
	 * specifies coords on an XY plane of where player should teleport to to exit from the related door
	 */
	public Vector2 getExitPosition() {
		
		float[] offset;
		if(partner.type.equals("northDoor"))
			offset = new float[] {0, -Constants.TILESIZE};
		else if(partner.type.equals("southDoor"))
			offset = new float[] {0, Constants.TILESIZE};
		else if(partner.type.equals("eastDoor"))
			offset = new float[] {-Constants.TILESIZE, 0};
		else if(partner.type.equals("westDoor"))
			offset = new float[] {Constants.TILESIZE, 0};
		
		else 
			offset = null;
		
		return new Vector2(partner.getItemLocation().x + offset[0], partner.getItemLocation().y + offset[1]);
	}
	
	public Room getPartnerRoom() {
		return partner.getParentRoom();
	}

	
}
