package com.cryptescape.game.entities;

import java.util.ArrayList;

import com.badlogic.gdx.ai.steer.Proximity;
import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.steer.behaviors.CollisionAvoidance;
import com.badlogic.gdx.ai.steer.behaviors.PrioritySteering;
import com.badlogic.gdx.ai.steer.behaviors.RaycastObstacleAvoidance;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.steer.utils.RayConfiguration;
import com.badlogic.gdx.ai.steer.utils.rays.CentralRayWithWhiskersConfiguration;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.cryptescape.game.GameScreen;

public class BehaviorTypes {
    
    public static void arriveAtTarget(Mob entity, Mob target) {
        Arrive<Vector2> arriveAtTarget = new Arrive<Vector2>(entity, target)
            .setTimeToTarget(0.5f)
            .setArrivalTolerance(0.5f)
            .setDecelerationRadius(1f);
            
        entity.setSteeringBehavior(arriveAtTarget);
    }
    

    
    
    public static void batSteering(Bat bat) {
        
    }
    
    public static void ratSteering(Rat rat) {
        
    }
    
    /*
     * Create a custom priority steering behavior with an arraylist
     */
    public static void customPrioritySteering(Mob entity, ArrayList<SteeringBehavior> steeringBehaviors) {
        PrioritySteering<Vector2> steering = new PrioritySteering<Vector2>(entity);
        for(SteeringBehavior b : steeringBehaviors)
            steering.add(b);
        
        entity.setSteeringBehavior(steering);
    }
    
    
    /* 
     * Default priority steering for mobs
     */
    public static void steerToPlayer(Mob entity) {
        PrioritySteering<Vector2> steering = new PrioritySteering<Vector2>(entity);        
        //steering.add(newDefaultRaycast(entity)); //Highest level will check for obstacles
        steering.add(newArriveAtPlayer(entity)); //Then find player.
        
        entity.setSteeringBehavior(steering);
    }
    
    
    //Returns to make creating steering behavior easy below.
    
    public static CollisionAvoidance<Vector2> newCollisonAvoidance(Mob entity) {
        Proximity<Vector2> proximity = new RadiusProximity<Vector2>(entity, null, 0);
        return new CollisionAvoidance<Vector2>(entity, proximity);
    }
       
    /**
     * Returns a default Arrive at player 
     */
    public static Arrive<Vector2> newArriveAtPlayer(Mob entity) {
        Arrive<Vector2> arriveAtTarget = new Arrive<Vector2>(entity, GameScreen.player)
            .setTimeToTarget(0.5f)
            .setArrivalTolerance(1f)
            .setDecelerationRadius(1.5f);
            
        return arriveAtTarget;
    }
    
    /*
     * Default whisker raycast at a 45 degree angle on each side. Does not work right now.
     */
    public static RaycastObstacleAvoidance<Vector2> newDefaultRaycast(Mob entity) {
        RayConfiguration<Vector2> rayconfig = new CentralRayWithWhiskersConfiguration<Vector2>(entity, 2f, 0.5f, 0.78f);
        RaycastObstacleAvoidance<Vector2> raycast = new RaycastObstacleAvoidance<Vector2>(entity)
                .setRayConfiguration(rayconfig);
        
        return raycast;
    }
}
