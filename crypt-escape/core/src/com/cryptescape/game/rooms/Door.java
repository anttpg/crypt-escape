package com.cryptescape.game.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.graphics.TransitionScreen;

public class Door extends Interactable {
	
	private Door partner = null;
	

	private final String type;
	
	public static final float ANIMATION_SPEED = Constants.FRAME_SPEED * 10;
	
	private Phase animationPhase = Phase.FINISHED;
    enum Phase {
        OPENING,
        WALKIN,
        WALKOUT,
        CLOSING,
        FINISHED,
        RATTLE,
        CREAK
    }

	
	public Door(int col, int row, String name, Room p, int c) {
		super(col, row, name, p);
		type = name;
		
		animation = new Animation<TextureRegion>(ANIMATION_SPEED, GameScreen.atlas.findRegions(name));
		super.setTextureRegion(animation.getKeyFrame(0f));

		super.createStaticEdge(c);
		super.createInteractionRadius(Constants.TILESIZE*1.2f, Constants.TILESIZE*1.2f);
	}
	
	
	public void draw(SpriteBatch batch) {
		update();
		super.draw(batch);
	}
	
	
	public void update() {
        switch (this.animationPhase) {
            
            //Do nothing
            case FINISHED:
                break;

            // FOR SHAKING DOOR (NO TRANSPORT)
            case RATTLE:
                GameScreen.sounds.playSound("Rattle", 0.8f);
                this.animationPhase = Phase.FINISHED;
                break;
                
            case CREAK:
                GameScreen.sounds.playSound("MinecraftDoor", 0.6f);
                this.animationPhase = Phase.OPENING;
                break;

            // FOR OPENING DOOR
            case OPENING:
                if (timer > animation.getAnimationDuration() + 0.2f) {
                    timer = 0;
                    this.animationPhase = Phase.WALKIN;
                }
                super.setTextureRegion(animation.getKeyFrame(timer));
                break;

            // TELEPORT N WALK ANIMATION
            case WALKIN:
                if (timer > animation.getAnimationDuration() + 0.4f) {
                    Vector2 exitPos = new Vector2(getExitPosition());
                    TransitionScreen.fadeIn = true;
                    GameScreen.player.currentRoom = getPartnerRoom();
                    GameScreen.player.setPos(exitPos.x, exitPos.y);
                    
                    super.setTextureRegion(animation.getKeyFrame(0));
                    this.animationPhase = Phase.FINISHED;
                    
                    this.partner.animation.setPlayMode(PlayMode.REVERSED);
                    this.partner.setPhase(Phase.WALKOUT);
                }
                break;

            // LEAVE ANIMATION AND CLODE DOOR
            case WALKOUT:
                if (timer > animation.getAnimationDuration() + 0.6f) {
                    this.animationPhase = Phase.FINISHED;
                    animation.setPlayMode(PlayMode.NORMAL);
                    timer = 0;
                }
                this.setTextRegion(animation.getKeyFrame(timer));
                break;
        }

        if (this.animationPhase != Phase.FINISHED) 
            timer += Gdx.graphics.getDeltaTime(); // Fine because only called on draw
        
		
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
	
	private void setPhase(Phase phase) {
	    animationPhase = phase;
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
		this.animationPhase = Phase.CREAK;
		timer = 0;
	}
	
	public void blockedDoorAnimation() {
		this.animationPhase = Phase.RATTLE;
		timer = 0;
	}
	

}
