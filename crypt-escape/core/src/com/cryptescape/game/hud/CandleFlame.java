package com.cryptescape.game.hud;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CandleFlame extends HudElement {
    
    public CandleFlame(Animation<TextureRegion> e) {
        super(e);
    }
    
    public void updateFlame(HudElement candle) {
        //System.out.println(super.getY() + "    keyframe" + candle.getAnimation().getKeyFrameIndex( candle.getTime() ) + "        length" + (float) candle.getAnimation().getKeyFrames().length + "   current"  + currentRegion.getRegionHeight()*scale);
        
        if(super.currentRegion.getRegionHeight() != 0 && candle.getAnimation().getKeyFrameIndex( candle.getTime() ) != 0) {
            super.setY(super.y - ( candle.getAnimation().getKeyFrameIndex( candle.getTime() )  / (( (float) candle.getAnimation().getKeyFrames().length ) / (getHeight()*(5/8f))) ) );
            
        }
        else
            super.setY(getY());
    }
    
}
