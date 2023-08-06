package com.game.framework;

import java.awt.Rectangle;
import java.util.ArrayList;

import com.game.objs.Projectile;
import com.game.objs.Wall;

public class AIHelper {
	
	private final int widthShrink = 5;
	private final int directionDiffIgnoreThresh = 20;
	private String[] timerNames = new String[10];
	private int[] timerCounters = new int[10];
	private int[] timerLengths = new int[10];
	
	private Tank owner;
	private Handler handler;
	
	public AIHelper(Tank owner, Handler handler) {
		this.owner = owner;
		this.handler = handler;
		for(int i = 0; i < timerLengths.length; i++) timerLengths[i] = -1;
		for(int i = 0; i < timerNames.length; i++) timerNames[i] = "_";
	}
	
	/**
	 * Updates the timers running of this AIHelper object.  This method is to be called by a subclass of Tank every 60th of a second
	 */
	public void tick() {
		for(int i = 0; i < timerLengths.length; i++) {
			if(timerLengths[i] != -1) {
				timerCounters[i]++;
				if(timerCounters[i] == timerLengths[i] + 1) timerCounters[i] = 0;
			}
		}
	}
	
	/**
	 * Calculates the angle needed for the front of Tank owner to face at the origin point of gameObject
	 * @param gameObject The GameObject to point at
	 * @return The calculated angle
	 */
	public float direction(GameObject gameObject) {
		Point one = centerPoint(owner);
		Point two = centerPoint(gameObject);
		return direction(one, two);
	}
	
	/**
	 * Calculates the angle needed for the front of Tank owner to face at the specified coordinate 
	 * @param gameObject The GameObject to point at
	 * @return The calculated angle
	 */
	public float direction(float x, float y) {
		Point one = centerPoint(owner);
		Point two = new Point((int) x, (int) y);
		return direction(one, two);
	}
	
	/**
	 * Calculates the rotationSpeed needed to turn to the specified angle using the shortest path. This rotation speed is limited to positive or negative
	 * turnSpeed 
	 * @param targetRotation The rotation to turn to
	 * @param turnSpeed The maximum rotation speed of this Tank
	 * @return The calculated speed
	 */
	public float turnTo(float targetRotation, final float turnSpeed) {
		float rotation = owner.getTankRotation();
		float rotationSpeed;
		if(Math.abs(rotation - targetRotation) < 2) rotationSpeed = 0;
		else if(Math.abs(rotation - targetRotation) <= 180) rotationSpeed = turnSpeed * ((targetRotation - rotation) / Math.abs(targetRotation - rotation));
		else rotationSpeed = turnSpeed * ((rotation - targetRotation) / Math.abs(rotation - targetRotation));	
		return rotationSpeed;
	}

	/**
	 * Calculates the center point of the given GameObject
	 * @param gameObject The object to find the center of
	 * @return A Point representing the center of the gameObject
	 */
	public Point centerPoint(GameObject gameObject) {
		int playerCenterX = (int) (gameObject.getX() + gameObject.getWidth() / 2);
		int playerCenterY = (int) (gameObject.getY() + gameObject.getHeight() / 2);
		Point p = new Point(playerCenterX, playerCenterY);
		return p;
	}
	
	/**
	 * Calculates the center point of the given Rectangle using Rectangle.getCenterX() and Rectangle.getCenterY()
	 * @param r The Rectangle to calculate the center of
	 * @return A Point representing the center of the Rectangle
	 */
	public Point centerPoint(Rectangle r) {
		return new Point((int) r.getCenterX(), (int) r.getCenterY());
	}
	
	/**
	 * Finds the Point that is created by moving the given number of units along a line with the given rotation from a starting point of p
	 * @param p The point to translate from
	 * @param degrees The slope of the line to travel along, given in degrees relative to a vertical line
	 * @param dist The distance to travel along the given line
	 * @return
	*/
	public Point degreeTranslatePoint(Point p, float degrees, float dist) {
		float dx = (float) (dist * Math.cos(Math.toRadians(degrees + 270)));
		float dy = (float) (dist * Math.sin(Math.toRadians(degrees + 270)));
		return new Point(p.getX() + dx, p.getY() + dy);
	}
	
	/**
	 * Find the shortest distance in pixels from point a to point b
	 * @param a The first point to consider
	 * @param b The second point to consider
	 * @return The distance between the points
	 */
	public float distanceBetween(Point a, Point b) {
		return (float) Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX()) + (a.getY() - b.getY()) * (a.getY() - b.getY()));
	}
	
	/**
	 * Finds the straight line distance from Tank owner to the given point 
	 * @param p The Point to calculate the distance to
	 * @return The distance from Tank owner to the specified point as a float
	 */
	public float distanceTo(Point p) {
		return distance(p, centerPoint(owner));
	}
	
	/**
	 * Sets a timer that fires every x ticks
	 * @param name A String that will be associated with this timer
	 * @param length The number of ticks between timer fires
	 * @return The index that the timer information is being stored at in this AIHelper object
	 */
	public int setTimer(String name, int length) {
		int index = -1;
		for(int i = 0; i < timerNames.length; i++) {
			if(timerNames[i].equals("_")) {
				index = i;
				break;
			}
		}
		if(index != -1) {
			timerNames[index] = name;
			timerCounters[index] = 0;
			timerLengths[index] = length - 1;
		}
		return index;
	}
	
	public boolean timerExists(String name) {
		int index = timerIndexFromName(name);
		if(index != -1) return true;
		else return false;
	}
	
	public boolean timerExists(int index) {
		if(timerNames[index].equals("_")) return false;
		else return true;
	}
	
	/**
	 * Clears the timer with the specified name, allowing its array position to be filled by a future timer
	 * @param name The name of the timer to be cleared
	 */
	public void clearTimer(String name) {
		int index = timerIndexFromName(name);
		clearTimer(index);
	}
	
	/**
	 * Clears the timer at the specified index, allowing its array position to be used of r a future timer
	 * @param index
	 */
	public void clearTimer(int index) {
		if(index <= timerCounters.length && index != -1) {
			timerNames[index] = "_";
			timerLengths[index] = -1;
			timerCounters[index] = 0;
		}
	}
	
	public void clearAllTimers() {
		for(int i = 0; i < timerNames.length; i++) {
			timerNames[i] = "_";
			timerCounters[i] = 0;
			timerLengths[i] = -1;
		}
	}
	
	/**
	 * Checks if the timer corresponding to the specified String is currently firing.  If there is no timer with the specified String, the return value
	 * is false
	 * @param name The name that was assigned to the desired timer when it was created using the setTimer method
	 * @return The status of the timer
	 */
	public boolean checkTimer(String name) {
		int index = timerIndexFromName(name);
		return checkTimer(index);
	}
	
	/**
	 * Checks if the timer stored at the specified index is currently firing.  If there is no timer data being stored at this index, the return value 
	 * is false.
	 * @param name The index that the desired timer is stored at.  This information is returned by the setTimer method
	 * @return The status of the timer
	 */
	public boolean checkTimer(int index) {
		if(index == -1) return false;
		else {
			if(index <= timerCounters.length && timerLengths[index] != -1) {
				if(timerCounters[index] == timerLengths[index]) return true;
				else return false;
			}
			else return false;
		}
	}
	
	/**
	 * Prints the names, counters, and lengths of each timer on this AIHelper object in the console. 
	 */
	public void printTimer() {
		System.out.println("Timers for tank " + owner);
		for(int i = 0; i < timerNames.length; i++) {
			System.out.println("Name: " + timerNames[i] + ", Counter: " + timerCounters[i] + ", Length: " + timerLengths[i]);
		}
	}
	
	/**
	 * Checks to see if Tank owner will reach an obstacle that exists within the specified range if it continues in a straight line on its current path
	 * @param range The range to detect obstacles in
	 * @return A true boolean value if one or more obstacles were detected in the specified range, a false value otherwise
	 */
	public boolean searchObstacles(float range) {
		Point oneL = degreeTranslatePoint(centerPoint(owner), normalizeRotation(owner.tankRotation - 90), owner.getWidth() / 2 - widthShrink);
		Point oneR = degreeTranslatePoint(centerPoint(owner), normalizeRotation(owner.tankRotation + 90), owner.getWidth() / 2 - widthShrink);
		float[] vector = new float[2];
		vector[0] = (float) (range * Math.cos(Math.toRadians(owner.getTankRotation() + 270)));
		vector[1] = (float) (range * Math.sin(Math.toRadians(owner.getTankRotation() + 270)));
		Point twoL = new Point((int) (oneL.getX() + vector[0]), (int) (oneL.getY() + vector[1]));
		Point twoR = new Point((int) (oneR.getX() + vector[0]), (int) (oneR.getY() + vector[1]));
		boolean obstacleFound = false;
		for(int i = 0; i < handler.walls.size(); i++) {
			if(handler.walls.get(i).getBounds().intersectsLine(oneL.getX(), oneL.getY(), twoL.getX(), twoL.getY()) ||
					handler.walls.get(i).getBounds().intersectsLine(oneR.getX(), oneR.getY(), twoR.getX(), twoR.getY())) { 
				obstacleFound = true;
				break;
			}
		}
		return obstacleFound;
	}
	
	/**
	 * Checks to see if Tank owner has a direct path to the specified GameObject.  The tank is considered to have a direct path as long as there are no
	 * walls on the line between the tank and the given GameObject.
	 * @param gameObject The GameObject that is being considered
	 * @return A boolean representing if there is a straight, uninterrupted line connecting Tank owner to the specified GameObject
	 */
	public boolean directPath(GameObject gameObject) {
		if(gameObject == null) return false;
		else {
			Wall wall;
			boolean directPath = true;
			for(int i = 0; i < handler.walls.size(); i++) {
				wall = handler.walls.get(i);
				if(wall != null && wall.getBounds().intersectsLine(owner.x + owner.width / 2, owner.y + owner.height / 2, gameObject.x + gameObject.width / 2, gameObject.y + gameObject.height / 2)) {
					directPath = false;
					break;
				}
			}
			return directPath;
		}
	}
	
	/**
	 * Predicts if any bullets currently in the game will intersect with the specified rectangle within the specified number of ticks 
	 * @param r The rectangle to detect bullets for
	 * @param excludeFriendly When set to true, bullets that have been created by tank owner will be ignored when listing bullets that will collide
	 * with the given Rectangle. When set to false, all bullets will be considered.
	 * @param ticks The amount of time that must pass before a bullet would intersect with the rectangle
	 * @return A boolean representing whether or not a bullet would intersect with the rectangle
	 */
	public boolean intersectsBullet(Rectangle r, boolean excludeFriendly, int ticks) {
		Point[] currentBullets = new Point[handler.projectiles.size()];
		Point[] futureBullets = new Point[handler.projectiles.size()];
		Projectile p;
		for(int i = 0; i < handler.projectiles.size(); i++) {
			p = handler.projectiles.get(i);
			boolean validBullet = false;
			if(excludeFriendly) {
				if(p.getShooter() == owner) validBullet = false;
				else validBullet = true;
			}
			else validBullet = true;
			if(validBullet) {
				currentBullets[i] = new Point((int) p.getX(), (int) p.getY());
				futureBullets[i] = new Point((int) (p.getX() + p.getSpeedX() * ticks), (int) (p.getY() + p.getSpeedY() * ticks));
			}
			else {
				currentBullets[i] = new Point(0, 0);
				futureBullets[i] = new Point(0, 0);
			}
		}
		boolean bulletFound = false;
		for(int i = 0; i < currentBullets.length; i++) {
			if(r.intersectsLine(currentBullets[i].getX(), currentBullets[i].getY(), futureBullets[i].getX(), futureBullets[i].getY()) ||
					r.intersectsLine(currentBullets[i].getX() + 8, currentBullets[i].getY(), futureBullets[i].getX() + 8, futureBullets[i].getY()) ||
					r.intersectsLine(currentBullets[i].getX(), currentBullets[i].getY() + 8, futureBullets[i].getX(), futureBullets[i].getY() + 8) ||
					r.intersectsLine(currentBullets[i].getX() + 8, currentBullets[i].getY() + 8, futureBullets[i].getX() + 8, futureBullets[i].getY() + 8)){
				bulletFound = true;
				break;
			}
		}
		return bulletFound;
	}
	
	/**
	 * Returns the danger level being experienced by the given Rectangle using the location and speeds of every bullet currently in the world. This is 
	 * done by adding together the danger values for each bullet, each being calculated using the following formula: 
	 * thisBulletDanger = (10000 / distance) * (10000 / distance) * (10 / directionDiff), where distance is the length of the straight line connecting
	 * the owner tank to the bullet, and directionDiff is the difference between the bullet's rotation value and the rotation value required to point
	 * directly at the tank.
	 * @param bounds The Rectangle that is to be used for the danger calculation 
	 * @return A float value representing the danger of one or more bullets intersecting with the given Rectangle
	 */
	public float bulletDangerCalculation(Rectangle bounds) {
		Projectile p;
		float[] bulletDangers = new float[handler.projectiles.size()];
		for(int i = 0; i < handler.projectiles.size(); i++) {
			p = handler.projectiles.get(i);
			Point one = centerPoint(bounds);
			Point two = centerPoint(p);
			float distance = distance(one, two);
			float directionDiff = normalizeRotation(direction(two, one) - speedsToRotation(p.speedX, p.speedY));
			if(directionDiff > 180) directionDiff = 360 - directionDiff;
			if(distance < directionDiffIgnoreThresh) {
				directionDiff = 1; //Ignore direction difference benefit if bullet is about to hit 
			}
			float thisBulletDanger = (10000 / distance) * (10000 / distance) * (10 / directionDiff);
			bulletDangers[i] = thisBulletDanger;
		}
		return sum(bulletDangers);
	}
	
	/**
	 * Returns an ArrayList containing all existing bullets that have been created by the owner tank
	 * @return An ArrayList containing all existing bullets that have been created by the owner tank
	 */
	public ArrayList<Projectile> getOwnedBullets() {
		ArrayList<Projectile> ownedBullets = new ArrayList<Projectile>();
		for(int i = 0; i < handler.projectiles.size(); i++) {
			if(handler.projectiles.get(i).getShooter() == owner) ownedBullets.add(handler.projectiles.get(i));
		}
		return ownedBullets;
	}
	
	private int timerIndexFromName(String name) {
		int index = -1;
		for(int i = 0; i < timerNames.length; i++) {
			if(timerNames[i].equals(name)) {
				index = i;
			}
		}
		return index;
	}
	
	private float sum(float[] a) {
		float total = 0;
		for(int i = 0; i < a.length; i++) {
			total += a[i];
		}
		return total;
	}
	
	private float distance(Point one, Point two) {
		float a = (float) Math.abs(one.getX() - two.getX());
		float b = (float) Math.abs(one.getY() - two.getY());
		float c = (float) Math.sqrt(a * a + b * b);
		return c;
	}
	
	private float direction(Point one, Point two) {
		float rotation = (float) Math.toDegrees(Math.atan((one.getY() - two.getY()) / (one.getX() - two.getX()))) + 90;
		if(one.getX() > two.getX()) rotation += 180; 
		normalizeRotation(rotation);
		return rotation;
	}
	
	private float speedsToRotation(float x, float y) {
		float rotation = (float) Math.toDegrees(Math.atan((-y) / (-x))) + 90;
		if(x < 0) rotation += 180;
		return rotation;
	}
	/*
	/**
	 * Returns x and y speeds equivalent to a single speed in a given direction specified by slope.
	 * @param slope The slope of the line along which to calculate the x and y speeds
	 * @param speed The speed in the direction of the slope
	 * @return An int[] containing the calculated x and y speeds.
	
	public static float[] movementFormula(float slope, float speed){
		float[] speeds = new float[2];
		speeds[0] = (float) (speed / (Math.sqrt(slope * slope + 1)));
		speeds[1] = slope * speeds[0];
		return speeds;
	}
	*/
	/**
	 * Converts any rotation value in degrees to its equivalent value that is between 0 and 360. 
	 * @param rotation The rotation to normalize
	 * @return A float representing the normalized rotation.
	 */
	public static float normalizeRotation(float rotation) {
		rotation = rotation % 360;
		if(rotation < 0){
			rotation = 360 + rotation;
		}
		return rotation;
	}
	
	/**
	 * Returns the value of var unless var is less than min or greater than max, in which case min or max is returned.
	 * @param var The variable being clamped
	 * @param min The minimum value for var
	 * @param max The maximum value for var
	 * @return The clamped value as a float.
	 */
	public static float clamp(float var, float min, float max){
		if(var >= max) return max;
		else if(var <= min) return min;
		else return var;
	}
	
	public void setOwner(Tank owner) {
		this.owner = owner;
	}
}
