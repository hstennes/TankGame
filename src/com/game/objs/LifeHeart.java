package com.game.objs;

import java.awt.Graphics;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class LifeHeart extends GameObject{

	Texture tex = Game.getTexture();
	Handler handler;
	Player player;
	
	public LifeHeart(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		player = handler.getPlayer();
		width = 33;
		height = 30;
	}

	@Override
	public void tick() {
		collision();
	}

	@Override
	public void render(Graphics g) {
		g.drawImage(tex.hudSprites[0], (int) x, (int) y, null); 
	}
	
	private void collision() {
		if(getBounds().intersects(player.getBounds()) && player.getLives() < 3) {
			player.setLives(player.getLives() + 1);
			handler.removeHelper(this);
		}
	}
}
