package com.cryptescape.game.hud;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.Constants;
import com.cryptescape.game.CustomFixtureData;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.InputHandler;
import com.cryptescape.game.rooms.Interactable;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public abstract class InventoryItem extends Actor{
    private Fixture fixture;
	protected Fixture interactionBody;
    protected TextureRegion currentRegion;
    protected Animation<TextureRegion> animation;


    private String name;
    private float scale;
    private float[] bounds = new float[4];
    
    protected float time = 0;
    protected float countdown = 0;
    protected boolean mouseInRange = false;
    protected boolean startAnimation = false;
    
    
    public InventoryItem(World world, String name, TextureRegion region, float x, float y, float scale, int zindex) {
        this.name = name;
        this.animation = null;
        this.currentRegion = region;
        this.scale = scale;
        this.checkBounds(name);
        super.setZIndex(zindex);
    }
    
    public InventoryItem(World world, String name, String regions, float x, float y, float scale, int zindex) {
        this.name = name;
        this.animation = new Animation<TextureRegion>(0.05f, GameScreen.atlas.findRegions(name));
        this.countdown = animation.getAnimationDuration();
        this.currentRegion = animation.getKeyFrame(0);
        this.scale = scale;
        this.checkBounds(name);
//        super.setZIndex(zindex);
    }
    
    
    public void applyForce(float xN, float yN, float s) {
        fixture.getBody().applyForceToCenter(xN, yN, true);
    }
    
    public void checkBounds(String name) {
        if(Interactable.itemBounds.get(name) != null) {
            int i = 0;
            for(String s : Interactable.itemBounds.get(name).split(",")) {
                bounds[i] = Float.valueOf(s);
                i++;
            }
            
        }
        else
            bounds = new float[] { 0, 0, currentRegion.getRegionWidth(), currentRegion.getRegionHeight() };
    }
    
    public void resize(float newWidth, float newHeight) {
        //Resises based on stage size, not screen size
    	getBody().setTransform(
    			getBody().getPosition().x * (newWidth / Inventory.oldWidth), 
    			getBody().getPosition().y * (newHeight / Inventory.oldHeight), 
    			getBody().getAngle());
    	
        debugItem();
        Inventory.debugInventory();
       
        setWidth((Inventory.tileSize * scale) * (bounds[2]-bounds[0])/currentRegion.getRegionHeight());
        setHeight((Inventory.tileSize * scale) * (bounds[3]-bounds[1])/currentRegion.getRegionHeight());
    }

    
    public void defaultAct(float delta){
        time += delta;
        setX(fixture.getBody().getPosition().x - getWidth()/2f);
        setY(fixture.getBody().getPosition().y - getHeight()/2f);
        
        if(mouseInRange && !startAnimation) {
        	startAnimation = true;
        	time = 0;
        }
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {	
        batch.draw(currentRegion, getX(), getY(), 
        		((Inventory.tileSize * scale) * (bounds[2]-bounds[0])/currentRegion.getRegionHeight()/2f), //These are just to set the origin of the rotation
        		((Inventory.tileSize * scale) * (bounds[3]-bounds[1])/currentRegion.getRegionHeight()/2f), 
        		getWidth(), getHeight(), 1f, 1f, (fixture.getBody().getAngle()*180)/3.14f);

    }
    
    public void debugItem() {
        System.out.println(
                "Item:" + name + " X/Y " + getX() + "  " + getY() + "   WIDTH/HEIGHT " + getWidth() + "  " + getHeight());
        
        //System.out.println("boundary: " + bounds[0] + " " + bounds[1] + " " + bounds[2] + " " + bounds[3]);
    }
    
    
    public Body getBody() {
    	return fixture.getBody();
    }
    
    public float getScale() {
    	return scale;
    }
    
    public void createUserData(boolean ans) {
    	getBody().setUserData(new CustomFixtureData(ans));
    }
    
    public void setIsMovable(boolean ans) {
    	((CustomFixtureData)(getBody().getUserData())).setMovable(ans);
    }
    
    public void setInRange() {
    	if(interactionBody != null)
    		mouseInRange = interactionBody.testPoint(InputHandler.relativeMouseInventory.x, InputHandler.relativeMouseInventory.y);
    }
    
    public void makeCircleFixture(World world, float x, float y) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
        bodyDef.angularDamping = 0.5f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(Inventory.tileSize * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; 

        fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }
    
    public void makeSquareFixture(World world, float x, float y, float density) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
        bodyDef.angularDamping = 5.0f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();  // Create a polygon shape 
		box.setAsBox(
				((Inventory.tileSize * scale) * (bounds[2]-bounds[0])/currentRegion.getRegionHeight()/2f),
				((Inventory.tileSize * scale) * (bounds[3]-bounds[1])/currentRegion.getRegionHeight()/2f)
				);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.3f; 

        fixture = body.createFixture(fixtureDef);
        box.dispose();
    }
    
    /**
     * Generates a new polygon fixture with x number of edges. Edges should be a value from 0-1, 
     * As it will be scaled to the size of the inventory item. This method will ignore traditional
     * bounds.txt values, as it is assumed you calculate those beforehand when making the edges.
     */
    public void makePolygonFixture(World world, float x, float y, float density, Vector2[] vertices) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.gravityScale = 0;
        bodyDef.linearDamping = 0.5f;
        bodyDef.angularDamping = 5.0f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        PolygonShape poly = new PolygonShape();  //Create a polygon shape 
        
        
        for(Vector2 vertex : vertices) {
            vertex.x = ((Inventory.tileSize * scale) * vertex.x) - (Inventory.tileSize * scale)/2f; 
            vertex.y = ((Inventory.tileSize * scale) * vertex.y) - (Inventory.tileSize * scale)/2f; 
        }
        
        
        poly.set(vertices); //Draws the polygon based on these vertices


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = poly;
        fixtureDef.density = density;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.3f; 

        fixture = body.createFixture(fixtureDef);
        poly.dispose();
    }
    
    
    /**
     * Generates a new edge-based chain fixture. Less resource intensive than Polygon fixtures
     * Edges should be a value from 0-1, As it will be scaled to the size of the inventory item. 
     * This method will ignore traditional bounds.txt values, as it is assumed you calculate 
     * those beforehand when making the edges.
     */
    public void makeChainFixture(World world, float x, float y, float density, Vector2[] vertices) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.gravityScale = 0.0f;
        bodyDef.linearDamping = 0.5f;
        bodyDef.angularDamping = 5.0f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        ChainShape chain = new ChainShape();  // Create a polygon shape 
        
        for(Vector2 vertex : vertices) {
            vertex.x = ((Inventory.tileSize * scale) * vertex.x) - (Inventory.tileSize * scale)/2f; 
            vertex.y = ((Inventory.tileSize * scale) * vertex.y) - (Inventory.tileSize * scale)/2f; 
        }
        
        System.out.println(vertices[0]);
        chain.createChain(vertices);


        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = chain;
        fixtureDef.density = density;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.45f; 

        fixture = body.createFixture(fixtureDef);
        chain.dispose();
    }
    
    /**
     * Creates a new interaction radius at coordinates x, y with the radius r. Then adds it to the current body.
     */
	public void createInteractionRadius(float x, float y, float r) {
		CircleShape circle = new CircleShape(); // Create a polygon shape
        circle.setRadius(r);
		
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.shape = circle;
		fixtureDef.isSensor = true;
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;     
		fixtureDef.restitution = 0f;
		interactionBody = getBody().createFixture(fixtureDef);
		interactionBody.setUserData(new CustomFixtureData(false)); //This represents that this item is a interaction object, and is not movable, nor should be. 
		circle.dispose();
	}
	
}
