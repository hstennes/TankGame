package com.game.window;

import java.util.Random;

import com.game.framework.GameObject;

public class Camera {

	private final int shakeIntensity = 10;
	public final static int NORMAL_SHAKE_DURATION = 40;
	public final static int LONG_SHAKE_DURATION = 80;
	
	private float x, y;
	private int shaking;
	
	Random r;
	
	public Camera(float x, float y){
		this.x = x;
		this.y = y;
		r = new Random();
	}

	public void tick(GameObject player){
		x = -player.getX() + (Game.WIDTH/2-player.getWidth()/2);
		y = -player.getY() + (Game.HEIGHT/2-player.getHeight()/2);
		
		if(shaking > 0) { 
			shaking--;
			x += r.nextInt(shakeIntensity) - shakeIntensity / 2;
			y += r.nextInt(shakeIntensity) - shakeIntensity / 2;
		}
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public int getShaking() {
		return shaking;
	}

	public void setShaking(int shaking) {
		this.shaking = shaking;
	}
}
