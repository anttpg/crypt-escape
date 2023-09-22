package com.cryptescape.game.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.cryptescape.game.GameScreen;

public class TransitionScreen {
    private static Texture overlay = new Texture(Gdx.files.internal("TestOverlay.png"));
    private Screen currentScreen;
    private Screen nextScreen;
    
    // Once this reaches 1.0f the next scene is shown
    private static float alpha = 0;
    private static float alphaChange = 0.01f;
    // Should be fading current
    public static boolean fadeOut = false;
    public static boolean fadeIn = false;
    

    public static void render(SpriteBatch batch, Stage stage) {
        if(alpha != 0) {
            batch.setColor(1, 1, 1, alpha);
            batch.begin();
            batch.draw(overlay, 0, 0, GameScreen.realWidth, GameScreen.realHeight);
            batch.end(); 
            batch.setColor(1, 1, 1, 1);
        }
    }
    
    
    public static void update() {
        if(fadeOut)
            fadeOut();
        if(fadeIn)
            fadeIn();
    }
    
    
    
    
    public static void fadeOut() {
        if (alpha >= (1-alphaChange)) { 
            fadeOut = false;
            return;
        }
        alpha += alphaChange;
    }
    
    public static void fadeIn() {
        if(fadeOut)
            fadeOut = false;
        
        if (alpha <= (0+alphaChange))  {
            fadeIn  = false;
            alpha = 0;
            return;
        }
        alpha -= alphaChange;
    }
    
}