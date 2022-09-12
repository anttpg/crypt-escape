package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Movables {
	public boolean isRunning = false;
	private TextureRegion spriteRegion;

	public Player(double x, double y, Texture t) {
		super(x, y, 1.0, 5.0, t);
		spriteRegion = new TextureRegion(spriteMap);
	}

	public TextureRegion spriteStage() {
		if (spritePos < width) {
			spritePos += 1;
		} else {
			spritePos = 0;
		}

		if ((vel[0] > 0) && (vel[1] == 0)) { // East
			spriteRegion.setRegion(0 + 32 * spritePos, 0, 32, 32);
			return spriteRegion;
		} else if ((vel[0] < 0) && (vel[1] == 0)) { // West
			spriteRegion.setRegion(0 + 32 * spritePos, 0, 32, 32);
			return spriteRegion;
		} else if ((vel[0] == 0) && (vel[1] > 0)) { // North
			spriteRegion.setRegion(0 + 32 * spritePos, 32, 32, 32);
			return spriteRegion;
		} else if ((vel[0] == 0) && (vel[1] < 0)) { // South
			spriteRegion.setRegion(0 + 32 * spritePos, 64, 32, 32);
			return spriteRegion;
		} else if ((vel[0] > 0) && (vel[1] > 0)) { // Northwest
			spriteRegion.setRegion(0 + 32 * spritePos, 96, 32, 32);
			return spriteRegion;
		} else if ((vel[0] > 0) && (vel[1] < 0)) { // Southwest
			spriteRegion.setRegion(0 + 32 * spritePos, 128, 32, 32);
			return spriteRegion;
		} else if ((vel[0] < 0) && (vel[1] > 0)) { // Northeast
			spriteRegion.setRegion(0 + 32 * spritePos, 96, 32, 32);
			return spriteRegion;
		} else if ((vel[0] < 0) && (vel[1] < 0)) { // Southeast
			spriteRegion.setRegion(0 + 32 * spritePos, 128, 32, 32);
			return spriteRegion;
		} else if ((vel[0] == 0) && (vel[1] == 0)) { // Standing still 
			// dosomething
		}

		// else return Error Section
		spriteRegion.setRegion(0 + 32 * spritePos, 160, 32, 32);
		return spriteRegion;
	}
}
