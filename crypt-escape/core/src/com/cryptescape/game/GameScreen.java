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
import com.badlogic.gdx.math.Rectangle;

//import com.badlogic.gdx.*;



public class GameScreen implements Screen {
	final MainCE game;
	
	private OrthographicCamera camera;
	public BitmapFont font;
	public TextureAtlas atlas;
	public float FRAME_SPEED = 1/60f;

	private Texture enemySheet;
	public Enemy enemy;
	
	private Texture playerSheet;
	public Player player;
	private AnimationHandler playerAnimation;
	private String pN, pE, pS, pW, pNE, pNW, pSE, pSW;
	
	private Rectangle playerRect;
	
	private Music ambiance;
	private int[] wasd = new int[] {0,0,0,0};

	float playerCounter = 0;
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas"));
		
		playerAnimation = new AnimationHandler();
		playerAnimation.add(pN, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerN")));
		playerAnimation.add(pS, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerS")));
		playerAnimation.add(pE, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerE")));
		playerAnimation.add(pW, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerW")));
		playerAnimation.add(pNE, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerNE")));
		playerAnimation.add(pNW, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerNW")));
		playerAnimation.add(pSE, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerSE")));
		playerAnimation.add(pSW, new Animation<TextureRegion>(FRAME_SPEED, atlas.findRegions("playerSW")));
		player = new Player(200, 200, playerAnimation); 
		
		enemySheet = new Texture("monster2.png");
		enemy = new Enemy(400, 400, enemySheet);
		
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		ambiance = Gdx.audio.newMusic(Gdx.files.internal("caveAmbiance.mp3"));
		ambiance.setLooping(true);
		
		
		
		playerRect = new Rectangle();
		playerRect.width = 32;
		playerRect.height = 32;

		
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
		game.font.draw(game.batch, Integer.toString(player.spriteStage().getRegionX()), 50, Gdx.graphics.getHeight()-140);
		
		
		game.batch.enableBlending();
		game.batch.draw(player.spriteStage(), playerRect.x, playerRect.y, 96, 96);
		// draws at x, y from bottom left corner. Then stretches to fit 128x128 pixels
		game.batch.end();
		
		
		//handles movement 		
		player.setAccel((wasd[3]-wasd[1])*0.2, (wasd[0]-wasd[2])*0.2);
		player.updateTick();
		
		playerRect.x = (float) player.getPos()[0];
		playerRect.y = (float) player.getPos()[1];
		
//		try {
//		    Thread.sleep(300);                 //2000 milliseconds is one second.
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
	
		

	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		ambiance.play();
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
