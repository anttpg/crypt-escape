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
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class Interactable extends Actor{
	private Fixture fixture;
	private Room parent;
	private String bounds;
	private TextureRegion texture;
	private String name;
	private boolean playerInRange = false;
	private int col;
	private int row;
	
	protected Animation<TextureRegion> animation;
	public static HashMap<String, String> itemBounds;
	protected Body interactionBody;
	
	public Interactable(int c, int r, String current, Room p) {
		parent = p;
		row = r;
		col = Math.abs(Constants.Y_TILES-c)-1;
		name = current;
		setItemLocation();
		
		super.setZIndex(1);
		GameScreen.mainGroup.addActor(this);
	}
	
	private void setItemLocation() {
		setX(parent.getRoomLocation()[1] + (Constants.X_ROOM_METERS * (row/(float)Constants.X_TILES)));
		setY(parent.getRoomLocation()[0] + (Constants.Y_ROOM_METERS * (col/(float)Constants.Y_TILES)));		
	}
	
	public Vector2 getItemLocation() {
		return new Vector2( // vectors are in x,y,z
				parent.getRoomLocation()[1] + (Constants.X_ROOM_METERS * (row/(float)Constants.X_TILES)) + Constants.TILESIZE/2,
				//Original corner        + Location at X tiles over          
				parent.getRoomLocation()[0] + (Constants.Y_ROOM_METERS * (col/(float)Constants.Y_TILES)) + Constants.TILESIZE/2 
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
	
	
	public void createStaticBox() {
		BodyDef bodyDef = new BodyDef(); 
		
		if (bounds == null) {
			bodyDef.position.set(getItemLocation()); //Set its position 
			Body bd = GameScreen.world.createBody(bodyDef);  
			PolygonShape box = new PolygonShape();  // Create a polygon shape 
			box.setAsBox(Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
			fixture = bd.createFixture(box, 0.0f);
			box.dispose();
		}
		
		else {
			String[] b = bounds.split(",");
			float hx = ((Constants.TILESIZE - ( Constants.TILESIZE * ( Float.valueOf(b[0]) / (float)texture.getRegionWidth()) ) - (Constants.TILESIZE - ( Constants.TILESIZE * ( Float.valueOf(b[2]) / (float)texture.getRegionWidth()) ))) / 2f);
			float hy = ((Constants.TILESIZE - ( Constants.TILESIZE * ( Float.valueOf(b[1]) / (float)texture.getRegionHeight()) ) - (Constants.TILESIZE - ( Constants.TILESIZE * ( Float.valueOf(b[3]) / (float)texture.getRegionHeight())) )) / 2f);
			Vector2 corner = new Vector2((getItemLocation().x - Constants.TILESIZE/2) + hx + ( Constants.TILESIZE * ( Float.valueOf(b[0]) / (float)texture.getRegionWidth())),
					(getItemLocation().y - Constants.TILESIZE/2) + hy + ( Constants.TILESIZE * ( Float.valueOf(b[1]) / (float)texture.getRegionHeight())));  //Normalizes it based on current location
			
			bodyDef.position.set(corner); //Set its position 
			Body bd = GameScreen.world.createBody(bodyDef);  
			PolygonShape box = new PolygonShape();  // Create a polygon shape
			box.setAsBox(hx, hy);

			fixture = bd.createFixture(box, 0.0f);
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
	    fixtureDef.shape = box;
		fixtureDef.isSensor = true;
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
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, getX(), getY(), Constants.TILESIZE, Constants.TILESIZE);
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
}
