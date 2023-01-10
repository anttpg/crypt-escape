package com.cryptescape.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.cryptescape.game.hud.Inventory;

public class Attack {
    
    
    public Attack() {
        
    }
    
    /** Returns a list of all fixtures affected by the attack. 
     * Attack manager can then decide what to do.
     */
    public void attack(double angle, float xpos, float ypos) { 
        
    }
    
    
    public void makeSquareFixture(World world, float x, float y, float density) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 3.5f;
        bodyDef.angularDamping = 5.0f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        PolygonShape box = new PolygonShape();  // Create a polygon shape 
        box.setAsBox(
                ((Inventory.tileSize * scale) * (bounds[2]-bounds[0])/currentRegion.getRegionHeight()/2f),
                ((Inventory.tileSize * scale) * (bounds[3]-bounds[1])/currentRegion.getRegionHeight()/2f)
                );

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = box;
        fixtureDef.density = density;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.3f; 

        fixture = body.createFixture(fixtureDef);
        box.dispose();
    }
}
