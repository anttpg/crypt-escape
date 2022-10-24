package com.cryptescape.game;

import java.io.IOException;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.cryptescape.game.MainCE;

import com.badlogic.gdx.tools.texturepacker.TexturePacker;
//FUCK THIS TOOK FOREVER
//Trying to add to the gradle build path took so long to figure out :(



// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {	
	public static void main (String[] arg) throws IOException {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setTitle("cryptescape-demo");
		config.setWindowedMode(1600, 900);
		config.useVsync(true);
		
		
		//Temp disabled
		if(false) {
    		System.out.println("Do you want to repack assets? If unsure what this means, type no. (y/n)");
    		Scanner s = new Scanner(System.in);
    		if (s.nextLine().equals("y")) {
    			TexturePacker.process("../assets/imageAssets", "../assets/packedImages", "pack");
    			// ../ sets path back one folder, then reads from the general assets folder
    		}
		}

		
		//main window
		new Lwjgl3Application(new MainCE(), config);
	}
}
