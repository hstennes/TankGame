package com.game.objs;

import java.awt.Graphics;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class Checkpoint extends GameObject{

	private boolean used = false;
	
	Texture tex = Game.getTexture();
	Game game;
	Handler handler;
	Player player;
	
	public Checkpoint(float x, float y, ObjectID id, Game game, Handler handler) {
		super(x, y, id);
		this.game = game;
		this.handler = handler;
		player = handler.getPlayer();
		width = 64;
		height = 64;
	}

	@Override
	public void tick() {
		collision();
	}
	
	@Override
	public void render(Graphics g) {
		if(!used) g.drawImage(tex.levelSprites[6], (int) x, (int) y, null);
		else g.drawImage(tex.levelSprites[8], (int) x, (int) y, null);
	}
	
	private void collision() {
		if(getBounds().intersects(player.getBounds())) {
			if(!used) {
				used = true;
				game.getHud().dispMessage("Checkpoint", 120);
				player.setSpawnX(x);
				player.setSpawnY(y);
				player.setSpawnLives(player.getLives());
				handler.saveGameObjects();
				game.getBulletManager().saveAvailableBullets();
			}
		}
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}	
}
