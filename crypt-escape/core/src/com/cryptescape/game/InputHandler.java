package com.cryptescape.game;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.QueryCallback;
import com.badlogic.gdx.physics.box2d.joints.MouseJoint;
import com.badlogic.gdx.utils.Array;
import com.cryptescape.game.hud.CustomFixtureData;
import com.cryptescape.game.hud.Inventory;

public class InputHandler {
    public static int[] wasd = new int[] {0,0,0,0};
    public static float sprint = 1; //changes when sprinting
    public static boolean e_pressed;
    public static boolean tab_pressed;
    public static boolean lmouse_held;
    
    
    public static Vector2 relativeMousePosition = new Vector2(0, 0);
    public static Vector3 relativeMouseInventory = new Vector3();
    private static Vector2 temp2 = new Vector2();
    
    /**
     * Query callback for dragging items in the inventory
     */
    private static QueryCallback invCallback = new QueryCallback() {
    	@Override
    	public boolean reportFixture(Fixture fixture) {
    		//Testing because of QueryAABB error, checks each to see if actually inside the point. Also checks user data to make sure it is movable
    		if(!fixture.testPoint(relativeMouseInventory.x, relativeMouseInventory.y))
    			return true;
    		
    		try { //Checks if
	    		if(fixture.getUserData() != null && !((CustomFixtureData)fixture.getUserData()).getMovable() ) {
	    			return true;
	    		}
    		}
	    	catch(ClassCastException e) {
	    		e.printStackTrace();
	    		System.err.println("Cannot cast user data to boolean, type is not CustomFixtureData. Change user data definition.");
	    		return true;
	    	}
    		
    		System.out.println(fixture.getBody().getPosition() + " " + relativeMouseInventory);
    		System.out.println("fixture " + fixture.getUserData());
    		System.out.println();
    		
    		Inventory.currentlyDragging = fixture;
    		Inventory.getMouseDef().bodyB = fixture.getBody();
    		Inventory.getMouseDef().target.set(relativeMouseInventory.x, relativeMouseInventory.y);
    		Inventory.getMouseDef().maxForce = 50000;
    		Inventory.setMouseJoint((MouseJoint) Inventory.getWorld().createJoint(Inventory.getMouseDef()));
    		return false;
    	}
    };

    public static void createInput() {

        // INPUT HANDLING
        // All input is calculated per tick. WASD stored as a 0 or 1 for easy movement calculations
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
                    wasd[0] = 1;
                if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
                    wasd[1] = 1;
                if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
                    wasd[2] = 1;
                if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
                    wasd[3] = 1;
                
                if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT)
                    sprint = 1.7f; // Except here since its a multiplier

                if (keycode == Input.Keys.E)
                    e_pressed = true;
                if (keycode == Input.Keys.TAB)
                    tab_pressed = !tab_pressed;
                if (keycode == Input.Keys.GRAVE)
                    GameScreen.debugPerspective = !GameScreen.debugPerspective;
                if (keycode == Input.Keys.ESCAPE)
                    tab_pressed = false;
                
            	if(tab_pressed)
            		wasd = new int[] {0, 0, 0, 0};
            	
                
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.W || keycode == Input.Keys.UP)
                    wasd[0] = 0;
                if (keycode == Input.Keys.A || keycode == Input.Keys.LEFT)
                    wasd[1] = 0;
                if (keycode == Input.Keys.S || keycode == Input.Keys.DOWN)
                    wasd[2] = 0;
                if (keycode == Input.Keys.D || keycode == Input.Keys.RIGHT)
                    wasd[3] = 0;
                
                if (keycode == Input.Keys.SHIFT_LEFT || keycode == Input.Keys.SHIFT_RIGHT)
                    sprint = 1;

                if (keycode == Input.Keys.E)
                    e_pressed = false;
                return false;
            }

            @Override
            public boolean mouseMoved(int mouseX, int mouseY) {
            	relativeMouseInventory.set((mouseX/GameScreen.realWidth) * Inventory.getStage().getWidth(), 
            			Inventory.getStage().getHeight() - (mouseY/GameScreen.realHeight) * Inventory.getStage().getHeight(), 0);
            	
                relativeMousePosition.x = mouseX;
                relativeMousePosition.y = mouseY;
                return false;
            }
            
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            	if (button != Input.Buttons.LEFT || pointer > 0) return false;
            	
            	if(tab_pressed) {
                	relativeMouseInventory.set((screenX/GameScreen.realWidth) * Inventory.getStage().getWidth(), 
                			Inventory.getStage().getHeight() - (screenY/GameScreen.realHeight) * Inventory.getStage().getHeight(), 0);
                    Inventory.getWorld().QueryAABB(invCallback, relativeMouseInventory.x, relativeMouseInventory.y, relativeMouseInventory.x, relativeMouseInventory.y); 	
                    System.out.println("temp vector" + relativeMouseInventory);
                    return false;
            	}
            	else {
            	    InputHandler.lmouse_held = true;
            	    return false;
            	}
            }
            
            @Override
            public boolean touchDragged(int screenX, int screenY, int pointer) {
                relativeMousePosition.x = screenX;
                relativeMousePosition.y = screenY;
                if(Inventory.getMouseJoint() == null)
                	return false;
                
            	relativeMouseInventory.set((screenX/GameScreen.realWidth) * Inventory.getStage().getWidth(), 
            			Inventory.getStage().getHeight() - (screenY/GameScreen.realHeight) * Inventory.getStage().getHeight(), 0);
                temp2.set(relativeMouseInventory.x, relativeMouseInventory.y);
                Inventory.getMouseJoint().setTarget(temp2);
                return true;
            }
            
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            	if (button != Input.Buttons.LEFT || pointer > 0) return false;

				if(Inventory.getMouseJoint() == null) 
					return false;
				
				Inventory.currentlyDragging = null;
				Inventory.getWorld().destroyJoint(Inventory.getMouseJoint());
				Inventory.setMouseJoint(null);
				InputHandler.lmouse_held = false;
				return true;
            }
        });
    }
    
}
