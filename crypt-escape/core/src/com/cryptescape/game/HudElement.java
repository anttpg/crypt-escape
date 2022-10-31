package com.cryptescape.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HudElement extends Actor {
    private Animation<TextureRegion> animation;
    private TextureRegion currentRegion;
    private float scale = 5f;
    private float y = 6.5f;
    
    private float time = 0;
	
    
	public HudElement(Animation<TextureRegion> e) {
		animation = e;
		currentRegion = animation.getKeyFrame(time, true);
		setX(1f);
		setY(6.5f);
		setWidth(100f);
		setHeight(100f);
	}
	
    @Override
    public void act(float delta){
        time += delta;
        currentRegion = animation.getKeyFrame(time, true);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(currentRegion, getX(), getY(), getWidth(), getHeight());
    }
    
    public void resize(int width, int height) {
        setX(getStage().getWidth()/25f);
        setY(getStage().getHeight() - (getStage().getHeight()/4f));
        y = getY();
        setWidth(getStage().getHeight()/scale);
        setHeight(getStage().getHeight()/scale);
        
        System.out.println(getX() + " " + getY() + " " + getHeight() + " " + getWidth());
        System.out.println("Stage width/height: " +  getStage().getWidth() + "  " + getStage().getHeight());
    }
    
    public Animation getAnimation() {
    	return animation;
    }
    
    public void setDuration(float time) {
    	animation.setFrameDuration(time/animation.getKeyFrames().length);
    }
    
    public float getTime() {
    	return time;
    }
    
    public void updateFlame(HudElement candle) {
    	//System.out.println(super.getY() + "    keyframe" + candle.getAnimation().getKeyFrameIndex( candle.getTime() ) + "        length" + (float) candle.getAnimation().getKeyFrames().length + "   current"  + currentRegion.getRegionHeight()*scale);
		
    	if(currentRegion.getRegionHeight() != 0 && candle.getAnimation().getKeyFrameIndex( candle.getTime() ) != 0) {
    		super.setY(y - ( candle.getAnimation().getKeyFrameIndex( candle.getTime() )  / (( (float) candle.getAnimation().getKeyFrames().length ) / (getHeight()*(5/8f))) ) );
    		
    	}
    	else
    		super.setY(getY());
    }
    

    	
    
}
