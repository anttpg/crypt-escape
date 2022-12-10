package com.cryptescape.game.hud;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class CandleFlame extends HudElement {
    private float BURN_SPEED = ((GameScreen.player.getMaxCandleLevel() / GameScreen.player.getBurnPerTick()) * Constants.FRAME_SPEED) -
            (1/Constants.FRAME_SPEED * GameScreen.player.getMaxCandleLevel() * GameScreen.player.getBurnPerTick());
    public float burntime = BURN_SPEED;
    
    public CandleFlame(Animation<TextureRegion> e) {
        super(e);
        super.setDuration(BURN_SPEED);
    }
    
    public void updateFlame(HudElement candle) {
        burntime -= Gdx.graphics.getDeltaTime();

        //Idk why this works, it like reduces candle height or 
        if(super.currentRegion.getRegionHeight() != 0 && candle.getAnimation().getKeyFrameIndex( candle.getTime() ) != 0) {
            super.setY(super.y - ( candle.getAnimation().getKeyFrameIndex( candle.getTime() ) / (( (float) candle.getAnimation().getKeyFrames().length ) / (getHeight()*(5/8f))) ) );
        }
        else
            super.setY(getY());
    }
    
}
