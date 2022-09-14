package com.cryptescape.game;

import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Enemy extends Movables{

    int[][] previousRoom;
    public Enemy(double x,double y){
        //super(x, y, 6.0, 24.0, "monster2.png");
    }

    public TextureRegion spriteStage() {
    	if (spritePos < width) {
			spritePos +=1;
		} else {
			spritePos = 0;
		}
		if ((vel[0] > 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,0,32,32);
			return spriteRegion;
		}
		else if ((vel[0] < 0)){
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,0,32,32);
			return spriteRegion;
		}
		else {
			spriteRegion = new TextureRegion(spriteMap, 0+32*spritePos,32,32,32);
			return spriteRegion;
		}
		
	}
}
