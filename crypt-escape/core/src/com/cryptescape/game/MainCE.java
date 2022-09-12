package com.cryptescape.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MainCE extends Game {

	SpriteBatch batch;
	BitmapFont font;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont(); // libGDX's default Arial font

		this.setScreen(new Menu(this));
	}

	public void render() {
		super.render(); // important!
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}
