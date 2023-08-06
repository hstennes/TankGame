package com.game.objs;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;

import com.game.framework.AIHelper;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;

public class CautiousTank extends Tank {

	public final static int WHITE_TANK_VISIBLE_DURATION = 40;
	private final int sightDistance = 256;
	private final int coverSearchQuality = 32;
	private final float moveSpeed = 4;
	private final float turnSpeed = 3;
	
	private int state = 2;
	
	Random r;
	Handler handler;
	Player player;
	Rectangle sightRange;
	Point targetPoint;
	
	public CautiousTank(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id, handler);
		this.handler = handler;
		player = handler.getPlayer();
		sightRange = new Rectangle((int) x, (int) y, sightDistance * 2, sightDistance * 2);
		targetPoint = new Point(0, 0);
		r = new Random();
	}
	
	@Override
	public void tick() {
		if(frozen == 0) {
			tankAI();
		}
		super.tick();
	}
	
	private void tankAI() {
		if(state == 0) { 
			//running
			sightRange.setLocation((int) (x - (sightDistance - (width / 2))), (int) (y - (sightDistance - (height / 2))));
			driveTowards(x + (x - player.getX()), y + (y - player.getY()));
			Point possibleCover = new Point();
			Wall tempWall;
			boolean coverFound = false;
			boolean coverAccessible = true;
			
			int startX;
			int startY;
			int xLimit;
			int yLimit;
			int addX;
			int addY;
		
			if(r.nextBoolean()) {
				startX = (int) sightRange.getX();
				startY = (int) sightRange.getY();
				xLimit = (int) (sightRange.getX() + sightRange.width);
				yLimit = (int) (sightRange.getY() + sightRange.height);
				addX = coverSearchQuality;
				addY = coverSearchQuality;
			}
			else {
				startX = (int) (sightRange.getX() + sightRange.width);
				startY = (int) (sightRange.getY() + sightRange.height);
				xLimit = (int) sightRange.getX();
				yLimit = (int) sightRange.getY();
				addX = -coverSearchQuality;
				addY = -coverSearchQuality;
			}
			
			for(int yy = startY; yy < yLimit; yy += addY) {
				for(int xx = startX; xx < xLimit; xx += addX) {
					possibleCover.setLocation(xx, yy);
					//draw a line from possibleCover to the player and check if it intersects a wall to determine if possibleCover can be used as cover
					coverFound = false;
					coverAccessible = true;
					
					for(int i = 0; i < handler.walls.size(); i++) {   
						tempWall = handler.walls.get(i);                                     
						if(tempWall.getBounds().intersectsLine((int) possibleCover.getX(), (int) possibleCover.getY(), player.getX() + (player.getWidth() / 2), player.getY() + (player.getHeight() / 2))) {
							coverFound = true;				
						}   
						if(tempWall.getBounds().intersectsLine((int) possibleCover.getX(), (int) possibleCover.getY(), x, y)){
							coverAccessible = false;
							break;
						}
					}	
					if(coverFound && coverAccessible) break;	
				}
				if(coverFound && coverAccessible) break;	
			}
			if(coverFound && coverAccessible) { 
				state = 1;
				targetPoint = possibleCover;
			}
			
			if(player.getFrozen() != 0) state = 3;
		}
		else if(state == 1) {
			//hiding
			if(Math.abs(x - targetPoint.getX()) > 5 && Math.abs(y - targetPoint.getY()) > 5) {
				driveTowards((float) targetPoint.getX(), (float) targetPoint.getY());
			}
			else state = 2;
			
			if(player.getFrozen() != 0) state = 3;
		}
		else if(state == 2) {
			//hidden
			for(int i = 0;  i < handler.walls.size(); i++) handler.walls.get(i).setDebug(false);
			forwardSpeed = 0;
			rotationSpeed = 0;
			state = 0;
			sightRange.setLocation((int) (x - (sightDistance - (width / 2))), (int) (y - (sightDistance - (height / 2))));
			for(int i = 0; i < handler.walls.size(); i++) {
				if(handler.walls.get(i).getBounds().intersectsLine(x + (width / 2), y + (height / 2), player.getX() + (player.getWidth() / 2), player.getY() + (player.getHeight() / 2))) {
					state = 2;
				}
			}
			
			if(player.getFrozen() != 0) state = 3;
		}
		else if(state == 3) {
			forwardSpeed = 0;
			rotationSpeed = 0;
			if(player.getFrozen() == 0) state = 2;
		}
	}

	private void driveTowards(float targetX, float targetY) {
		float targetRotation = (float) Math.toDegrees(Math.atan((y - targetY) / (x - targetX)));
		targetRotation = -targetRotation + 90;
		if(x < targetX) targetRotation += 180;
		targetRotation = 360 - targetRotation;
		targetRotation = AIHelper.normalizeRotation(targetRotation);
		if(Math.abs(tankRotation - targetRotation) < 2) rotationSpeed = 0;
		else if(Math.abs(tankRotation - targetRotation) <= 180) rotationSpeed = turnSpeed * ((targetRotation - tankRotation) / Math.abs(targetRotation - tankRotation));
		else rotationSpeed = turnSpeed * ((tankRotation - targetRotation) / Math.abs(tankRotation - targetRotation));	
		tankRotation = AIHelper.normalizeRotation(tankRotation);
		forwardSpeed = moveSpeed;
	}
}
