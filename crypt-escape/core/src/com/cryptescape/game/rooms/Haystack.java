package com.cryptescape.game.rooms;

import java.util.Random;

import com.cryptescape.game.GameScreen;

public class Haystack extends Interactable {

    public Haystack(int c, int r, String current, Room p) {
        super(c, r, current, p);
        super.setTextureRegion(GameScreen.atlas.findRegion(current));
    }
    
}
