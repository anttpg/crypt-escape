package com.cryptescape.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;

public class Interactable {
	private Fixture fixture;
	
	public Interactable(Fixture f) {
		fixture = f;
	}
	
	
	public Vector2 getItemLocation(int col, int row, int[] roomLocation) {
		return new Vector2(roomLocation[0] + (Constants.CAMERA_HEIGHT/col), roomLocation[1] + (Constants.CAMERA_WIDTH/row));
	}
	
	
	public Fixture createStaticEdge(int col, int row, int[] rl) {
		BodyDef bodyDef = new BodyDef();  
		bodyDef.position.set(getItemLocation(col,row, rl)); //Set its position 
		Body bd = GameScreen.world.createBody(bodyDef);  
		
		EdgeShape edge = new EdgeShape(); //Walls/Doors dont need to be a full box
		
		// SETTING THE POINTS AS OFFSET DISTANCE FROM CENTER
		edge.set(-Constants.TILESIZE / 2f, 0, Constants.TILESIZE / 2f, 0);
		Fixture f = bd.createFixture(edge, 0.0f);
		edge.dispose();
		return f;
	}
	
	
	public Fixture createStaticBox(int col, int row, int[] rl) {
		BodyDef bodyDef = new BodyDef();  
		bodyDef.position.set(getItemLocation(col,row, rl)); //Set its position 
		Body bd = GameScreen.world.createBody(bodyDef);  
		
		PolygonShape box = new PolygonShape();  // Create a polygon shape 

		box.setAsBox(-Constants.TILESIZE / 2f, Constants.TILESIZE / 2f);
		Fixture f = bd.createFixture(box, 0.0f);
		box.dispose();
		return f;
	}
	
}
