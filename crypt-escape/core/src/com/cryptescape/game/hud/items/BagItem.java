package com.cryptescape.game.hud.items;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.hud.CustomFixtureData;
import com.cryptescape.game.hud.Inventory;
import com.cryptescape.game.hud.InventoryItem;

public class BagItem extends InventoryItem{
    private ArrayList<Actor> hiddenItems;
    private boolean makeVisible = false;
    
    
    public BagItem(World world, String name, float x, float y, int zindex, float scale) {
        super(world, name, name, x, y, scale, zindex, 0.05f);
        
        Vector2[] vertices = new Vector2[] {new Vector2(0.23f,0.5f), new Vector2(0.18f,0.05f), new Vector2(0.88f,0.05f), new Vector2(0.92f,0.5f)};
        super.makeChainFixture(world, x, y, 1f, vertices);
        super.createInteractionSquare(0 + scale/100f, 0 - scale/50f, scale/11.5f, scale/11f);
        super.createUserData(false); //Sets movable to false, as well as creating the default user data
    }
    
    
    public ArrayList<Actor> checkIfOverlap() {
        ArrayList<Actor> safe = new ArrayList<Actor>();     
        for(Actor item : Inventory.getItemGroup().getChildren()) {
            if(super.getInteractionBody().testPoint(item.getX() + item.getWidth()/2f, item.getY() + item.getHeight()/2f))  {
                safe.add(item);    
            }
        }
        
        return safe;
    }
    
    
    @Override
    public void act(float delta){
        super.defaultAct(delta);
        super.setInRange(); //Only will check for the ones you tell it to.

        //Checks if mouse hovering over
        if(startAnimation && mouseInRange) {
        	currentRegion = animation.getKeyFrame(time);
        	if(time > animation.getAnimationDuration() - animation.getFrameDuration()) {
        		super.toBack();
            	
        		if(makeVisible) {
            		//Reduce burden, and prevent items peeking out over :)
                    for(Actor hidden : hiddenItems) {
                        if(hidden.getName() == "briefcase") continue;
                        hidden.setVisible(true);
                    }
            		makeVisible = false;
        		}
        	}
        }
        
        //Else close bag
        else if(startAnimation) {
        	currentRegion = animation.getKeyFrame(countdown);
        	countdown -= delta;
        	
        	if(countdown < animation.getFrameDuration()) {
            	super.toFront();
            	
            	if(!makeVisible) {
            	    
                	//Reduce burden, and prevent items peeking out over :)
                	hiddenItems = checkIfOverlap();
                	for(Actor item : hiddenItems) {
                        if(item.getName() == "briefcase") continue;
                        if(Inventory.currentlyDragging != null && ((InventoryItem)item).checkIfSameFixture(Inventory.currentlyDragging)) continue;
                    	item.setVisible(false);
                	}
                	makeVisible = true;
            	}
        	}
        	
        	if(countdown < 0) {
        		countdown = animation.getAnimationDuration();
        		startAnimation = false;
        	}
        }
        
    }
    
}
