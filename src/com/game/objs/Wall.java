package com.game.objs;

import java.awt.Color;
import java.awt.Graphics;

import com.game.framework.GameObject;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class Wall extends GameObject{

	private Texture tex = Game.getTexture();
	private boolean debug = false;
	
	public Wall(float x, float y, ObjectID id) {
		super(x, y, id);
		width = 32;
		height = 32;
	}

	@Override
	public void tick() { }

	@Override
	public void render(Graphics g) {
		if(!debug) g.drawImage(tex.levelSprites[0], (int) x, (int) y, (int) width, (int) height, null);
		else {
			g.setColor(Color.red);
			g.drawRect((int) x, (int) y, (int) width, (int) height);
		}
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}
}
