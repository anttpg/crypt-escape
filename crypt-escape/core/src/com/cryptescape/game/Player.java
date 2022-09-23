package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;


public class Player extends Movables {
	public boolean isRunning = false;
	private TextureRegion frame;
	private AnimationHandler playerAnimation;
	private float elapsedTime = 1f;
	private boolean changeAnimation = true;
	
    private Body body;
    private ParticleEffect effect;
	private TextureAtlas textureAtlas;

	
	public Player(float x, float y, float w, float h, Rectangle r) {
		super(x, y, w, h, 2.0f, r);
		
        //effects
		textureAtlas = new TextureAtlas();
		textureAtlas.addRegion("note",new TextureRegion(new Texture("Old Assets/notusable/note.png")));
		
		playerAnimation = new AnimationHandler(); //Adds all the animations for the player
		playerAnimation.add("playerN", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerN")));
		playerAnimation.add("playerS", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerS")));
		playerAnimation.add("playerE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerE")));
		playerAnimation.add("playerW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerW")));
		playerAnimation.add("playerNE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerNE")));
		playerAnimation.add("playerNW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerNW")));
		playerAnimation.add("playerSE", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerSE")));
		playerAnimation.add("playerSW", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerSW")));
		playerAnimation.add("error", new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("error")));
		playerAnimation.setCurrent("playerS");
		
        effect = new ParticleEffect();
        effect.load(Gdx.files.internal("Old Assets/notusable/bubleNote.p"), textureAtlas);
        effect.scaleEffect(2, 1);
        effect.setDuration(3);
        effect.setPosition(this.getWidth()/2+this.getX(),this.getHeight()/2+this.getY());
        effect.start();
        
        
        //physics body definitions
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
        fixtureDef.density = 5f;
        fixtureDef.friction = 0f;
        fixtureDef.restitution= 1f;
        Fixture fixture = body.createFixture(fixtureDef);

        // Shape is the only disposable of the lot, so get rid of it
        shape.dispose();
        this.setOrigin(this.getWidth()/2,this.getHeight()/2);
	}
	
		
	@Override
	public void draw(SpriteBatch batch) {
		this.updateTick();
		//Gdx.graphics.getDeltaTime();
		if (elapsedTime > 0.3) {
			elapsedTime = 0;
			playerAnimation.setAnimationDuration(1000);
			
			
			if ((vel[0] >= 1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // East
				playerAnimation.setCurrent("playerE");
				
			} else if ((vel[0] <= -1) && ((vel[1] <= 1) && (vel[1] >= -1)) ) { // West
				playerAnimation.setCurrent("playerW");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] >= 1)) { // North
				playerAnimation.setCurrent("playerN");
				
			} else if (((vel[0] <= 1) && (vel[0] >= -1) ) && (vel[1] <= -1)) { // South
				playerAnimation.setCurrent("playerS");
				
			} else if ((vel[0] > 0) && (vel[1] > 0)) { // Northeast
				playerAnimation.setCurrent("playerNE");
				
			} else if ((vel[0] > 0) && (vel[1] < 0)) { // Southeast
				playerAnimation.setCurrent("playerSE");
				
			} else if ((vel[0] < 0) && (vel[1] > 0)) { // Northwest
				playerAnimation.setCurrent("playerNW");
				
			} else if ((vel[0] < 0) && (vel[1] < 0)) { // Southwest
				playerAnimation.setCurrent("playerSW");
				
			} else if ((vel[0] == 0) && (vel[1] == 0)) { // Standing still 
				playerAnimation.setCurrent("playerS");
				playerAnimation.setAnimationDuration(10000);
			}
			
		}		
		elapsedTime += Gdx.graphics.getDeltaTime();
		
		frame = playerAnimation.getFrame();
		batch.draw(frame, pos[0], pos[1], this.getWidth(), this.getHeight());
		effect.draw(batch);
	}
	
	
    @Override
    public void act(float delta) {
        super.act(delta);
        this.setRotation(body.getAngle()*  MathUtils.radiansToDegrees);

        this.setPosition(body.getPosition().x-this.getWidth()/2,body.getPosition().y-this.getHeight()/2);
        effect.setPosition(this.getWidth()/2+this.getX(),this.getHeight()/2+this.getY());
        effect.update(delta);
    }
    
}
