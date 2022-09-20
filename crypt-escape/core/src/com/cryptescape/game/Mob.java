package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;

public class Mob extends Movables{
	public static Player p;
    public Mob(double x,double y){
        super(x, y, 2.0, 4.0, Texture t);
    }
    public void setPlayer(Player x) {
    	p = x;
    }
    if (this.distance(p, this)) {
    	
    }
}
