package com.cryptescape.game.entities;

import com.cryptescape.game.GameScreen;
import com.cryptescape.game.rooms.Room;

public class Bat extends Mob {

    public Bat(float x, float y, float maxVelocity, Room currentRoom, float maxHealth) {
        super(x, y, maxVelocity, currentRoom, maxHealth);
        super.addAnimation("batWest");
        super.addAnimation("batEast");
        super.mobAnimation.setCurrent("batWest");
    }

    @Override
    public void update() {
        setX(GameScreen.player.getX() + 0.5f);
        setY(GameScreen.player.getY() + 0.5f);
        System.out.println(super.getX() + " " + super.getY());
        
    }

}
