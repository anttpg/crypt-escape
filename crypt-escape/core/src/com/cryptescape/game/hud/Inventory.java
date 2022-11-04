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
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Joint;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.physics.box2d.joints.MouseJointDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.cryptescape.game.Constants;
import com.cryptescape.game.CustomFixtureData;
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
    
    
    private Group itemGroup = new Group();

    public Inventory(Stage stage) {
        world = new World(new Vector2(0, 0), true);
        debugRenderer = new Box2DDebugRenderer();
        tileSize = (float)stage.getHeight()/Constants.Y_TILES;
        Inventory.stage = stage;
        createBoundary();
        
        mouseDef = new MouseJointDef();
        mouseDef.bodyA = boundary.getBody();
        mouseDef.collideConnected = true;
        
        oldWidth = stage.getWidth(); //Updates values befre stage modification
        oldHeight = stage.getHeight();
        tileSize = (float)stage.getHeight()/Constants.Y_TILES;
        
        
//        frontItems.add(new CandleItem(world, "candlestick", 1f, 1f, 0));
//        backItems.add(new BagItem(world, "briefcase", stage.getWidth()/2f, 1.3f, 1));
        
        itemGroup.addActor(new CandleItem(world, "candlestick", 1f, 1f, 1));
        itemGroup.addActor(new BatteryItem(world, "battery", 1.3f, 1f, 1));
        itemGroup.addActor(new DrinkItem(world, "water", 1.3f, 1f, 1));
        itemGroup.addActor(new SpraypaintItem(world, "spraypaint", 1.5f, 1f, 1));
        itemGroup.addActor(new BagItem(world, "briefcase", stage.getWidth()/2f, 1.3f, 2));
        	
        stage.addActor(itemGroup);
        
//        for(InventoryItem i : frontItems)
//            stage.addActor(i);
        
       	
        
        
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
        stage.getViewport().update(width, height, true);
        System.out.println(tileSize + " " + stage.getWidth()/stage.getHeight());
        debugInventory();

        for(Actor i : itemGroup.getChildren()) 
        	((InventoryItem)i).resize(stage.getWidth(), stage.getHeight());
        
        oldWidth = stage.getWidth(); //Updates values befre stage modification
        oldHeight = stage.getHeight();
        createBoundary(); //Remakes boundary for new size
        
        if(4f/3 < stage.getWidth()/stage.getHeight()) //checks for weird error that occurs when ratio 4w/3h is broken in height
        	tileSize = (float)stage.getHeight()/Constants.Y_TILES;

    }
    
    public void createBoundary() {
    	Vector2[] vertices = new Vector2[] {new Vector2(0,0), new Vector2(0,stage.getHeight()), new Vector2(stage.getWidth(),stage.getHeight()), new Vector2(stage.getWidth(),0), new Vector2(0,0)};
	        
    	//Creating interactable body
        BodyDef boundaryDef = new BodyDef();
        boundaryDef.type = BodyType.StaticBody;
        boundaryDef.position.set(0, 0); // Set its position
        Body body = world.createBody(boundaryDef);
        ChainShape chain = new ChainShape();  // Create a polygon shape 
        
        chain.createChain(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chain;
        fixtureDef.density = 0.0f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.45f; 

        boundary = body.createFixture(fixtureDef);
        boundary.setUserData(new CustomFixtureData(false)); //This represents that this item is a interaction object, and is not movable.
        chain.dispose();
    }
    
    public void render(SpriteBatch batch) {
        batch.setColor(1, 1, 1, 0.65f);
        batch.begin();
        batch.draw(overlay, 0, 0, stage.getWidth(), stage.getHeight());
        batch.end(); 
        batch.setColor(1, 1, 1, 1);
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
	
	public static void debugInventory() {
	    System.out.println("Inv stage w/h" + " " + stage.getWidth() + " " + stage.getHeight());
	}
	
	public static void debugInventory(String position) {
	    System.out.println("Inv stage w/h " + position + " " + stage.getWidth() + " " + stage.getHeight());
	}
}
