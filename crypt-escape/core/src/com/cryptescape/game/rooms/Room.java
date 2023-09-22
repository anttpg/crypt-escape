package com.cryptescape.game.rooms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.entities.MobManager;
import com.cryptescape.game.entities.Movables;
import com.cryptescape.game.hud.items.InventoryItem;

public class Room {
	
    private boolean smellsOfBlood = false;
    private double smellsCounter = 0;
    
	private boolean discovered;
	private int[] relativeLocation = new int[2]; // [Col, Row] on the map FROM THE TOP, NOT BOTTOM CORNER
	private float[] roomCorner = new float[2]; // room [y, x] real relative locations 
	private float[] roomTop = new float[] {Constants.Y_ROOM_METERS,Constants.X_ROOM_METERS};
	private String roomType; 
	
	public static final TextureRegion BACKGROUND = GameScreen.atlas.findRegion("stone");
	private ArrayList<float[]> background = new ArrayList<float[]>();
	
	//Seed is the string version of all the objects
	private String[][] seed;
	
	//iItems stores all the interactable objects
	private ArrayList<Interactable> interactableObjects = new ArrayList<Interactable>();
	private ArrayList<DroppedItem> droppedItems = new ArrayList<DroppedItem>();
	private ArrayList<Floorstain> floorstains = new ArrayList<Floorstain>();
	private ArrayList<Freeform> disposalList = new ArrayList<Freeform>();
	
	private ArrayList<Door> doors = new ArrayList<Door>(Arrays.asList(null, null, null, null)); //Seperate from items
	
	
	/**
	* Defines a Room object, where L is the relative [Y,X] position of the room on the map (IE: [2,1] for 2 rows down, 1 col over).
	* S is the String seed of what is within the room, RT is the room type, and d is the usable doors (IE: [T,T,F,T])
	*/
	public Room(int[] l, String[][] s, String rt) {	
		// Get relative x/y location and calculate real coords based on that.
		relativeLocation = l;
		
		roomCorner[0] = Constants.CAMERA_HEIGHT * (Constants.NUM_OF_ROOMS_Y - l[0]) + (Constants.Y_BUFFER + (Constants.TILESIZE)); 
		roomCorner[1] = Constants.CAMERA_WIDTH * (l[1]) + (Constants.X_BUFFER + (Constants.TILESIZE));
		
		seed = s;
		roomType = rt;
		String current = new String();
		
		
		int counter = 0;
		for(int col = 0; col < Constants.Y_TILES; col++) {
			for(int row = 0; row < Constants.X_TILES; row++) { 
				current = seed[col][row];
				
				
				counter = 0;
				for(String d : Constants.DOORTYPES) { //Of type Door
					if( current.equals(d) ) {
						if(doors.get(counter) == null) {
							doors.set(counter, new Door(col, row, current, this, counter));
						}
						
						else { //Temp to deal with 2 wide doors .-.
							current = current.replaceFirst("Door", "Wall");
						}
					}
					counter++;
				}
				
				counter = 0;
				//Checking if the current item should be a static object
				for(String w : Constants.WALLTYPES) { //Of type wall
					if( current.equals(w) ) interactableObjects.add(new Wall(col, row, current, this, counter));
					counter++;
				} 
                counter = 0;                
				
				
				switch(current) {
				    case "box":
				        interactableObjects.add(new Box(col, row, current, this));
				        break;
				        
				    case "boxUnlocked":
				        interactableObjects.add(new Box(col, row, current, this));
                        break;
                        
				    case "boxOpening":
				        interactableObjects.add(new Box(col, row, current, this));
                        break;
                        
                        
				    case "table":
				        interactableObjects.add(new Table(col, row, current, this));
				        break;
				        
				        
				    case "puddle":
				        interactableObjects.add(new Puddle(col, row, current, this));
				        break;
				    
				    case "haystack":
				        interactableObjects.add(new Haystack(col, row, current, this));
				        break;
				    
				    case "empty":
				        break;
				}
			
				if ( !current.equals("blocked") ) { //If NOT BLOCKED, then add to background
					float xCorner = getRoomLocation()[1] + (Constants.X_ROOM_METERS * (row/(float)Constants.X_TILES));
					float yCorner = getRoomLocation()[0] + (Constants.Y_ROOM_METERS * ((Math.abs(Constants.Y_TILES-col)-1)/(float)Constants.Y_TILES));
					background.add(new float[] {yCorner, xCorner});
				}
				
			}
		}
	}
	
	public Interactable getRecent() {
	    return interactableObjects.get(interactableObjects.size()-1);
	}
	
	/**
	 * Draws each part of the room, including the player.
	 */
	public void draw(SpriteBatch batch) {
	    
	    //Render background lowest, then
		batch.disableBlending();
		for(float[] backGtile : background) 
			batch.draw(BACKGROUND, backGtile[1], backGtile[0], Constants.TILESIZE, Constants.TILESIZE);
		batch.enableBlending();
		
		//Render all doors, above everything else.
		for(Door door : doors) 
			if(door != null) door.draw(batch);
		
	    //floor effects below everything
        for(Floorstain fs : floorstains) 
            fs.draw(batch);
		
		//This is for sprite ordering (by Z index), render lower level first.
		for(Interactable lowerZInteractable : getItems()) {
			if(lowerZInteractable.getZIndex() < GameScreen.player.getZIndex())
				lowerZInteractable.draw(batch);
		}
		
	    //render dropped items
        disposeOfUnused();
        for(DroppedItem item : droppedItems) 
            item.draw(batch);
		
        
		//Then Render player
		GameScreen.player.draw(batch);
		
		//Then draw mobs in this room
		MobManager.drawMobs(batch, this);
		
		
		//Finally render higher Z index items interactable on top of the player
		for(Interactable upperZInteractable : getItems()) {
			if(upperZInteractable.getZIndex() > GameScreen.player.getZIndex())
				upperZInteractable.draw(batch);
		}
		

	    updateEnemyFlags();
	}
	
	/**
	 * This method is VERY important! This is what will be used to help the AI determine
	 * which rooms it should enter, by giving it readable information (such as where the
	 * player bled last)
	 */
	public void updateEnemyFlags() {
	    if(smellsOfBlood) 
	        smellsCounter -= Constants.FRAME_SPEED;
	    if(smellsCounter < 0)
	        smellsOfBlood = false;  
	}
	
	/** 
	 * Prints the room seed for debugging to the console
	 */
	public void debugRoomSeed() {
		System.out.println(" \nStart of template: " + roomType);
		for(int yn = 0; yn < seed.length; yn++) {
			System.out.print("Col: " + yn);
			for(int xn = 0; xn < seed[yn].length; xn++) {
				System.out.print(" "+ seed[yn][xn]);
			}
			System.out.println("");
		}
	}
	
	/**
	 * Discoveres the partner doors for a certian room, and sets those.
	 */
	public void determinePartners() {
		if(relativeLocation[0]-1 != 0 && doors.get(0) != null) {
				doors.get(0).setPartner(GameScreen.rooms.get(relativeLocation[0]-2).get(relativeLocation[1]).getDoors().get(2));
		}
		
		if(relativeLocation[0] != Constants.NUM_OF_ROOMS_Y && doors.get(2) != null) {
				doors.get(2).setPartner(GameScreen.rooms.get(relativeLocation[0]).get(relativeLocation[1]).getDoors().get(0));
		}
		
		if(relativeLocation[1] != 0 && doors.get(3) != null) {
			doors.get(3).setPartner(GameScreen.rooms.get(relativeLocation[0]-1).get(relativeLocation[1]-1).getDoors().get(1));
		}
		
		if(relativeLocation[1] != Constants.NUM_OF_ROOMS_X-1 && doors.get(1) != null) {
			doors.get(1).setPartner(GameScreen.rooms.get(relativeLocation[0]-1).get(relativeLocation[1]+1).getDoors().get(3));
		}
		
	}
	
	/**
	 * Adds a new dropped item at current player location, of whatever inventory item it is.
	 */
	public void addDroppedItem(InventoryItem item) {
		DroppedItem droppedItem = new DroppedItem(item, this);
		droppedItems.add(droppedItem);
	}
	
	public ArrayList<DroppedItem> getDroppedItems() {
		return droppedItems;
	}
	
	/** Adds a new bloodstain at XY, with the default fade of 10s
	 */
	public void addBloodstain(float decayTime, float xpos, float ypos) {
	    floorstains.add(new Floorstain(decayTime, xpos, ypos, "bloodstain", this, 25));
	}
	
	/**
	 * Queues the removal of an item from play.
	 */
	public void queueForDisposal(Freeform item) {
		disposalList.add(item);
	}
	
	/**
	 * Loops through and removes all to dispose
	 */
	public void disposeOfUnused() {
		for(Freeform item : disposalList) {
			item.destroyFixtures();
			if(droppedItems.contains(item)) 
				droppedItems.remove(item);
			
	        if(floorstains.contains(item)) 
	            floorstains.remove(item);
		}
		
		disposalList.clear();
	}
	
	public ArrayList<Door> getDoors() {
	        return doors;
	}
	
	public List<Box> getBoxes() {
	    return (List<Box>)(Object)findByType("Box");
	}
	
    public List<Table> getTables() {
        return (List<Table>)(Object)findByType("Table");
    }
    
    public List<Interactable> findByType(String type) {
        List<Interactable> results = interactableObjects.stream()
                .filter(i -> i.getClass().getSimpleName().equals(type))
                .collect(Collectors.toList());
        return results;
    }

	public String getRoomType() {
		return roomType;
	}

	public String[][] getSeed() {
		return seed;
	}

	public float[] getRoomLocation() {
		return roomCorner;
	}

	public ArrayList<Interactable> getItems() {
		return interactableObjects;
	}
	
	
	
	public void discover() {
		discovered = true;
	}
	
	
	//IMPLETMENT THESE LATER
	public void sleepRoom() {
		for(Interactable i : interactableObjects) {
			//Does nothing currently, bodies automatically sleep 
		}
	}
	
	public void wakeRoom() {
		for(Interactable i : interactableObjects) {
			//Does nothing currently, bodies automatically sleep 
		}
	}
	
	//Debug functions
	@SuppressWarnings("unused")
	public void debugItem(Fixture f, int col, int row) {
		System.out.println("Position of item at col  " + col + "  and row  " + row + "  : " + f.getBody().getPosition());
	}
	
	public void debugRoomPosition() {
		System.out.println("END OF Room col: " + relativeLocation[0] + "    and row: " + relativeLocation[1]);
		System.out.println("Y meters: " + roomCorner[0] + "   X meters: " + roomCorner[1] + "\n");
	}



    public boolean doesRoomSmellOfBlood() {
        return smellsOfBlood;
    }



    public void setRoomSmellsOfBlood(boolean smellsOfBlood) {
        this.smellsOfBlood = smellsOfBlood;
    }

}
