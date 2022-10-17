package com.cryptescape.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.files.FileHandle;

public class SaveReader {
	
	public static HashMap<String, String> readObjectBounds(FileHandle fileHandle) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileHandle.path()));
		try {
		    HashMap<String, String> sb = new HashMap<String, String>();
		    String line = br.readLine();
		    String[] parts;
	
		    while (line != null) {
		    	parts = line.split(":", 2);
		        sb.put(parts[0], parts[1]);
		        line = br.readLine();
		    }
		    br.close();
		    return sb;
		    
		} catch(IOException e) {
		  }
		  finally {
		    br.close();
		}
		return null;
	}
}
