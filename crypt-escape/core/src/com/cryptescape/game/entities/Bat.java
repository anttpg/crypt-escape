package com.cryptescape.game.entities;

import com.badlogic.gdx.ai.pfa.Connection;
import com.cryptescape.game.Filters;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.rooms.Room;

public class Bat extends Mob {

    public Bat(float x, float y, float maxVelocity, Room currentRoom, float maxHealth) {
        super(x, y, maxVelocity, currentRoom, maxHealth, 0.1f, new float[] {9f,9f}, (short)-3);
        super.addAnimation("batWest");
        super.addAnimation("batEast");
        super.mobAnimation.setCurrent("batWest");
        BehaviorTypes.steerToPlayer(this);
    }

    @Override
    public void update() {
        if(super.steeringBehavior != null) {
            super.steeringBehavior.calculateSteering(steeringOutput);
            super.applySteering();
        }
    }
    
    public void debugBat() {
        System.out.println(super.getX() + " " + super.getY());
    }

}
