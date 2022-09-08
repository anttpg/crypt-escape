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
		enemyRegion = new TextureRegion(enemySheet, 20, 20, 50, 50);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(enemySheet, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		enemySheet.dispose();
	}
}
