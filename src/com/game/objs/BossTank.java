package com.game.objs;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import com.game.framework.AIHelper;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Point;
import com.game.framework.Tank;
import com.game.window.Camera;
import com.game.window.Game;

public class BossTank extends Tank {

	private final int maxLives = 3;
	private final float moveSpeed = 5;
	private final float turnSpeed = 4;
	private final float attackDist = 300;
	private final float runDist = 250;
	private final float atPointRange = 50;
	private final int rotationSense = 20;
	private final float obstacleDetectRange = 64;
	private final int heartSpacing = 10;
	private final int heartSize = 16;
	private final int heartDisplayWidth = heartSize * maxLives + heartSpacing * (maxLives - 1); 
	private final int dodgeDangerLevel = 1000;
	private ArrayList<Point> playerPath; 
	private Point findPlayerPoint;
	private float prevPlayerRotation;
	private boolean recordPlayerPos;
	private boolean playerVisible;
	private boolean playerPrevVisible;
	private int playerPathIndex;
	private boolean dodgePriority;
	private boolean isFlagCarrier;
	Player player;
	Handler handler;
	Camera camera;
	Game game;
	Random r;
	
	public BossTank(float x, float y, ObjectID id, Game game, Handler handler, Camera camera, boolean isFlagCarrier) {
		super(x, y, id, handler);
		playerPath = new ArrayList<Point>();
		r = new Random();
		player = handler.getPlayer();
		this.game = game;
		this.camera = camera;
		this.handler = handler;
		this.isFlagCarrier = isFlagCarrier;
		lives = maxLives;
	}
	
	@Override
	public void tick() {
		tankAI();
		cannonAI();
		if(lives <= 0) die();
		super.tick();
	}
	
	@Override
	public void render(Graphics g) {
		int iteratorStart = (int) ((heartDisplayWidth - width) / 2);
		int iterations = iteratorStart;
		for(int i = 0; i < maxLives; i++) {
			boolean hasCurrentHeart = (i + 1) <= lives;
			if(hasCurrentHeart) g.drawImage(tex.hudSprites[0], (int) x - iterations, (int) (y + height + heartSpacing), heartSize, heartSize, null);
			else g.drawImage(tex.hudSprites[1], (int) x - iterations, (int) (y + height + heartSpacing), heartSize, heartSize, null);
			iterations -= heartSize + heartSpacing;
		}
		super.render(g);
	}
	
	private void tankAI() {
		a.tick();
		if(dodgePriority) {
			pathfind();
			dodge();
		}
		else {
			dodge();
			pathfind();
		}
	}
	
	private void cannonAI() {
		
	}
	
	private void dodge() {
		Point currentPoint = new Point(x, y);
		float currentBulletDanger = a.bulletDangerCalculation(getBounds());
		if(currentBulletDanger > dodgeDangerLevel) {
			dodgePriority = true; 
			float[] dangers = new float[7];
			dangers[0] = currentBulletDanger;
			Point fdlPoint = a.degreeTranslatePoint(currentPoint, AIHelper.normalizeRotation(tankRotation - turnSpeed), moveSpeed * 2);
			Rectangle fdlRect = new Rectangle((int) fdlPoint.getX(), (int) fdlPoint.getY(), (int) width, (int) height);
			dangers[1] = a.bulletDangerCalculation(fdlRect);
			Point fdrPoint = a.degreeTranslatePoint(currentPoint, tankRotation + turnSpeed, moveSpeed * 2);
			Rectangle fdrRect = new Rectangle((int) fdrPoint.getX(), (int) fdrPoint.getY(), (int) width, (int) height);
			dangers[2] = a.bulletDangerCalculation(fdrRect);
			Point fdPoint = a.degreeTranslatePoint(currentPoint, tankRotation, moveSpeed * 2);
			Rectangle fdRect = new Rectangle((int) fdPoint.getX(), (int) fdPoint.getY(), (int) width, (int) height);
			dangers[3] = a.bulletDangerCalculation(fdRect);
			Point bklPoint = a.degreeTranslatePoint(currentPoint, AIHelper.normalizeRotation(tankRotation - turnSpeed), -moveSpeed * 2);
			Rectangle bklRect = new Rectangle((int) bklPoint.getX(), (int) bklPoint.getY(), (int) width, (int) height);
			dangers[4] = a.bulletDangerCalculation(bklRect);
			Point bkrPoint = a.degreeTranslatePoint(currentPoint, tankRotation + turnSpeed, -moveSpeed * 2);
			Rectangle bkrRect = new Rectangle((int) bkrPoint.getX(), (int) bkrPoint.getY(), (int) width, (int) height);
			dangers[5] = a.bulletDangerCalculation(bkrRect);
			Point bkPoint = a.degreeTranslatePoint(currentPoint, 0, -moveSpeed * 2);
			Rectangle bkRect = new Rectangle((int) bkPoint.getX(), (int) bkPoint.getY(), (int) width, (int) height);
			dangers[6] = a.bulletDangerCalculation(bkRect);
			float lowestDanger = 1000000000;
			int lowestDangerIndex = 0;
			for(int i = 0; i < dangers.length; i++) {
				if(dangers[i] < lowestDanger) {
					lowestDanger = dangers[i];
					lowestDangerIndex = i;
				}
			}
			doDodgeMovement(lowestDangerIndex);
		}
		else dodgePriority = false;
	}
	
	private void pathfind() {
		playerVisible = a.directPath(player);
		if(playerVisible) {
			playerPrevVisible = true;
			playerPath.clear();
			playerPathIndex = 0;
			openAreaActions();
		}
		else followPlayerPath();
		recordPlayerInfo();
	}
	
	private void openAreaActions() {
		if(distanceTo(player) >= attackDist) {
			a.clearAllTimers();
			rotationSpeed = a.turnTo(a.direction(player), turnSpeed);
			forwardSpeed = moveSpeed;
		}
		else if(distanceTo(player) <= runDist) runFromPlayer();
		else {
			a.clearAllTimers();
			rotationSpeed = 0;
			forwardSpeed = 0;
		}
	}
	
	private void recordPlayerInfo() {
		if(Math.abs(player.getTankRotation() - prevPlayerRotation) > rotationSense) {
			recordPlayerPos = true;
			prevPlayerRotation = player.getTankRotation();
		}
		if(recordPlayerPos && player.getForwardSpeed() != 0) {
			recordPlayerPos = false;
			if(playerVisible) findPlayerPoint = a.centerPoint(player);
			else playerPath.add(a.centerPoint(player));
		}
	}
	
	private void runFromPlayer() {
		if(a.timerExists("run_turn")) {
			if(a.checkTimer("run_turn")) {
				rotationSpeed = 0;
				a.clearTimer("run_turn");
			}
		}
		else { 
			if(a.searchObstacles(obstacleDetectRange)) {
				a.setTimer("run_turn", (int) (90 / turnSpeed));
				if(r.nextBoolean()) rotationSpeed = turnSpeed;
				else rotationSpeed = -turnSpeed;
				forwardSpeed = moveSpeed;
			}
			else {
				rotationSpeed = a.turnTo(AIHelper.normalizeRotation(a.direction(player) + 180), turnSpeed);
				forwardSpeed = moveSpeed;
			}
		}
	}
	
	private void followPlayerPath() {
		a.clearAllTimers();
		if(playerPrevVisible) playerPath.add(findPlayerPoint);
		if(playerPathIndex < playerPath.size()) {
			Point target = playerPath.get(playerPathIndex);
			playerPrevVisible = false;
			if(target != null) {
				rotationSpeed = a.turnTo(a.direction((float) target.getX(), (float) target.getY()), turnSpeed);
				forwardSpeed = moveSpeed;
				if(a.distanceTo(target) < atPointRange) playerPathIndex++;
			}
		}
	}
	
	private void doDodgeMovement(int index) {
		switch(index) {
		case 0:
			forwardSpeed = 0;
			rotationSpeed = 0;
			break;
		case 1:
			forwardSpeed = moveSpeed;
			rotationSpeed = -turnSpeed;
			break;
		case 2:
			forwardSpeed = moveSpeed;
			rotationSpeed = turnSpeed;
		case 3:
			forwardSpeed = moveSpeed;
			rotationSpeed = 0;
			break;
		case 4:
			forwardSpeed = -moveSpeed;
			rotationSpeed = -turnSpeed;
			break;
		case 5:
			forwardSpeed = -moveSpeed;
			rotationSpeed = turnSpeed;
			break;
		case 6:
			forwardSpeed = -moveSpeed;
			rotationSpeed = 0;
		}
	}
	
	private void die() {
		if(!exploding) { 
			camera.setShaking(Camera.LONG_SHAKE_DURATION);
			exploding = true;
		}
		if(camera.getShaking() == 0) {
			if(isFlagCarrier) handler.setLevelFinish(new LevelFinish(x, y, ObjectID.LevelFinish, game, handler));
			handler.removeTank(this);
		}
	}
}