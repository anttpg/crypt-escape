package com.cryptescape.game.rooms;

import java.util.Random;

import com.cryptescape.game.GameScreen;

public class Puddle extends Interactable {

	public Puddle(int c, int r, String name, Room p) {
		super(c, r, name, p);
		super.findRandomSkin(2, name);
	}
	
}
