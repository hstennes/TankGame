package com.game.framework;

import java.util.Random;

import com.game.objs.Cannon;
import com.game.objs.Player;

public class CannonInfo {

	private Cannon cannon;
	private Player player;
	private Random r;
	
	public CannonInfo(Cannon cannon, Player player) {
		this.cannon = cannon;
		this.player = player;
		r = new Random();
	}
	
	public int getFireRate() {
		switch(cannon.getId()) {
		case BlueCannon:
			return 0;
		case RedCannon:
			return 180;
		case YellowCannon:
			return 100;
		case PurpleCannon:
			return 180;
		case BlackCannon:
			if(player.getFrozen() == 0) return 40;
			else return 120;
		case WhiteCannon:
			return 150;
		case TurretCannon:
			return 180;
		case MenuCannon:
			return 0;
		case BossCannon:
			return 180;
		default:
			System.out.println("Invalid cannon ID: " + cannon.getId());
			return 0;
		}
	}
	
	public ObjectID getBulletType() {
		switch(cannon.getId()) {
		case BlueCannon:
			return null;
		case RedCannon:
			return ObjectID.BasicBullet;       
		case YellowCannon:
			return ObjectID.FastBullet;
		case PurpleCannon:
			return ObjectID.BouncyBullet;
		case BlackCannon:
			if(player.getFrozen() == 0) return ObjectID.EmpBullet;
			else return ObjectID.BasicBullet;
		case WhiteCannon:
			return ObjectID.BasicBullet;
		case TurretCannon:
			return randomTurretBulletType();
		case MenuCannon:
			return null;
		case BossCannon:
			return ObjectID.TargetingBullet;
		default:
			System.out.println("Invalid cannon ID: " + cannon.getId());
			return null;
		}
	}
	
	public Point getOffsets() {
		if(cannon.getOwner().getId() == ObjectID.Turret) return new Point(5, -20);
		else return new Point(12, -11);
	}
	
	public int getSpriteNumber() {
		return Tank.getSpriteNumber(cannon.getOwner().getId()) + 1;
	}
	
	private ObjectID randomTurretBulletType() {
		int random = r.nextInt(100);
		ObjectID tempBullet;
		if(random <= 90) tempBullet = ObjectID.BasicBullet;
		else if(random <= 94) tempBullet = ObjectID.FastBullet;
		else if(random <= 96) tempBullet = ObjectID.EmpBullet;
		else if(random  <= 98) tempBullet = ObjectID.BouncyBullet;
		else tempBullet = ObjectID.TargetingBullet;
		return tempBullet;
	}
	
	/**
	 * Returns the ID of the type of cannon that should be used with this tank
	 * @param tankID The id of the tank that is finding its cannon type
	 * @return An ObjectID representing the correct type of cannon
	 */
	public static ObjectID getCannonType(ObjectID tankID) {
		switch(tankID) {
		case Player:
			return ObjectID.BlueCannon;
		case RedTank:
			return ObjectID.RedCannon;
		case YellowTank:
			return ObjectID.YellowCannon;
		case PurpleTank:
			return ObjectID.PurpleCannon;
		case BlackTank:
			return ObjectID.BlackCannon;
		case WhiteTank:
			return ObjectID.WhiteCannon;
		case LeftMenuTank:
			return ObjectID.MenuCannon;
		case RightMenuTank:
			return ObjectID.MenuCannon;
		case Turret:
			return ObjectID.TurretCannon;
		case BossTank:
			return ObjectID.BossCannon;
		default:
			return ObjectID.RedCannon;
		}
	}
	
}
