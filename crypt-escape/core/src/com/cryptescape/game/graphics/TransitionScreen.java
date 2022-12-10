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
    // true if fade in, false if fade out
    private static boolean fadeDirection = true;
    

    public static void render(SpriteBatch batch, Stage stage) {
        batch.setColor(1, 1, 1, alpha);
        batch.begin();
        batch.draw(overlay, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end(); 
        batch.setColor(1, 1, 1, 1);
    }
    
    
    public static void update() {
        if (alpha >= 1) {
            fadeDirection = false;
        } 
        
        if(fadeDirection) 
            alpha += 0.01f;
        
        else {
            alpha -= 0.01f;
            
            if(alpha <= 0) {
                fadeDirection = true;
                GameScreen.fade = false;
            }   
        }
    }
    
}