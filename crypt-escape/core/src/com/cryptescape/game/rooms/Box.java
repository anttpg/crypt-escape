package com.cryptescape.game.rooms;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.InputHandler;
import com.cryptescape.game.hud.Inventory;

public class Box extends Interactable  {
    private String animationPhase = "idle";
    private String storedItem = "candlestick";
    private boolean isUnlocked = false;
    private int l;
    private static final float ANIMATION_SPEED = Constants.FRAME_SPEED * 10;
    
	public Box(int col, int row, String name, Room p) {
		super(col, row, name, p);
		if(getName().equals("boxUnlocked"))
		    isUnlocked = true;
		
		Random r = new Random();
		l = r.nextInt(1)+1;
		setName(getName() + l);
		
		if(GameScreen.atlas.findRegion(getName()) != null) {
		    super.animation = new Animation<TextureRegion>(ANIMATION_SPEED, GameScreen.atlas.findRegions("boxOpening" + l));
			super.setTextureRegion(GameScreen.atlas.findRegion(getName()));
			super.checkBounds(getName());
		}	
		else {
			System.out.println("NullBoxType; Could not find box of type '" + getName() + "" + l + "'. "
			        + "Make sure all boxes have a boxUnlocked version. "); 
		}
		
		super.createStaticBox();
		super.createInteractionRadius(Constants.TILESIZE*1.2f, Constants.TILESIZE*1.2f);
	}
	
    public void draw(SpriteBatch batch) {
        update();
        super.draw(batch);
    }
    
    public void update() {
        if (isUnlocked && animationPhase.equals("opening")) {
                InputHandler.tab_pressed = true;
                animationPhase = "idle";
                Inventory.openBox(this);
                
            }
            timer += Gdx.graphics.getDeltaTime();   
    }
    
    public void setAnimationPhase(String phase) {
        animationPhase = phase;
    }
    
    public int getL() {
        return l;
    }
    
    public String getStoredItem() {
        return storedItem;
    }
    
    public void emptyStoredItem() {
        storedItem = null;
    }
}
