package com.cryptescape.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cryptescape.game.MainCE;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(30);
		config.setTitle("cryptescape-demo");
		config.setWindowedMode(800, 480);
		config.useVsync(true);
		//main window
		new Lwjgl3Application(new MainCE(), config);
	}
}
