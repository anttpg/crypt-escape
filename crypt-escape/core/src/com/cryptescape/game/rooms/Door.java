package com.cryptescape.game.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class Door  extends Interactable {
	
	private Door partner = null;
	
	private boolean TEMPVARIABLEhasAnimation = false;
	
	private String animationPhase = "finished";
	private float timer = 0;
	private float relativeTime;
	private final String type;
	private final float ANIMATION_SPEED = Constants.FRAME_SPEED * 10;

	
	public Door(int col, int row, String current, Room p, int c) {
		super(col, row, current, p);
		type = current;
		

		animation = new Animation<TextureRegion>(ANIMATION_SPEED, GameScreen.atlas.findRegions(current));
		
		if(animation.getKeyFrames().length == 0) { 
			super.setTextureRegion(GameScreen.atlas.findRegion(current));
		}
		else {
			super.setTextureRegion(animation.getKeyFrame(0f));
			TEMPVARIABLEhasAnimation = true ;
		}
			
		
		super.createStaticEdge(c);
		super.createInteractionRadius(Constants.TILESIZE*1.2f, Constants.TILESIZE*1.2f);
	}
	
	
	public void draw(SpriteBatch batch) {
		update();
		super.draw(batch);
	}
	
	public void update() {
		if (TEMPVARIABLEhasAnimation) {
			
			//FOR SHAKING DOOR (NO TRANSPORT)
			if(animationPhase.equals("blocked")) {
				if(timer == 0)
					GameScreen.sounds.playSound("Rattle", 0.8f);
			}
			
			
			//FOR OPENING DOOR
			else if (animationPhase.equals("opening")) {
				if(timer == 0)
					GameScreen.sounds.playSound("MinecraftDoor", 0.6f);
				
				super.setTextureRegion(animation.getKeyFrame(relativeTime));
				
				if (timer > animation.getAnimationDuration() + 0.2f) {
					relativeTime = 0;
					animationPhase = "walkIn";
				}
			}

			else if (animationPhase.equals("walkIn")) {

				if (timer > animation.getAnimationDuration() + 0.4f) {
					Vector2 exitPos = new Vector2(getExitPosition());
					GameScreen.player.currentRoom = getPartnerRoom();
					GameScreen.player.setPos(exitPos.x, exitPos.y);
					relativeTime = 0;

					animationPhase = "walkOut";
				}
			}

			else if (animationPhase == "walkOut") {
				animation.setPlayMode(PlayMode.REVERSED); // reverse animation
				partner.setTextRegion(animation.getKeyFrame(relativeTime));

				if (timer > animation.getAnimationDuration()*2 + 0.6f) {
					animationPhase = "finished";
					animation.setPlayMode(PlayMode.NORMAL);
				}
			}
			relativeTime += Gdx.graphics.getDeltaTime();
			timer += Gdx.graphics.getDeltaTime();
		}
	}


	/**
	 * Gets the partner Door for this item, if Null no such door exists.
	 */
	public Door getPartner() {
		return partner;
	}
	
	public void setPartner(Door p) {
		partner = p;
	}
	
	public void setFrame(int frame) {
		animation.getKeyFrame(animation.getFrameDuration() * frame);
	}
	
	public void setToOpen() {
		animation.getKeyFrame(animation.getAnimationDuration());
	}
	
	public void setTextRegion(TextureRegion t) {
		super.setTextureRegion(t);
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


	public void startAnimation() {
		animationPhase = "opening";
		timer = 0;
		relativeTime = 0;
	}
	
	public void blockedDoorAnimation() {
		animationPhase = "blocked";
		timer = 0;
		relativeTime = 0;
	}
	

	
}
