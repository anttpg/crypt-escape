package com.cryptescape.game.hud;

import java.nio.channels.AcceptPendingException;
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
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.hud.items.BagItem;
import com.cryptescape.game.hud.items.BatteryItem;
import com.cryptescape.game.hud.items.BoxItem;
import com.cryptescape.game.hud.items.CandleItem;
import com.cryptescape.game.hud.items.CannedFoodItem;
import com.cryptescape.game.hud.items.CrowbarItem;
import com.cryptescape.game.hud.items.DrinkItem;
import com.cryptescape.game.rooms.Box;
import com.cryptescape.game.rooms.Table;

public class Inventory {
    private static Stage stage;
    private static World world;
    private Box2DDebugRenderer debugRenderer;
    
    public static float tileSize;
    public static float oldWidth;
    public static float oldHeight;
    
    public static MouseJointDef mouseDef;
    public static MouseJoint mouse;
    public static Fixture currentlyDragging;
    
    private static ArrayList<InventoryItem> dispose = new ArrayList<InventoryItem>();
    private static ArrayList<InventoryItem> remove = new ArrayList<InventoryItem>();
    private static ArrayList<InventoryItem> alwaysSafe = new ArrayList<InventoryItem>();
    private static BagItem bag;
    
    private static boolean toDispose = false;
    private Texture overlay = new Texture(Gdx.files.internal("TestOverlay.png"));
    private Fixture boundary;
    
    private static EquipmentSlot equipmentLeft;
    private static EquipmentSlot equipmentRight;
    
    
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
        bag = new BagItem(world, "briefcase", stage.getWidth()/2f, 1.7f, 2, 12f);
        itemGroup.addActor(bag);
        
        equipmentLeft = new EquipmentSlot(world, "equipmentLeft", stage.getWidth()/8f, stage.getHeight() - stage.getHeight()/6f, 2, 7f);
        equipmentRight = new EquipmentSlot(world, "equipmentRight", stage.getWidth() - stage.getWidth()/8f, stage.getHeight()  - stage.getHeight()/6f, 2, 7f);
        itemGroup.addActor(equipmentLeft);
        itemGroup.addActor(equipmentRight);
        alwaysSafe.add(equipmentLeft);
        alwaysSafe.add(equipmentRight);
        ArrayList<String> acceptedItemsL = new ArrayList<String>();
        acceptedItemsL.add("spraypaint");
        acceptedItemsL.add("water");
        acceptedItemsL.add("crowbar");
        equipmentLeft.setAcceptedItems(acceptedItemsL);
        
        ArrayList<String> acceptedItemsR = new ArrayList<String>();
        acceptedItemsR.add("candlestick");
        equipmentRight.setAcceptedItems(acceptedItemsR);
        
        itemGroup.addActor(new CrowbarItem(world, "crowbar", 5f, 1.2f, 3, 6f));
        itemGroup.addActor(new CandleItem(world, "candlestick", 5f, 1.2f, 3));
        itemGroup.addActor(new BatteryItem(world, "battery", 5.3f, 1.2f, 3));
        itemGroup.addActor(new DrinkItem(world, "water", 5.9f, 1.2f, 3, 3f));
        itemGroup.addActor(new CannedFoodItem(world, "beans", 5.7f, 1.2f, 3));
        itemGroup.addActor(new SpraypaintItem(world, "spraypaint", 5.5f, 1.2f, 3));

        stage.addActor(itemGroup);
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
	
	public static void openTable(Table table) {
	    
	}
	
	/**
	 * Adds a new item to a box, used on first time open.
	 */
	public static void addItemFromBox(String itemName, InventoryItem boxParent) {
	    float x = boxParent.getX() + boxParent.getWidth()/2f;
	    float y = boxParent.getY() + boxParent.getHeight()/2f;
	    
	    if(itemName.equals("candlestick"))
	        itemGroup.addActorAt(3, new CandleItem(world, "candlestick", x, y, 2));
	    else if(itemName.equals("battery"))
	        itemGroup.addActorAt(3, new BatteryItem(world, "battery", x, y, 2));
	    else if(itemName.equals("water"))
	        itemGroup.addActorAt(3, new DrinkItem(world, "water", x, y, 2, 3f));
	    else if(itemName.equals("beans"))
	        itemGroup.addActorAt(3, new CannedFoodItem(world, "beans", x, y, 2));
	    else if(itemName.equals("spraypaint"))
	        itemGroup.addActorAt(3, new SpraypaintItem(world, "spraypaint", x, y, 2));
	}
	
	/**
	 * Used when you have an item already created, and just need to reinsert it into the inventory
	 */
	public static void addItem(InventoryItem item) {
	    itemGroup.addActor(item);
	}
	
	
	/**
	 * Queues an item for disposal, called before each dispose.
	 */
	public static void queueForDisposal(InventoryItem i) {
	    if(remove.indexOf(i) == -1)
	        remove.add(i);
	}
	
	/**
	 * Removes the unused items, and drops them on the floor. Called on inventory close.
	 */
	public static void disposeUnusedItems() {
	    if(toDispose) {
	        ArrayList<Actor> safe = bag.checkIfOverlap();
    	    
	        
    	    for(Actor a : itemGroup.getChildren()) 
    	        if(alwaysSafe.indexOf(a) == -1 && equipmentLeft.getEquippedItem() != ((InventoryItem)a) && equipmentRight.getEquippedItem() != ((InventoryItem)a))
    	            if(safe.indexOf(a) == -1 && remove.indexOf((InventoryItem)a) == -1)
    	                remove.add((InventoryItem)a); //Cant dispose of it yet, will skip items
    	            
    	    safe.clear();
    	    
    	    if(!dispose.isEmpty()) { //Disposal queue for complete destruction
                for(InventoryItem i : dispose) { 
                    itemGroup.removeActor(i);    
                    world.destroyBody(i.getBody());
                }
            dispose.clear();   
    	    }
    	    
    	    if(!remove.isEmpty()) { //Simply remove to use at a later date.
                for(InventoryItem i : remove) { 
                    if(i.isDroppable()) 
                        GameScreen.player.getRoom().addDroppedItem(i);
                    
                    itemGroup.removeActor(i);    
                }
            remove.clear();   
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
