package com.game.framework;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.Random;

import com.game.objs.AttackTank;
import com.game.objs.CautiousTank;
import com.game.objs.PatrolTank;
import com.game.window.Camera;
import com.game.window.Game;

public class SurvivalMode {
	
	public static final int SURVIVAL_BULLETS = 1000000;
	private final int spawnRate = 200;
	private final int chanceRed = 60;
	private final int chancePurple = 20;
	private final int chanceYellow = 10;
	private final int stageLength = 1500;
	private final int startStage = 1;
	private int highscore;
	
	private int stage;
	private int stageTicks;
	private int spawnTicks;
	private int width;
	private int height;
	
	Handler handler;
	Camera camera;
	Random r;
	
	public SurvivalMode(int width, int height, Handler handler, Camera camera) {
		this.handler = handler;
		this.camera = camera;
		this.width = width;
		this.height = height;
		stage = startStage;
		highscore = Integer.parseInt(TextReader.readOneLine("data/highscore.txt"));
		r = new Random();
	}
	
	public void tick() {
		if(stageTicks >= stageLength) {
			stage++;
			stageTicks = 0;
		}
		
		if(spawnTicks > spawnRate) {
			spawn(stage);
			spawnTicks = 0;
		}
		
		stageTicks++;
		spawnTicks++;
	}
	
	private void spawn(int quantity) {
		Point randomPoint; 
		Rectangle cameraView = new Rectangle((int) -camera.getX() - 64, (int) -camera.getY() - 64, Game.WIDTH + 128, Game.HEIGHT + 128);
		for(int i = 0; i < quantity; i++) {
			while(true) {
				randomPoint = new Point(r.nextInt(width), r.nextInt(height));
				if(!cameraView.contains(randomPoint)) break;
			}
			int random = r.nextInt(100);
			if(random <= chanceRed) handler.addTank(new PatrolTank((float) randomPoint.getX(), (float) randomPoint.getY(), ObjectID.RedTank, handler));
			else if(random <= chanceRed + chancePurple) handler.addTank(new PatrolTank((float) randomPoint.getX(), (float) randomPoint.getY(), ObjectID.PurpleTank, handler));
			else if(random <= chanceRed + chancePurple + chanceYellow) handler.addTank(new AttackTank((float) randomPoint.getX(), (float) randomPoint.getY(), ObjectID.YellowTank, handler));
			else handler.addTank(new CautiousTank((float) randomPoint.getX(), (float) randomPoint.getY(), ObjectID.BlackTank, handler));
		}
	}
	
	public void survivalComplete() {
		int score = handler.getSurvivalTanksDestroyed();
		if(score > highscore) {
			System.out.println("New highscore saved");
			try {
				String[] args = new String[1];
				args[0] = Integer.toString(score);
				SaveFile.saveFile("data/highscore.txt", args);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void reset() {
		handler.clearGraphics();
		highscore = Integer.parseInt(TextReader.readOneLine("data/highscore.txt"));
		stage = startStage;
		stageTicks = 0;
		spawnTicks = 0;
	}
	
	public int getHighscore() {
		return highscore;
	}

	public void setHighscore(int highscore) {
		this.highscore = highscore;
	}
}
