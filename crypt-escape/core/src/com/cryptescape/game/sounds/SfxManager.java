package com.cryptescape.game.sounds;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Stream;

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
	
	public SfxManager(String path) {
	    
	       ArrayList<String> soundN = new ArrayList<String>();     
	        try (Stream<Path> paths2 = Files.walk(Paths.get(Gdx.files.internal(path).file().getAbsolutePath()))) {
	            paths2
	            .filter(Files::isRegularFile)
	            .forEach(p -> soundN.add(p.getFileName().toString()));
	        } 
	        catch(Exception e) {
	            e.printStackTrace();
	        }
	    
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
