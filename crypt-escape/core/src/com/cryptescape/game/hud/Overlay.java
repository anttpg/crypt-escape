package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.entities.StatusManager;

public class Overlay {
    private Table debugTable;
    
    private ArrayList<HudElement> overlayElements = new ArrayList<HudElement>();
    private HudElement candle;
    private CandleFlame flame;
 
    private Stage overlayStage;
    
    private Label timer;
    private Label health;
    private Label fps;

    
    public Overlay(Stage overlayStage) {
        candle = new HudElement(new Animation<TextureRegion>(1, GameScreen.atlas.findRegions("candle")));
        overlayElements.add(candle);
        
        flame = new CandleFlame(new Animation<TextureRegion>(Constants.FRAME_SPEED*8, GameScreen.atlas.findRegions("candleFlame")));
        overlayElements.add(flame);
        
        
        debugTable = new Table();
        debugTable.right().top();
        debugTable.setFillParent(true);
        
        Label.LabelStyle style = new Label.LabelStyle(new BitmapFont(), Color.WHITE);
        float fontScale = 1;
        
        timer = new Label(String.format("%06d", (int)flame.burntime), style);
        health = new Label(String.format("%.2f", StatusManager.health), style);
        fps = new Label(String.format("%03d", (int)Gdx.graphics.getFramesPerSecond()), style);
        
//        timer.setFontScale(fontScale);
//        health.setFontScale(fontScale);
//        fps.setFontScale(fontScale);

        debugTable.add(new Label("FPS: ", style)).right().padTop(10);
        debugTable.add(fps).padTop(10);
        debugTable.row();
        
        debugTable.add(new Label("Candle: ", style)).right().padTop(10);
        debugTable.add(timer).padTop(10);
        debugTable.row();
        
        debugTable.add(new Label("Health: ", style)).right().padTop(10);
        debugTable.add(health).padTop(10);
        debugTable.row();
        
        
        overlayStage.addActor(debugTable);
        overlayStage.addActor(candle);
        overlayStage.addActor(flame);
        this.overlayStage = overlayStage;
    }


    public void resize(int width, int height) {
        overlayStage.getViewport().update(width, height, true);
        for (HudElement e : overlayElements)
            e.resize(width, height);
    }


    public void update() {
        flame.updateFlame(candle);
        
        timer.setText (String.format("%06d", (int)flame.burntime));
        health.setText(String.format("%.2f", StatusManager.health));
        fps.setText(Gdx.graphics.getFramesPerSecond());
        
        if(GameScreen.debugPerspective) {
            debugTable.setDebug(true);
        }
        else {
            debugTable.setDebug(false);
        }
    }
}
