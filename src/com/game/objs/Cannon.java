package com.game.objs;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Random;

import com.game.framework.CannonInfo;
import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;
import com.game.framework.Texture;
import com.game.window.Game;

public class Cannon extends GameObject {

	private int xOffset, yOffset;
	private int spriteNumber;
	private float rotation;
	private int fireTicks;
	private int fireRate;
	private ObjectID bulletType;
	private CannonInfo cannonInfo;
	private final float pivotX = 10.5f;
	private final float pivotY = 36f;
	private final int bulletPlacementOffset = 8;
	
	Texture tex = Game.getTexture();
	Random r;
	GameObject owner;
	Handler handler;
	Player player;
	
	public Cannon(float x, float y, ObjectID id, GameObject owner, Handler handler) {
		super(x, y, id);
		this.owner = owner;
		this.handler = handler;
		player = handler.getPlayer();
		r = new Random();
		cannonInfo = new CannonInfo(this, handler.getPlayer());
		xOffset = (int) cannonInfo.getOffsets().getX();
		yOffset = (int) cannonInfo.getOffsets().getY();
		fireRate = cannonInfo.getFireRate();
		bulletType = cannonInfo.getBulletType();
		spriteNumber = cannonInfo.getSpriteNumber();
	}

	@Override
	public void tick() {
		x = owner.getX() + xOffset;
		y = owner.getY() + yOffset;
		if(id != ObjectID.BlueCannon && id != ObjectID.MenuCannon) {
			fireRate = cannonInfo.getFireRate();
			bulletType = cannonInfo.getBulletType();
			if(id == ObjectID.WhiteCannon) {
				if(fireRate - fireTicks == CautiousTank.WHITE_TANK_VISIBLE_DURATION / 2) owner.setVisible(true);
				else if(fireRate - fireTicks == fireRate - CautiousTank.WHITE_TANK_VISIBLE_DURATION / 2) owner.setVisible(false);
			}
			targetPlayer();
			shootAI();	
		}
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		AffineTransform cannon = new AffineTransform();
		cannon.translate((int) x, (int) y);
		cannon.rotate(Math.toRadians(rotation), pivotX, pivotY);
		g2d.drawImage(tex.tankSprites[spriteNumber], cannon, null);
	}
	
	private void targetPlayer() {
		rotation = (float) Math.toDegrees(Math.atan((player.getY() - y) / (player.getX() - x))) + 90;
		if(player.getX() < x) rotation += 180;
	}
	
	private void shootAI() {
		fireTicks++;
		Wall tempWall = null;
		boolean pathObstructed = false;
		for(int i = 0; i < handler.walls.size(); i++){
			if(i < handler.walls.size() + 1) tempWall = handler.walls.get(i);
			if(player != null && tempWall != null && tempWall.getBounds().intersectsLine(x + Tank.TANK_SIZE / 2, y + Tank.TANK_SIZE / 2, player.getX() + Tank.TANK_SIZE / 2, 
					player.getY() + Tank.TANK_SIZE)) pathObstructed = true;
		}
		if(!pathObstructed && fireTicks > fireRate) {
			shoot(bulletType);
			fireTicks = 0;
		}
	}
	
	public void shoot(ObjectID bulletType) {
		float x = (float) ((this.x + bulletPlacementOffset) + Math.cos(Math.toRadians(90 - rotation)) * pivotY);
		float y = (float) ((this.y + pivotY) - Math.sin(Math.toRadians(90 - rotation)) * pivotY);
		if(bulletType == ObjectID.TargetingBullet) handler.addProjectile(new TargetingBullet(x, y, rotation, bulletType, owner, handler));
		else handler.addProjectile(new Projectile(x, y, rotation, bulletType, owner, handler));
	}
	
	public void refreshLocation() {
		x = owner.getX() + xOffset;
		y = owner.getY() + yOffset;
	}
	
	public GameObject getOwner() {
		return owner;
	}
	
	public void setOwner(GameObject owner) {
		this.owner = owner;
	}

	public float getRotation() {
		return rotation;
	}

	public void setRotation(float cannonRotation) {
		this.rotation = cannonRotation;
	}
	
}
