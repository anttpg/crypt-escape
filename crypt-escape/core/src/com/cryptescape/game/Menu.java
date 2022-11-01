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
	
	boolean pressed = false;
	
	float sourceX = 0;
	float sourceX2 = 0;
	float sourceX3 = 0;
	float xCorner;
	float yCorner;
	
	float timer = 0;
	float fallTimer = 0;
	
	final float velocity = 175;
	Texture groundReel;
	Texture grassReel;
	Texture treeReel;
	Animation<TextureRegion> playerWalking;
	Animation<TextureRegion> playerFalling;
	Animation<TextureRegion> hole;

	public Menu(final MainCE gam) {
		game = gam;
		size = new int[] { 6, 4 }; // testing constants

		camera = new OrthographicCamera();
		camera.position.set(WIDTH / 2, HEIGHT / 2, 0);
		viewport = new ExtendViewport(WIDTH, HEIGHT, camera);

		stage = new Stage(viewport);
		camera.update();

		
//		Scanner s = new Scanner(System.in);
//		System.out.println("Do you want launch a graphical menu? (y/n)");
//		if (s.nextLine().equals("n")) {
//			pressed = true;
//			fallTimer = 15;
//		}
		
		pressed = true;
		fallTimer = 15;
		
		GameScreen.atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas")); //loads images for later
		
		playerWalking = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerW"));
		playerWalking.setFrameDuration(Constants.FRAME_SPEED*10);
		playerWalking.setPlayMode(Animation.PlayMode.LOOP);
		
		playerFalling = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("playerFalling"));
		playerFalling.setFrameDuration(Constants.FRAME_SPEED*6);
		
		hole = new Animation<TextureRegion>(Constants.FRAME_SPEED, GameScreen.atlas.findRegions("hole"));
		hole.setFrameDuration(Constants.FRAME_SPEED*3);
		

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
				pressed = true;
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
		
		xCorner = camera.position.x - camera.viewportWidth/2f;
		yCorner = camera.position.y - camera.viewportHeight/2f;
		
		
		game.batch.begin();
		
		if(pressed) {
			if(fallTimer > 2) 
				game.setScreen(new GameScreen(game));
			
			fallTimer += Gdx.graphics.getDeltaTime();
		}
		
		else {
			timer += Gdx.graphics.getDeltaTime();
			sourceX3 = (sourceX3 - Gdx.graphics.getDeltaTime() * velocity/6f) % groundReel.getWidth();
			sourceX2 = (sourceX2 - Gdx.graphics.getDeltaTime() * velocity/3f) % groundReel.getWidth();
			sourceX = (sourceX - Gdx.graphics.getDeltaTime() * velocity) % groundReel.getWidth();
		}
		
		
		//All reels drawn here
		drawReel(treeReel, 8f, sourceX3);
		drawReel(groundReel, 2.5f, sourceX2);

		
		if(!pressed) 
			game.batch.draw(playerWalking.getKeyFrame(timer), 
					xCorner + camera.viewportWidth/2f, yCorner + 5, SQUARE*2, SQUARE*2);
		
		else {
			game.batch.draw(hole.getKeyFrame(fallTimer), xCorner + camera.viewportWidth/2f, 
					yCorner + 5, SQUARE*2, SQUARE*2);
			game.batch.draw(playerFalling.getKeyFrame(fallTimer), xCorner + camera.viewportWidth/2f, 
					yCorner + 10, SQUARE*2, SQUARE*2);
		}
		
		drawReel(grassReel, 4f, sourceX);

		
		
		game.batch.end();
	
		
		stage.act();
		stage.draw();
		camera.update();
	}
	
	
	public void drawReel(Texture reel, float scale, float source) {
		game.batch.draw(reel,
	               // position and size of texture
	               xCorner, yCorner, reel.getWidth(), SQUARE*scale, 
	               // srcX, srcY, srcWidth, srcHeight
	               (int) source, 0, reel.getWidth(), reel.getHeight(),
	               // flipX, flipY
	               false, false);
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
		groundReel.dispose();
		grassReel.dispose();
		treeReel.dispose();
		
	}

}