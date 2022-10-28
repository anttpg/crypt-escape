package com.cryptescape.game;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class HudElement extends Actor {
    private Animation<TextureRegion> animation;
    private TextureRegion currentRegion;
    private float time = 0;
    private float scale = 3f;
    
    private float x = 100f;
    private float y = 650f;
	
	public HudElement(Animation<TextureRegion> e) {
		animation = e;
		currentRegion = animation.getKeyFrame(time, true);
		super.setX(x);
		super.setY(y);
	}
	
    @Override
    public void act(float delta){
        time += delta;
        currentRegion = animation.getKeyFrame(time, true);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(currentRegion, getX(), getY(), currentRegion.getRegionWidth()*scale, currentRegion.getRegionHeight()*scale);
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
    		super.setY(y - ( candle.getAnimation().getKeyFrameIndex( candle.getTime() )  / (( (float) candle.getAnimation().getKeyFrames().length ) / (currentRegion.getRegionHeight()*scale*(5/8f))) ) );
    		
    	}
    	else
    		super.setY(y);
    }
    

    	
    
}
