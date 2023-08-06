package com.game.objs;

import com.game.framework.AIHelper;
import com.game.framework.GameObject;
import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;

public class TargetingBullet extends Projectile {

	private final float accuracy = 1.5f;
	private float rotationSpeed;
	private float rotation;

	private Tank target;
	
	public TargetingBullet(float x, float y, float rotation, ObjectID id, GameObject shooter, Handler handler) {
		super(x, y, rotation, id, shooter, handler);
		if(shooter.getId() != ObjectID.Player) target = handler.getPlayer();
		else {
			for(int i = 0; i < handler.tanks.size(); i++) {
				if(target == null) target = handler.tanks.get(i);
				else if(distanceTo(handler.tanks.get(i)) < distanceTo(target)) target = handler.tanks.get(i);
			}
		}
		this.rotation = rotation + 180;
		this.rotation = AIHelper.normalizeRotation(this.rotation);
	}
	
	@Override
	public void tick() {
		super.tick();
		bulletAI();
	}
	
	private void bulletAI() {
		if(target != null) {
			float targetRotation = (float) Math.toDegrees(Math.atan((y - target.getY()) / (x - target.getX())));
			targetRotation += 90;
			if(x < target.getX()) targetRotation += 180;
			AIHelper.normalizeRotation(targetRotation);
			if(rotation == targetRotation) rotationSpeed = 0;
			else if(Math.abs(rotation - targetRotation) <= 180) rotationSpeed = accuracy * ((targetRotation - rotation) / Math.abs(targetRotation - rotation));
			else rotationSpeed = accuracy * ((rotation - targetRotation) / Math.abs(rotation - targetRotation));	
			rotation += rotationSpeed;
			rotation = AIHelper.normalizeRotation(rotation);
			speedX = (float) -(stats[0] * Math.cos(Math.toRadians(rotation + 270)));
			speedY = (float) -(stats[0] * Math.sin(Math.toRadians(rotation + 270)));
		}
	}
}
