package com.cryptescape.game;

import java.io.IOException;


import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
//import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.cryptescape.game.MainCE;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) throws IOException {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("cryptescape-demo");
		config.setWindowedMode(1600, 900);
		config.useVsync(true);
		//TexturePacker.process(settings, "../images", "../game-android/assets", "game");
		
		//main window
		new Lwjgl3Application(new MainCE(), config);
	}
}
