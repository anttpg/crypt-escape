package com.cryptescape.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;

//import com.badlogic.gdx.graphics.

public class MainCE extends ApplicationAdapter {
	private OrthographicCamera camera;
	private SpriteBatch batch;

	public Texture enemySheet;
	public TextureRegion enemyRegion;
	private Rectangle sqr;
	private Texture sqrImage;

	private Music ambiance;
	private boolean KEY_W, KEY_A, KEY_S, KEY_D;
	private Player player = new Player(200,200);

	@Override
	public void create() {
		camera = new OrthographicCamera();
		camera.setToOrtho(false, 1600, 900);

		batch = new SpriteBatch();
		enemySheet = new Texture("monster.png");
		enemyRegion = new TextureRegion(enemySheet, 0, 0, 32, 32);

		// xPos, yPos within texture. IE: 0,0 is Image1 32,0 is Image2, ect

		ambiance = Gdx.audio.newMusic(Gdx.files.internal("caveAmbiance.mp3"));
		ambiance.setLooping(true);
		ambiance.play();
		
		
		
		sqrImage = new Texture(Gdx.files.internal("testsqr.png"));
		sqr = new Rectangle();
		sqr.x = 800 / 2 - 64 / 2;
		sqr.y = 20;
		sqr.width = 64;
		sqr.height = 64;

		
		Gdx.input.setInputProcessor(new InputAdapter() {	
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Input.Keys.W)
					KEY_W = true;
				if (keycode == Input.Keys.A)
					KEY_A = true;
				if (keycode == Input.Keys.S)
					KEY_S = true;
				if (keycode == Input.Keys.D)
					KEY_D = true;
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
					KEY_W = false;
				if (keycode == Input.Keys.A)
					KEY_A = false;
				if (keycode == Input.Keys.S)
					KEY_S = false;
				if (keycode == Input.Keys.D)
					KEY_D = false;
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
	public void render() {
		ScreenUtils.clear(1, 1, 1, 1);
		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		batch.draw(sqrImage, sqr.x, sqr.y);
		batch.draw(enemyRegion, 200, 200, 128, 128);
		// draws at x, y from bottom left corner. Then stretches to fit 128x128 pixels
		batch.end();
		
		
		//handles movement 
		if (KEY_W) { player.setAccel(player.acc[0], 0.1); } 
		else { player.setAccel(player.acc[0], 0); }
		
		if (KEY_A) { player.setAccel(-0.1, player.acc[1]); } 
		else { player.setAccel(0, player.acc[1]); }
		
		if (KEY_S) { player.setAccel(player.acc[0], -0.1); } 
		else { player.setAccel(player.acc[0], 0); }
		
		if (KEY_D) { player.setAccel(0.1, player.acc[1]); } 
		else { player.setAccel(0, player.acc[1]); }
		
		player.updateTick();
		
		sqr.x = (float) player.getPos()[0];
		sqr.y = (float) player.getPos()[1];
		

	}

	@Override
	public void dispose() {
		batch.dispose();
		enemySheet.dispose();
	}
}
