package com.game.framework;

public class Point {
	private float x;
	private float y;
	
	
	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}
	
	public Point() {
		gotoOrigin();
	}
	
	public void gotoOrigin() {
		x = 0;
		y = 0;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}
	
	public void setX(int x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
