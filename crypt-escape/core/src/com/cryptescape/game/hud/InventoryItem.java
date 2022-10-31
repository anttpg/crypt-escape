package com.cryptescape.game.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.rooms.Interactable;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;

public abstract class InventoryItem extends Actor{
    private Fixture fixture;
    private TextureRegion currentRegion;
    private String name;
    
    private float scale;
    private float[] bounds = new float[4];
    
    private float time = 0;
    
    public InventoryItem(World world, String name, TextureRegion region, float x, float y, float scale) {
        this.name = name;
        this.currentRegion = region;
        this.scale = scale;
        this.checkBounds(name);
        
        //Actor settings
        setX(x);
        setY(y);
        setWidth(Inventory.tileSize * scale);
        setHeight(Inventory.tileSize * scale);  
        
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
        bodyDef.angularDamping = 0.5f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(1f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; 

        fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }
    
    public void applyForce(float xN, float yN, float s) {
        fixture.getBody().applyForceToCenter(xN, yN, true);
    }
    
    public void resize(int newWidth, int newHeight) {
        setX(getX() - (Inventory.oldWidth - newWidth)/2f);
        setY(getY() - (Inventory.oldHeight - newHeight)/2f);
        setWidth(Inventory.tileSize * scale);
        setHeight(Inventory.tileSize * scale);       
    }
    
    public void checkBounds(String name) {
        if(Interactable.itemBounds.get(name) != null) {
            int i = 0;
            for(String s : Interactable.itemBounds.get(name).split(",")) {
                bounds[i] = Float.valueOf(s);
                i++;
            }
        }
        else
            bounds = null;
    }
    
    @Override
    public void act(float delta){
        time += delta;
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(bounds != null) {
            batch.draw(currentRegion.getTexture(), 
                    getX(), getY(), getWidth(), getHeight(), 
                    bounds[0], bounds[1], bounds[2], bounds[3]);
        }
        
        else {
            batch.draw(currentRegion.getTexture(), 
                    getX(), getY(), getWidth(), getHeight());
        }
    }
}
