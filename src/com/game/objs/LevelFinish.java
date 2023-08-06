package com.game.objs;

import java.awt.Graphics;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class LevelFinish extends GameObject {

	Texture tex= Game.getTexture();
	Game game;
	Handler handler;
	Player player;
	
	public LevelFinish(float x, float y, ObjectID id, Game game, Handler handler) {
		super(x, y, id);
		this.game = game;
		player = handler.getPlayer();
		width = 64;
		height = 64;
	}

	@Override
	public void tick() {
		if(game.getHud().getLevelCompleteMessage() == -1) collision();
	}
	
	@Override
	public void render(Graphics g) {
		g.drawImage(tex.levelSprites[3], (int) x, (int) y, (int) width, (int) height, null);
	}
	
	private void collision() {	
		if(player.getBounds().intersects(getBounds())) game.levelComplete();
	}
}
