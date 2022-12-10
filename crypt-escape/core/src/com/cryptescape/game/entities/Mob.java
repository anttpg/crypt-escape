package com.cryptescape.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.rooms.Room;
import com.badlogic.gdx.graphics.Texture;

public abstract class Mob extends Movables{
    protected AnimationHandler mobAnimation;
    private float health;
    private boolean lockAnimation = false;
	
    public Mob(float x, float y, float maxVelocity, Room currentRoom, float maxHealth){
        super(x, y, maxVelocity, currentRoom, new float[] {8f,16f});
        this.health = maxHealth;
        this.mobAnimation = new AnimationHandler();
    }
    
    
    /**
     * Used to describe the animation for the current mob. 
     * Created in the constructor of each individual mob
     */
    public void addAnimation(String animationName) {
        mobAnimation.add(animationName, new Animation<TextureRegion>(1/5f, GameScreen.atlas.findRegions(animationName)));
    }
    
    
    public void setAnimation(String animationName) {
        mobAnimation.setCurrent(animationName, true);
    }
    
    @Override
    public void act(float delta) {
        this.defaultAct();
    }

    
    @Override
    public void draw(SpriteBatch batch) {
        frame = mobAnimation.getFrame();
        batch.draw(frame, getX(), getY(), Constants.TILESIZE*3, Constants.TILESIZE*3);
    }
    
    
    /** 
     * Some sort of animation to go here... TODO
     */
    public void die() {
        lockAnimation = true;
        mobAnimation.setCurrent("death_animation");
    }


    public void dispose() {
        super.body.destroyFixture(fixture);
    }


    public abstract void update();
    
    
}
