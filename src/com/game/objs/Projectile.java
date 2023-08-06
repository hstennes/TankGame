package com.game.objs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.util.Random;

import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;
import com.game.framework.Texture;
import com.game.window.Game;

public class Projectile extends GameObject{

	private final int spriteWidth = 8;
	private final int spriteHeight = 12;
	private final float renderRotationOffsetOne = 4.71f;
	private final float renderRotationOffsetTwo = 1.57f;
	private final int empFreezeDuration = 180;
	protected int[] stats;
	private boolean active = false;
	private int age;
	private int bounces;
	
	Texture tex = Game.getTexture();
	Handler handler;
	Player player;
	GameObject shooter;
	Random r;
	
	public Projectile(float x, float y, float rotation, ObjectID id, GameObject shooter, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		player = handler.getPlayer();
		this.shooter = shooter;
		r = new Random();
		width = 8;
		height = 8;
		stats = statsById();
		speedX = (float) (stats[0] * Math.cos(Math.toRadians(rotation + 270)));
		speedY = (float) (stats[0] * Math.sin(Math.toRadians(rotation + 270)));
	}

	@Override
	public void tick() {
		age++;
		x += speedX;
		y += speedY;
		if(age >= 3) active = true;
		if(active) collision();
	}

	@Override
	public void render(Graphics g) {
		AffineTransform at = new AffineTransform();
		at.translate(x, y);
		if(speedX <= 0) at.rotate(Math.atan(speedY / speedX) + renderRotationOffsetOne, spriteWidth / 2, spriteHeight / 2);
		else at.rotate(Math.atan(speedY / speedX) + renderRotationOffsetTwo, spriteWidth / 2, spriteHeight / 2);
		Graphics2D g2d = (Graphics2D) g;
		g2d.drawImage(tex.bulletSprites[stats[3]], at, null);
	}
	
	private void collision(){
		Wall tempWall;
		Tank tempTank;
		for(int i = 0; i < handler.walls.size(); i++){
			tempWall = handler.walls.get(i);
			if(getBounds().intersects(tempWall.getBounds())){
				if(bounces < stats[2]){
					bounce(tempWall);
					break;
				}
				else handler.removeProjectile(this);
			}
		}
		for(int i = 0; i < handler.tanks.size(); i++){
			tempTank = handler.tanks.get(i);
			if(getBounds().intersects(tempTank.getBounds())){
				if(id == ObjectID.EmpBullet) tempTank.setFrozen(empFreezeDuration);
				else tempTank.takeDamage(stats[1]);
				handler.removeProjectile(this);
			}
		}
		if(getBounds().intersects(player.getBounds())){
			if(id == ObjectID.EmpBullet) player.setFrozen(empFreezeDuration);
			player.takeDamage(stats[1]);
			handler.removeProjectile(this);
		}
	}
	
	private void bounce(Wall wall){		
		bounces++;
		Rectangle intersection = getBounds().intersection(wall.getBounds());
		if(intersection.getWidth() > intersection.getHeight()) speedY *= -1;
		else if(intersection.getWidth() < intersection.getHeight()) speedX *= -1;
		else{
			speedX *= -1;
			speedY *= -1;
		}	
	}
	
	private int[] statsById(){
		//speed, damage, bounces, sprite number
		switch(id){
		case BasicBullet:
			return new int[]{5, 1, 0, 0};
		case FastBullet:
			return new int[]{8, 1, 0, 1};
		case EmpBullet:		
			return new int[]{4, 0, 0, 2};
		case BouncyBullet:	
			return new int[]{6, 1, 3, 3};
		case TargetingBullet:
			return new int[]{6, 1, 0, 4};
		default:
			return new int[]{0, 0};
		}
	}
	
	public GameObject getShooter() {
		return shooter;
	}
}
