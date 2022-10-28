package com.cryptescape.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class SfxManager {
	Map<String, Sound> sounds = new HashMap<String, Sound>();
	ArrayList<String> soundNames = new ArrayList<String>();
	Random random = new Random();
	float elapsedTime = 0;
	private boolean wait = false;
	
	/**
	 * Searches for each sound in soundNames and adds it to sounds (sound must exist inside of the sfx folder)
	 */
	public SfxManager() {
		
	}
	
	public SfxManager(ArrayList<String> soundN) {
		for(String s : soundN) {
			Sound sound = Gdx.audio.newSound(Gdx.files.internal("soundDesign/sfx/" + s));
			sounds.put(s, sound);
			soundNames.add(s);
		}
	}
	
	public void addSound(String s) {
		Sound sound = Gdx.audio.newSound(Gdx.files.internal("soundDesign/sfx/" + s));
		sounds.put(s, sound);
	}

	public void debugSounds() {
		sounds.forEach((k,v) -> System.out.println(k +"\n"));
	}
	
	/**
	 * Playing the specified sound
	 */
	public void playSound(String s) {
		Sound csound = sounds.get(s + ".wav");
		csound.play();
	}
}
