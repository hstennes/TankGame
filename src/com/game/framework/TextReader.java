package com.game.framework;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import com.game.window.Game;

public class TextReader {
	
	public TextReader() {
	
	}
	
	public static String readOneLine(String path) {
		String text = null;
		FileReader fr = null;
		try {
			fr = new FileReader(path);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
        BufferedReader br = new BufferedReader(fr);      
        
        try {
			text = br.readLine();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
		return text;
	}
	
	public static int[] loadOptions() {
		int[] options = new int[Game.NUM_OPTIONS];
		FileReader fr = null;
		
		try {
			fr = new FileReader("data/options.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
        BufferedReader br = new BufferedReader(fr);      
        
        for(int i = 0; i < Game.NUM_OPTIONS; i++) {
			try {
				if(i == 0) options[i] = Integer.parseInt(br.readLine().replaceFirst("camera_shake: ", ""));
				else if(i == 1) options[i] = Integer.parseInt(br.readLine().replaceFirst("driving_mode: ", ""));
				else if(i == 2) options[i] = Integer.parseInt(br.readLine().replaceFirst("show_fps: ", ""));
				else if(i == 3) options[i] = Integer.parseInt(br.readLine().replaceFirst("mouse_lock: ", ""));
				System.out.println("options[" + i + "] = " + options[i]);
			} catch (NumberFormatException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }
		
        try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}       
        
        return options;
	}
}

