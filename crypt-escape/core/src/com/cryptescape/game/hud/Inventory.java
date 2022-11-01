package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class Inventory {
    private static Stage stage;
    private static World world;
    private Box2DDebugRenderer debugRenderer;
    
    public static float tileSize;
    public static float oldWidth;
    public static float oldHeight;
    public static MouseJointDef mouseDef;
    public static MouseJoint mouse;
    
    private Texture overlay = new Texture(Gdx.files.internal("TestOverlay.png"));
    private Fixture boundary;
    private ArrayList<InventoryItem> items = new ArrayList<InventoryItem>();

    public Inventory(Stage stage) {
        world = new World(new Vector2(0, -4), true);
        debugRenderer = new Box2DDebugRenderer();
        tileSize = (float)stage.getHeight()/Constants.Y_TILES;
        Inventory.stage = stage;
        createBoundary();
        
        mouseDef = new MouseJointDef();
        mouseDef.bodyA = boundary.getBody();
        mouseDef.collideConnected = true;
        
        for(int row = 0; row < 20; row++)
        	for(int col = 0; col < 20; col++)
        		items.add(new CandleItem(world, "candlestick", 1f + row/4f, 1f + col/2f));
        
        for(InventoryItem i : items)
        	stage.addActor(i);
    }
    
    public Box2DDebugRenderer getDebugRenderer() {
        return debugRenderer;
    }
    
    public static Stage getStage() {
    	return stage;
    }
    
    public static World getWorld() {
        return world;
    }
    
    public void update() {
        world.step(1/60f, 6, 2);
    }
    
    public void resize(int width, int height) {
        System.out.println("Inv stage w/h" + " "+ stage.getWidth() + " " + stage.getHeight());
        
        
        tileSize = (float)stage.getHeight()/Constants.Y_TILES; 
        for(InventoryItem i : items) {
            i.resize(stage.getWidth(), stage.getHeight());
        }
        
        oldWidth = stage.getWidth();
        oldHeight = stage.getHeight();
        createBoundary(); //Remakes boundary for new size
    }
    
    public void createBoundary() {
        if(boundary != null) {
            boundary.getBody().destroyFixture(boundary);
        }
        
        //Creates the boundary
        BodyDef boundaryDef = new BodyDef(); 
        boundaryDef.type = BodyType.KinematicBody;
        boundaryDef.position.set(0, 0); // Set its position
        Body body = world.createBody(boundaryDef);
        
        EdgeShape edge = new EdgeShape(); 
        edge.set(0, 0, stage.getWidth(), 0);
        boundary = body.createFixture(edge, 0.0f);
        edge.dispose(); 
    }
    
    public void render(SpriteBatch batch) {
//        batch.setColor(1, 1, 1, 1f);
//        batch.begin();
//        batch.draw(overlay, 0, 0, 100f, 100f);
//        batch.end();
// 
    }

	public static MouseJointDef getMouseDef() {
		return mouseDef;
	}
	
	public static MouseJoint getMouseJoint() {
		return mouse;
	}


	public static void setMouseJoint(MouseJoint createJoint) {
		mouse = createJoint;
	}
}
