/**
 * 
 */
package com.cryptescape.game;

import com.badlogic.gdx.graphics.g2d.Sprite;
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
	public final double speed;
	public final double maxVel;
	
	//Jolt is deacceleration variable
	//Normally Jolt = [0, 0, 0, 0], but when A[x] or A[y] == 0,
	// EX:     Jolt = [-0.05, 30, 0, 0] and counts down
	public double[] jolt = new double[4]; // [xD, xT, yD, yT]

	// CONSTRUCTORS
	public Movables(double x, double y, double s, double mv) {
		pos[0] = x;
		pos[1] = y;
		speed = s;
		maxVel = mv;
	}

	// METHODS
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

	public void setAccel(double x, double y) {
		acc[0] = x;
		acc[1] = y;
		if (acc[0] == 0){
			jolt[1] = 60;
			jolt[0] = -(vel[0]/60);
			
		}
		if (acc[1] == 0){
			jolt[3] = 60;
			jolt[2] = -(vel[1]/60);
		}
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
		
	public void updateTick() {
		vel[0] += acc[0] + jolt [0];
		vel[1] += acc[1] + jolt [2];
		if(jolt[0] != 0 && jolt[1] == 0) {
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
}
