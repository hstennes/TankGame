package com.game.objs;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;

import com.game.framework.BulletManager;
import com.game.framework.GameObject;
import com.game.framework.GameState;
import com.game.framework.Handler;
import com.game.framework.LevelLoader;
import com.game.framework.MouseInput;
import com.game.framework.ObjectID;
import com.game.framework.Tank;
import com.game.framework.Texture;
import com.game.window.Camera;
import com.game.window.Game;
import com.game.window.HUD;

public class Player extends Tank{

	public static final int PLAYER_LIVES = 3;
	public final int moveSpeed = 5;
	public final int turnSpeed = 4;
	private float spawnX;
	private float spawnY;
	private int spawnLives;
	
	Texture tex = Game.getTexture();
	Game game;
	Handler handler;
	Camera camera;
	
	public Player(float x, float y, ObjectID id, Game game, Handler handler, Camera camera) {
		super(x, y, id, handler);
		lives = 3;
		spawnX = x;
		spawnY = y;
		spawnLives = lives;
		this.game = game;
		this.handler = handler;
		this.camera = camera;
	}

	@Override
	public void tick() {
		super.tick();
		pointCannon();
		if(lives <= 0) die();
	}

	private void pointCannon(){
		Point mouse = new Point(MouseInfo.getPointerInfo().getLocation());
		float mouseX = (float) (mouse.getX() - game.getLocationOnScreen().getX() + MouseInput.MOUSE_ICON_WIDTH / 2);
		float mouseY = (float) (mouse.getY() - game.getLocationOnScreen().getY() + MouseInput.MOUSE_ICON_WIDTH / 2);
		setCannonRotation((float) Math.toDegrees(Math.atan((mouseY - Game.HEIGHT / 2) / (mouseX - Game.WIDTH / 2))));
		setCannonRotation(getCannonRotation() + 90);
		if(mouseX < Game.WIDTH / 2) setCannonRotation(getCannonRotation() + 180);
	}
	
	private void die() {
		if(!exploding) camera.setShaking(Camera.LONG_SHAKE_DURATION);
		if(LevelLoader.levelNumber != 0) game.getHud().dispMessage("Mission Failed!", Camera.LONG_SHAKE_DURATION);
		exploding = true;
		
		if(camera.getShaking() == 0) {
			if(LevelLoader.levelNumber == 0) {
				game.getHud().survivalComplete();
				game.getSurvival().survivalComplete();
			}
			respawn();
		}
	}
	
	public void shootCannon() {
		if(Game.gameState == GameState.Game && frozen == 0) {
			HUD hud = game.getHud();
			int[] availableBullets = hud.getAvailableBullets();
			ObjectID selectedItem;
			selectedItem = hud.getItems()[hud.getSelectedSlot()];
			switch(selectedItem) {
			case BasicBullet:
				if(availableBullets[0] > 0) {
					shootCannon(selectedItem);
					availableBullets[0]--;
				}
				break;
			case FastBullet:
				if(availableBullets[1] > 0) {
					shootCannon(selectedItem);
					availableBullets[1]--;
				}		
				break;
			case EmpBullet:
				if(availableBullets[2] > 0) {
					shootCannon(selectedItem);
					availableBullets[2]--;
				}		
				break;
			case BouncyBullet:
				if(availableBullets[3] > 0) {
					shootCannon(selectedItem);
					availableBullets[3]--;
				}	
				break;
			case TargetingBullet:
				if(availableBullets[4] > 0) {
					shootCannon(selectedItem);
					availableBullets[4]--;
				}		
				break;
			default:
				break;
			}
		}
	}
	
	public void collectBullets() {
		int range = AmmoBox.COLLECTION_RANGE;
		Rectangle collectionRange = new Rectangle((int) x - range, (int) y - range, 2 * range + (int) width, 2 * range + (int) height);
		GameObject tempHelper;
		for(int i = 0; i < handler.helpers.size(); i++) {
			tempHelper = handler.helpers.get(i);
			if(tempHelper.getId() == ObjectID.AmmoBox && collectionRange.intersects(tempHelper.getBounds())) {
				if(tempHelper instanceof AmmoBox) {
					int[] collectedBullets = ((AmmoBox) tempHelper).takeBullets();
					for(int x = 0; x < BulletManager.BULLET_TYPES; x++) game.getHud().getAvailableBullets()[x] += collectedBullets[x];
					game.getHud().refreshItems();
				}
			}
		}
	}
	
	public void respawn() {
		System.out.println("Player respawning");
		x = spawnX;
		y = spawnY;
		tankRotation = 0;
		lives = spawnLives;
		exploding = false;
		deathAnimationFrame = 0;
		handler.revertGameObjects();
		handler.clearGraphics();
		game.getBulletManager().revertAvailableBullets();
		game.getHud().refreshItems();
		if(game.getSurvival() != null) game.getSurvival().reset();
		handler.setSurvivalTanksDestroyed(0);
		frozen = 0;
	}

	public float getSpawnX() {
		return spawnX;
	}

	public void setSpawnX(float spawnX) {
		this.spawnX = spawnX;
	}

	public float getSpawnY() {
		return spawnY;
	}

	public void setSpawnY(float spawnY) {
		this.spawnY = spawnY;
	}

	public int getSpawnLives() {
		return spawnLives;
	}

	public void setSpawnLives(int spawnLives) {
		this.spawnLives = spawnLives;
	}
}
