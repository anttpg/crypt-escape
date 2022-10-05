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
	private boolean isRunning = false;
	private TextureRegion frame;
	private AnimationHandler playerAnimation;
	private float elapsedTime = 1f;
	private boolean changeAnimation = true;
	
    private ParticleEffect effect;
	private TextureAtlas textureAtlas;

	private Room currentRoom; 

	/**
	* Defines a Player object. Player extends Movables. 
	* X, Y represent where the player will show on the map.
	* W, H scale the sprite of the player respectivly. t scales all
	* math/tolerances within the function. All values must be given with respect 
	* to meters, not pixels. 
	*/
	public Player(float x, float y, float w, float h, float t, Room starting) {
		super(x, y, w, h, 2.1f, t);
		currentRoom = starting;
		
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
        
        //physics body definitions
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create a body in the world using our definition
        this.body = GameScreen.world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        shape.setAsBox(this.getWidth()/2, this.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution= 1f;
        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
	}
	
		
	@Override
	public void draw(SpriteBatch batch) {
		this.updateTick();
		
		//this.debugPlayer();
		//Gdx.graphics.getDeltaTime();
		if (elapsedTime > 0.3) {
			elapsedTime = 0;
			
			if(Math.abs(xVel) > 0.0001 || Math.abs(yVel) > 0.0001) {
				playerAnimation.setAnimationDuration(Math.abs(-15.217f*Math.abs(this.xVel) + 0.6522f));
			}
			else {
				playerAnimation.setAnimationDuration(10000);
			}
			
			if ((xVel >= (1/tc)) && ((yVel <= (1/tc)) && (yVel >= -(1/tc))) ) { // East
				playerAnimation.setCurrent("playerE");
				
			} else if ((xVel <= -(1/tc)) && ((yVel <= (1/tc)) && (yVel >= -(1/tc))) ) { // West
				playerAnimation.setCurrent("playerW");
				
			} else if (((xVel <= (1/tc)) && (xVel >= -(1/tc)) ) && (yVel >= (1/tc))) { // North
				playerAnimation.setCurrent("playerN");
				
			} else if (((xVel <= (1/tc)) && (xVel >= -(1/tc)) ) && (yVel <= -(1/tc))) { // South
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
		body.applyForceToCenter(forceVector, true);
		batch.draw(frame, xPos, yPos, this.getWidth(), this.getHeight());
//		effect.draw(batch);
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
		System.out.println("Jolt: " + jolt[0] + "  " + jolt[1] + "  " + jolt[2] + "  " + jolt[3]);
		System.out.println(Math.abs(-15.217f*Math.abs(this.xVel) + 0.6522f));
		System.out.println("");
    }
    
    public Room getRoom() {
    	return currentRoom;
    }
    
    public void setRoom(Room r) {
    	currentRoom = r;
    }
    
}
