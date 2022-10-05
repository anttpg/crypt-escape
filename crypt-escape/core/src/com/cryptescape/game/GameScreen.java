package com.cryptescape.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
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
	public static TextureAtlas atlas;
	
	private BitmapFont font;

	public static Enemy enemy;	
	public static Player player;
	public static TextureRegion BACKGROUND;
	
	
	private Music ambiance;
	private int[] wasd = new int[] {0,0,0,0};
	public float sprint = 1; //changes when sprinting

	float playerCounter = 0;

	
	
	public GameScreen(final MainCE gam) {
		this.game = gam;
		
		camera = new OrthographicCamera(Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		camera.position.set(Constants.CAMERA_WIDTH/2, Constants.CAMERA_HEIGHT/2, 0);
		
		ambiance = Gdx.audio.newMusic(Gdx.files.internal("caveAmbiance.mp3"));
		ambiance.setLooping(true);
		
		atlas = new TextureAtlas(Gdx.files.internal("packedImages/pack.atlas")); //loads images
	
		
		//DYNAMIC ACTOR GENERATION
		player = new Player(12f, 10f, 0.85f, 0.85f, 100f, null); 
		stage.addActor(player);
	
		enemy = new Enemy(4f, 4f, 0.95f, 0.95f, 100f);
		stage.addActor(enemy);
		
		Texture b = new Texture(Gdx.files.internal("imageAssets/stone.png"));
		b.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		BACKGROUND = new TextureRegion(b);
		
		//X,Y, then SIZE AFTER REPEAT
		BACKGROUND.setRegion(0, 0, Constants.CAMERA_WIDTH - Constants.X_BUFFER*2, Constants.CAMERA_HEIGHT - Constants.Y_BUFFER*2); 
		
		
		// ROOM GENERATION BELOW
		int[] p = new int[] {92, 4, 3, 1}; // Probability of a interactable type
		String[] key = new String[] {"empty", "box", "puddle", "bat"}; //The cooresponding type
		
		RandomCollection<String> roomItemGen = new RandomCollection<String>();
		for(int i = 0; i < p.length; i++) {
			roomItemGen.add(p[i], key[i]);
		}
		
		
		int[] p2 = new int[] {75, 5, 2, 2, 6, 6, 2, 2}; // Probability of room type
		String[] key2 = new String[] {"open", "blocked", "bN3", "bS3", "bW1", "bE1", "bW3", "bE3"}; 
		// Type names, open means all 4 doors are usable [T,T,T,T]. Blocked is the opposite [F,F,F,F]
		// b stands for blocked, and then follows the direction and number of doors blocked
		// EX: bN3 means the northmost 3 doors of that room are blocked. (the east, west, and north doors [F,F,T,F]
		
		RandomCollection<Integer> roomTypeGen = new RandomCollection<Integer>();
		for(int i = 0; i < p.length; i++) {
			roomTypeGen.add(p2[i], i);
		}

		// use ##.next() to get where in key[i] to use
		String item;
		String roomType;
		String[][] seed = new String[Constants.Y_TILES][Constants.X_TILES];
		ArrayList<String[][]> pregenTemplate = new ArrayList<String[][]>();

		
		//Fill pregens of room types (Walls, door, blocked, ect)
		for(String k : key2) {
			for(int y = 0; y < Constants.Y_TILES; y++) { //Loop through and fill the boundaries
				for(int x = 0; x < Constants.X_TILES; x++) {
					seed[y][x] = "empty";
					
					//NORTH-SOUTH
					if(y == 0) { //NORTH FACING
						if(x == (Constants.X_TILES/2) || x == (Constants.X_TILES/2)-1) // If doorway
							seed[y][x] = "northDoor";
						else  // regular wall
							seed[y][x] = "northWall";	
					}
					else if(y == Constants.Y_TILES-1) { // SOUTH FACING
						if(x == (Constants.X_TILES/2) || x == (Constants.X_TILES/2)-1) 
							seed[y][x] = "southDoor";
						else  
	 						seed[y][x] = "southWall";	
					}
					
					//EAST-WEST
					else if(x == 0) { //WEST FACING
						if(y == (Constants.Y_TILES/2) || y == (Constants.Y_TILES/2)-1) // If doorway
							seed[y][x] = "westDoor";
						else  // regular wall
							seed[y][x] = "westWall";
						
					}
					else if(x == Constants.X_TILES-1) { //EAST FACING
						if(y == (Constants.Y_TILES/2) || y == (Constants.Y_TILES/2)-1) 
							seed[y][x] = "eastDoor";
						else  
	 						seed[y][x] = "eastWall";
					}

					
					// Type modifiers: 
					if(k.equals("blocked")) 
						seed[y][x] = "blocked";
					
					
					else if(k.equals("bN3")) { //North 3 doors are blocked
						if(y < (Constants.Y_TILES/2) + 3) seed[y][x] = "blocked";  
						if (y == (Constants.Y_TILES/2)+3) seed[y][x] = "northWall"; 
					}
					
					else if(k.equals("bS3")) { // South 3 doors are blocked
						if(y > (Constants.Y_TILES/2) - 3) seed[y][x] = "blocked"; 
						if (y == (Constants.Y_TILES/2)-3) seed[y][x] = "southWall"; 
					}
					
					else if(k.equals("bW1")) { // West door is blocked
						if(x < (Constants.X_TILES/2) - 3)  seed[y][x] = "blocked"; 
						if(x == (Constants.X_TILES/2) - 3) seed[y][x] = "westWall";
					}
					
					else if(k.equals("bE1")) { // East door is blocked
						if(x > (Constants.X_TILES/2) + 3)  seed[y][x] = "blocked"; 
						if(x == (Constants.X_TILES/2) + 3) seed[y][x] = "eastWall";
					}

					else if(k.equals("bW3")) { // West 3 doors are blocked
						if(x < (Constants.X_TILES/2) + 3)  seed[y][x] = "blocked"; 
						if(x == (Constants.X_TILES/2) + 3) seed[y][x] = "westWall"; 
					}
					
					else if(k.equals("bE3")) { // East 3 doors are blocked
						if(x > (Constants.X_TILES/2) - 3)  seed[y][x] = "blocked"; 
						if(x == (Constants.X_TILES/2) - 3) seed[y][x] = "eastWall";
					}
				}
			}
			pregenTemplate.add(clone2dArray(seed)); 
			
//			//Debugging each template
//			System.out.println(" \nStart of template: " + k);
//			for(int yn = 0; yn < seed.length; yn++) {
//				System.out.print("Col: " + yn);
//				for(int xn = 0; xn < seed[yn].length; xn++) {
//					System.out.print(" "+ seed[yn][xn]);
//				}
//				System.out.println("");  
//			}
//			System.out.println(); 
		}	
		
		
		
		int index;
		final ArrayList<String[][]> TEMPLATE = new ArrayList<String[][]>(Collections.unmodifiableList(pregenTemplate));
		
		for(int col = 0; col < Constants.Y_MAPSIZE; col++) {
			rooms.add(new ArrayList<Room>());  //instantiate all columns in the 2d array
			
			for(int row = 0; row < Constants.X_MAPSIZE; row++) {
				//For each room in an NxN grid, that will make up the playfield...
				//DETERMINE: Room type, and what its filled with.
				index = roomTypeGen.next();
				roomType = key2[index];
				seed = clone2dArray(TEMPLATE.get(index)); 
				
//				System.out.println(index); //TRYING TO DEBUG
//				for(String t : key2) System.out.println(t);  
//				printSeedArray(seed, roomType);
				
				for(int y = 1; y < Constants.Y_TILES-1; y++) { //Loop through and fill the seed (Excluding boundaries)
					for(int x = 1; x < Constants.X_TILES-1; x++) {
						if(seed[y][x].equals("empty")) {
							seed[y][x] = roomItemGen.next();
						}
						
						if(x == (Constants.X_TILES/2) || x == (Constants.X_TILES/2)-1) { //Exceptions are, if in front of door
							if(y == 1 || y == Constants.Y_TILES-2) {
								seed[y][x] = "empty";
							}
						}
						
						if(y == (Constants.Y_TILES/2) || y == (Constants.Y_TILES/2)-1) { //Exceptions are, if in front of door
							if(x == 1 || x == Constants.X_TILES-2) {
								seed[y][x] = "empty";
							}
						}
						
					}
				}
				rooms.get(col).add(new Room(new int[] {col+1, row}, seed.clone(), roomType));
			}
		}
		
		player.setRoom(rooms.get(3).get(0));


		
		
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
		});
	}

	
	@Override
	public void render(float delta) {
		ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1);
		camera.update();
		game.batch.setProjectionMatrix(camera.combined);
		game.batch.begin();
		
		
		game.batch.disableBlending(); //save resources when not needed
		
		//debugging tools ->
		game.font.draw(game.batch, "FPS: "+ Gdx.graphics.getFramesPerSecond(), 1f, Constants.CAMERA_HEIGHT-1f);
		game.font.draw(game.batch, "Player xV: " + player.xVel + "Player yV: " + player.yVel,  1f, Constants.CAMERA_HEIGHT-1.5f);
		game.font.draw(game.batch, "Player xA: " + player.xAcc + "Player yA: " + player.yAcc, 1f, Constants.CAMERA_HEIGHT-2f);
		game.font.draw(game.batch, player.jolt[0] + "  " + player.jolt[1] + "  "+ player.jolt[2] + "  "+ player.jolt[3], 1f, Constants.CAMERA_HEIGHT-2.5f);
		//game.font.draw(game.batch, player.debugPlayer(), 1f, Constants.HEIGHT-3f);
		
		//System.out.println("Player xV: " + player.xVel + "Player yV: " + player.yVel);		
		game.batch.enableBlending();
		
		
		player.setAcceleration((wasd[3]-wasd[1])*0.0013f, (wasd[0]-wasd[2])*0.0013f, sprint); //handles player movement
		player.draw(game.batch);
		
		enemy.implementAction(); //decides what the enemy will do
		enemy.draw(game.batch);
		
		player.getRoom().draw(game.batch); //draw the room that the player is currently in
		
		game.batch.end();
		
        stage.act();
        stage.draw();
		debugRenderer.render(world, camera.combined);
		world.step(Constants.FRAME_SPEED, 6, 2);
		
//		System.out.println(camera.position);
//		Room cr = rooms.get(0).get(0);
//		for(String[] s : cr.getSeed()) {
//			for(String s2 : s) {
//				System.out.print(s2 + " ");
//			}
//			System.out.println();
//		}
//		cr.debugRoomPosition();
//		cr = rooms.get(3).get(0);
//		cr.debugRoomPosition();
		
		
//		try {
//		    Thread.sleep(50);                 //2000 milliseconds is one second.
//		} catch(InterruptedException ex) {
//		    Thread.currentThread().interrupt();
//		}
		

	}
	
	public void printSeedArray(String[][] org, String rt) {
		System.out.println("Type of room should be: " + rt);
		for(String[] s : org) {
			for(String s2 : s) {
				System.out.print(s2 + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public String[][] clone2dArray(String[][] original) {
		return Arrays.stream(original).map(String[]::clone).toArray(String[][]::new);
	}
	
	@Override
	public void resize(int width, int height) {
		//resize() can be called anytime you change your screen size(eg: when you screen orientation changes). 
		//resize() takes the actual width and height (320x480 etc), which is the pixel value, and converts it
        camera.viewportHeight = (Constants.CAMERA_WIDTH / width) * height;
        camera.update();
	}

	@Override
	public void show() {
		// start the playback of the background music
		// when the screen is shown
		ambiance.play();
		
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
	
	}
}
