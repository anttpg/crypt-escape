package com.cryptescape.game.rooms;

import java.util.HashMap;
import java.util.Random;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.Constants;
import com.cryptescape.game.Filters;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.graphics.LightingManager;

public class Interactable extends Actor{
	private Fixture fixture;
	private Room parent;
	private String bounds;
	private TextureRegion texture;
	private boolean playerInRange = false;
	private int col;
	private int row;
    private int skinNumber;
	
	protected Animation<TextureRegion> animation;
	public static HashMap<String, String> itemBounds;
	protected Body interactionBody;
    protected float timer = 0;
	
	public Interactable(int c, int r, String current, Room p) {
		parent = p;
		row = r;
		col = Math.abs(Constants.Y_TILES-c)-1;
		setItemLocation();
		setName(current);
		super.setWidth(Constants.TILESIZE);
		super.setHeight(Constants.TILESIZE);
		
		super.setZIndex(1);
		GameScreen.mainGroup.addActor(this);
	}
	
	public Interactable(int c, int r, String current, Room p, float width, float height) {
	        this(c, r, current, p);
	        super.setWidth(width);
	        super.setHeight(height);
	}
	
	private void setItemLocation() {
		setX(parent.getRoomLocation()[1] + (Constants.X_ROOM_METERS * (row/(float)Constants.X_TILES)));
		setY(parent.getRoomLocation()[0] + (Constants.Y_ROOM_METERS * (col/(float)Constants.Y_TILES)));		
	}
	
	/**
	 * Gets a random textureregion super from all possible verisons 
	 * (IE for different boxes)
	 */
	public static TextureRegion getRandomRegion(String name, int numOfTypes) {
	    Random r = new Random();
	    int l = r.nextInt(numOfTypes)+1; //Specifies number of possible box types (currently 6) 
	    if(GameScreen.atlas.findRegion(name + l) != null) {
	        return GameScreen.atlas.findRegion(name + l);
	    }
	    
	    return null;
	}
	
	public Vector2 getItemLocation() {
		return new Vector2( // vectors are in x,y,z
				parent.getRoomLocation()[1] + (Constants.X_ROOM_METERS * (row/(float)Constants.X_TILES)) + getWidth()/2,
				//Original corner        + Location at X tiles over          
				parent.getRoomLocation()[0] + (Constants.Y_ROOM_METERS * (col/(float)Constants.Y_TILES)) + getHeight()/2 
				);
	}
	
	public void createStaticEdge(int c) {         
		BodyDef bodyDef = new BodyDef();                   
		bodyDef.position.set(getItemLocation()); //Set its position 
		Body bd = GameScreen.world.createBody(bodyDef);                      
		
		EdgeShape edge = new EdgeShape(); //Walls/Doors dont need to be a full box
		
		// SETTING THE POINTS AS OFFSET DISTANCE FROM CENTER
		edge.set(Constants.edgeSizes[c][0], Constants.edgeSizes[c][1], Constants.edgeSizes[c][2], Constants.edgeSizes[c][3]);
		fixture = bd.createFixture(edge, 0.0f);
		//DEBUG VERSION --> debugItem(bd.createFixture(edge, 0.0f), col, row);
		edge.dispose();	
	}
	
	/*
	 * Creates a box around the texture (if given bounds), or a box around the tile,
	 * (Default contant) that can interact/block other objects
	 */
	public void createStaticBox(int groupIndex) {
		BodyDef bodyDef = new BodyDef(); 
		
		if (bounds == null) {
			bodyDef.position.set(getItemLocation()); //Set its position 
			Body bd = GameScreen.world.createBody(bodyDef);  
			PolygonShape box = new PolygonShape();  // Create a polygon shape 
			box.setAsBox(getWidth() / 2f, getHeight() / 2f);
			
			FixtureDef fixtureDef = new FixtureDef();
			fixtureDef.filter.groupIndex = (short)groupIndex;
		    fixtureDef.shape = box;
		    fixtureDef.density = 0f;
			fixture = bd.createFixture(fixtureDef);
			box.dispose();
		}
		
		else {
			String[] b = bounds.split(",");
			float hx = ((getWidth() - ( getWidth() * ( Float.valueOf(b[0]) / (float)texture.getRegionWidth()) ) - (getWidth() - ( getWidth() * ( Float.valueOf(b[2]) / (float)texture.getRegionWidth()) ))) / 2f);
			float hy = ((getHeight() - ( getHeight() * ( Float.valueOf(b[1]) / (float)texture.getRegionHeight()) ) - (getHeight() - ( getHeight() * ( Float.valueOf(b[3]) / (float)texture.getRegionHeight())) )) / 2f);
			Vector2 corner = new Vector2((getItemLocation().x - getWidth()/2) + hx + ( getWidth() * ( Float.valueOf(b[0]) / (float)texture.getRegionWidth())),
					(getItemLocation().y - getHeight() /2) + hy + ( getHeight()  * ( Float.valueOf(b[1]) / (float)texture.getRegionHeight())));  //Normalizes it based on current location
			
			bodyDef.position.set(corner); //Set its position 
			Body bd = GameScreen.world.createBody(bodyDef);  
			PolygonShape box = new PolygonShape();  // Create a polygon shape
			box.setAsBox(hx, hy);

            FixtureDef fixtureDef = new FixtureDef();
            fixtureDef.filter.groupIndex = (short)groupIndex;
            fixtureDef.shape = box;
            fixtureDef.density = 0f;
			fixture = bd.createFixture(fixtureDef);
			box.dispose();
		}
	}
	
	
	/**
	 * Creates a rectangle that you cannot touch, but can be seen when the player enters.
	 * Use for interaction decisions.
	 * hx: the half-width of the rect.
	 * hy: the half-height of the rect.   
	 */
	public void createInteractionRadius(float hx, float hy) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(getItemLocation()); // Set its position
		interactionBody = GameScreen.world.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape(); // Create a polygon shape
		box.setAsBox(hx, hy);
		
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.filter.groupIndex = -2;
	    fixtureDef.shape = box;
		fixtureDef.isSensor = true; //Makes sensor?
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;
		fixture = interactionBody.createFixture(fixtureDef);
		box.dispose();
	}
	

	public boolean isPlayerInRange() {
		return playerInRange;
	}

	public void setPlayerInRange(boolean playerInRange) {
		this.playerInRange = playerInRange;
	}
	
	public Fixture getFixture() {
		return fixture;
	}
	
	public void checkBounds(String name) {
		if(itemBounds.get(name) != null) 
			bounds = itemBounds.get(name);
	}
	
	public void setTextureRegion(TextureRegion t) {
		texture = t;
	}
	
	 /**
     * Used to draw a interactable object
     */
	public void draw(SpriteBatch batch) {
		batch.draw(texture, getX(), getY(), getWidth(), getHeight());
	}
	
	
	public Body getInteractionBody() {
		return interactionBody;
	}

	public void debugInteractable(Fixture f, int col, int row) {
		System.out.println("Position of item at col  " + col + "  and row  " + row + "  : " + f.getBody().getPosition());
	}
	
	public Room getParentRoom() {
		return parent;
	}
	
	/**
	 * Sets the current skin version to a randomly chosen version, and finds it correspondign animation
	 */
    public void findRandomAnimation(int numOfSkins, String name, float animation_speed) {
        findRandomSkin(numOfSkins, name);
        this.animation = new Animation<TextureRegion>(animation_speed, GameScreen.atlas.findRegions(name + skinNumber));
        
    }
    
    /**
     * Sets the current skin to a randomly chosen version.
     */
    public void findRandomSkin(int numOfSkins, String name) {
        Random r = new Random();
        skinNumber = r.nextInt(numOfSkins) + 1; // Specifies number of possible box types (currently 6)
        setName(getName() + skinNumber);

        if (GameScreen.atlas.findRegion(getName()) != null) {
            this.setTextureRegion(GameScreen.atlas.findRegion(getName()));
            this.checkBounds(getName());
        } else {
            System.out.println("NullBoxType; Could not find " + name + " of type '" + getName() + "" + skinNumber + "'. "
                            + "Make sure all " + name + " have a correct version. ");
        }
    }
    
    public int getSkin() {
        return skinNumber;
    }
}
