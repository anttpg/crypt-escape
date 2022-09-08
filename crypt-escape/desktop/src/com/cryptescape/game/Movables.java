/**
 * 
 */
package com.cryptescape.game;

/**
 * @author
 *
 */
public class Movables {

	// VARIABLES
	public double X;
	public double Y;
	public double[] pos = new double[2];
	public double velocityX = 0;
	public double velocityY = 0;
	public double[] velocity = new double[2];;
	public double acceleration = 0;
	public final double speed;
	public final double maxVelocity;

	// CONSTRUCTORS
	public Movables(double x, double y, double s, double mv) {
		X = x;
		Y = y;
		pos[0] = x;
		pos[1] = y;
		speed = s;
		maxVelocity = mv;
	}

	// METHODS
	public void move(double x, double y) {
		X += x;
		Y += y;
	}

	public void visible(boolean b) {

	}

	public void setXY(double x, double y) {
		X = x;
		Y = y;
	}

	public void setAcceleration(double a) {
		acceleration = a;
	}

	public double getX() {
		return X;
	}

	public double getY() {
		return Y;
	}

	public double[] getPos() {
		return pos;
	}

	public void updateTick() {
		velocityX += acceleration;
		velocityY += acceleration;
	}
}
