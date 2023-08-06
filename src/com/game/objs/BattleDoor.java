package com.game.objs;

import java.awt.Graphics;
import java.awt.Rectangle;

import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class BattleDoor extends Wall{

	private int doorId;
	private boolean closed;
	
	Texture tex = Game.getTexture();
	Handler handler;
	
	public BattleDoor(float x, float y, ObjectID id, int doorId, Handler handler) {
		super(x, y, id);
		closed = true;
		this.doorId = doorId;
		this.handler = handler;
	}
	
	public void tick() { }
	
	public void render(Graphics g) {
		if(closed) g.drawImage(tex.levelSprites[1], (int) x, (int) y, 32, 32, null);
		else g.drawImage(tex.levelSprites[2], (int) x, (int) y, 32, 32, null);
	}
	
	public void onTankDestroyed() {
		boolean tankFound = false;
		for(int i = 0; i < handler.tanks.size(); i++) {
			if(handler.tanks.get(i).getDoorLink() == doorId) { 
				tankFound = true;
				break;
			}
		}
		if(!tankFound) closed = false;
	}
	
	@Override
	public Rectangle getBounds() {
		if(closed) return new Rectangle((int) x, (int) y, (int) width, (int) height);
		else return new Rectangle(0, 0, 0, 0);
	}

	public boolean isClosed() {
		return closed;
	}

	public void setClosed(boolean closed) {
		this.closed = closed;
	}
}
