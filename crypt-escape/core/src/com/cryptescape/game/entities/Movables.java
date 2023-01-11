package com.cryptescape.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.rooms.Room;


/* A movables object is anything on the screen that should be able to move,
 * and intereact with other objects. Movables extends Image, and all movable
 * instances will be dynamic bodies
 */
public abstract class Movables extends Actor implements Steerable<Vector2> {

	// VARIABLES
	protected float xVel;
	protected float yVel;
	protected float maxSpeed; // pixels/tick
	protected float speed = 1; //changes when sprinting
	
    protected float maxAcceleration = 1;
    protected float maxAngularVelocity = 1;
    protected float maxAngularAcceleration = 1;
    protected float steeringTolerance = 0.00001f;
    protected float boundingRadius = 0.2f;
    protected boolean isTagged;
    
    protected SteeringBehavior<Vector2> steeringBehavior;
    protected SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
    
    
	
	public boolean visible;
	public Room currentRoom; 
	protected TextureRegion frame;
	
	protected Fixture fixture;
	protected Body body;
	protected Vector2 forceVector = new Vector2();
	

	/**
	 * maxV is the maximum X OR Y velocity, not combinded.  Only use for the PLAYER. 
	 * boundsSize is XY the collision box will be divided by to get where on the object to register action from
	 */
	public Movables(float x, float y, float maxV, Room startRoom, float[] boundsSize, short groupIndex) {
		setX(x);
		setY(y);
		this.maxSpeed = maxV;
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
	
	
	/*
	 * Use for every other mob. 
	 * hitbox, is the x,y / tilesize to create hitbox. Leave 1 for default each.
	 */
	public Movables(float x, float y, float width, float height, float maxV, Room startRoom, float[] hitbox, float density, short groupIndex) {
        setX(x);
        setY(y);
        setWidth(width);
        setHeight(height);
        this.maxSpeed = maxV;
        currentRoom = startRoom;

        //physics body definitions
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 5f;
        bodyDef.fixedRotation = true;
        this.body = GameScreen.world.createBody(bodyDef);
        
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants.TILESIZE/hitbox[0], Constants.TILESIZE/hitbox[1]);
        FixtureDef fixtureDef = new FixtureDef();
        
        //Physics rules
        fixtureDef.shape = shape;
        fixtureDef.filter.groupIndex = groupIndex;
        fixtureDef.density = density;
        fixtureDef.friction = 0.7f;
        fixtureDef.restitution= 0.05f;
        fixture = body.createFixture(fixtureDef);

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
	
	/*
	 * Updates Actor position to physics body location. 
	 */
	public void defaultAct() {
        //DOES NOT CHANGE BODY POSITION, ONLY ACTOR TEXTURE LOCATION
        this.setRotation(body.getAngle() *  MathUtils.radiansToDegrees);
        this.setPosition(
                body.getPosition().x-this.getWidth()/2, 
                body.getPosition().y-this.getHeight()/2);
        
		xVel = body.getLinearVelocity().x;
		yVel = body.getLinearVelocity().y;
	}
	
	public void changeRoom(Room newRoom) {
	    currentRoom = newRoom;
	}
	
	public Room getRoom() {
	    return currentRoom;
	}
	
	public Body getBody() {
		return body;
	}
	
	
    /*
     * Will instantly teleport a movable body to specific location, and cancels all acceleration/velocity
     * Do not use for regular movement.
     */
    public void teleportMob(float x, float y) {
        getBody().setAngularVelocity(0);
        getBody().setLinearVelocity(new Vector2(0,0));
        getBody().setTransform(x, y, 0);
    }
    
    
    
    public void setSteeringBehavior(SteeringBehavior<Vector2> behavior) {
        steeringBehavior = behavior;
    }
    
    
    protected void applySteering() {
        boolean anyAccelerations = false;
        if(!steeringOutput.linear.isZero()) {
                Vector2 force = steeringOutput.linear.scl(Constants.FRAME_SPEED); 
                body.applyForceToCenter (force, true); 
                anyAccelerations = true;
         }
        
        if(!body.isFixedRotation())
        if(steeringOutput.angular != 0) {
            body.applyTorque(steeringOutput.angular * Constants.FRAME_SPEED, true);
            anyAccelerations = true;
        } else {
            Vector2 linearVel = getLinearVelocity();
            if(!linearVel.isZero()) {
                float newOrientation = vectorToAngle(linearVel);
                body.setAngularVelocity((newOrientation - getAngularVelocity()) * Constants.FRAME_SPEED);
                body.setTransform(body.getPosition(), newOrientation);
            }
        }
        
        
        if (anyAccelerations) {
            Vector2 velocity = body.getLinearVelocity();
            float currentSpeedSquare = velocity.len2();
            if (currentSpeedSquare > this.maxSpeed * this.maxSpeed) {
                body.setLinearVelocity (velocity.scl(getMaxLinearSpeed() / (float) Math.sqrt(currentSpeedSquare)));
            }
        }
    }

    @Override
    public Vector2 getPosition() {
        return body.getPosition();
    }


    @Override
    public float getOrientation() {
        return body.getAngle();
    }


    @Override
    public void setOrientation(float orientation) {
        //Doesnothing rn. TODO
    }


    @Override
    public float vectorToAngle(Vector2 vector) {
        return (float)Math.atan2(-vector.x, vector.y);
    }


    @Override
    public Vector2 angleToVector(Vector2 outVector, float angle) {
        outVector.x = -(float)Math.sin(angle);
        outVector.y = (float)Math.cos(angle);
        return outVector;
    }


    @Override
    public Location<Vector2> newLocation() {
        // TODO Auto-generated method stub
        return null;
    }


    @Override
    public float getZeroLinearSpeedThreshold() {
        return this.steeringTolerance;
    }


    @Override
    public void setZeroLinearSpeedThreshold(float value) {
        this.steeringTolerance = value;
    }


    @Override
    public float getMaxLinearSpeed() {
        return this.maxSpeed;
    }


    @Override
    public void setMaxLinearSpeed(float maxLinearSpeed) {
        this.maxSpeed = maxLinearSpeed;
        
    }


    @Override
    public float getMaxLinearAcceleration() {
        return this.maxAcceleration;
    }


    @Override
    public void setMaxLinearAcceleration(float maxLinearAcceleration) {
        this.maxAcceleration = maxLinearAcceleration;   
    }


    @Override
    public float getMaxAngularSpeed() {
        return this.maxAngularVelocity;
    }


    @Override
    public void setMaxAngularSpeed(float maxAngularSpeed) {
        this.maxAngularVelocity = maxAngularSpeed;
    }


    @Override
    public float getMaxAngularAcceleration() {
        return this.maxAngularAcceleration;
    }


    @Override
    public void setMaxAngularAcceleration(float maxAngularAcceleration) {
        this.maxAngularAcceleration = maxAngularAcceleration; 
    }


    @Override
    public Vector2 getLinearVelocity() {
        return this.body.getLinearVelocity();
    }


    @Override
    public float getAngularVelocity() {
        return this.body.getAngularVelocity();
    }


    @Override
    public float getBoundingRadius() {
        return this.boundingRadius;
    }


    @Override
    public boolean isTagged() {
        return this.isTagged;
    }


    @Override
    public void setTagged(boolean tagged) {
        this.isTagged = tagged;
    }
    
    
}