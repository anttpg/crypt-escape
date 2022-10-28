package com.cryptescape.game;

import java.util.ArrayList;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.ScreenUtils;

public class Menu implements Screen {

	final MainCE game;
	private Stage stage;
	private static int size[];
	
	int HEIGHT = Gdx.graphics.getHeight(); //Used to keep everything on the right scale
	int WIDTH = Gdx.graphics.getWidth();
	float SQUARE = (HEIGHT/10);
	
	Array<Actor> items;
	OrthographicCamera camera;
	ExtendViewport viewport;
	
	float sourceX = 0;
	float sourceX2 = 0;
	float sourceX3 = 0;
	float xCorner;
	float yCorner;
	
	float timer = 0;
	
	final float velocity = 175;
	Texture groundReel;
	Texture grassReel;
	Texture treeReel;
	Animation playerWalking;


	public Menu(final MainCE gam) {
		game = gam;
		size = new int[] { 1, 1 }; // testing constants

		camera = new OrthographicCamera();
		camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

		stage = new Stage(viewport);
		camera.update();

		// Temp disabled
//		Scanner s = new Scanner(System.in);
//		System.out.println("Do you want launch a graphical menu? (y/n)");
//		if (s.nextLine().equals("y")) {
		
		
		GameScreen.atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas")); //loads images for later
		
		playerWalking = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerW"));
		playerWalking.setFrameDuration(Constants.FRAME_SPEED*10);
		playerWalking.setPlayMode(Animation.PlayMode.LOOP);

		groundReel = new Texture(Gdx.files.internal("groundReel.png"));
	    groundReel.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);

		grassReel = new Texture(Gdx.files.internal("grassReel.png"));
	    grassReel.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
		
		treeReel = new Texture(Gdx.files.internal("treeReel.png"));
	    treeReel.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.ClampToEdge);
	    
	    
	    
		// All this is a temp menu start screen.
		Skin skin = new Skin(Gdx.files.internal("Old Assets/skin/glassy-ui.json"));
		Label title = new Label("Crypt Escape V1.1", skin, "big-black");
		title.setAlignment(Align.center);
		title.setY(HEIGHT * 2 / 3);
		title.setWidth(WIDTH);
		stage.addActor(title);

		TextButton playButton = new TextButton("Start!", skin);
		playButton.setWidth(WIDTH / 2);
		playButton.setPosition(WIDTH / 2 - playButton.getWidth() / 2,
				HEIGHT / 2 - playButton.getHeight() / 2);

		playButton.addListener(new InputListener() {
			@Override
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				game.setScreen(new GameScreen(game));
			}

			@Override
			public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				return true;
			}
		});

		stage.addActor(playButton);
		items = stage.getActors();

	}

	@Override
	public void render(float delta) {
		ScreenUtils.clear(0, 0.2f, 0.8f, 1);
		game.batch.setProjectionMatrix(camera.combined);
		
		timer += Gdx.graphics.getDeltaTime();
		xCorner = camera.position.x - camera.viewportWidth/2f;
		yCorner = camera.position.y - camera.viewportHeight/2f;
		
		game.batch.begin();
		
		
		//All reels drawn here
		sourceX3 = (sourceX3 - Gdx.graphics.getDeltaTime() * velocity/6f) % groundReel.getWidth();
		game.batch.draw(treeReel,
	               // position and size of texture
	               xCorner, yCorner, treeReel.getWidth(), SQUARE*8f, 
	               // srcX, srcY, srcWidth, srcHeight
	               (int) sourceX3, 0, treeReel.getWidth(), treeReel.getHeight(),
	               // flipX, flipY
	               false, false);
		
		sourceX2 = (sourceX2 - Gdx.graphics.getDeltaTime() * velocity/3f) % groundReel.getWidth();
		game.batch.draw(groundReel,
	               // position and size of texture
	               xCorner, yCorner, groundReel.getWidth(), SQUARE*1.5f, 
	               // srcX, srcY, srcWidth, srcHeight
	               (int) sourceX2, 0, groundReel.getWidth(), groundReel.getHeight(),
	               // flipX, flipY
	               false, false);
		
		game.batch.draw((TextureRegion)playerWalking.getKeyFrame(timer), xCorner + camera.viewportWidth/1.25f, 
				yCorner + 5, SQUARE*2, SQUARE*2);
		
		sourceX = (sourceX - Gdx.graphics.getDeltaTime() * velocity) % groundReel.getWidth();
		game.batch.draw(grassReel,
	               // position and size of texture
	               xCorner, yCorner, grassReel.getWidth(), SQUARE*4, 
	               // srcX, srcY, srcWidth, srcHeight
	               (int) sourceX, 0, grassReel.getWidth(), grassReel.getHeight(),
	               // flipX, flipY
	               false, false);
		
		
		
		game.batch.end();
	
		
		stage.act();
		stage.draw();
		camera.update();
	}

	// Y, X map size gen
	public static int[] getSize() {
		if (size[0] >= 3 && size[1] >= 3) {
			return size;
		} else {
			return new int[] { 3, 3 };
		}

	}

	@Override
	public void resize(int w, int h) {		
		camera.viewportHeight = (WIDTH / w) * h;
		camera.position.set(w / 2, h / 2, 0);
		viewport.update(w, h);
		camera.update();
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(stage);
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
		stage.dispose();
	}

}