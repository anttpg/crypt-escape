package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Enemy extends Movables {
	public boolean hasSwitched = false;
	private AnimationHandler animate;
	private TextureRegion frame;
	private float elapsedTime = 1f;
	int[][] previousRoom;


	public Enemy(float x, float y, AnimationHandler a, Rectangle r) {
		super(x, y, 3.5f, r);
		animate = a;
	}
	
	//this will be the base of the AI
	public void decideDirection() {
		if (sniff() > 1 || hasSwitched) {
			this.setAcceleration(-0.1f, 0.0f);
			hasSwitched = true;
			
			if(sniff() < 0.2) {
				hasSwitched = false;
			}
		}
		else {
			this.setAcceleration(0.1f, 0.0f);
		}
		
		//this sort of stuff. Want to make a method that somehow
		//decides the direction of the enemy to go in.
	}
	
	//Add more methods to help build complex behavior from simple actions IE:
	//Return should always be from 0.0 -> 1.0, to determine hierarchy of what to do
	public float sniff() {
		float temp1 = (this.pos[0] / 1000);
		return temp1;
	}
	
	
	@Override
	public void draw(SpriteBatch batch) {
		this.updateTick();
		//Gdx.graphics.getDeltaTime();
		if (elapsedTime > 0.3) {
			elapsedTime = 0;
			animate.setAnimationDuration(1000);
			
			
			if ((vel[0] >= 1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // East
				animate.setCurrent("enemyE");
				
			} else if ((vel[0] <= -1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // West
				animate.setCurrent("enemyW");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] >= 1)) { // North
				animate.setCurrent("error");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] <= -1)) { // South
				animate.setCurrent("error");
				
			} else if ((vel[0] > 0) && (vel[1] > 0)) { // Northeast
				animate.setCurrent("error");
				
			} else if ((vel[0] > 0) && (vel[1] < 0)) { // Southeast
				animate.setCurrent("error");
				
			} else if ((vel[0] < 0) && (vel[1] > 0)) { // Northwest
				animate.setCurrent("error");
				
			} else if ((vel[0] < 0) && (vel[1] < 0)) { // Southwest
				animate.setCurrent("error");
				
			} else if ((vel[0] == 0) && (vel[1] == 0)) { // Standing still 
				animate.setCurrent("error");
				animate.setAnimationDuration(10000);
			}
			
		}		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		frame = animate.getFrame();
		batch.draw(frame, pos[0], pos[1], 128, 128);
	}
}
