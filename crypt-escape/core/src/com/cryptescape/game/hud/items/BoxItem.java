package com.cryptescape.game.hud.items;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.hud.Inventory;
import com.cryptescape.game.rooms.Box;

public class BoxItem extends InventoryItem {
    private boolean open = false; 
    private Box box;
    private boolean runonce = true;
    
    public BoxItem(World world, Box box, float x, float y, int zindex) {
        super(world, "boxOpening"+box.getSkin(), "boxOpening"+box.getSkin(), x, y, 9f, zindex, 0.2f); 
        this.box = box;
        super.isDroppable = false;
        Vector2[] vertices = new Vector2[] {new Vector2(0.2f,0.79f), new Vector2(0.59f,0.79f), new Vector2(0.65f,0.2f)};
        
        super.makeChainFixture(world, x, y, 1f, vertices);
        super.createInteractionRadius(x, y, 1.2f);
        super.createUserData(false);
        
        super.toBack();
        
    }
    
    @Override
    public void act(float delta) {
        super.defaultAct(delta);
        super.setInRange();
        
        if(startAnimation && !open) {
            if(runonce) {
                runonce = false;
                Inventory.queueForDisposal(this);
                
            }
            currentRegion = animation.getKeyFrame(time);
            
            if(animation.getAnimationDuration() < time) {
                startAnimation = false; 
                open = true;
                if(box.getStoredItem() != null)
                    Inventory.addItemFromBox(box.getStoredItem(), this);
                box.emptyStoredItem();
            }
        }
        else if(open) 
            currentRegion = animation.getKeyFrame(animation.getAnimationDuration());
        else 
            currentRegion = animation.getKeyFrame(0);
        
        
    }
}
