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


/* A movables object is anything on the screen that should be able to move,
 * and intereact with other objects. Movables extends Image, and all movable
 * instances will be dynamic bodies
 */

public abstract class Movables extends Actor{

	// VARIABLES
	public float xPos; // x
	public float yPos; // y
	public float xVel; // xY
	public float yVel; // yV
	public float xAcc; // xA
	public float yAcc; // yA
	public final float maxVel; // pixels/tick
	public float speed = 1; //changes when sprinting
	
	public Body body;
	public Room currentRoom; 
	public Fixture fixture;
	public Vector2 forceVector = new Vector2();
	public boolean visible;

	// CONSTRUCTORS
	public Movables(float x, float y, float mv, Room s) {
		xPos = x;
		yPos = y;
		maxVel = mv;
		currentRoom = s;
		
        //physics body definitions
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 5.0f;
        bodyDef.fixedRotation = true;
        this.body = GameScreen.world.createBody(bodyDef);
        

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.TILESIZE/4, Constants.TILESIZE/16);
        FixtureDef fixtureDef = new FixtureDef();
        
        //Physics rules
        fixtureDef.shape = shape;
        fixtureDef.density = 8f;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution= 0.05f;
        fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(Constants.TILESIZE/2,Constants.TILESIZE/2);
		xVel = this.body.getLinearVelocity().x;
		yVel = this.body.getLinearVelocity().y;
	}
		
	abstract void draw(SpriteBatch batch);
	
	public void setVelocity(float xV, float yV, float s) {
		xVel = xV*s;
		yVel = yV*s;
		speed = s;
	}
	
	//S is for sprinting, a speed multiplier
	public void setAcceleration(float x, float y, float s) {
		xAcc = x*s;
		yAcc = y*s;
		speed = s;
		forceVector.set(xAcc, yAcc); 
	}
		

	public void updateTick() {
//		if(-maxVel*speed < xVel+xAcc && xVel+xAcc < maxVel*speed) xVel += xAcc;
//		if(-maxVel*speed < yVel+yAcc && yVel+yAcc < maxVel*speed) yVel += yAcc;
//		if(!(-maxVel*speed < xVel && xVel < maxVel*speed)) xVel -= xVel/10; //Checks for fucky problem when switching sides
//		if(!(-maxVel*speed < yVel && yVel < maxVel*speed)) yVel -= yVel/10; // If maxspeed eclipsed do this
		xVel = this.body.getLinearVelocity().x;
		yVel = this.body.getLinearVelocity().y;

		xPos = this.body.getPosition().x;
		yPos = this.body.getPosition().y;
		super.setX(xPos);
		super.setY(yPos);
	}
}