package com.game.framework;

import java.awt.image.BufferedImage;

import com.game.objs.AmmoBox;
import com.game.objs.AttackTank;
import com.game.objs.BattleDoor;
import com.game.objs.BossTank;
import com.game.objs.CautiousTank;
import com.game.objs.Checkpoint;
import com.game.objs.LandMine;
import com.game.objs.LevelFinish;
import com.game.objs.LifeHeart;
import com.game.objs.PatrolTank;
import com.game.objs.Player;
import com.game.objs.Turret;
import com.game.objs.Wall;
import com.game.window.Camera;
import com.game.window.Game;
import com.game.window.HUD;

public class LevelLoader {
	
	private Game game;
	private Handler handler;
	private HUD hud;
	private static Texture texture;
	private BulletManager bulletManager;
	private Camera camera;
	private BufferedImage level;
	
	public static int levelNumber;
	
	public LevelLoader(Game game, Handler handler, Camera camera) {
		this.game = game;
		this.handler = handler;
		hud = game.getHud();
		texture = Game.getTexture();
		bulletManager = game.getBulletManager();
		this.camera = camera;
		level = texture.loadImage("res/level_1.png");
	}
	
	/**
	 * Loads the specified level and changes the game state to Game. An input of 0 will load survival mode
	 * @param level The level to be loaded, 0 if survival mode
	 */
	public void enterGame(int level) {
		hud = game.getHud();
		levelNumber = level;
		bulletManager = game.getBulletManager();
		System.out.println("Loading level " + level + "...");
		handler.clearObjects();
		handler.setLevelFinish(null);
		hud.levelComplete(-1);
		int[] availableBullets = bulletManager.getAvailableBullets();
		for(int i = 0; i < availableBullets.length; i++) availableBullets[i] = 0;
		if(level == 0) {
			availableBullets[0] = SurvivalMode.SURVIVAL_BULLETS;	
			this.level = texture.loadImage("res/survival_1.png");
			game.setSurvival(new SurvivalMode(this.level.getWidth() * Game.UNIT, this.level.getHeight() * Game.UNIT, handler, camera));	
		}
		else {
			if(Game.IS_TESTING) this.level = texture.loadImage("res/tester.png");
			else this.level = texture.loadImage("res/level_" + level + ".png");
			game.setSurvival(null);
		}
		Game.gameState = GameState.Game;
		loadImageLevel(this.level);
		handler.saveGameObjects();
		bulletManager.saveAvailableBullets();
		hud.refreshItems();
		hud.setSelectedSlot(0);
		handler.getPlayer().setSpawnLives(Player.PLAYER_LIVES);
		handler.getPlayer().setLives(Player.PLAYER_LIVES);
		handler.setSurvivalTanksDestroyed(0);
		handler.getPlayer().respawn();
		hud.dispMessage("", 1);
		System.out.println("Level loaded");
	}
	
	/**
	 * Loads the level represented by the given image by instantiating GameObjects based on pixel color values
	 * @param image The BufferedImage that is to be used to load the level
	 */
	private void loadImageLevel(BufferedImage image){
		//see info/level_design.txt for directions on creating levels  
		//Or just read this fun code and figure it out yourself
		handler.clearObjects();
		int width = image.getWidth();
		int height = image.getHeight();
		
		for(int x = 0; x < width; x++){
			for(int y = 0; y < height; y++){
				int pixel = image.getRGB(x, y);
				int red = (pixel >> 16) & 0xff;
				int green = (pixel >> 8) & 0xff;
				int blue = (pixel) & 0xff;
				if(x == 0 && y < 5 && blue > 1) bulletManager.getAvailableBullets()[y] = blue;
				if(x != 0){
					Tank newTank = null;
					if(red == 255) {
						if(green == 255 && blue == 255) handler.addWall(new Wall((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.Wall));
						else if(green == 254) handler.addWall(new BattleDoor((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.BattleDoor, blue, handler));
						else if(green == 0 && blue == 255) handler.addHazard(new LandMine((x - 1) * Game.UNIT + 8, y * Game.UNIT + 8, ObjectID.LandMine, handler));
						else if(green == 255 && blue == 0) handler.addHelper(new Checkpoint((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.Checkpoint, game, handler));
						else if(green == 0 && blue == 0) handler.addHelper(new LifeHeart((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.LifeHeart, handler));
					}	
					else if(red == 254) newTank = new PatrolTank((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.RedTank, handler);
					else if(red == 253) newTank = new AttackTank((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.YellowTank, handler);	
					else if(red == 252 && green == 0 && blue == 0) handler.addHazard(new Turret((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.Turret, handler));
					else if(red == 251) newTank = new PatrolTank((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.PurpleTank, handler);	
					else if(red == 250) newTank = new CautiousTank((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.BlackTank, handler);	
					else if(red == 249) newTank =  new CautiousTank((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.WhiteTank, handler);
					else if(red == 248) newTank = new BossTank((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.BossTank, game, handler, camera, green == 1);
					else if(red == 0) {
						if(green == 0 && blue == 255) initializePlayer(x, y);
						else if(green == 255 && blue == 255) handler.addHelper(createAmmoBox(image, x, y));
						else if(green == 255 && blue == 0) handler.setLevelFinish(new LevelFinish((x - 1) * Game.UNIT, y * Game.UNIT, ObjectID.LevelFinish, game, handler));
					}
					if(newTank != null) {
						newTank.setDoorLink(blue);
						handler.addTank(newTank);
					}
				}
			}
		}
	}
	
	private void initializePlayer(int x, int y) {
		Player player = handler.getPlayer();
		player.setX((x - 1) * Game.UNIT);
		player.setY(y * Game.UNIT);
		player.setSpawnX((x - 1) * Game.UNIT);
		player.setSpawnY(y * Game.UNIT);
	}
	
	private AmmoBox createAmmoBox(BufferedImage image, int x, int y) {
		int[] bullets = new int[5];
		int value = (image.getRGB(x, y - 1)) & 0xff;
		bullets[0] = value;
		value = (image.getRGB(x + 1, y - 1)) & 0xff;
		bullets[1] = value;
		value = (image.getRGB(x + 1, y)) & 0xff;
		bullets[2] = value;
		value = (image.getRGB(x + 1, y + 1)) & 0xff;
		bullets[3] = value;
		value = (image.getRGB(x, y + 1)) & 0xff;
		bullets[4] = value;
		return new AmmoBox(x * Game.UNIT, y * Game.UNIT, ObjectID.AmmoBox, bullets);
	}
	
	public Point getLevelDimensions() {
		return new Point(level.getWidth(), level.getHeight());
	}
}
