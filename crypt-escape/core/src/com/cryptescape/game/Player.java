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
import com.cryptescape.game.rooms.Door;
import com.cryptescape.game.rooms.Room;

import box2dLight.RayHandler;


public class Player extends Movables {
	private static AnimationHandler playerAnimation;
	private TextureAtlas textureAtlas; 
	private TextureRegion frame;
	private float elapsedTime = 1f;
	private float teleportCooldown = 0f;
	private float scale;
	
	private float batteryLevel = 0f;

	public float maxCandleLevel = 3.5f;
	private float candleLevel = maxCandleLevel;
	public float burnPerTick = Constants.FRAME_SPEED/(15f*maxCandleLevel); //15 is exactly 5 minutes to get to 0.
	private float offset;
	
	private Random rand = new Random();
	private static boolean runningAnimation;
	private static float runningAnimationCounter;

	/**
	* Defines a Player object. Player extends Movables. 
	* X, Y represent where the player will show on the map.
	* W, H scale the sprite of the player respectivly. t scales all
	* math/tolerances within the function. All values must be given with respect 
	* to meters, not pixels. 
	*/
	public Player(float x, float y, float t, Room s) {
		super(x, y, 2.1f, s, new float[] {8f,16f});
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
//		playerAnimation.setPlayModes(Animation.PlayMode.LOOP);

	}
	
		
	@Override
	public void draw(SpriteBatch batch) {
		elapsedTime += Gdx.graphics.getDeltaTime();
		frame = playerAnimation.getFrame();
		batch.draw(frame, getX() - (Constants.TILESIZE/1.8f), getY() - (Constants.TILESIZE/3.8f), Constants.TILESIZE*1.1f, Constants.TILESIZE*1.1f);
	}
	
	
    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(body.getAngle() *  MathUtils.radiansToDegrees);
        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
		body.applyForceToCenter(forceVector, true);
        this.update();
        
        if(runningAnimation) {
        	runningAnimationCounter -= delta;
        	if(runningAnimationCounter < 0)
        		runningAnimation = false;
        }
    }
    
    public void debugPlayer() {
    	System.out.println(playerAnimation.toString());
    	System.out.println("Player X: " + this.getX() + " PlayerY: " + this.getY());
		System.out.println("Player xV: " + xVel + " Player yV: " + yVel);
		//System.out.println("Player xA: " + xAcc + " Player yA: " + yAcc);
		System.out.println(Math.abs(-15.217f*Math.abs(this.xVel) + 0.6522f));
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
    		return true;
    	}
    	
    	d.blockedDoorAnimation();
    	return false;
    }
    
	public void update() {
		this.updateTick();
		
		if (teleportCooldown < 0 && InputHandler.e_pressed) {
			for (Door d : currentRoom.getDoors()) {
				if (d != null && d.isPlayerInRange()) {
					GameScreen.player.changeRoom(d);
					
					teleportCooldown = 3f;
				}
			}
		}
		teleportCooldown -= Gdx.graphics.getDeltaTime();
		
		if (elapsedTime > 0.3 && !runningAnimation) {
			offset = rand.nextFloat()*0.2f;
			elapsedTime = 0;
			
			if(Math.abs(xVel) > 0.001) { //A weird function made to control animation speed
				playerAnimation.setAnimationDuration(0.2f); //Sets frame duration
				playerAnimation.addTime(Math.abs(xVel/5f) - 0.13f);
			}
			else if(Math.abs(yVel) > 0.001 && Math.abs(yVel) > Math.abs(xVel)) {
				playerAnimation.setAnimationDuration(0.2f); //Sets frame duration
				playerAnimation.addTime(Math.abs(yVel/5f) - 0.13f);
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
	
	public static void initiateAnimation(String current) {
		playerAnimation.setCurrent(current);
		runningAnimation = true;
		runningAnimationCounter = playerAnimation.getCurrent().getAnimationDuration();
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
