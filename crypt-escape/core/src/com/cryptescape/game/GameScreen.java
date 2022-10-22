package com.cryptescape.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.Filter;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;

//import com.badlogic.gdx.*;



public class GameScreen implements Screen {
	
	// REMEMBER BOX2D WORKS IN METERS NOT PIXELS
	public MainCE game;
	public static Stage stage = new Stage(new ScreenViewport());
	public static World world = new World(new Vector2(0, 0), true);
	public static Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public static List<List<Room>> rooms = new ArrayList<List<Room>>(); //Holds all the rooms
	
	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static TextureAtlas atlas;
	
	private BitmapFont font;
	private Vector2 mousePosition = new Vector2(0, 0);
	private Vector2 relativeMousePosition = new Vector2(0, 0);

	public static Enemy enemy;	
	public static Player player;
	
	private Fixture BASE_FLOOR;
	
	private RayHandler rayHandler;
	private PointLight playerLight;
	private ConeLight playerFlashlight;

	private boolean debugPerspective = false;
	private boolean runOnceTempDebugVariable = true;
	
	private MusicManager music;
	
	private int[] wasd = new int[] {0,0,0,0};
	public float sprint = 1; //changes when sprinting

	float playerCounter = 0;
	
	
	
	
	
	
	

	
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		camera = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		camera.position.set(Constants.CAMERA_WIDTH/2, Constants.CAMERA_HEIGHT/2, 0);
		
		//Different types of viewports for debugging
		if(debugPerspective)
			//viewport = new ExtendViewport(Constants.VIEWPORT_WIDTH*15, Constants.VIEWPORT_HEIGHT*15, camera);
			viewport = new ExtendViewport(Constants.VIEWPORT_WIDTH*3, Constants.VIEWPORT_HEIGHT*3, camera);
		else
			viewport = new ExtendViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
		
		
		try (Stream<Path> paths = Files.walk(Paths.get(Gdx.files.internal("soundDesign/music").file().getAbsolutePath()))) {
		    paths
		    .filter(Files::isRegularFile)
		    .forEach(System.out::println);
		} 
		catch(Exception e) {
			e.printStackTrace();
		}
		
//		Gdx.files.
//		
//		ambiance = Gdx.audio.newMusic(Gdx.files.internal("COPYRIGHTED-SubwooferLullaby.mp3"));
//		ambiance2 = Gdx.audio.newMusic(Gdx.files.internal("COPYRIGHTED-caveAmbiance.mp3"));
//		ambiance2.setVolume(0.15f);
//		ambiance.setLooping(true);
//		ambiance2.setLooping(true);
		
		atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas")); //loads images
		

		FileHandle file = Gdx.files.internal("packedImages/bounds.txt");
		
		try {
			Interactable.itemBounds = SaveReader.readObjectBounds(file.readString());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	
		
		//DYNAMIC ACTOR GENERATION
		player = new Player(12f, 10f, 100f, null); 
		stage.addActor(player);
		
		RayHandler.useDiffuseLight(true);
		rayHandler = new RayHandler(world);
		rayHandler.setAmbientLight(0f);
		rayHandler.setShadows(true);
		rayHandler.setBlur(true);
		
		
		//rays, color, how far out to project 
		playerLight = new PointLight(rayHandler, 100, null, 2.5f, player.xPos, player.yPos);
		playerLight.setSoftnessLength(2f);
		playerLight.setXray(true);
		
		
		playerFlashlight = new ConeLight(rayHandler, 300, Color.WHITE, 7f, player.xPos, player.yPos, 0, 20f);
		playerFlashlight.setSoftnessLength(2f);
		playerFlashlight.setXray(false);
	
		//enemy = new Enemy(4f, 4f, 0.95f, 0.95f, 100f);
		//stage.addActor(enemy);
		
		//Generate Rooms
		RoomGeneration.generateTemplates();
		
		boolean roomFound = false;
		while(!roomFound) {
			Random r = new Random();
			int x = r.nextInt(Constants.NUM_OF_ROOMS_X-2)+1;
			int y = r.nextInt(Constants.NUM_OF_ROOMS_Y-2)+1;
			
			if(!rooms.get(y).get(x).getRoomType().equals("blocked"))  {
				GameScreen.player.setStartingRoom(GameScreen.rooms.get(y).get(x));
				player.setPos(rooms.get(y).get(x).getRoomLocation()[1]+ Constants.CAMERA_WIDTH/3, rooms.get(y).get(x).getRoomLocation()[0]+Constants.CAMERA_HEIGHT/2);
				System.out.println(player.xPos +"     "+ player.yPos);
				roomFound = true;
			}
		}
		
		
		
		//INPUT HANDLING
		//All calculated per tick, and stored as 0 or 1
		
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
				if (keycode == Input.Keys.SHIFT_LEFT)
					sprint = 1.6f; //Except here since its a multiplier
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
				if (keycode == Input.Keys.SHIFT_LEFT)
					sprint = 1;
				return false;
			}
			
			@Override
			public boolean mouseMoved(int mouseX, int mouseY) {
				relativeMousePosition.x = mouseX;
				relativeMousePosition.y = mouseY;
				return false;
			}
		});
	}

	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		
		game.batch.disableBlending(); //save resources when not needed
		//debugging tools ->
		game.font.draw(game.batch, "FPS: "+ Gdx.graphics.getFramesPerSecond(), 1f, Constants.CAMERA_HEIGHT-1f);
		game.font.draw(game.batch, "Player xV: " + player.xVel + "Player yV: " + player.yVel,  1f, Constants.CAMERA_HEIGHT-1.5f);
		game.font.draw(game.batch, "Player xA: " + player.xAcc + "Player yA: " + player.yAcc, 1f, Constants.CAMERA_HEIGHT-2f);
		//game.font.draw(game.batch, player.debugPlayer(), 1f, Constants.HEIGHT-3f);
		
		
		game.batch.enableBlending();
		
		//IMPORTANT <<< DO ALL RENDERING IN THE ORDER OF WHICH YOU WANT IT TO APPEAR
		// Ie: Enemy on top of Player on top of Room.
		
		player.getRoom().draw(game.batch); //draw the room that the player is currently in
		
		
		player.setAcceleration((wasd[3]-wasd[1]), (wasd[0]-wasd[2]), sprint); //handles player movement
		player.draw(game.batch);

		
//		enemy.implementAction(); //decides what the enemy will do
//		enemy.draw(game.batch);
		
		game.batch.end();
		
		
        stage.act();
        stage.draw();
        camera.position.set(player.xPos, player.yPos, 0); //So camera follows player
		
        mousePosition.x = (player.xPos - (Constants.VIEWPORT_WIDTH/2)) + (((float)relativeMousePosition.x/Gdx.graphics.getWidth()) * Constants.VIEWPORT_WIDTH);
		mousePosition.y = (player.yPos + (Constants.VIEWPORT_HEIGHT/2)) - (((float)relativeMousePosition.y/Gdx.graphics.getHeight()) * Constants.VIEWPORT_HEIGHT);

		
		
		if(debugPerspective) {
			debugRenderer.render(world, camera.combined);
		}
		

		else {
			playerLight.setPosition(player.xPos, player.yPos);
			playerLight.setDistance(player.getCandleLevel());
			
			playerFlashlight.setDistance(player.getBatteryLevel());
			playerFlashlight.setPosition(player.xPos, player.yPos);
	        playerFlashlight.setDirection(getAngle(player.xPos, player.yPos, mousePosition));
	        
			rayHandler.setCombinedMatrix(camera);
			rayHandler.updateAndRender();
		}
		
		world.step(Constants.FRAME_SPEED, 6, 2);
		
		
//		try {
//		    Thread.sleep(50);                 //2000 milliseconds is one second.
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
		

	}
	
	
	
	
	
	public float getAngle(Float x, Float y, Vector2 target) {
	    float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));
	    if(angle < 0){
	        angle += 360;
	    }
	    return angle;
	}
	
	@Override
	public void resize(int width, int height) {
		//resize() will be called anytime you change your screen size(eg: when you screen orientation changes). 
		//resize() takes the actual width and height (320x480 etc), which is the pixel value, and converts it
        camera.viewportHeight = (Constants.CAMERA_WIDTH / width) * height;
        camera.update();
        viewport.update(width, height);
        
	}

	@Override
	public void show() {
		
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
		rayHandler.dispose();
		world.dispose();
			
	}
}
