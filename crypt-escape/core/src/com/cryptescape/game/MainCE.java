package com.cryptescape.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
		
		
		if(Gdx.input.isKeyPressed(Input.Keys.W)) sqr.y += 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.A)) sqr.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.S)) sqr.y -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.D)) sqr.x += 200 * Gdx.graphics.getDeltaTime();
		
	}

	@Override
	public void dispose() {
		batch.dispose();
		enemySheet.dispose();
	}
}
