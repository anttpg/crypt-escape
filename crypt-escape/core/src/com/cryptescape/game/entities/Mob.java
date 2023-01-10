package com.cryptescape.game.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.rooms.Room;
import com.badlogic.gdx.graphics.Texture;

public abstract class Mob extends Movables{
    private float health;
    private boolean lockAnimation = false;
    protected AnimationHandler mobAnimation;
    
    
    public Mob(float x, float y, float maxVelocity, Room currentRoom, float maxHealth, float density, float[] hitboxBounds, short groupIndex){
        super(x, y, Constants.TILESIZE, Constants.TILESIZE, maxVelocity, currentRoom, hitboxBounds, density, groupIndex);
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
    
    
    /**
     * Sets the current animation if not locked.
     * Returns true if completed, false otherwise
     */
    public boolean setAnimation(String animationName) {
        if(lockAnimation)
            return false;
  
        mobAnimation.setCurrent(animationName, true);
        return true;
    }
    
    @Override
    public void act(float delta) {
        this.defaultAct();
    }

    
    @Override
    public void draw(SpriteBatch batch) {
        frame = mobAnimation.getFrame();
        batch.draw(frame, getX(), getY(), getWidth(), getHeight());
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

    /*
     * Use to determine the behavior of the mob. Each Mob will have set routines.
     */
    public abstract void update();

}
