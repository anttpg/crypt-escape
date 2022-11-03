package com.cryptescape.game.hud;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cryptescape.game.GameScreen;

public class BagItem extends InventoryItem{
    
    public BagItem(World world, String name, float x, float y, int zindex) {
        super(world, name, name, x, y, 8f, zindex);
        
        Vector2[] vertices = new Vector2[] {new Vector2(0.23f,0.5f), new Vector2(0.18f,0.05f), new Vector2(0.88f,0.05f), new Vector2(0.92f,0.5f)};
        super.makeChainFixture(world, x, y, 1f, vertices);
        super.createInteractionRadius(x, y-0.2f, 1.2f);
        super.createUserData(false); //Sets movable to false, as well as creating the default user data
    }
    
    
    @Override
    public void act(float delta){
        super.defaultAct(delta);
        super.setInRange(); //Only will check for the ones you tell it to.
        
        
        if(startAnimation && mouseInRange) {
        	currentRegion = animation.getKeyFrame(time);
        	if(time > animation.getAnimationDuration() - animation.getFrameDuration())
        		super.toBack();
        }
        
        else if(startAnimation) {
        	currentRegion = animation.getKeyFrame(countdown);
        	countdown -= delta;
        	
        	if(countdown < animation.getFrameDuration())
            	super.toFront();
        	
        	if(countdown < 0) {
        		countdown = animation.getAnimationDuration();
        		startAnimation = false;
        	}
        }
        
    }
    
}
