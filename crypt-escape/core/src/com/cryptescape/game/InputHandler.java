package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector2;

public class InputHandler {
    public static int[] wasd = new int[] {0,0,0,0};
    public static float sprint = 1; //changes when sprinting
    public static boolean e_pressed;
    public static boolean tab_pressed;
    
    public static Vector2 relativeMousePosition = new Vector2(0, 0);

    public static void createInput() {

        // INPUT HANDLING
        // All calculated per tick, and stored as 0 or 1

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.W)
                    wasd[0] = 1;
                if (keycode == Input.Keys.A)
                    wasd[1] = 1;
                if (keycode == Input.Keys.S)
                    wasd[2] = 1;
                if (keycode == Input.Keys.D)
                    wasd[3] = 1;
                if (keycode == Input.Keys.SHIFT_LEFT)
                    sprint = 1.6f; // Except here since its a multiplier

                if (keycode == Input.Keys.E)
                    e_pressed = true;
                if (keycode == Input.Keys.TAB)
                    tab_pressed = true;
                
                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.W)
                    wasd[0] = 0;
                if (keycode == Input.Keys.A)
                    wasd[1] = 0;
                if (keycode == Input.Keys.S)
                    wasd[2] = 0;
                if (keycode == Input.Keys.D)
                    wasd[3] = 0;
                if (keycode == Input.Keys.SHIFT_LEFT)
                    sprint = 1;

                if (keycode == Input.Keys.E)
                    e_pressed = false;
                if (keycode == Input.Keys.TAB)
                    tab_pressed = true;
                
                return false;
            }

            @Override
            public boolean mouseMoved(int mouseX, int mouseY) {
                relativeMousePosition.x = mouseX;
                relativeMousePosition.y = mouseY;
                return false;
            }
        });
    }
    
}
