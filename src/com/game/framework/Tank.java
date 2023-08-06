package com.game.framework;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

import com.game.objs.Cannon;
import com.game.objs.Trail;
import com.game.objs.Wall;
import com.game.window.Game;

public class Tank extends GameObject{

	public static final float TANK_SIZE = 45;
	protected float forwardSpeed;
	protected float rotationSpeed;
	protected float tankRotation;
	protected int frozen;
	protected int lives = 1;
	protected boolean exploding = false;
	protected float deathAnimationFrame;
	protected AIHelper a;
	private final float trailPlaceDistFromCenter = 26.6f;
	private final float trailPlaceRotation = 230;
	private final float trailInterval = 24;
	private final float deathAnimationIncrement = 0.2f;
	private final Point rotationAnchorPoint = new Point(22, 25);
	private Point lastTrailLocation;
	
	private int sprite;
	protected final float deathAnimationLength = 6;
	private int doorLink;

	protected Texture tex = Game.getTexture();
	private Cannon cannon;
	private Handler handler;
	
	public Tank(float x, float y, ObjectID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		cannon = new Cannon(x, y, CannonInfo.getCannonType(id), this, handler);
		a = new AIHelper(this, handler);
		lastTrailLocation = new Point(0, 0);
		sprite = getSpriteNumber(id);
		width = TANK_SIZE;
		height = TANK_SIZE;
	}

	@Override
	public void tick() {
		if(!exploding) {
			if(frozen == 0) {
				calculateSpeeds();	
				x += speedX;
				y += speedY;
				placeTrail();
				cannon.tick();
				tankRotation += rotationSpeed;
				collision();
				tankRotation = AIHelper.normalizeRotation(tankRotation);
			}
			else frozen--;
		}
		else {
			if(deathAnimationFrame < deathAnimationLength) deathAnimationFrame += deathAnimationIncrement;
			else if(id != ObjectID.Player && id != ObjectID.BossTank) handler.removeTank(this);
		}
	}

	@Override
	public void render(Graphics g) {
		if(exploding) {
			BufferedImage explosion = tex.explosionSprites[(int) deathAnimationFrame];
			g.drawImage(explosion, (int) (x - (explosion.getWidth() - width) / 2), (int) (y - (explosion.getHeight() - height) / 2), null);
		}
		else { 
			if(visible) {
				Graphics2D g2d = (Graphics2D) g;
				AffineTransform tank = new AffineTransform();
				tank.translate(x, y);
				tank.rotate(Math.toRadians(tankRotation), rotationAnchorPoint.getX(), rotationAnchorPoint.getY());
				g2d.drawImage(tex.tankSprites[sprite], tank, null);
				cannon.render(g);
			}
		}
	}

	private void calculateSpeeds(){
		speedX = (float) (forwardSpeed * Math.cos(Math.toRadians(tankRotation + 270)));
		speedY = (float) (forwardSpeed * Math.sin(Math.toRadians(tankRotation + 270)));
	}
	
	protected void collision(){
		Wall tempWall = null;
		GameObject tempHelper;
		for(int i = 0; i < handler.walls.size(); i++){
			try{
				tempWall = handler.walls.get(i);
			}
			catch(IndexOutOfBoundsException e) {
				System.out.println("Caught IndexOutOfBoundsException");
				e.printStackTrace();
			}
			if(tempWall != null) {
				if(getBoundsTop().intersects(tempWall.getBounds())) y = tempWall.getY() + tempWall.getHeight();		
				if(getBoundsBottom().intersects(tempWall.getBounds())) y = tempWall.getY() - height;	
				if(getBoundsRight().intersects(tempWall.getBounds())) x = tempWall.getX() - width;
				if(getBoundsLeft().intersects(tempWall.getBounds())) x = tempWall.getX() + tempWall.getWidth();	
			}
			cannon.refreshLocation();
		}
		for(int i = 0; i < handler.helpers.size(); i++){
			tempHelper = handler.helpers.get(i);
			if(tempHelper.getId() == ObjectID.AmmoBox) {
				if(getBoundsTop().intersects(tempHelper.getBounds())) y = tempHelper.getY() + tempHelper.getHeight();		
				if(getBoundsBottom().intersects(tempHelper.getBounds())) y = tempHelper.getY() - height;	
				if(getBoundsRight().intersects(tempHelper.getBounds())) x = tempHelper.getX() - width;
				if(getBoundsLeft().intersects(tempHelper.getBounds())) x = tempHelper.getX() + tempHelper.getWidth();	
				cannon.refreshLocation();
			}
		}
	}
	
	private void placeTrail() {
		Point center = a.centerPoint(this);
		Point placeTrail = a.degreeTranslatePoint(center, AIHelper.normalizeRotation(tankRotation + trailPlaceRotation), trailPlaceDistFromCenter);
		if(a.distanceBetween(lastTrailLocation, center) >= trailInterval && visible) {
			handler.addGraphicsElement(new Trail(placeTrail.getX(), placeTrail.getY(), tankRotation, ObjectID.Trail, handler));
			lastTrailLocation = center;
		}
	}
	
	public void shootCannon(ObjectID bulletType) {
		if(frozen == 0) cannon.shoot(bulletType);
	}
	
	public void takeDamage(int damage) {
		lives -= damage;		
		if(id == ObjectID.Player) {
			if(lives <= 0) lives = 0;
		}
		else {
			if(lives <= 0) {		
				exploding = true;	
				frozen = 1;
				handler.onTankDestroyed();
			}	
		}
	}
	
	public void refreshReferencedObjects() {
		cannon.setOwner(this);
		a.setOwner(this);
	}
	
	public static int getSpriteNumber(ObjectID tankID) {
		switch(tankID) {
		case Player:
			return 0;
		case RedTank:
			return 2;
		case YellowTank:
			return 4;
		case PurpleTank:
			return 6;
		case BlackTank:
			return 8;
		case WhiteTank:
			return 10;
		case LeftMenuTank:
			return 2;
		case RightMenuTank:
			return 2;
		case BossTank:
			return 20;
		case Turret:
			return 2;
		default:
			return 12;
		}
	}
	
	public Rectangle getBoundsBottom() {
		return new Rectangle((int) (x + width / 6), (int) (y + height / 2), (int) width / 3 * 2, (int) height / 2);		
	}
	public Rectangle getBoundsTop() {
		return new Rectangle((int) (x + width / 6), (int) y, (int) width / 3 * 2, (int) height / 2);
	}
	public Rectangle getBoundsRight() {
		return new Rectangle((int) (x + width - 5), (int) y + 5, 5, (int) height - 10);
	}
	public Rectangle getBoundsLeft() {
		return new Rectangle((int) x, (int) y + 5, 5, (int) height - 10);
	}
	
	public float getRotationSpeed() {
		return rotationSpeed;
	}

	public void setRotationSpeed(int rotationSpeed) {
		this.rotationSpeed = rotationSpeed;
	}

	public float getTankRotation() {
		return tankRotation;
	}

	public void setTankRotation(int tankRotation) {
		this.tankRotation = tankRotation;
	}
	
	public float getCannonRotation() {
		return cannon.getRotation();
	}
	
	public void setCannonRotation(float cannonRotation) {
		cannon.setRotation(cannonRotation);
	}

	public float getForwardSpeed() {
		return forwardSpeed;
	}

	public void setForwardSpeed(float forwardSpeed) {
		this.forwardSpeed = forwardSpeed;
	}
	
	public int getLives(){
		return lives;
	}
	
	public void setLives(int lives) {
		this.lives = lives;
	}

	public int getFrozen() {
		return frozen;
	}

	public void setFrozen(int frozen) {
		this.frozen = frozen;
	}

	public int getDoorLink() {
		return doorLink;
	}

	public void setDoorLink(int doorLink) {
		this.doorLink = doorLink;
	}
}