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
    private Table table;
 
    private ExtendViewport hud;
    private ArrayList<HudElement> overlayElements = new ArrayList<HudElement>();
    private HudElement candle;
    private CandleFlame flame;
   
    private static Label timer;
    private Label timeLabel;
        
    private float BURN_SPEED = ((GameScreen.player.getMaxCandleLevel() / GameScreen.player.getBurnPerTick()) * Constants.FRAME_SPEED) -
    		(1/Constants.FRAME_SPEED * GameScreen.player.getMaxCandleLevel() * GameScreen.player.getBurnPerTick());
    private float burntime = BURN_SPEED;
    
    public PlayerHud(SpriteBatch spriteBatch) {
        overlayStage = new Stage(new ExtendViewport(GameScreen.stage.getWidth(), GameScreen.stage.getHeight()), spriteBatch); 
        inventoryStage = new Stage(new ExtendViewport(GameScreen.stage.getWidth(), GameScreen.stage.getHeight()), spriteBatch); 
        
        candle = new HudElement(new Animation<TextureRegion>(1, GameScreen.atlas.findRegions("candle")));
        candle.setDuration(BURN_SPEED);
        overlayElements.add(candle);
        
        flame = new CandleFlame(new Animation<TextureRegion>(Constants.FRAME_SPEED*8, GameScreen.atlas.findRegions("candleFlame")));
        overlayElements.add(flame);
        
        
        table = new Table();
        table.top();
        table.setFillParent(true);
        
        timer = new Label(String.format("%06d", (int)BURN_SPEED), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME REMAINING: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        table.add(timeLabel).expandX().padTop(20);
        table.row();
        table.add(timer).expandX();
        
        
        
        overlayStage.addActor(table);
        overlayStage.addActor(candle);
        overlayStage.addActor(flame);
    }
    
    public void resize(int width, int height) {
        if(InputHandler.tab_pressed) {
            //DOSOMETHING
        }
        
        else {
            overlayStage.getViewport().update(width, height, true);
            for(HudElement e : overlayElements) 
                e.resize(width, height);
        }
       
       System.out.println("Hud camera: " + overlayStage.getCamera().viewportWidth + "  " + overlayStage.getCamera().viewportHeight);
       System.out.println("Hud stages: " + overlayStage.getWidth() + "  " + overlayStage.getHeight());
       
    }

    public Stage getStage() { 
        if(InputHandler.tab_pressed) 
            return inventoryStage;
        
        else 
            return overlayStage; 
    }
    
 

    public void dispose() {
        overlayStage.dispose();
    }
    
    
    public void update() {
        if(InputHandler.tab_pressed) {
            //Dosomething
        }
        
        else {
            flame.updateFlame(candle);
            burntime -= Gdx.graphics.getDeltaTime();
            timer.setText(String.format("%06d", (int)burntime));
        }
    }
    
}
