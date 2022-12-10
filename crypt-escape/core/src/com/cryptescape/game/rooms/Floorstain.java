package com.cryptescape.game.rooms;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class Floorstain extends Freeform{
    private double timeLeft = 10; //By defualt, last 10 seconds
    private double maxTime = 10; //Sets fade
    private boolean decaying = true; //By default, count down
    
    /**
     * Creates new effect with default width and height parameters (tilesize). NumOfVersions means how many
     * different versions of the effect there are (bloodstain1, bloodstain2, ect.)
     */
    public Floorstain(float decayTime, float xpos, float ypos, String name, Room room, int numOfVersions) {
        super(xpos, ypos, Constants.TILESIZE, Constants.TILESIZE, name, room);
        super.setTextureRegion(Interactable.getRandomRegion(name, numOfVersions));
        timeLeft = decayTime;
        maxTime = decayTime;
    }
    
    /**
     * Creates new effect with custom width and height parameters. NumOfVersions means how many
     * different versions of the effect there are (bloodstain1, bloodstain2, ect.)
     */
    public Floorstain(float decayTime, float xpos, float ypos, float width, float height, String name, Room room, int numOfVersions) {
        super(xpos, ypos, width, height, name, room);
        super.setTextureRegion(Interactable.getRandomRegion(name, numOfVersions));
        timeLeft = decayTime;
    }
    
    public void draw(SpriteBatch batch) {
        update();
        
        //This is all for opacity fade
        Color color = batch.getColor();
        float oldAlpha = color.a;
        color.a = (float) (timeLeft/maxTime);
        batch.setColor(color);
      
        super.draw(batch);
        
        color.a = oldAlpha;
        batch.setColor(color);
    }
    
    
    protected void update() {
        if(decaying)
            timeLeft -= Gdx.graphics.getDeltaTime();
        
        if(timeLeft < 0) 
            super.getParentRoom().queueForDisposal(this);
    }
    
    /**
     * Starts counting down on the effect. Once it reaches 0, it will delete itself.
     */
    protected void startCountdown() {
        decaying = true;
    }
    
    /**
     * Pauses counting down on the effect.
     */
    protected void pauseCountdown() {
        decaying = false;
    }
    

}
