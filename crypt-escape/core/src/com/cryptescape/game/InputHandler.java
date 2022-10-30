package com.cryptescape.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

public class InputHandler {

    public static void createInput() {

        // INPUT HANDLING
        // All calculated per tick, and stored as 0 or 1

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.W)
                    GameScreen.wasd[0] = 1;
                if (keycode == Input.Keys.A)
                    GameScreen.wasd[1] = 1;
                if (keycode == Input.Keys.S)
                    GameScreen.wasd[2] = 1;
                if (keycode == Input.Keys.D)
                    GameScreen.wasd[3] = 1;
                if (keycode == Input.Keys.SHIFT_LEFT)
                    GameScreen.sprint = 1.6f; // Except here since its a multiplier

                if (keycode == Input.Keys.E)
                    GameScreen.e_pressed = true;

                return false;
            }

            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.W)
                    GameScreen.wasd[0] = 0;
                if (keycode == Input.Keys.A)
                    GameScreen.wasd[1] = 0;
                if (keycode == Input.Keys.S)
                    GameScreen.wasd[2] = 0;
                if (keycode == Input.Keys.D)
                    GameScreen.wasd[3] = 0;
                if (keycode == Input.Keys.SHIFT_LEFT)
                    GameScreen.sprint = 1;

                if (keycode == Input.Keys.E)
                    GameScreen.e_pressed = false;

                return false;
            }

            @Override
            public boolean mouseMoved(int mouseX, int mouseY) {
                GameScreen.relativeMousePosition.x = mouseX;
                GameScreen.relativeMousePosition.y = mouseY;
                return false;
            }
        });
    }
    
}
