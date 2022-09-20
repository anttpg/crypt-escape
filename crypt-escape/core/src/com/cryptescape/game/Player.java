package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class Player extends Movables {
	public boolean isRunning = false;
	private TextureRegion frame;
	private AnimationHandler animate;
	private float elapsedTime = 1f;
	private boolean changeAnimation = true;
	
	public Player(float x, float y, AnimationHandler h) {
		super(x, y, 2.0f);
		animate = h;
	}
		
	@Override
	public void draw(SpriteBatch batch) {
		this.updateTick();
		//Gdx.graphics.getDeltaTime();
		if (elapsedTime > 0.3) {
			elapsedTime = 0;
			animate.setAnimationDuration(1000);
			
			
			if ((vel[0] >= 1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // East
				animate.setCurrent("playerE");
				
			} else if ((vel[0] <= -1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // West
				animate.setCurrent("playerW");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] >= 1)) { // North
				animate.setCurrent("playerN");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] <= -1)) { // South
				animate.setCurrent("playerS");
				
			} else if ((vel[0] > 0) && (vel[1] > 0)) { // Northeast
				animate.setCurrent("playerNE");
				
			} else if ((vel[0] > 0) && (vel[1] < 0)) { // Southeast
				animate.setCurrent("playerSE");
				
			} else if ((vel[0] < 0) && (vel[1] > 0)) { // Northwest
				animate.setCurrent("playerNW");
				
			} else if ((vel[0] < 0) && (vel[1] < 0)) { // Southwest
				animate.setCurrent("playerSW");
				
			} else if ((vel[0] == 0) && (vel[1] == 0)) { // Standing still 
				animate.setCurrent("playerS");
				animate.setAnimationDuration(10000);
			}
			
		}		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		frame = animate.getFrame();
		batch.draw(frame, pos[0], pos[1], 96, 96);
	}
}
