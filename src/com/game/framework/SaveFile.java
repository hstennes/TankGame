package com.game.framework;

import java.io.FileWriter;
import java.io.IOException;

public class SaveFile{
	
	public SaveFile() {
		
	}
	
	public static void saveFile(String path, String[] args) throws IOException {
	    FileWriter saveFile = new FileWriter(path);
	    for(int i = 0; i < args.length; i++) {
	    		saveFile.write(args[i]); 
	    		saveFile.write("\n");
	    }
	    saveFile.close();
	}
}