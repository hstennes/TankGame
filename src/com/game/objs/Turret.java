package com.game.objs;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class Turret extends GameObject{

	private final int rebuildTime = 900;
	private int rebuildTicks;
	private boolean exploding = false;
	private boolean destroyed = false;
	private float deathAnimationFrame;
	private final float deathAnimationLength = 6;
	
	Texture tex = Game.getTexture();
	Random r;
	private Cannon cannon;
	Handler handler;
	Player player;
	
	public Turret(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		r = new Random();
		cannon = new Cannon(x, y, ObjectID.TurretCannon, this, handler);
		player = handler.getPlayer();
		width = 32;
		height = 32;
	}

	@Override
	public void tick() {		
		if(!destroyed) {			
			if(!exploding) {
				cannon.tick();
				cannon.setOwner(this);
				collision();
			}
			else {
				if(deathAnimationFrame < deathAnimationLength) deathAnimationFrame += 0.2;
				else {
					destroyed = true;
					exploding = false;
				}
			}
		}
		else if(rebuildTicks >= rebuildTime) respawn();
		else rebuildTicks++;
	}
	
	@Override
	public void render(Graphics g) {
		if(!destroyed) {
			if(!exploding) {
				g.drawImage(tex.levelSprites[0], (int) x, (int) y, 32, 32, null); 
				cannon.render(g);	
			}
			else {
				g.drawImage(tex.levelSprites[0], (int) x, (int) y, 32, 32, null); 
				g.drawImage(tex.levelSprites[7], (int) x, (int) y, 32, 32, null);
				BufferedImage explosion = tex.explosionSprites[(int) deathAnimationFrame];
				g.drawImage(explosion, (int) (x - (explosion.getWidth() - width) / 2), (int) (y - (explosion.getHeight() - height) / 2), null);
			}
		}
		else {
			g.drawImage(tex.levelSprites[0], (int) x, (int) y, 32, 32, null); 
			g.drawImage(tex.levelSprites[7], (int) x, (int) y, 32, 32, null);
		}
	}

	private void collision() {
		for(int i = 0; i < handler.projectiles.size(); i++) {
			if(handler.projectiles.get(i) != null) {
				if(getBounds().intersects(handler.projectiles.get(i).getBounds())) { 
					exploding = true;	
					handler.removeProjectile(handler.projectiles.get(i));
				}
			}
		}
	}
	
	private void respawn() {
		destroyed = false;
		exploding = false;
		rebuildTicks = 0;
		deathAnimationFrame = 0;
	}
}
