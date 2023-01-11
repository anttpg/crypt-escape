package com.cryptescape.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Actor;


//THIS CLASS IS OUTDATED. REWROTE IT ALL WHEN REALIZED IT IS OBSOLETE.

/* A movables object is anything on the screen that should be able to move,
 * and intereact with other objects. Movables extends Image, and all movable
 * instances will be dynamic bodies
 * 
 * 
 */

public abstract class MovablesObsolete extends Actor{

	// VARIABLES
	
	//Jolt is the deceleration variable
	//Normally Jolt = [0, 0, 0, 0], but when A[x] or A[y] == 0,
	// EX:     Jolt = [-0.05, 30, 0, 0] and counts down
	public float[] jolt = new float[] {0,0,0,0}; // [xD, xT, yD, yT]


	
	public int[] room = new int[2]; // [roomX,roomY]
	public float xPos; // x
	public float yPos; // y
	public float xVel; // xY
	public float yVel; // yV
	public float xAcc; // xA
	public float yAcc; // yA
	public final float maxVel; // pixels/tick
	public float speed = 1; //changes when sprinting
	public float tc; //totalChange (used for scaling to new Box2D meters)
	
	public Body body;
	public Vector2 forceVector = new Vector2();
	public boolean visible;
	

	private float width; // Of the room
	private float height; // Of the room

	// CONSTRUCTORS
	public MovablesObsolete(float x, float y, float w, float h, float mv, float t) {
		xPos = x;
		yPos = y;
		maxVel = mv/t;
		width = w;
		height = h;
		tc = t;
	}
		
	abstract void draw(SpriteBatch batch);
	
	public void setVelocity(float xV, float yV) {
		xVel = xV;
		yVel = yV;
	}
	
	
	public void setAcceleration(float x, float y, float s) {
		xAcc = x*s;
		yAcc = y*s;
		
		forceVector.set(xAcc*4000, yAcc*4000); 
		speed = s;
		
		//Checks if the xAccel is zero, AND the velocity is not zero, 
		//AND ((Jolt xT is zero) OR (Jolt xV and xAccel, have different signs))
		if (x == 0 && ((float) Math.round(xVel * (100*tc)) / (100*tc)) != 0 && (jolt[1] == 0 || !sameSign(jolt[0], x) )){
			jolt[1] = 23;                   //wtf java
			jolt[0] = -(xVel/23);	
			//Solved issue
		} 
		else if (x != 0) { 
			jolt[0] = 0;
			jolt[1] = 0;
		}
		
		if (y == 0 && ((float) Math.round(yVel * (100*tc)) / (100*tc)) != 0 && (jolt[3] == 0 || !sameSign(jolt[1], y) )){
			jolt[3] = 23;
			jolt[2] = -(yVel/23);
		}
		else if (y != 0) { 
			jolt[2] = 0;
			jolt[3] = 0;
		}
	}
		

	public void updateTick() {
		if(-maxVel*speed < xVel+xAcc && xVel+xAcc < maxVel*speed) xVel += xAcc;
		if(-maxVel*speed < yVel+yAcc && yVel+yAcc < maxVel*speed) yVel += yAcc;
		if(!(-maxVel*speed < xVel && xVel < maxVel*speed) && jolt[1] == 0) xVel -= xVel/10; //Checks for wucky problem when switching sides
		if(!(-maxVel*speed < yVel && yVel < maxVel*speed) && jolt[3] == 0) yVel -= yVel/10; // If maxspeed eclipsed, and not deaccell do this
		
		xVel = xVel + jolt[0];
		yVel = yVel + jolt[2];
		
		if(jolt[0] != 0 && jolt[1] == 0) { //if and timer 0, clear jolt
			jolt[0] = 0;
			xVel = 0;
		} else if(jolt[0] != 0) { 
			jolt[1] -= 1; 
		}
			
		if(jolt[2] != 0 && jolt[3] == 0) {	
			jolt[2] = 0;
			yVel = 0;
		} else if(jolt[2] != 0) { 
			jolt[3] -= 1;
		}
		
		xPos+= xVel;
		yPos+= yVel;	
		super.setX(xPos);
		super.setY(yPos);
	}
	
	private boolean sameSign(float num1, float num2) {
	    if (num1 > 0 && num2 < 0)
	        return false;
	    if (num1 < 0 && num2 > 0)
	        return false;
	    return true;
	}
	
	public void debugMovable() {
		// debugging tools ->

	}
	
	public float getWidth() {
		return width;
	}
	
	public float getHeight() {
		return height;
	}
	
	public float getX() {
		return xPos;
	}
	
	public float getY() {
		return yPos;
	}
}