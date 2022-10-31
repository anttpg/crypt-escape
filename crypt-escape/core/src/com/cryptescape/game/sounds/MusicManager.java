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
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicManager {
	Map<String, Music> songs = new HashMap<String, Music>();
	ArrayList<String> songNames = new ArrayList<String>();
	Random random = new Random();
	Music currentSong;
	float elapsedTime = 0;
	private boolean wait = false;
	
	/**
	 * Searches for each song in songNames and adds it to songs (song must exist inside of the MUSIC folder)
	 */
	public MusicManager() {
		
	}
	
	public MusicManager(String path) {
	       
        ArrayList<String> songN = new ArrayList<String>();      
        try (Stream<Path> paths = Files.walk(Paths.get(Gdx.files.internal(path).file().getAbsolutePath()))) {
            paths
            .filter(Files::isRegularFile)
            .forEach(p -> songN.add(p.getFileName().toString()));
        } 
        catch(Exception e) {
            e.printStackTrace();
        }
        
	    
		for(String s : songN) {
			Music song = Gdx.audio.newMusic(Gdx.files.internal("soundDesign/music/" + s));
			songs.put(s, song);
			songNames.add(s);
		}
	}
	
	public void addSong(String s) {
		Music song = Gdx.audio.newMusic(Gdx.files.internal("soundDesign/music/" + s));
		songs.put(s, song);
	}
	
	public void setVolume(String s, float volume) {
		songs.get(s).setVolume(volume);
	}
	
	public void setPlayingVolume(float volume) {
		currentSong.setVolume(volume);
	}

	public void debugSongs() {
		songs.forEach((k,v) -> System.out.println(k +"\n"));
	}
	
	public void update() {
		if(!wait && !currentSong.isPlaying()) {
			playRandomSong();
		}
		
		elapsedTime += Gdx.graphics.getDeltaTime();
	}
	
	/**
	 * Overrides the current song and starts playing the specified song
	 */
	public void playSong(String s) {
		currentSong.stop();
		currentSong = songs.get(s);
		currentSong.play();
	}
	
	public void playRandomSong() {
		currentSong = songs.get(songNames.get(random.nextInt(songNames.size())));
		currentSong.play();
	}
}
