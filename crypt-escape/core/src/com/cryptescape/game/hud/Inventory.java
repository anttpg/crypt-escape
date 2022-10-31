package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class Inventory {
    private Stage stage;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    
    public static float tileSize;
    public static float oldWidth;
    public static float oldHeight;
    
    private Texture overlay = new Texture(Gdx.files.internal("TestOverlay.png"));
    private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();

    public Inventory(Stage stage) {
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        tileSize = Constants.Y_TILES/(float)stage.getHeight();
        this.stage = stage;
        
        items.add(new CandleItem(world, "candle", 5f, 5f));
    }
    
    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }
    
    public World getWorld() {
        return world;
    }
    
    public void update() {
        world.step(1/60f, 6, 2);
    }
    
    public void resize(int width, int height) {
        tileSize = Constants.Y_TILES/(float)height; 
        for(InventoryItem i : items) {
            i.resize(width, height);
        }
        
        oldWidth = width;
        oldHeight = height;
     }
    
    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 1f);
        batch.begin();
        batch.draw(overlay, 0, 0, 100f, 100f);
        batch.end();
        
       
    }
}
