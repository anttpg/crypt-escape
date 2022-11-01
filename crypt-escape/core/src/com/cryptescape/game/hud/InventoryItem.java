package com.cryptescape.game.hud;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.cryptescape.game.Constants;
import com.cryptescape.game.rooms.Interactable;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
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
    }
    
    
    public void applyForce(float xN, float yN, float s) {
        fixture.getBody().applyForceToCenter(xN, yN, true);
    }
    
    public void resize(float newWidth, float newHeight) {
        setX(getX() - (Inventory.oldWidth - newWidth)/2f);
        setY(getY() - (Inventory.oldHeight - newHeight)/2f);
        
        System.out.println(
                "Candle: X/Y " + getX() + "  " + getY() + "   WIDTH/HEIGHT " + getWidth() + "  " + getHeight());
           
        if(bounds == null) {
            setWidth(Inventory.tileSize * scale);
            setHeight(Inventory.tileSize * scale); 
        }
        else {  
            setWidth((Inventory.tileSize * scale) * (bounds[2]-bounds[0])/currentRegion.getRegionHeight());
            setHeight((Inventory.tileSize * scale) * (bounds[3]-bounds[1])/currentRegion.getRegionHeight());
        }
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
        setX(fixture.getBody().getPosition().x - getWidth()/2f);
        setY(fixture.getBody().getPosition().y - getHeight()/2f);
    }
    
    @Override
    public void draw(Batch batch, float parentAlpha) {	
        batch.draw(currentRegion, getX(), getY(), 
        		((Inventory.tileSize * scale) * (bounds[2]-bounds[0])/currentRegion.getRegionHeight()/2f), //These are just to set the origin of the rotation
        		((Inventory.tileSize * scale) * (bounds[3]-bounds[1])/currentRegion.getRegionHeight()/2f), 
        		getWidth(), getHeight(), 1f, 1f, (fixture.getBody().getAngle()*180)/3.14f);

    }
    
    public void debugItem() {
        System.out.println(
                "Item:" + name + " X/Y " + getX() + "  " + getY() + "   WIDTH/HEIGHT " + getWidth() + "  " + getHeight());
    }
    
    public void makeCircleFixture(World world, float x, float y) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
        bodyDef.angularDamping = 0.5f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);
        CircleShape circle = new CircleShape();
        circle.setRadius(Inventory.tileSize * scale);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circle;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; 

        fixture = body.createFixture(fixtureDef);
        circle.dispose();
    }
    
    public void makeSquareFixture(World world, float x, float y, float density) {
        //Creating interactable body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 0.5f;
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
        fixtureDef.restitution = 0.6f; 

        fixture = body.createFixture(fixtureDef);
        box.dispose();
    }
}
