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
	
	final MainCE game;
	private Stage stage = new Stage(new ScreenViewport());
	private World world = new World(new Vector2(0, 0), true);
	private Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	
	private OrthographicCamera camera;
	private BitmapFont font;
	private TextureAtlas atlas;
	public float FRAME_SPEED = 1/60f;

	private Texture enemySheet;
	private Rectangle enemyRect;
	private AnimationHandler enemyAnimation;
	public Enemy enemy;
	
	private Texture playerSheet;
	private Rectangle playerRect;
	private AnimationHandler playerAnimation;
	public Player player;
	
	private Music ambiance;
	private int[] wasd = new int[] {0,0,0,0};

	float playerCounter = 0;

	
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		ambiance = Gdx.audio.newMusic(Gdx.files.internal("caveAmbiance.mp3"));
		ambiance.setLooping(true);
		
		atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas"));
		
		playerAnimation = new AnimationHandler();
		playerAnimation.add("playerN", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerN")));
		playerAnimation.add("playerS", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerS")));
		playerAnimation.add("playerE", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerE")));
		playerAnimation.add("playerW", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerW")));
		playerAnimation.add("playerNE", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerNE")));
		playerAnimation.add("playerNW", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerNW")));
		playerAnimation.add("playerSE", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerSE")));
		playerAnimation.add("playerSW", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerSW")));
		playerAnimation.add("error", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("error")));
		playerAnimation.setCurrent("playerS");
		playerRect = new Rectangle();
		player = new Player(200, 200, 96, 96, playerAnimation, playerRect, world); 
		stage.addActor(player);
		
		enemyAnimation = new AnimationHandler();
		enemyAnimation.add("enemyE", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("monsterE")));
		enemyAnimation.add("enemyW", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("monsterW")));
		enemyAnimation.add("error", new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("error")));
		enemyAnimation.setCurrent("enemyE");
		enemyRect = new Rectangle();
		enemy = new Enemy(400, 400, 128, 128, enemyAnimation, enemyRect, world);
		stage.addActor(enemy);
		
		

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
		
		
		game.batch.disableBlending();
		//game.batch.draw(enemyRegion, 200, 200, 128, 128);
		game.font.draw(game.batch, "FPS: "+ Gdx.graphics.getFramesPerSecond(), 50, Gdx.graphics.getHeight()-180);
		game.font.draw(game.batch, "Player xV: " + player.vel[0] + "Player yV: " + player.vel[1],  50, Gdx.graphics.getHeight()-50);
		game.font.draw(game.batch, "Player xA: " + player.acc[0] + "Player yA: " + player.acc[1], 50, Gdx.graphics.getHeight()-80);
		game.font.draw(game.batch, player.jolt[0] + "  " + player.jolt[1] + "  "+ player.jolt[2] + "  "+ player.jolt[3], 50, Gdx.graphics.getHeight()-110);
		game.font.draw(game.batch, playerAnimation.toString(), 50, Gdx.graphics.getHeight()-140);
		
		
		game.batch.enableBlending();
		
		//handles movement 		
		player.setAcceleration((wasd[3]-wasd[1])*0.13f, (wasd[0]-wasd[2])*0.13f);
		player.draw(game.batch);
		enemy.implementAction();
		enemy.draw(game.batch);
		
		game.batch.end();
		
        stage.act();
        stage.draw();
		debugRenderer.render(world, camera.combined);
		world.step(FRAME_SPEED, 6, 2);
		
//		try {
//		    Thread.sleep(50);                 //2000 milliseconds is one second.
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
//	
		

	}

	@Override
	public void resize(int width, int height) {
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
