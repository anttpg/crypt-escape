package com.cryptescape.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Movables {

	// VARIABLES

	public boolean visible;
	public int[] room = new int[2]; // [roomX,roomY]
	public float[] pos = new float[2]; // [x, y]
	public float[] vel = new float[2]; // [xV, yV]
	public float[] acc = new float[2]; // [xA, yA]
	public final float maxVel; // pixels/tick
	private Rectangle collisonBox;

	//Jolt is the deceleration variable
	//Normally Jolt = [0, 0, 0, 0], but when A[x] or A[y] == 0,
	// EX:     Jolt = [-0.05, 30, 0, 0] and counts down
	public float[] jolt = new float[] {0,0,0,0}; // [xD, xT, yD, yT]

	// CONSTRUCTORS
	public Movables(float x, float y, float mv, Rectangle r) {
		pos[0] = x;
		pos[1] = y;
		maxVel = mv;
		collisonBox = r;
	}
		
	abstract void draw(SpriteBatch batch);
	
	public void setAcceleration(float x, float y) {
		acc[0] = x;
		acc[1] = y;
		//Checks if the xAccel is zero, AND the velocity is not zero, 
		//AND ((Jolt xT is zero) OR (Jolt xV and xAccel, have different signs))
		if (x == 0 && ((float) Math.round(vel[0] * 100) / 100) != 0 && (jolt[1] == 0 || !sameSign(jolt[0], x) )){
			jolt[1] = 23;                   //wtf java
			jolt[0] = -(vel[0]/23);	
			//PROBLEM IS HERE, WITH JOLT[1] == 0. ROUNDOFF ERROR WHEN ADDING TO V
		} 
		else if (x != 0) { 
			jolt[0] = 0;
			jolt[1] = 0;
		}
		
		if (y == 0 && ((float) Math.round(vel[1] * 100) / 100) != 0 && (jolt[3] == 0 || !sameSign(jolt[1], y) )){
			jolt[3] = 23;
			jolt[2] = -(vel[1]/23);
		}
		else if (y != 0) { 
			jolt[2] = 0;
			jolt[3] = 0;
		}
	}
		

	public void updateTick() {
		if(-maxVel < vel[0]+acc[0] && vel[0]+acc[0] < maxVel) vel[0] += acc[0];
		if(-maxVel < vel[1]+acc[1] && vel[1]+acc[1] < maxVel) vel[1] += acc[1];
		vel[0] = vel[0] + jolt[0];
		vel[1] = vel[1] + jolt[2];
		
		if(jolt[0] != 0 && jolt[1] == 0) { //if and timer 0, clear jolt
			jolt[0] = 0;
			vel[0] = 0;
		} else if(jolt[0] != 0) { 
			jolt[1] -= 1; 
		}
			
		if(jolt[2] != 0 && jolt[3] == 0) {	
			jolt[2] = 0;
			vel[1] = 0;
		} else if(jolt[2] != 0) { 
			jolt[3] -= 1;
		}
		
		pos[0]+= vel[0];
		pos[1]+= vel[1];	
	}
	
	private boolean sameSign(float num1, float num2) {
	    if (num1 > 0 && num2 < 0)
	        return false;
	    if (num1 < 0 && num2 > 0)
	        return false;
	    return true;
	}
	
	//Get methods 
	public float[] getPos() {
		return pos;
	}
}