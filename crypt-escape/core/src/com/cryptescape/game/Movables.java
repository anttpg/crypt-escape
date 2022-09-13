package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * @author
 *
 */
public class Movables {

	// VARIABLES

	public boolean visible;
	public int[] room = new int[2]; // [roomX,roomY]
	public double[] pos = new double[2]; // [x, y]
	public double[] vel = new double[2]; // [xV, yV]
	public double[] acc = new double[2]; // [xA, yA]
	public double[] velBACKUP = new double[2]; //wtf java
	public final double speed;
	public final double maxVel;
	
	public int spritePos = -1;
	public Texture spriteMap;
	public TextureRegion spriteRegion;
	int height;
	int width;
	
	//Jolt is the deacceleration variable
	//Normally Jolt = [0, 0, 0, 0], but when A[x] or A[y] == 0,
	// EX:     Jolt = [-0.05, 30, 0, 0] and counts down
	public double[] jolt = new double[] {0,0,0,0}; // [xD, xT, yD, yT]

	// CONSTRUCTORS
	public Movables(double x, double y, double s, double mv, Texture mapPath) {
		pos[0] = x;
		pos[1] = y;
		speed = s;
		maxVel = mv;
		spriteMap = mapPath;
		spriteRegion = new TextureRegion(spriteMap, 0,0,32,32);
		height = spriteMap.getHeight()/32;
		width = spriteMap.getWidth()/32;
	}
	
	private boolean sameSign(double num1, double num2)
	{
	    if (num1 > 0 && num2 < 0)
	        return false;
	    if (num1 < 0 && num2 > 0)
	        return false;
	    return true;
	}

	public void setAccel(double x, double y) {
		acc[0] = x;
		acc[1] = y;
		//Checks if the xAccel is zero, AND the velocity is not zero, 
		//AND ((Jolt xT is zero) OR (Jolt xV and xAccel, have different signs))
		if (x == 0 && ((double) Math.round(vel[0] * 100) / 100) != 0 && (jolt[1] == 0 || !sameSign(jolt[0], x) )){
			jolt[1] = 23;
			jolt[0] = -(vel[0]/23);	
			//PROBLEM IS HERE, WITH JOLT[1] == 0. ROUNDOFF ERROR WHEN ADDING TO V
		}
		if (y == 0 && ((double) Math.round(vel[1] * 100) / 100) != 0 && (jolt[3] == 0 || !sameSign(jolt[1], y) )){
			jolt[3] = 23;
			jolt[2] = -(vel[1]/23);
		}
	}
		

	public void updateTick() {
		if(-maxVel < vel[0] && vel[0] < maxVel) vel[0] += acc[0];
		if(-maxVel < vel[1] && vel[1] < maxVel) vel[1] += acc[1];
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
		
		pos[0]+= vel[0]*speed;
		pos[1]+= vel[1]*speed;
//		if(-0.000005 < jolt[0] && jolt[0] > 0.000005) {
//			jolt[1] = 0;
//		}
//		if(-0.000005 < jolt[2] && jolt[2] > 0.000005) {
//			jolt[3] = 0;
//		}
//		
	}
	
	
	// OTHER METHODS
	public void move(double x, double y) {
		pos[0] += x;
		pos[1] += y;
	}

	public void setVisible(boolean b) {
		visible = b;
	}
	
	public boolean getVisible() {
		return visible;
	}
	public void setPos(double x, double y) {
		pos[0] = x;
		pos[1] = y;
	}
	public double getX() {
		return pos[0];
	}

	public double getY() {
		return pos[1];
	}

	public double[] getPos() {
		return pos;
	}
	
	public int getMapHeight() {
		return width;
	}
	

}