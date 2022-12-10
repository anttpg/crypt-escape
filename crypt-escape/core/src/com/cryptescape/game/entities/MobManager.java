package com.cryptescape.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.cryptescape.game.GameScreen;
import com.cryptescape.game.rooms.Room;

public class MobManager {
    public static ArrayList<Mob> mobs = new ArrayList<Mob>();
    public static ArrayList<Mob> deadMobs = new ArrayList<Mob>();
    
    
    
    public static void update() {
        for(Mob mob : mobs)
            mob.update();
        
        if(!deadMobs.isEmpty())
            removeDead();
    }
    
    public static void addBat(float x, float y, Room currentRoom) {
        mobs.add(new Bat(x, y, 10f, currentRoom, 50f));
        mobs.get(0).setAnimation("batEast");
        GameScreen.mainGroup.addActorAt(100, mobs.get(0));
    }
    
    /*
     *  Adds current mob to the list to dispose of.
     */
    public static void addDeceased(Mob dead) {
        deadMobs.add(dead);
    }
    
    private static void removeDead() {
        for(Mob dead : deadMobs) {
            dead.dispose();
            mobs.remove(dead);
        }
        deadMobs.clear();
    }

    public static void drawMobs(SpriteBatch batch) {
        for(Mob mob : mobs)
            mob.draw(batch);    
    }
}
