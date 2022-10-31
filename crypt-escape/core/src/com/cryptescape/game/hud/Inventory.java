package com.cryptescape.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Inventory {
    private Stage stage;
    OrthographicCamera camera;
    ExtendViewport inv;
    
    int HEIGHT = Gdx.graphics.getHeight(); //Used to keep everything on the right scale
    int WIDTH = Gdx.graphics.getWidth();
    
    public Inventory(SpriteBatch batch) {
        camera = new OrthographicCamera();
        camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
        inv = new ExtendViewport(WIDTH, HEIGHT, camera);
    
        stage = new Stage(inv, batch); 
        camera.update();
    }
    
    
    
    public void resize(int width, int height) {
        inv.update(width, height);
     }
}
