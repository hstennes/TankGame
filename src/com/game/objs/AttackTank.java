package com.game.objs;

import java.awt.Rectangle;
import java.util.Random;

import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;

public class AttackTank extends Tank{

	private final float moveSpeed = 2;
	private final float turnSpeed = 3;
	private final float dodgeLength = 70;
	private final float dodgeSpeed = 4;
	private final float dodgeBoxSize = 175;
	private final int closeEnough = 64;
	
	private float dodgeTicks;
	private boolean canSeePlayer = false;
	private float lastSeenX;
	private float lastSeenY;
	
	Random r;
	Handler handler;
	Player player;
	
	public AttackTank(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id, handler);
		this.handler = handler;
		player = handler.getPlayer();
		r = new Random();
		lastSeenX = x;
		lastSeenY = y;
	}
	
	public void tick(){
		if(frozen == 0) {
			tankAI();
		}
		super.tick();
	}

	private void tankAI() {
		canSeePlayer = a.directPath(player);
		double centerX = a.centerPoint(this).getX();
		double centerY = a.centerPoint(this).getY();
		Rectangle dodgeBox = new Rectangle((int) (centerX - dodgeBoxSize / 2), (int) (centerY - dodgeBoxSize / 2), (int) dodgeBoxSize, (int) dodgeBoxSize);
		if(a.intersectsBullet(dodgeBox, true, 0) && dodgeTicks == 0) dodge();
		if(dodgeTicks > 0) dodge();
		else {
			if(canSeePlayer == true) {
				driveTowards(player.getX(), player.getY());
				lastSeenX = player.getX();
				lastSeenY = player.getY();
			}
			else {
				if(Math.abs(x - lastSeenX) > closeEnough || Math.abs(y - lastSeenY) > closeEnough) driveTowards(lastSeenX, lastSeenY);
				else {
					forwardSpeed = 0;
					rotationSpeed = 0;
				}
			}
		}
	}

	private void driveTowards(float targetX, float targetY) {
		float targetRotation = a.direction(targetX, targetY);
		rotationSpeed = a.turnTo(targetRotation, turnSpeed);
		forwardSpeed = moveSpeed;
	}
	
	private void dodge() {
		dodgeTicks++;
		if(dodgeTicks == 1) {
			if(r.nextBoolean()) rotationSpeed = dodgeSpeed;
			else rotationSpeed = -dodgeSpeed;
			forwardSpeed = -dodgeSpeed;
		}
		else if(dodgeTicks == dodgeLength / 2){
			rotationSpeed *= -1;
			forwardSpeed *= -1;
		}
		else if(dodgeTicks >= dodgeLength) { 
			dodgeTicks = 0;
			rotationSpeed = 0;
			forwardSpeed = 0;
		}
	}
}
