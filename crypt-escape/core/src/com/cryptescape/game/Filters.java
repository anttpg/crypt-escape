package com.cryptescape.game;

import com.badlogic.gdx.physics.box2d.Filter;

public class Filters {
    
    //Objects with an index of -2 will NEVER collide (with lights or anything);
    public static Filter doNotBlockLighting = new Filter();
   
    //Flying objects wont collide with filter, index of -3
    public static Filter dontCollideWithFlying = new Filter(); 
    
    public static void setupFilters() {
        Filters.doNotBlockLighting.groupIndex = -2; 
        Filters.dontCollideWithFlying.groupIndex = -3; 
    }
}   
