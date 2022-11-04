package com.cryptescape.game.rooms;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class Haystack extends Interactable {
	private final float ANIMATION_SPEED = Constants.FRAME_SPEED * 8;
	private float timer = 0;
	
    public Haystack(int c, int r, String current, Room p) {
        super(c, r, current, p);

        animation = new Animation<TextureRegion>(ANIMATION_SPEED, GameScreen.atlas.findRegions(current));
        super.setZIndex(3);
    }
    
    public void draw(SpriteBatch batch) {
    	if(timer < 3f)
    		update();
    	
    	super.draw(batch);
	}
	
	public void update() {
		if(timer < animation.getAnimationDuration()) {
			setTextureRegion(animation.getKeyFrame(timer));
			super.toFront();
		}
		else {
			setZIndex(getZIndex()-1);
			timer = 5f;
		}
		timer += Gdx.graphics.getDeltaTime();
	}
}
