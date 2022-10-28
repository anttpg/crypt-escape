package com.cryptescape.game;

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
    
    private HudElement candle;
    private HudElement flame;
    
    
    private static Label timer;
    private Label timeLabel;
        
    private float BURN_SPEED = ((GameScreen.player.getMaxCandleLevel() / GameScreen.player.getBurnPerTick()) * Constants.FRAME_SPEED) -
    		(1/Constants.FRAME_SPEED * GameScreen.player.getMaxCandleLevel() * GameScreen.player.getBurnPerTick());
    private float burntime = BURN_SPEED;
    
    public PlayerHud(SpriteBatch spriteBatch) {
    	hud = new ExtendViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, new OrthographicCamera());
        stage = new Stage(hud, spriteBatch); 
        
        table = new Table();
        table.top();
        table.setFillParent(true);
        
        timer =new Label(String.format("%06d", (int)BURN_SPEED), new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        timeLabel = new Label("TIME REMAINING: ", new Label.LabelStyle(new BitmapFont(), Color.WHITE));
        
        candle = new HudElement(new Animation<TextureRegion>(1, GameScreen.atlas.findRegions("candle")));
        candle.setDuration(BURN_SPEED);
        flame = new HudElement(new Animation<TextureRegion>(Constants.FRAME_SPEED*8, GameScreen.atlas.findRegions("candleFlame")));
        
        
        table.add(timeLabel).expandX().padTop(20);
        table.row();
        table.add(timer).expandX();
        

        GameScreen.stage.addActor(table);
        GameScreen.stage.addActor(candle);
        GameScreen.stage.addActor(flame);
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
