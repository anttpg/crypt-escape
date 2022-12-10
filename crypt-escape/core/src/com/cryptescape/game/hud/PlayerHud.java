package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.InputHandler;

public class PlayerHud {
    private Stage overlayStage;
    private Stage inventoryStage;
    private Inventory inventory;
    private Overlay overlay;
    

    
    public PlayerHud(SpriteBatch spriteBatch) {
        overlayStage = new Stage(new ExtendViewport(GameScreen.stage.getWidth(), GameScreen.stage.getHeight()), spriteBatch); 
        inventoryStage = new Stage(new ExtendViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT), spriteBatch); 
        inventory = new Inventory(inventoryStage);
        overlay = new Overlay(overlayStage);
    }
    
    public void resize(int width, int height) {
        inventory.resize(width, height);
        overlay.resize(width, height);
    }

    
    public Inventory getInventory() {
        return inventory;
    }
    
    public Stage getInventoryStage() {
        return inventoryStage;
    }
    
    public void dispose() {
        overlayStage.dispose();
        inventoryStage.dispose();
    }
    
    
    public void update(float delta, SpriteBatch batch) {
        if(InputHandler.tab_pressed) {
            batch.setProjectionMatrix(inventoryStage.getCamera().combined);
            inventory.update(); 
            inventory.render(batch); //Render overlay dark first
            
            inventoryStage.act(delta);
            inventoryStage.draw();
            
            if(GameScreen.debugPerspective)
                inventory.getDebugRenderer().render(Inventory.getWorld(), inventoryStage.getCamera().combined);
        }
        
        else {
            Inventory.disposeUnusedItems();
            
            batch.setProjectionMatrix(overlayStage.getCamera().combined);
            overlay.update();
            
            overlayStage.act(delta);
            overlayStage.draw();
        }
    }
    
}
