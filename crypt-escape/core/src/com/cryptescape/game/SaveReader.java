package com.cryptescape.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;

public class SaveReader {
	
	
	// <, This character designates a comment. Anything after a comment will not be read in the code.
	// For each interactable item (Not walls or floor though), you must designate the bounds.
	// IE: for box1, the corner of where the box should actually collide with the player starts at 8,0
	// While the top corner is at 90,100. The format can be seen as follows;
	// NAME:x1,y1,x2,y2     EX:  cabnet3:5,10,100,100
	public static HashMap<String, String> readObjectBounds(String file) throws IndexOutOfBoundsException {
		HashMap<String, String> sb = new HashMap<String, String>();
		String[] fileArr = file.split("\n");
		int count = 0;
		String line = fileArr[count];
		String[] parts;

		try {
			while(true) {
				parts = line.split(":", 2);
				sb.put(parts[0].trim(), parts[1].trim());
				count++;
				line = fileArr[count];
				System.out.println(count + "current: " + line);
			}
		}
		catch(IndexOutOfBoundsException e) {
			System.out.println("Finished reading file on line" + count);
		}

		return sb;
	}
}
