package com.cryptescape.game;

import java.util.ArrayList;

import com.cryptescape.game.entities.BehaviorTypes;
import com.cryptescape.game.entities.MobManager;

public class TimedEventManager {
    public static ArrayList<Event> events = new ArrayList<Event>();
    private static ArrayList<Event> disposal  = new ArrayList<Event>();
    
    /* 
     * Instead of a constructor, call this to create and start all timed events. 
     */
    public static void createEvents() {
        events.add(new Event(2f, new Runnable() { public void run() { generateMob(); } }));
    }
    
    
    protected static void generateMob() {
        MobManager.addBat(GameScreen.player.getX() + 0.5f, GameScreen.player.getY() + 0.5f, GameScreen.player.getRoom()); //Temp testing adding mobs.
    }


    /* 
     * Updates all timers and runs events that have reahed 0, before removing them
     */
    public static void update() {
        for(Event event : events) {
            if(event.trigger()) //Will run inside method call if true.
                disposal.add(event);
        }
        
        for(Event event : disposal) {
           events.remove(event);
        }
        disposal.clear();
    }
    

}
