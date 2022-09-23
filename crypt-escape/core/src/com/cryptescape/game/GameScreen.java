package com.cryptescape.game;

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

	private Texture enemySheet;
	private Rectangle enemyRect;
	public static Enemy enemy;
	
	private Texture playerSheet;
	private Rectangle playerRect;
	public static Player player;
	
	
	
	private Music ambiance;
	private int[] wasd = new int[] {0,0,0,0};

	float playerCounter = 0;

	
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		ambiance = Gdx.audio.newMusic(Gdx.files.internal("caveAmbiance.mp3"));
		ambiance.setLooping(true);
		
		atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas")); //loads images
	
		playerRect = new Rectangle();
		player = new Player(1, 1, 2.5f, 2.5f, playerRect); 
		stage.addActor(player);
		
		enemyAnimation = new AnimationHandler(); //Adds all animations for the enemy manager
		enemyAnimation.add("enemyE", new Animation<TextureRegion>(Constants.FRAME_SPEED, atlas.findRegions("monsterE")));
		enemyAnimation.add("enemyW", new Animation<TextureRegion>(Constants.FRAME_SPEED, atlas.findRegions("monsterW")));
		enemyAnimation.add("error", new Animation<TextureRegion>(Constants.FRAME_SPEED, atlas.findRegions("error")));
		enemyAnimation.setCurrent("enemyE");
		enemyRect = new Rectangle();
		enemy = new Enemy(1, 1, 2.5f, 2.5f, enemyAnimation, enemyRect);
		stage.addActor(enemy);
		

		
		
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
//				if (keycode == Input.Keys.SPACE)
//					KEY_SPACE = true;
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
//				if (keycode == Input.Keys.SPACE)
//					KEY_SPACE = false;
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
		game.font.draw(game.batch, "FPS: "+ Gdx.graphics.getFramesPerSecond(), 50, Gdx.graphics.getHeight()-180);
		game.font.draw(game.batch, "Player xV: " + player.vel[0] + "Player yV: " + player.vel[1],  50, Gdx.graphics.getHeight()-50);
		game.font.draw(game.batch, "Player xA: " + player.acc[0] + "Player yA: " + player.acc[1], 50, Gdx.graphics.getHeight()-80);
		game.font.draw(game.batch, player.jolt[0] + "  " + player.jolt[1] + "  "+ player.jolt[2] + "  "+ player.jolt[3], 50, Gdx.graphics.getHeight()-110);
		game.font.draw(game.batch, playerAnimation.toString(), 50, Gdx.graphics.getHeight()-140);
		
		
		game.batch.enableBlending();
		
		
		player.setAcceleration((wasd[3]-wasd[1])*0.13f, (wasd[0]-wasd[2])*0.13f); //handles player movement
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
//	
		

	}

	@Override
	public void resize(int width, int height) {
		//resize() can be called anytime you change your screen size(eg: when you screen orientation changes). 
		//resize() takes the actual width and height (320x480 etc), which is the pixel value, and converts it
        camera.viewportHeight = (Constants.VIEWPORT_WIDTH / width) * height;
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
		enemySheet.dispose();
	
	}
}
