package com.game.objs;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class LandMine extends GameObject{

	private final int damage = 2;
	private boolean exploding = false;
	private float deathAnimationFrame;
	private final float deathAnimationLength = 6;
	
	Texture tex = Game.getTexture();
	Player player;
	Handler handler;
	
	public LandMine(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id);
		player = handler.getPlayer();
		this.handler = handler;
		width = 16;
		height = 16;
	}

	@Override
	public void tick() {
		if(!exploding) {
			x += speedX;
			y += speedY;
			collision();
		}
		else {
			if(deathAnimationFrame < deathAnimationLength) deathAnimationFrame += 0.2;
			else handler.removeHazard(this);
		}
	}

	@Override
	public void render(Graphics g) {
		if(exploding) {
			BufferedImage explosion = tex.explosionSprites[(int) deathAnimationFrame];
			g.drawImage(explosion, (int) (x - (explosion.getWidth() - width) / 2),
					(int) (y - (explosion.getHeight() - height) / 2), null);
		}
		else g.drawImage(tex.levelSprites[4], (int) x, (int) y, null);
	}
	
	private void collision() {
		Projectile tempProjectile;
		for(int i = 0; i < handler.projectiles.size(); i++) {
			tempProjectile = handler.projectiles.get(i);
			if(tempProjectile != null) {
				if(getBounds().intersects(tempProjectile.getBounds())) {
					handler.removeProjectile(tempProjectile);
					exploding = true;
				}
			}
		}
		if(getBounds().intersects(player.getBounds())) {
			player.takeDamage(damage);
			exploding = true;
		}
	}
}
