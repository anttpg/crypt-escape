package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Interactable {
	private Fixture fixture;
	private Room parent;
	private int col;
	private int row;
	private TextureRegion texture;
	private float xCorner;
	private float yCorner;
	
	public Interactable(int c, int r, String current, Room p) {
		parent = p;
		row = r;
		col = c;
		setItemLocation();
	}
	
	private void setItemLocation() {
		xCorner = parent.getRoomLocation()[1] + (Constants.X_ROOM_METERS * (row/(float)Constants.X_TILES));
		yCorner = parent.getRoomLocation()[0] + (Constants.Y_ROOM_METERS * (col/(float)Constants.Y_TILES));		
	}
	
	private Vector2 getItemLocation() {
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
		bodyDef.position.set(getItemLocation()); //Set its position 
		Body bd = GameScreen.world.createBody(bodyDef);  
		
		PolygonShape box = new PolygonShape();  // Create a polygon shape 

		box.setAsBox(-Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
		fixture = bd.createFixture(box, 0.0f);
		box.dispose();
	}
	
	public Fixture getFixture() {
		return fixture;
	}
	
	public void setTextureRegion(TextureRegion t) {
		texture = t;
	}
	
	public void draw(SpriteBatch batch) {
		batch.draw(texture, xCorner, yCorner, Constants.TILESIZE, Constants.TILESIZE);
	}
	
	public void debugInteractable(Fixture f, int col, int row) {
		System.out.println("Position of item at col  " + col + "  and row  " + row + "  : " + f.getBody().getPosition());
	}
	
}
