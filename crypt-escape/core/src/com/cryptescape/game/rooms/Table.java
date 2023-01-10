package com.cryptescape.game.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cryptescape.game.Constants;
import com.cryptescape.game.InputHandler;
import com.cryptescape.game.hud.Inventory;

public class Table extends Interactable {
    private String objectState = "idle";
    
    public Table(int c, int r, String name, Room p) {
        super(c, r, name, p, Constants.TILESIZE * 2, Constants.TILESIZE);
        super.findRandomSkin(2, name);
        
        super.createStaticBox(-3);
        super.createInteractionRadius(super.getWidth()*0.7f, super.getHeight()*0.7f);
        
    }
    
    public void draw(SpriteBatch batch) {
        this.update();
        super.draw(batch);
    }
    
    /*
     * Called every time draw is called
     */
    public void update() {
        if (objectState.equals("open")) {
            InputHandler.tab_pressed = true;
            objectState = "idle";
            Inventory.openTable(this);
        }
    }
    
    /*
     * Use to set changes to the state
     */
    public void setInteractionState(String phase) {
        this.objectState = phase;
    }

}
