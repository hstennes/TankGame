package com.game.objs;

import java.awt.AlphaComposite;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.window.Game;

public class Trail extends GameObject{

	private final float life = 0.005f;
	private final float lifeSubtract = 0.001f;
	private float alpha = 1;
	private float rotation;
	
	private Handler handler;
	private Texture tex = Game.getTexture();
	
	public Trail(float x, float y, float rotation, ObjectID id, Handler handler) {
		super(x, y, id);
		width = 40;
		height = 5;
		this.rotation = rotation;
		this.handler = handler;
	}

	public void tick() {
		if(alpha > life) alpha -= life - lifeSubtract;
		else handler.removeGraphicsElement(this);
	}

	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform trail = new AffineTransform();
		trail.setToRotation(Math.toRadians(rotation), x, y);
		trail.translate(x, y);
		trail.scale(5, 5);
		
		g2d.setComposite(makeTransparent(alpha));
		g2d.drawImage(tex.tankSprites[22], trail, null);
		g2d.setComposite(makeTransparent(1));
	}
	
	private AlphaComposite makeTransparent(float alpha){
		int type = AlphaComposite.SRC_OVER;
		return(AlphaComposite.getInstance(type, alpha));
	}
}
