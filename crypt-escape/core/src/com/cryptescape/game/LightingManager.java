package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

import box2dLight.ConeLight;
import box2dLight.PointLight;
import box2dLight.RayHandler;

public class LightingManager {
    private static RayHandler rayHandler;
    private static PointLight playerLight;
    private static ConeLight playerFlashlight;
    
    private static Vector2 mousePosition = new Vector2(0, 0);
    
    public static void createLights() {
        RayHandler.useDiffuseLight(true);
        rayHandler = new RayHandler(GameScreen.world);
        rayHandler.setAmbientLight(0f);
        rayHandler.setShadows(true);
        rayHandler.setBlur(true);
        
        
        //rays, color, how far out to project 
        playerLight = new PointLight(rayHandler, 100, null, 2.5f, GameScreen.player.xPos, GameScreen.player.yPos);
        playerLight.setSoftnessLength(2f);
        playerLight.setXray(true);
        
        
        playerFlashlight = new ConeLight(rayHandler, 300, Color.WHITE, 7f, GameScreen.player.xPos, GameScreen.player.yPos, 0, 20f);
        playerFlashlight.setSoftnessLength(2f);
        playerFlashlight.setXray(false);
    }
    
    public static void updateLights() {
        mousePosition.x = (GameScreen.player.xPos - (Constants.VIEWPORT_WIDTH/2)) + (((float)GameScreen.relativeMousePosition.x/Gdx.graphics.getWidth()) * Constants.VIEWPORT_WIDTH);
        mousePosition.y = (GameScreen.player.yPos + (Constants.VIEWPORT_HEIGHT/2)) - (((float)GameScreen.relativeMousePosition.y/Gdx.graphics.getHeight()) * Constants.VIEWPORT_HEIGHT);
        
        playerLight.setPosition(GameScreen.player.xPos, GameScreen.player.yPos);
        playerLight.setDistance(GameScreen.player.getCandleLevel());
        
        playerFlashlight.setDistance(GameScreen.player.getBatteryLevel());
        playerFlashlight.setPosition(GameScreen.player.xPos, GameScreen.player.yPos);
        playerFlashlight.setDirection(getAngle(GameScreen.player.xPos, GameScreen.player.yPos, mousePosition));
        
        rayHandler.setCombinedMatrix(GameScreen.camera);
        rayHandler.updateAndRender();
    }
    
    
    public static float getAngle(Float x, Float y, Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - y, target.x - x));
        if(angle < 0){
            angle += 360;
        }
        return angle;
    }
    
    
    public static void dispose() {
        rayHandler.dispose();
        playerLight.dispose();
        playerFlashlight.dispose();
    }
}
