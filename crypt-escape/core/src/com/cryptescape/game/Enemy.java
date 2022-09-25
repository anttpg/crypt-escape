package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.btree.decorator.Random;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Enemy extends Movables {
	public boolean hasSwitched = false;
	private AnimationHandler enemyAnimation;
	private TextureRegion frame;
	private float elapsedTime = 1f;
	int[][] previousRoom;
	
    private Body body;

	/**
	* Defines a Enemy object. Enemy extends Movables. 
	* X, Y represent where the enemy will show on the map.
	* W, H scale the sprite of the enemy respectivly. t scales all
	* math/tolerances within the function. All values must be given with respect 
	* to meters, not pixels. 
	*/
	public Enemy(float x, float y, float w, float h, Rectangle r, float t) {
		super(x, y, w, h, 0.07f, r, t);
		
		enemyAnimation = new AnimationHandler(); //Adds all animations for the enemy manager
		enemyAnimation.add("enemyE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("monsterE")));
		enemyAnimation.add("enemyW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("monsterW")));
		enemyAnimation.add("error", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("error")));
		enemyAnimation.setCurrent("enemyE");
		
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(x, y);

        // Create a body in the world using our definition
        body = GameScreen.world.createBody(bodyDef);

        // Now define the dimensions of the physics shape
        PolygonShape shape = new PolygonShape();
        // We are a box, so this makes sense, no?
        // Basically set the physics polygon to a box with the same dimensions as our sprite
        shape.setAsBox(this.getWidth()/2, this.getHeight()/2);

        // FixtureDef is a confusing expression for physical properties
        // Basically this is where you, in addition to defining the shape of the body
        // you also define it's properties like density, restitution and others we will see shortly
        // If you are wondering, density and area are used to calculate over all mass
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 10f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution= 1f;
        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
	}
	
	//this will be the base of the AI
	public void implementAction() {
		float[] pos = {50,50};
		this.circle(pos);
//		if (sniff() > 1 || hasSwitched) {
//			this.setAcceleration(-0.1f, 0.0f);
//			hasSwitched = true;
//			
//			if(sniff() < 0.2) {
//				hasSwitched = false;
//			}
//		}
//		else {
//			this.setAcceleration(0.1f, 0.0f);
//		}
		
		
		//this sort of stuff. Want to make a method that somehow
		//decides the direction of the enemy to go in.
	}
	
	public void circle(float[] playerPos) {
		float x = playerPos[0];
		float y = playerPos[1];
		float x2 = this.pos[0];
		float y2 = this.pos[1];
		float distance = (float)Math.sqrt(Math.pow((x2-x),2.0)+Math.pow((y2-y),2.0));
		double angle = (float)Math.atan2(y2-y, x2-x)*(180/Math.PI);
		x = (float) (x + -1*Math.cos(angle)*distance/2);
		y = (float) (y + -1*Math.sin(angle)*distance/2);
		this.setAcceleration(x, y, 1);
	}
	
	//Add more methods to help build complex behavior from simple actions IE:
	//Return should always be from 0.0 -> 1.0, to determine hierarchy of what to do
	public float sniff() {
		float temp1 = (this.pos[0] / 1000);
		return temp1;
	}
	
	
	@Override
	public void draw(SpriteBatch batch) {
		this.updateTick();
		//Gdx.graphics.getDeltaTime();
		if (elapsedTime > 0.3) {
			elapsedTime = 0;
			//enemyAnimation.setAnimationDuration(1000);
			
			
			if ((vel[0] >= 1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // East
				enemyAnimation.setCurrent("enemyE");
				
			} else if ((vel[0] <= -1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // West
				enemyAnimation.setCurrent("enemyW");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] >= 1)) { // North
				enemyAnimation.setCurrent("error");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] <= -1)) { // South
				enemyAnimation.setCurrent("error");
				
			} else if ((vel[0] > 0) && (vel[1] > 0)) { // Northeast
				enemyAnimation.setCurrent("error");
				
			} else if ((vel[0] > 0) && (vel[1] < 0)) { // Southeast
				enemyAnimation.setCurrent("error");
				
			} else if ((vel[0] < 0) && (vel[1] > 0)) { // Northwest
				enemyAnimation.setCurrent("error");
				
			} else if ((vel[0] < 0) && (vel[1] < 0)) { // Southwest
				enemyAnimation.setCurrent("error");
				
			} else if ((vel[0] == 0) && (vel[1] == 0)) { // Standing still 
				enemyAnimation.setCurrent("error");
				//enemyAnimation.setAnimationDuration(10000);
			}
			
		}		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		frame = enemyAnimation.getFrame();
		batch.draw(frame, pos[0], pos[1], this.getWidth(), this.getHeight());
	}
}
