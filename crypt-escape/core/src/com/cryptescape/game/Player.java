package com.cryptescape.game;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.cryptescape.game.rooms.Box;
import com.cryptescape.game.rooms.Door;
import com.cryptescape.game.rooms.DroppedItem;
import com.cryptescape.game.rooms.Freeform;
import com.cryptescape.game.rooms.Interactable;
import com.cryptescape.game.rooms.Room;

import box2dLight.RayHandler;


public class Player extends Movables {
	private static AnimationHandler playerAnimation;
	private TextureAtlas textureAtlas; 
	private float elapsedTime = 1f;
	private static float fadeTimer = 0;
	private float teleportCooldown = 0f;
	private float scale;
	
	private float batteryLevel = 7f;

	public float maxCandleLevel = 3.5f;
	private float candleLevel = maxCandleLevel;
	public float burnPerTick = Constants.FRAME_SPEED/(15f*maxCandleLevel); //15 is exactly 5 minutes to get to 0.
	private float offset;
	
	private Random rand = new Random();
	private static boolean overrideAnimation;
	private static float overrideAnimationCounter;

	/**
	* Defines a Player object. Player extends Movables. 
	* X, Y represent where the player will show on the map.
	* W, H scale the sprite of the player respectivly. t scales all
	* math/tolerances within the function. All values must be given with respect 
	* to meters, not pixels. 
	*/
	public Player(float x, float y, float t, Room s, float maxVelocity) {
		super(x, y, maxVelocity, s, new float[] {8f,16f});
		scale = t;
		
        //effects
		textureAtlas = new TextureAtlas();
		textureAtlas.addRegion("note",new TextureRegion(new Texture("Old Assets/notusable/note.png")));
		
		playerAnimation = new AnimationHandler(); //Adds all the animations for the player
		playerAnimation.add("playerN", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerN")));
		playerAnimation.add("playerS", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerS")));
		playerAnimation.add("playerE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerE")));
		playerAnimation.add("playerW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerW")));
		playerAnimation.add("playerNE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerNE")));
		playerAnimation.add("playerNW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerNW")));
		playerAnimation.add("playerSE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerSE")));
		playerAnimation.add("playerSW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerSW")));
		playerAnimation.add("error", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("error")));
		playerAnimation.setCurrent("playerS");
		
		super.setZIndex(2);

	}
	
		
	@Override
	public void draw(SpriteBatch batch) {
	    //debugPlayer();
        elapsedTime += Gdx.graphics.getDeltaTime();
		frame = playerAnimation.getFrame();
		batch.draw(frame, getX() - (Constants.TILESIZE/1.8f), getY() - (Constants.TILESIZE/3.8f), Constants.TILESIZE*1.1f, Constants.TILESIZE*1.1f);
	}
	
	
    @Override
    public void act(float delta) {
        this.defaultAct();
        this.update();
        body.applyForceToCenter(forceVector, true);
    }
    
    public void debugPlayer() {
    	System.out.println(playerAnimation.toString());
    	System.out.println("Player X: " + this.getX() + " PlayerY: " + this.getY());
		System.out.println("Player xV: " + xVel + " Player yV: " + yVel);
		System.out.println(Math.abs(-15.217f*Math.abs(this.xVel) + 0.6522f));
		System.out.println();
    }
    
    public Room getRoom() {
    	return currentRoom;
    }
    
    /**
     * ONLY USE TO SET THE STARTING ROOM, WILL NOT GO THROUGH DOOR CHECK SEQUENCE
     */
    public void setStartingRoom(Room r) {
    	currentRoom = r;
    } 
    
    
    public boolean changeRoom(Door d) {
    	if(d.getPartner() != null) {
    		d.startAnimation();
    		GameScreen.fade = true;
    		return true;
    	}
    	
    	d.blockedDoorAnimation();
    	return false;
    }
    
    /**
     * This deals with all interactions the player will be making, as well as setting the current animation
     */
	public void update() {
        if(overrideAnimation) {
            overrideAnimationCounter -= Constants.FRAME_SPEED;
            if(overrideAnimationCounter < 0)
                overrideAnimation = false;
        }
	
		if (InputHandler.e_pressed && !InputHandler.tab_pressed) {
		    if(teleportCooldown < 0) {
    			for (Door door : currentRoom.getDoors()) {
    				if (door != null && door.isPlayerInRange()) {
    					GameScreen.player.changeRoom(door);
    					teleportCooldown = 3f;
    				}
    			}
		    }
	
		    
		    for (Box box : currentRoom.getBoxes()) 
                if (box.isPlayerInRange()) 
                    box.setAnimationPhase("opening");
		    
		    
		    for (DroppedItem item : currentRoom.getDroppedItems()) 
                if (item.isPlayerInRange()) 
                    item.pickup();
 
		}
		teleportCooldown -= Gdx.graphics.getDeltaTime();
		
		if (elapsedTime > 0.3 && !overrideAnimation) {
			offset = rand.nextFloat()*0.2f;
			elapsedTime = 0;
			//System.out.println(super.getBody().getLinearVelocity());
			
			if(Math.abs(xVel) > 0.001) { //A weird function made to control animation speed
				playerAnimation.setAnimationDuration(0.4f); //Sets frame duration
				playerAnimation.addTime((Math.abs(xVel/5f) - 0.13f)*2f);
			}
			else if(Math.abs(yVel) > 0.001 && Math.abs(yVel) > Math.abs(xVel)) {
				playerAnimation.setAnimationDuration(0.4f); //Sets frame duration
				playerAnimation.addTime((Math.abs(yVel/5f) - 0.13f)*2f);
			}
			else {
				playerAnimation.setAnimationDuration(10000);
			}
			
			if ((xVel >= (1/scale)) && ((yVel <= (1/scale)) && (yVel >= -(1/scale))) ) { // East
				playerAnimation.setCurrent("playerE");
				
			} else if ((xVel <= -(1/scale)) && ((yVel <= (1/scale)) && (yVel >= -(1/scale))) ) { // West
				playerAnimation.setCurrent("playerW");
				
			} else if (((xVel <= (1/scale)) && (xVel >= -(1/scale)) ) && (yVel >= (1/scale))) { // North
				playerAnimation.setCurrent("playerN");
				
			} else if (((xVel <= (1/scale)) && (xVel >= -(1/scale)) ) && (yVel <= -(1/scale))) { // South
				playerAnimation.setCurrent("playerS");
				
			} else if ((xVel > 0) && (yVel > 0)) { // Northeast
				playerAnimation.setCurrent("playerNE");
				
			} else if ((xVel > 0) && (yVel < 0)) { // Southeast
				playerAnimation.setCurrent("playerSE");
				
			} else if ((xVel < 0) && (yVel > 0)) { // Northwest
				playerAnimation.setCurrent("playerNW");
				
			} else if ((xVel < 0) && (yVel < 0)) { // Southwest
				playerAnimation.setCurrent("playerSW");
				
			} else if ((xVel == 0) && (yVel == 0)) { // Standing still 
				playerAnimation.setCurrent("playerS");
			}
		}	 	
	}          
	
	/**
	 * forces the current animation given to run its course.
	 */
	public static void forceAnimationPlay(String current) {
		playerAnimation.setCurrent(current);
		overrideAnimation = true;
		overrideAnimationCounter = playerAnimation.getCurrent().getAnimationDuration();
	}

    public float getCandleLevel() {
    	candleLevel -= (burnPerTick);
    	return candleLevel + offset;
    }
    
    public float getBatteryLevel() {
    	//batteryLevel -= Constants.FRAME_SPEED;
    	return batteryLevel;
    }

	public float getMaxCandleLevel() {
		return maxCandleLevel;
	}

	public float getBurnPerTick() {
		return burnPerTick;
	}  
}
