package com.cryptescape.game.entities;

import com.badlogic.gdx.Gdx;
import com.cryptescape.game.Constants;
import com.cryptescape.game.GameScreen;

public class StatusManager {
   
    public static boolean bleeding = false;
    public static float bleedingTill = 0;
    public static float addBloodPuddleTimer = 0;
    
    public static boolean sick = false;
    public static float sickTill = 0;
    
    public static boolean healing = false;
    public static float healingTill = 0;
    public static float healAmount = 0;
    public static float health = 100;
    
    public static float food = 100;
    public static float water = 100;


    /**
     * Instantly applies heal to player. Max health 100. If healOverTime true,
     * applies healAmount amount of health over time. If called before another item
     * finished healing, adds to duration but does not override health amount unless
     * health amount greater (in which it then resets duration)
     */
    public static void useHealable(float instantHeal, boolean healOverTime, float healAmountPerTick, float healDurationSeconds) {
        health += instantHeal;
        
        if(healOverTime) {
            if(healAmountPerTick > healAmount && healAmount != 0) {
                healingTill += healDurationSeconds;
            }
            else if(healAmountPerTick < healAmount && healAmount != 0) {
                healAmount = healAmountPerTick; 
                healingTill = healDurationSeconds;
            } 
            else if(healAmount == 0) {
                healAmount = healAmountPerTick; 
                healingTill = healDurationSeconds;
            } 
        }
    }
    
    /**
     * Injure the player. Max health is 100, injure instantly by damage. Apply bleed
     * Effect is optional, if true select duration
     */
    public static void injurePlayer(float damage, boolean bleed, float bleedDurationSeconds) {
        health -= damage;
        
        if(bleed) {
            bleeding = true;
            bleedingTill = bleedDurationSeconds;
        }
    }
    
    
    /**
     * Updates the status of the player. 
     */
    public static void update() {
        food -= Constants.FRAME_SPEED;
        water -= Constants.FRAME_SPEED;
        
        if(bleeding) {
            if(!GameScreen.player.getRoom().doesRoomSmellOfBlood())
                GameScreen.player.getRoom().setRoomSmellsOfBlood(true);
            
            bleedingTill -= Constants.FRAME_SPEED;
            health -= Constants.FRAME_SPEED/5f; // 5 sec bleeded == 1 HP lost
            addBloodPuddleTimer += Constants.FRAME_SPEED;
            
            if(addBloodPuddleTimer > 0.35) {
                GameScreen.player.getRoom().addBloodstain(90,
                        GameScreen.player.getX() - Constants.TILESIZE/2, 
                        GameScreen.player.getY() - Constants.TILESIZE/2);
                addBloodPuddleTimer = 0;
            }
            
            if(bleedingTill < 0) 
                bleeding = false;
        }
        
        
        
        if(sick) {
            sickTill -= Constants.FRAME_SPEED;
            //TODO 
            
            if(sickTill < 0)  
                sick = false;
        }
        
        
        
        if(healing) {
            healingTill -= Constants.FRAME_SPEED;
            if(health < 100)
                health += healAmount;
            
            if(healingTill < 0) {
                healing = false;
                healingTill = 0;
                healAmount = 0;
            }
        }  
    }
}
