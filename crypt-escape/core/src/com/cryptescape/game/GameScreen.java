package com.cryptescape.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

//import com.badlogic.gdx.*;



public class GameScreen implements Screen {
	
	// REMEMBER BOX2D WORKS IN METERS NOT PIXELS
	public MainCE game;
	public static Stage stage = new Stage(new ScreenViewport());
	public static World world = new World(new Vector2(0, 0), true);
	public static Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	public static OrthographicCamera camera;
	public static TextureAtlas atlas;
	
	private BitmapFont font;

	public static Enemy enemy;	
	public static Player player;
	
	
	
	private Music ambiance;
	private int[] wasd = new int[] {0,0,0,0};
	public float sprint = 1; //changes when sprinting

	float playerCounter = 0;

	
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		camera = new OrthographicCamera(Constants.WIDTH, Constants.HEIGHT);
		camera.position.set(Constants.WIDTH/2, Constants.HEIGHT/2, 0);
		
		ambiance = Gdx.audio.newMusic(Gdx.files.internal("caveAmbiance.mp3"));
		ambiance.setLooping(true);
		
		atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas")); //loads images
	
		player = new Player(2f, 3f, 0.85f, 0.85f, 100f); 
		stage.addActor(player);
	
		enemy = new Enemy(4f, 4f, 0.95f, 0.95f, 100f);
		stage.addActor(enemy);
		
		
		ArrayList<Double> p = new ArrayList<Double>();
		Collections.addAll(p, 0.92, 0.04, 0.035, 0.005); // Probability of a interactable type
		String[] key = new String[] {"empty", "box", "puddle", "bat"}; //The cooresponding type
		
		ArrayList<Double> p2 = new ArrayList<Double>();
		Collections.addAll(p2, 0.92, 0.04, 0.035, 0.005); // Probability of room typeS
		String[] key2 = new String[] {"empty", "box", "puddle", "bat"}; // Type names
		
		AliasMethod roomType = new AliasMethod(p2);
		AliasMethod roomGen = new AliasMethod(p);
		// use roomGen.next() to get where in key[i] to use
		
		
		
		//All input is calculated per tick, and stored as 0 or 1
		Gdx.input.setInputProcessor(new InputAdapter() {	
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.W)
					wasd[0] = 1;
				if (keycode == Input.Keys.A)
					wasd[1] = 1;
				if (keycode == Input.Keys.S)
					wasd[2] = 1;
				if (keycode == Input.Keys.D)
					wasd[3] = 1;
				if (keycode == Input.Keys.SHIFT_LEFT)
					sprint = 1.6f;
//				if (keycode == Input.Keys.X)
//					KEY_X = true;
//				if (keycode == Input.Keys.Z)
//					KEY_Z = true;
				return false;
			}

			@Override
			public boolean keyUp(int keycode) {
				if (keycode == Input.Keys.W)
					wasd[0] = 0;
				if (keycode == Input.Keys.A)
					wasd[1] = 0;
				if (keycode == Input.Keys.S)
					wasd[2] = 0;
				if (keycode == Input.Keys.D)
					wasd[3] = 0;
				if (keycode == Input.Keys.SHIFT_LEFT)
					sprint = 1;
//				if (keycode == Input.Keys.X)
//					KEY_X = false;
//				if (keycode == Input.Keys.Z)
//					KEY_Z = false;
				return false;
			}
		});
	}

	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		
		game.batch.disableBlending(); //save resources when not needed
		
		//debugging tools ->
		game.font.draw(game.batch, "FPS: "+ Gdx.graphics.getFramesPerSecond(), 1f, Constants.HEIGHT-1f);
		game.font.draw(game.batch, "Player xV: " + player.xVel + "Player yV: " + player.yVel,  1f, Constants.HEIGHT-1.5f);
		game.font.draw(game.batch, "Player xA: " + player.xAcc + "Player yA: " + player.yAcc, 1f, Constants.HEIGHT-2f);
		game.font.draw(game.batch, player.jolt[0] + "  " + player.jolt[1] + "  "+ player.jolt[2] + "  "+ player.jolt[3], 1f, Constants.HEIGHT-2.5f);
		//game.font.draw(game.batch, player.debugPlayer(), 1f, Constants.HEIGHT-3f);
		
		//System.out.println("Player xV: " + player.xVel + "Player yV: " + player.yVel);
		System.out.println("Static constant fuckery: " + Constants.MAPSIZE);
		
		game.batch.enableBlending();
		
		
		player.setAcceleration((wasd[3]-wasd[1])*0.0013f, (wasd[0]-wasd[2])*0.0013f, sprint); //handles player movement
		player.draw(game.batch);
		
		enemy.implementAction(); //decides what the enemy will do
		enemy.draw(game.batch);
		
		game.batch.end();
		
        stage.act();
        stage.draw();
		debugRenderer.render(world, camera.combined);
		world.step(Constants.FRAME_SPEED, 6, 2);
		
//		try {
//		    Thread.sleep(50);                 //2000 milliseconds is one second.
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
		

	}

	@Override
	public void resize(int width, int height) {
		//resize() can be called anytime you change your screen size(eg: when you screen orientation changes). 
		//resize() takes the actual width and height (320x480 etc), which is the pixel value, and converts it
        camera.viewportHeight = (Constants.WIDTH / width) * height;
        camera.update();
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		ambiance.play();
		
		//log 
		Gdx.app.log("MainScreen","show");
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	
	}
}
