package com.game.window;

import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;

import com.game.framework.BulletManager;
import com.game.framework.GameState;
import com.game.framework.Handler;
import com.game.framework.KeyInput;
import com.game.framework.LevelLoader;
import com.game.framework.MouseInput;
import com.game.framework.SaveFile;
import com.game.framework.SurvivalMode;
import com.game.framework.TextReader;
import com.game.framework.Texture;

//Created by GoopyLotus5844 

/*
 * Improve Cannon system *
 * Eliminate magic numbers
 * Make out of bullets message
 * Clean up menu class
 * Add message when starting level
 * Add something at the end of the game (after level 15)
 * Prevent tanks from overlapping 
 * Fix JFrame title bug *
 * Fix spawning dead bug *
 * Make boss less jittery
 * Add sound system
 */
public class Game extends Canvas implements Runnable{
	private static final long serialVersionUID = 1L;
	public static final Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
	public static final int WIDTH = (int) SCREEN_SIZE.getWidth(), HEIGHT = (int) SCREEN_SIZE.getHeight();
	public static final int  UNIT = 32;
	public static GameState gameState;
	public static int currentFps;
	public static int[] options;
	public static final int NUM_OPTIONS = 4;
	public static final boolean IS_TESTING = false;
	private boolean running = false;
	
	private BufferedImage background;
	private Thread thread;
	private Menu menu;
	private Handler handler;
	private KeyInput keyInput;
	private MouseInput mouseInput;
	private static Texture texture;
	private Camera camera;
	private HUD hud;
	private SurvivalMode survival;
	private BulletManager bulletManager;
	private LevelLoader levelLoader;
	private Cursor cursor;
	
	public Game(){
		System.setProperty("sun.java2d.opengl", "true");
		System.out.println("Loading game settings...");
		options = TextReader.loadOptions();
		gameState = GameState.MainMenu;
		System.out.println("Loading sprites...");
		loadSprites();
		System.out.println("Instantiating main classes...");
		initMainClasses();
		System.out.println("Opening game window...");
		new Window(WIDTH, HEIGHT, "TANKS!", this);
		hud = new HUD(this, handler, menu, bulletManager.getAvailableBullets());
		System.out.println("Adding event listeners...");
		initMouseAndKeyboard();
		gameState = GameState.MainMenu;	
		System.out.println("Application started");
	}
	
	private void loadSprites() {
		texture = new Texture();
		background = texture.loadImage("res/grass.png");
	}
	
	private void initMainClasses() {
		camera = new Camera(0, 0);
		handler = new Handler(this, camera);	
		levelLoader = new LevelLoader(this, handler, camera);
		menu = new Menu(this, handler, levelLoader);
		bulletManager = new BulletManager(BulletManager.BULLET_TYPES);
	
	}
	
	private void initMouseAndKeyboard() {
		mouseInput = new MouseInput(this, handler, hud);
		this.addMouseListener(mouseInput);
		this.addMouseWheelListener(mouseInput);
		keyInput = new KeyInput(handler, hud, menu);
		this.addKeyListener(keyInput);
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		cursor = toolkit.createCustomCursor(texture.hudSprites[4], new Point(0, 0), "img");
		setCursor(cursor);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	
	public synchronized void stop(){
		try{
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int updates = 0;
		int frames = 0;
		while(running) {	
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				tick();
				updates++;
				delta--;
			}	
			render();
			frames++;				
			if(System.currentTimeMillis() - timer > 1000){
				timer += 1000;
				System.out.println("FPS: " + frames + " TICKS: " + updates);
				currentFps = frames;
				frames = 0;
				updates = 0;
			}
		}
		stop();
	}
	
	private void tick(){
		mouseInput.tick();
		if(gameState != GameState.Paused) handler.tick();
		if(gameState == GameState.Game) {
			hud.tick();
			camera.tick(handler.getPlayer());
			if(survival != null) survival.tick();
		}
		else if(gameState == GameState.Paused) {
			camera.tick(handler.getPlayer());
			menu.tick();
		}
		else {
			menu.tick();
			camera.setX(0);
			camera.setY(0);
		}
	}
	
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g = bs.getDrawGraphics();
		Graphics2D g2d = (Graphics2D) g; 
		g2d.translate(camera.getX(), camera.getY());

		int grassTileSize = 64;
		Rectangle cameraView = new Rectangle((int) -camera.getX() - grassTileSize, (int) -camera.getY() - grassTileSize, Game.WIDTH + grassTileSize * 2, Game.HEIGHT + grassTileSize * 2);

		for(int x = cameraView.x - cameraView.x % grassTileSize; x < cameraView.x + cameraView.width; x += grassTileSize){
			for(int y = cameraView.y - cameraView.y % grassTileSize; y < cameraView.y + cameraView.height; y += grassTileSize) {
				g.drawImage(background, x, y, grassTileSize, grassTileSize, null);
			}
		}

		handler.render(g);
		g2d.translate(-camera.getX(), -camera.getY());
		if(gameState == GameState.MainMenu || gameState == GameState.LevelSelect || gameState == GameState.Help || gameState == GameState.Paused || gameState == GameState.Options) menu.render(g);
		else hud.render(g);
		g.dispose();
		bs.show();
	}
	
	/**
	 * Saves the current game options to the file data/options.txt. This function should be called whenever a setting is changed using the 
	 * options menu that can be accessed from the main menu screen.
	 */
	public static void saveCurrentOptions() {
		String[] args = new String[NUM_OPTIONS];
		args[0] = "camera_shake: " + options[0];
		args[1] = "driving_mode: " + options[1];
		args[2] = "show_fps: " + options[2];
		args[3] = "mouse_lock: " + options[3];
		try {
			SaveFile.saveFile("data/options.txt", args);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returns the game's Texture instance for use with drawing images
	 * @return The game's texture instance
	 */
	public static Texture getTexture(){
		return texture;
	}

	/**
	 * Returns the game's HUD instance 
	 * @return The game's HUD instance
	 */
	public HUD getHud() {
		return hud;
	}
	
	/**
	 * Returns the game's BulletManager instance
	 * @return The game's BulletManager instance
	 */
	public BulletManager getBulletManager() {
		return bulletManager;
	}
	
	/**
	 * Returns the game's SurvivalMode instance if the game is in survival mode, returns null otherwise.
	 * @return The game's survival instance which is null when not in survival mode.
	 */
	public SurvivalMode getSurvival() {
		return survival;
	}
	
	public void setSurvival(SurvivalMode survival) {
		this.survival = survival;
	}
	
	/**
	 * Displays a message notifying the player of mission completion using HUD.dispMessage.
	 */
	public void levelComplete() {
		System.out.println("Level " + LevelLoader.levelNumber + " completed");
		hud.levelComplete(HUD.LC_MESSAGE_DURATION);
		if(hud.getLevelCompleteMessage() == 0) menu.levelComplete();
	}
	
	public static void main(String[] args){
		new Game();
	}
	
	public static void printGCStats() {
	    long totalGarbageCollections = 0;
	    long garbageCollectionTime = 0;

	    for(GarbageCollectorMXBean gc :
	            ManagementFactory.getGarbageCollectorMXBeans()) {

	        long count = gc.getCollectionCount();

	        if(count >= 0) {
	            totalGarbageCollections += count;
	        }

	        long time = gc.getCollectionTime();

	        if(time >= 0) {
	            garbageCollectionTime += time;
	        }
	    }

	    System.out.println("Total Garbage Collections: "
	        + totalGarbageCollections);
	    System.out.println("Total Garbage Collection Time (ms): "
	        + garbageCollectionTime);
	}
}
