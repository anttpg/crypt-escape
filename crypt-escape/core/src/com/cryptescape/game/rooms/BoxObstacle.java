package com.cryptescape.game.rooms;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class BoxObstacle extends Interactable  {
    private String animationPhase = "notRunning";
    private static final float ANIMATION_SPEED = Constants.FRAME_SPEED * 10;
    
	public BoxObstacle(int col, int row, String current, Room p) {
		super(col, row, current, p);
		
		Random r = new Random();
		int l = r.nextInt(6)+1;
		if(GameScreen.atlas.findRegion(current + l) != null) {
		    super.animation = new Animation<TextureRegion>(ANIMATION_SPEED, GameScreen.atlas.findRegions(current));
			super.setTextureRegion(GameScreen.atlas.findRegion(current + l));
			super.checkBounds(current + l);
		}	
		else {
			System.out.println("NullBoxType; Could not find box of type '" + current + "" + l + "'. "
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
        if (animationPhase.equals("opening")) {
            
            //GameScreen.sounds.playSound("MinecraftDoor", 0.6f);
            super.setTextureRegion(animation.getKeyFrame(timer));
            
//            if (timer > animation.getAnimationDuration() + 0.2f) {
//                animationPhase = "walkIn";
//            }
            timer += Gdx.graphics.getDeltaTime();
        }
        
    }
}
