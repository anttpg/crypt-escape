package com.cryptescape.game;

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

public class PlayerHud {
    private Stage stage;
    private Table table;
 
    private ExtendViewport hud;
    private ArrayList<HudElement> elements = new ArrayList<HudElement>();
    private HudElement candle;
    private HudElement flame;
   
    private static Label timer;
    private Label timeLabel;
        
    private float BURN_SPEED = ((GameScreen.player.getMaxCandleLevel() / GameScreen.player.getBurnPerTick()) * Constants.FRAME_SPEED) -
    		(1/Constants.FRAME_SPEED * GameScreen.player.getMaxCandleLevel() * GameScreen.player.getBurnPerTick());
    private float burntime = BURN_SPEED;
    
    public PlayerHud(SpriteBatch spriteBatch) {
        stage = new Stage(new ExtendViewport(GameScreen.stage.getWidth(), GameScreen.stage.getHeight()), spriteBatch); 
        
        
        candle = new HudElement(new Animation<TextureRegion>(1, GameScreen.atlas.findRegions("candle")));
        candle.setDuration(BURN_SPEED);
        elements.add(candle);
        
        flame = new HudElement(new Animation<TextureRegion>(Constants.FRAME_SPEED*8, GameScreen.atlas.findRegions("candleFlame")));
        elements.add(flame);
        
        
        table = new Table();
        table.top();
        table.setFillParent(true);
        
        timer = new Label(String.format("%06d", (int)BURN_SPEED), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME REMAINING: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        table.add(timeLabel).expandX().padTop(20);
        table.row();
        table.add(timer).expandX();
        
        
        
        stage.addActor(table);
        stage.addActor(candle);
        stage.addActor(flame);
    }
    
    public void resize(int width, int height) {
       stage.getViewport().update(width, height, true);
       
       System.out.println("Hud camera: " + stage.getCamera().viewportWidth + "  " + stage.getCamera().viewportHeight);
       System.out.println("Hud stages: " + stage.getWidth() + "  " + stage.getHeight());
       
       for(HudElement e : elements) {
           e.resize(width, height);
       }
    }

    public Stage getStage() { 
    	return stage; 
    }
 

    public void dispose() {
        stage.dispose();
    }
    
    
    public void update() {
    	flame.updateFlame(candle);
    	
    	burntime -= Gdx.graphics.getDeltaTime();
        timer.setText(String.format("%06d", (int)burntime));
    }
    
}
