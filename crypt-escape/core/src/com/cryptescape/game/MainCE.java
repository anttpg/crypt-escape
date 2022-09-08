package com.cryptescape.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
//import com.badlogic.gdx.graphics.

public class MainCE extends ApplicationAdapter {
	private SpriteBatch batch;
	public Texture enemySheet;
	private TextureRegion enemyRegion;
	
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		//img = new Texture("badlogic.jpg");
		enemySheet = new Texture("monster.png");
		enemyRegion = new TextureRegion(enemySheet, 0, 0, 32, 32);
		// xPos, yPos within texture. IE: 0,0 is Image1 32,0 is Image2, ect
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 1, 1, 1);
		batch.begin();
		batch.draw(enemyRegion, 100, 100, 128, 128);
		// draws at x, y from bottom left corner. Then stretches to fit 128x128 pixels
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		enemySheet.dispose();
	}
}
