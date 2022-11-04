package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.rooms.Room;


/* A movables object is anything on the screen that should be able to move,
 * and intereact with other objects. Movables extends Image, and all movable
 * instances will be dynamic bodies
 */
public abstract class Movables extends Actor{

	// VARIABLES
	protected float xVel;
	protected float yVel;
	public final float maxVel; // pixels/tick
	public float speed = 1; //changes when sprinting
	
	public boolean visible;
	public Room currentRoom; 
	
	private Fixture fixture;
	protected Body body;
	protected Vector2 forceVector = new Vector2();
	

	/**
	 * maxV is the maximum X OR Y velocity, not combinded.
	 * boundsSize is XY the collision box will be divided by to get where on the object to register action from
	 */
	public Movables(float x, float y, float maxV, Room startRoom, float[] boundsSize) {
		setX(x);
		setY(y);
		this.maxVel = maxV;
		currentRoom = startRoom;
		
		
        //physics body definitions
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 5.0f;
        bodyDef.fixedRotation = true;
        this.body = GameScreen.world.createBody(bodyDef);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.TILESIZE/boundsSize[0], Constants.TILESIZE/boundsSize[1]);
        FixtureDef fixtureDef = new FixtureDef();
        
        //Physics rules
        fixtureDef.shape = shape;
        fixtureDef.density = 23f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution= 0.05f;
        fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(Constants.TILESIZE/2,Constants.TILESIZE/2);
	}
		
	abstract void draw(SpriteBatch batch);
	
	/**
	 * Use this verison of the method to make sure Movables X and Y are updated
	 */
	public void setPos(float x, float y) {
		body.setTransform(x, y, body.getAngle());
	}
	
	//S is for sprinting, a speed multiplier 
	public void setAcceleration(float x, float y, float s) {
		forceVector.set(x*s, y*s); 
	}
		
	public void updateTick() {
		super.setX(this.body.getPosition().x);
		super.setY(this.body.getPosition().y);
		xVel = body.getLinearVelocity().x;
		yVel = body.getLinearVelocity().y;
	}
	
	public Body getBody() {
		return body;
	}
}