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
import com.cryptescape.game.rooms.Box;

public class Inventory {
    private static Stage stage;
    private static World world;
    private Box2DDebugRenderer debugRenderer;
    
    public static float tileSize;
    public static float oldWidth;
    public static float oldHeight;
    public static MouseJointDef mouseDef;
    public static MouseJoint mouse;
    
    private static ArrayList<InventoryItem> disposal = new ArrayList<InventoryItem>();
    private static BagItem bag;
    
    private static boolean toDispose = false;
    private Texture overlay = new Texture(Gdx.files.internal("TestOverlay.png"));
    private Fixture boundary;
    
    
    private static Group itemGroup = new Group();

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
        
        //ALWAYS LEAVE BREIFCASE FIRST
        bag = new BagItem(world, "briefcase", stage.getWidth()/2f, 1.3f, 2);
        itemGroup.addActor(bag);
        
        itemGroup.addActor(new CandleItem(world, "candlestick", 5f, 1.2f, 3));
        itemGroup.addActor(new BatteryItem(world, "battery", 5.3f, 1.2f, 3));
        itemGroup.addActor(new DrinkItem(world, "water", 5.9f, 1.2f, 3));
        itemGroup.addActor(new CannedFoodItem(world, "beans", 5.7f, 1.2f, 3));
        itemGroup.addActor(new SpraypaintItem(world, "spraypaint", 5.5f, 1.2f, 3));

        	
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
        toDispose = true;
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
        if(boundary != null)
            world.destroyBody(boundary.getBody());
        
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
	
	public static void openBox(Box box) {
	    itemGroup.addActorAt(1, new BoxItem(world, box, 
	            stage.getWidth()/2f, stage.getHeight() - stage.getHeight()/5f, 1));
	}

	public static void addItem(String itemName, InventoryItem i) {
	    float x = i.getX() + i.getWidth()/2f;
	    float y = i.getY() + i.getHeight()/2f;
	    
	    if(itemName.equals("candlestick"))
	        itemGroup.addActorAt(3, new CandleItem(world, "candlestick", x, y, 2));
	    else if(itemName.equals("battery"))
	        itemGroup.addActorAt(3, new BatteryItem(world, "battery", x, y, 2));
	    else if(itemName.equals("water"))
	        itemGroup.addActorAt(3, new DrinkItem(world, "water", x, y, 2));
	    else if(itemName.equals("beans"))
	        itemGroup.addActorAt(3, new CannedFoodItem(world, "beans", x, y, 2));
	    else if(itemName.equals("spraypaint"))
	        itemGroup.addActorAt(3, new SpraypaintItem(world, "spraypaint", x, y, 2));
	}
	
	
	/**
	 * Queues an item for disposal, called before each dispose.
	 */
	public static void queueForDisposal(InventoryItem i) {
	    if(disposal.indexOf(i) == -1)
	        disposal.add(i);
	}
	
	/**
	 * Removes the unused items, and drops them on the floor. Called on inventory close.
	 */
	public static void disposeUnusedItems() {
	    if(toDispose) {
	        ArrayList<Actor> safe = bag.checkIfOverlap();
    	    
    	    for(Actor a : itemGroup.getChildren()) 
    	        if(safe.indexOf(a) == -1 && disposal.indexOf((InventoryItem)a) == -1) 
    	            disposal.add((InventoryItem)a); //Cant dispose of it yet, will skip items
    	            
    	    safe.clear();
    	    
    	    if(!disposal.isEmpty()) {
                for(InventoryItem i : disposal) { 
                	GameScreen.player.getRoom().addDroppedItem(i);
                    itemGroup.removeActor(i);    
                    world.destroyBody(i.getBody());
                }
            disposal.clear();   
    	    }
    	    
    	    toDispose = false;
	    }
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
    
    public static Group getItemGroup() {
        return itemGroup;
    }
 	
}
