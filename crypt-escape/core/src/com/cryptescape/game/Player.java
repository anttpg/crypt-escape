package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Player extends Movables{
    public boolean isRunning = false;

    public Player(double x,double y, Texture t){
        super(x, y, 1.0, 5.0, t);
    }
    public TextureRegion spriteStage() {
    	if (spritePos < width) {
			spritePos +=1;
		} else {
			spritePos = 0;
		}
		if ((vel[0] > 0) && (vel[1] == 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,0,32,32);
			return spriteRegion;
		}
		else if ((vel[0] < 0) && (vel[1] == 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,0,32,32);
			return spriteRegion;
		}
		else if ((vel[0] == 0) && (vel[1] > 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,32,32,32);
			return spriteRegion;
		}
		else if ((vel[0] == 0) && (vel[1] < 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,64,32,32);
			return spriteRegion;
		}
		else if ((vel[0] > 0) && (vel[1] > 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,96,32,32);
			return spriteRegion;
		}
		else if ((vel[0] > 0) && (vel[1] > 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,96,32,32);
			return spriteRegion;
		}
		else if ((vel[0] < 0) && (vel[1] > 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,96,32,32);
			return spriteRegion;
		}
		else if ((vel[0] > 0) && (vel[1] < 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,128,32,32);
			return spriteRegion;
		}
		else if ((vel[0] < 0) && (vel[1] < 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,128,32,32);
			return spriteRegion;
		}
		else {
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,160,32,32);
			return spriteRegion;
		}
		
	}

}
