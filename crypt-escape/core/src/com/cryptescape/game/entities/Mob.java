package com.cryptescape.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.Texture;

public class Mob extends Movables{
	public static Player p;
	
    public Mob(double x,double y){
        super(null, null, null, null, null);
    }
    
    public void setPlayer(Player x) {
    	p = x;
    }

    @Override
    void draw(SpriteBatch batch) {
        // TODO Auto-generated method stub
        
    }
}
