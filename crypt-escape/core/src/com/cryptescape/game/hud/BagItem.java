package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.CustomFixtureData;
import com.cryptescape.game.GameScreen;

public class BagItem extends InventoryItem{
    
    public BagItem(World world, String name, float x, float y, int zindex) {
        super(world, name, name, x, y, 8f, zindex, 0.05f);
        
        Vector2[] vertices = new Vector2[] {new Vector2(0.23f,0.5f), new Vector2(0.18f,0.05f), new Vector2(0.88f,0.05f), new Vector2(0.92f,0.5f)};
        super.makeChainFixture(world, x, y, 1f, vertices);
        super.createInteractionSquare(x, y, 0.9f, 0.9f);
        super.createUserData(false); //Sets movable to false, as well as creating the default user data
    }
    
    
    public ArrayList<Actor> checkIfOverlap() {
        ArrayList<Actor> safe = new ArrayList<Actor>();;
        
        for(Actor a : Inventory.getItemGroup().getChildren()) {
            if(super.getInteractionBody().testPoint(a.getX() + a.getWidth()/2f, a.getY() + a.getHeight()/2f))  {
                safe.add(a);    
            }
            else if(((InventoryItem)a).getName().substring(0, 3).equals("box")) 
                safe.add(a);
        }
        
        return safe;
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
