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
		super(x, y, 1.0f, 3.0f);
		animate = h;
	}
	
	@Override
	public void draw(SpriteBatch batch) {
		//Gdx.graphics.getDeltaTime();
		if (elapsedTime > 0.3) {
			elapsedTime = 0;
			animate.setAnimationDuration(1000);
			
			
			if (!animate.isCurrent("playerE") && (vel[0] >= 1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // East
				animate.setCurrent("playerE");
				
			} else if (!animate.isCurrent("playerW") && (vel[0] <= -1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // West
				animate.setCurrent("playerW");
				
			} else if (!animate.isCurrent("playerN") && ((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] >= 1)) { // North
				animate.setCurrent("playerN");
				
			} else if (!animate.isCurrent("playerS") && ((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] <= -1)) { // South
				animate.setCurrent("playerS");
				
			} else if (!animate.isCurrent("playerNE") && (vel[0] > 0) && (vel[1] > 0)) { // Northeast
				animate.setCurrent("playerNE");
				
			} else if (!animate.isCurrent("playerSE") && (vel[0] > 0) && (vel[1] < 0)) { // Southeast
				animate.setCurrent("playerSE");
				
			} else if (!animate.isCurrent("playerNW") && (vel[0] < 0) && (vel[1] > 0)) { // Northwest
				animate.setCurrent("playerNW");
				
			} else if (!animate.isCurrent("playerSW") && (vel[0] < 0) && (vel[1] < 0)) { // Southwest
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
