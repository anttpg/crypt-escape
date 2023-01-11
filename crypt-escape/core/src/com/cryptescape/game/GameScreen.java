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
import com.cryptescape.game.entities.Enemy;
import com.cryptescape.game.entities.MobManager;
import com.cryptescape.game.entities.Player;
import com.cryptescape.game.entities.StatusManager;
import com.cryptescape.game.graphics.LightingManager;
import com.cryptescape.game.graphics.TransitionScreen;
import com.cryptescape.game.hud.PlayerHud;
import com.cryptescape.game.rooms.Interactable;
import com.cryptescape.game.rooms.Room;
import com.cryptescape.game.rooms.RoomGeneration;
import com.cryptescape.game.sounds.MusicManager;
import com.cryptescape.game.sounds.SfxManager;

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
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;

//import com.badlogic.gdx.*;



public class GameScreen implements Screen {
	
	// REMEMBER BOX2D WORKS IN METERS NOT PIXELS
	public MainCE game;
	public static Stage stage = new Stage(new ScreenViewport());
	public static World world = new World(new Vector2(0, 0), true);
	public static Group mainGroup = new Group();
	public static Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	public static List<List<Room>> rooms = new ArrayList<List<Room>>(); //Holds all the rooms
	
	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static TextureAtlas atlas;
	public static PlayerHud hud;
	public static float realWidth;
	public static float realHeight;
	
	private BitmapFont font;

	public static Enemy enemy;	
	public static Player player;
	
	private Fixture BASE_FLOOR;
	

	public static boolean debugPerspective = true;
	
	public MusicManager music;
	public static SfxManager sounds;
    public static boolean fade;
    
	float playerCounter = 0;
    private float[] oldP = new float[] { 0,0 };
    private float accumulator;
	
	
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		ContactManager.createCollisionListener();
		camera = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		camera.position.set(Constants.CAMERA_WIDTH/2, Constants.CAMERA_HEIGHT/2, 0);
		float maxPV = 2.1f;
		
		//Different types of viewports for debugging
		if(debugPerspective) {
			float scale = 1;
			maxPV = 10f;
			viewport = new ExtendViewport(Constants.VIEWPORT_WIDTH*scale, Constants.VIEWPORT_HEIGHT*scale, camera);
			//viewport = new ExtendViewport(Constants.VIEWPORT_WIDTH*15, Constants.VIEWPORT_HEIGHT*15, camera);
		}
		else
			viewport = new ExtendViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, camera);
		
		
		music = new MusicManager("soundDesign/music"); //Load Music
		sounds = new SfxManager("soundDesign/sfx"); //Load SFX

		
		FileHandle file = Gdx.files.internal("packedImages/bounds.txt"); //Loads interactable item boundaries
		
		try {
			Interactable.itemBounds = SaveReader.readObjectBounds(file.readString());
		} catch (IndexOutOfBoundsException e) {
			e.printStackTrace();
		}

	
		
		//DYNAMIC ACTOR GENERATION
		player = new Player(12f, 10f, 2f, null, maxPV); 
		mainGroup.addActor(player);
		
        hud = new PlayerHud(game.batch);
    	
        
		//enemy = new Enemy(4f, 4f, 0.95f, 0.95f, 100f);
		//mainGroup.addActor(enemy);
		
        stage.addActor(mainGroup);
        
		RoomGeneration.generateTemplates(); //Generate Rooms, and sets player starting room.
		
		InputHandler.createInput(); //Setup input
		
		LightingManager.createLights(); //Creates Lighting
		player.toFront();
		
		TimedEventManager.createEvents();
		StatusManager.injurePlayer(10, true, 30); //Temp testing bleeding effects
		
	}

	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.0f, 0.0f, 0.0f, 1);
		
		game.batch.setProjectionMatrix(camera.combined);
		camera.position.set(player.getX(), player.getY(), 0); //So camera follows player
		camera.update();
		
		
		//IMPORTANT <<< DO ALL RENDERING IN THE ORDER OF WHICH YOU WANT IT TO APPEAR. 
		// Ie: Enemy on top of Player on top of Room.
		player.setAcceleration((InputHandler.wasd[3]-InputHandler.wasd[1]), (InputHandler.wasd[0]-InputHandler.wasd[2]), InputHandler.sprint); //handles player movement

		
		game.batch.begin();
		player.getRoom().draw(game.batch); //draw the room that the player is currently in. This will also draw the player.
		
		
//		enemy.implementAction(); //decides what the enemy will do
//		enemy.draw(game.batch);
		
		game.batch.end(); 
		
		
//        try {
//            Thread.sleep(15);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        
		
		if(debugPerspective) 
		    debugRenderer.render(world, camera.combined);
		else  //Else render lighting
		    LightingManager.updateLights();
		
		
        accumulator += delta;
        while (accumulator+0.01f >= Constants.FRAME_SPEED) { //Do physics step until up to date
            accumulator -= Constants.FRAME_SPEED;
            player.update();
            stage.act();
            world.step(Constants.FRAME_SPEED, 6, 2);
            MobManager.update(); // Updates the decisions for each mob, and removes dead
            TransitionScreen.update();
            TimedEventManager.update();
        }
        
        //Update/Draw the game stage
        //viewport.apply();
        
        //Update/Draw the Hud
        hud.update(delta, game.batch);
        //music.update();
        
        if(fade) //Apply fade out effect last
            TransitionScreen.render(game.batch, stage);
        
	}
	
	
	public void debugScreenResize(int width, int height) {
        System.out.println("camera wid/h" + camera.viewportWidth + " " + camera.viewportHeight);
        System.out.println("width/height" + width + " " + height);
        System.out.println("stage wid/ht" + stage.getWidth() + "  " + stage.getHeight());
	}
	
	
	@Override
	public void resize(int width, int height) {
		//resize() will be called anytime you change your screen size(eg: when you screen orientation changes). 
		//resize() takes the actual width and height (320x480 etc), which is the pixel value, and converts it
        camera.viewportHeight = (Constants.CAMERA_WIDTH / width) * height;
        camera.update();
        viewport.update(width, height);
        hud.resize(width, height);
        hud.resize(width, height); //Called twice because it fixes a weird bug for some reason :/
        realWidth = width;
        realHeight = height;  
        
        debugScreenResize(width, height);
	}

	@Override
	public void show() {
		//music.playRandomSong();
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
		LightingManager.dispose();
		world.dispose();
		hud.dispose();
	}
}
