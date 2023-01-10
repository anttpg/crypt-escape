package com.cryptescape.game.rooms;

import java.util.HashMap;

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
import com.cryptescape.game.GameScreen;

public class Freeform extends Actor{
	private Fixture interactionFixture;
	private Room parent;
	private TextureRegion texture;
	private boolean playerInRange = false;
	
	private float width;
	private float height;
	
	protected Animation<TextureRegion> animation;
	public static HashMap<String, String> itemBounds;
	protected Body interactionBody;
    protected float timer = 0;
	
	public Freeform(float x, float y, float width, float height, String name, Room p) {
		parent = p;
		setPosition(x, y);
		this.width = (width);
		this.height = (height);
		setName(name);
		
		super.setZIndex(1);
	}
	
	
	private Vector2 getItemLocation() {
		return new Vector2(getX() + width/2f, getY() + height/2);
	}

	
	public void setTextureRegion(TextureRegion t) {
		texture = t;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, getX(), getY(), width, height);
	}
	
	
	
//	public void createStaticEdge(int c) {         
//		BodyDef bodyDef = new BodyDef();                   
//		bodyDef.position.set(getItemLocation()); //Set its position 
//		Body bd = GameScreen.world.createBody(bodyDef);                      
//		
//		EdgeShape edge = new EdgeShape(); //Walls/Doors dont need to be a full box
//		
//		// SETTING THE POINTS AS OFFSET DISTANCE FROM CENTER
//		edge.set(Constants.edgeSizes[c][0], Constants.edgeSizes[c][1], Constants.edgeSizes[c][2], Constants.edgeSizes[c][3]);
//		interactionFixture = bd.createFixture(edge, 0.0f);
//		//DEBUG VERSION --> debugItem(bd.createFixture(edge, 0.0f), col, row);
//		edge.dispose();	
//	}
//	
//	
//	public void createStaticBox() {
//		BodyDef bodyDef = new BodyDef(); 
//	
//		bodyDef.position.set(getItemLocation()); // Set its position
//		Body bd = GameScreen.world.createBody(bodyDef);
//		PolygonShape box = new PolygonShape(); // Create a polygon shape
//		box.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
//		interactionFixture = bd.createFixture(box, 0.0f);
//		box.dispose();
//	}
	
	
	/**
	 * Creates a rectangle that you cannot touch, but can be seen when the player enters.
	 * Use for interaction decisions.
	 * hx: the half-width of the rect.
	 * hy: the half-height of the rect.   
	 */
	public void createInteractionRectangle(float hx, float hy, short groupIndex) {
		BodyDef bodyDef = new BodyDef();
		bodyDef.position.set(getItemLocation()); // Set its position
		interactionBody = GameScreen.world.createBody(bodyDef);
		
		PolygonShape box = new PolygonShape(); // Create a polygon shape
		box.setAsBox(hx, hy);
		
	    FixtureDef fixtureDef = new FixtureDef();
	    fixtureDef.filter.groupIndex = groupIndex;
	    fixtureDef.shape = box;
		fixtureDef.isSensor = true; //Makes sensor?
		fixtureDef.density = 0f;
		fixtureDef.friction = 0f;
		fixtureDef.restitution = 0f;
		interactionFixture = interactionBody.createFixture(fixtureDef);
		box.dispose();
	}

	public boolean isPlayerInRange() {
		return playerInRange;
	}

	public void setPlayerInRange(boolean playerInRange) {
		this.playerInRange = playerInRange;
	}
	
	public Fixture getFixture() {
		return interactionFixture;
	}
	
	public TextureRegion getRegion() {
		return texture;
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
	 * Deletes this object
	 */
	public void destroyFixtures() {
	    if(interactionBody != null)
	        interactionBody.destroyFixture(interactionFixture);
	}
}
