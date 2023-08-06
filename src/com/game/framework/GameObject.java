package com.game.framework;

import java.awt.Graphics;
import java.awt.Rectangle;

public abstract class GameObject implements Cloneable {
	
	protected float x, y;
	protected ObjectID id;
	protected float speedX, speedY;
	protected float width, height;
	protected boolean visible = true;
	
	public GameObject(float x, float y, ObjectID id){
		this.x = x;
		this.y = y;
		this.id = id;
	}

	public abstract void tick();
	public abstract void render(Graphics g);
	
	public Rectangle getBounds(){
		return new Rectangle((int) x, (int) y, (int) width, (int) height);
	}
	
	public float distanceTo(GameObject object){
		return (float) Math.sqrt(((x + width / 2) - (object.x + object.width / 2)) * ((x + width / 2) - (object.x + object.width / 2)) +
				((y + height / 2) - (object.y + object.height / 2)) * ((y + height / 2) - (object.y + object.height / 2)));
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
	    return super.clone();
	}
	
	public void setX(float x){
		this.x = x;
	}
	public float getX(){
		return x;
	}
	public void setY(float y){
		this.y = y;
	}
	public float getY(){
		return y;
	}
	public void setId(ObjectID id){
		this.id = id;
	}
	public ObjectID getId(){
		return id;
	}
	public void setSpeedX(int speedX){
		this.speedX = speedX;
	}
	public float getSpeedX(){
		return speedX;
	}
	public void setSpeedY(int speedY){
		this.speedY = speedY;
	}
	public float getSpeedY(){
		return speedY;
	}
	
	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}
	
	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
	
	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}
