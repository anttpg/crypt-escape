package com.cryptescape.game;

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


public class Player extends Movables {
	private AnimationHandler playerAnimation;
	private TextureAtlas textureAtlas;
	private TextureRegion frame;
	private float elapsedTime = 1f;
	
	private float scale;
	



	/**
	* Defines a Player object. Player extends Movables. 
	* X, Y represent where the player will show on the map.
	* W, H scale the sprite of the player respectivly. t scales all
	* math/tolerances within the function. All values must be given with respect 
	* to meters, not pixels. 
	*/
	public Player(float x, float y, float t, Room s) {
		super(x, y, 2.1f, s);
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
	}
	
		
	@Override
	public void draw(SpriteBatch batch) {
		body.applyForceToCenter(forceVector, true);
		this.updateTick();

		if (elapsedTime > 0.3) {
			elapsedTime = 0;

			if(Math.abs(xVel) > 0.0001 || Math.abs(yVel) > 0.0001) { //A weird function made to control animation speed
				playerAnimation.setAnimationDuration(Math.abs(-15.217f*Math.abs(this.xVel) + 0.6522f));
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
		
		elapsedTime += Gdx.graphics.getDeltaTime();
		frame = playerAnimation.getFrame();
		batch.draw(frame, xPos - (Constants.TILESIZE/1.8f), yPos - (Constants.TILESIZE/3.8f), Constants.TILESIZE*1.1f, Constants.TILESIZE*1.1f);
	}
	
	
    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(body.getAngle() *  MathUtils.radiansToDegrees);
        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
    }
    
    public void debugPlayer() {
    	System.out.println(playerAnimation.toString());
    	System.out.println("Player X: " + this.getX() + " PlayerY: " + this.getY());
		System.out.println("Player xV: " + xVel + " Player yV: " + yVel);
		System.out.println("Player xA: " + xAcc + " Player yA: " + yAcc);
		System.out.println(Math.abs(-15.217f*Math.abs(this.xVel) + 0.6522f));
    }
    
    public Room getRoom() {
    	return currentRoom;
    }
    
    public void changeRoom(Room r) {
    	currentRoom = r;
    	
    	//Change the size of the background proportional to the current room REMEBER ITS IN X/Y
    	int[] bounds = Constants.ROOMSIZES.get(r.getRoomType());
    	GameScreen.BACKGROUND.setRegion(0, 0, 
    			(bounds[3]*Constants.TILESIZE) - (bounds[1]*Constants.TILESIZE) - Constants.TILESIZE*2,
    			(bounds[2]*Constants.TILESIZE) - (bounds[0]*Constants.TILESIZE) - Constants.TILESIZE*2
    			);
    }   
}
