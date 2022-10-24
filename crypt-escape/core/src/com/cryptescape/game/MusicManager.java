package com.cryptescape.game;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;

public class MusicManager {
	Map<String, Music> songs = new HashMap<String, Music>();
	ArrayList<String> songNames = new ArrayList<String>();
	Random random = new Random();
	Music currentSong;
	float elapsedTime = 0;
	
	/**
	 * Searches for each song in songNames and adds it to songs (song must exist inside of the MUSIC folder)
	 */
	public MusicManager() {
		
	}
	
	public MusicManager(ArrayList<String> songN) {
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
	
	public void setSongVolume(String s, float volume) {
		songs.get(s).setVolume(volume);
	}

	public void debugSongs() {
		songs.forEach((k,v) -> System.out.println(k +"\n"));
	}
	
	public void update() {
		if(!currentSong.isPlaying()) {
			playRandomSong();
		}
		elapsedTime += Gdx.graphics.getDeltaTime();
	}
	
	public void playSong() {
		//TODO
	}
	
	public void playRandomSong() {
		currentSong = songs.get(songNames.get(random.nextInt(songNames.size())));
		currentSong.play();
	}
}
