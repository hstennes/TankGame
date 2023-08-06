package com.game.objs;

import java.awt.Graphics;

import com.game.framework.BulletManager;
import com.game.framework.GameObject;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class AmmoBox extends GameObject{

	public static int COLLECTION_RANGE = 32;
	private final int bulletSpacing = 12;
	private final int bulletOffset = 4;
	private final int firstLayerY = 4;
	private final int secondLayerY = 16;
	private int[] bullets;
	
	Texture tex = Game.getTexture();
	
	public AmmoBox(float x, float y, ObjectID id, int[] bullets) {
		super(x, y, id);
		this.bullets = bullets;
		width = 64;
		height = 32;
	}

	@Override
	public void tick() { }

	@Override
	public void render(Graphics g) {
		g.drawImage(tex.levelSprites[5], (int) x, (int) y, (int) width, (int) height, null);
		int bulletsRendered = 0;
		for(int i = 0; i < bullets.length; i++) {
			if(bullets[i] != 0) {
				g.drawImage(tex.bulletSprites[i], (int) x + bulletSpacing * bulletsRendered + bulletOffset, (int) y + firstLayerY, null);
				g.drawImage(tex.bulletSprites[i], (int) x + bulletSpacing * bulletsRendered + bulletOffset, (int) y + secondLayerY, null);
				bulletsRendered++;
			}
		}
	}
	
	@Override
	public Object clone() {
		AmmoBox clone = null;
		int[] cloneBullets = new int[BulletManager.BULLET_TYPES];
		try {
			clone = (AmmoBox) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		for(int i = 0; i < 5; i++) cloneBullets[i] = bullets[i];
		clone.bullets = cloneBullets;
		return clone;
	}
	
	public int[] takeBullets(){
		int[] bulletsToTake = new int[BulletManager.BULLET_TYPES];
		for(int i = 0; i < bulletsToTake.length; i++) bulletsToTake[i] = bullets[i];
		for(int i = 0; i < bullets.length; i++) bullets[i] = 0;
		return bulletsToTake;
	}
}
