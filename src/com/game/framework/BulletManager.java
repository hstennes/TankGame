package com.game.framework;

public class BulletManager {

	public static final int BULLET_TYPES = 5;
	
	/**
	 * Holds the quantity of bullets that the player has. 
	 * Index 0: BasicBullet
	 * Index 1: FastBullet
	 * Index 2: EmpBullet
	 * Index 3: BouncyBullet
	 * Index 4: TargetingBullet
	 */
	private int[] availableBullets;
	private int[] savedAvailableBullets;
	
	public BulletManager(int arraySize) {
		availableBullets = new int[arraySize];
		savedAvailableBullets = new int[arraySize];
	}
	
	/**
	 * Saves the current available bullet array so that it can be restored if the player dies
	 */
	public void saveAvailableBullets() {
		for(int i = 0; i < 5; i++) savedAvailableBullets[i] = availableBullets[i];
	}
	
	/**
	 * Changes all indexes in the available bullets array to match their previously saved states
	 */
	public void revertAvailableBullets() {
		for(int i = 0; i < 5; i++) availableBullets[i] = savedAvailableBullets[i];
	}

	public int[] getAvailableBullets() {
		return availableBullets;
	}

	public int[] getSavedAvailableBullets() {
		return savedAvailableBullets;
	}
}
