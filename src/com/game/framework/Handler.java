package com.game.framework;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import com.game.objs.BattleDoor;
import com.game.objs.LevelFinish;
import com.game.objs.Player;
import com.game.objs.Projectile;
import com.game.objs.Wall;
import com.game.window.Camera;
import com.game.window.Game;

public class Handler {
	
	public ArrayList<Wall> walls = new ArrayList<Wall>();
	public ArrayList<Tank> tanks = new ArrayList<Tank>();
	public ArrayList<Projectile> projectiles = new ArrayList<Projectile>();
	public ArrayList<GameObject> helpers = new ArrayList<GameObject>();
	public ArrayList<GameObject> hazards = new ArrayList<GameObject>();
	public ArrayList<GameObject> graphics = new ArrayList<GameObject>();
	
	public ArrayList<Wall> savedWalls = new ArrayList<Wall>();
	public ArrayList<Tank> savedTanks  = new ArrayList<Tank>();
	public ArrayList<Projectile> savedProjectiles = new ArrayList<Projectile>();
	public ArrayList<GameObject> savedHelpers = new ArrayList<GameObject>();
	public ArrayList<GameObject> savedHazards = new ArrayList<GameObject>();
	
	private int survivalTanksDestroyed;
	private int existingTanks;
	
	Player player;
	LevelFinish levelFinish;
	Camera camera;
	
	public Handler(Game game, Camera camera){
		this.camera = camera;
		player = new Player(32, 32, ObjectID.Player, game, this, camera);
	}
	
	public void tick(){
		if(Game.gameState == GameState.Game) player.tick();
		for(int i = 0; i < walls.size(); i++){
			walls.get(i).tick();
		}
		for(int i = 0; i < projectiles.size(); i++){
			projectiles.get(i).tick();
		}
		for(int i = 0; i < tanks.size(); i++){
			tanks.get(i).tick();
		}
		for(int i = 0; i < helpers.size(); i++) {
			helpers.get(i).tick();
		}
		for(int i = 0; i < hazards.size(); i++) {
			hazards.get(i).tick();
		}
		for(int i = 0; i < graphics.size(); i++) {
			graphics.get(i).tick();
		}
		if(levelFinish != null) levelFinish.tick();
		existingTanks = tanks.size();
	}
	
	public void render(Graphics g){		
		Rectangle cameraView = new Rectangle((int) -camera.getX() - Game.UNIT, (int) -camera.getY() - Game.UNIT, Game.WIDTH + Game.UNIT, Game.HEIGHT + Game.UNIT);
		GameObject tempGraphicsElement;
		Wall tempWall;
		Tank tempTank;
		Projectile tempProjectile;
		GameObject tempHelper;
		GameObject tempHazard;
		for(int i = 0; i < graphics.size(); i++){
			tempGraphicsElement = graphics.get(i);
			if(cameraView.contains(tempGraphicsElement.getX(), tempGraphicsElement.getY()) && tempGraphicsElement != null) tempGraphicsElement.render(g);
		}
		if(levelFinish != null) if(cameraView.contains(levelFinish.getX(), levelFinish.getY())) levelFinish.render(g);
		for(int i = 0; i < walls.size(); i++){
			tempWall = walls.get(i);
			if(tempWall != null) if(cameraView.contains(tempWall.getX(), tempWall.getY())) tempWall.render(g);
		}	
		for(int i = 0; i < helpers.size(); i++){
			tempHelper = helpers.get(i);
			if(cameraView.contains(tempHelper.getX(), tempHelper.getY())) tempHelper.render(g);
		}
		for(int i = 0; i < hazards.size(); i++){
			tempHazard = hazards.get(i);
			if(cameraView.contains(tempHazard.getX(), tempHazard.getY())) tempHazard.render(g);
		}
		for(int i = 0; i < projectiles.size(); i++){
			tempProjectile = projectiles.get(i);
			if(cameraView.contains(tempProjectile.getX(), tempProjectile.getY()) && tempProjectile != null) tempProjectile.render(g);
		}
		for(int i = 0; i < tanks.size(); i++){
			tempTank = tanks.get(i);
			if(cameraView.contains(tempTank.getX(), tempTank.getY())) tempTank.render(g);
		}
		if(Game.gameState == GameState.Game) player.render(g);
	}
	
	public void onTankDestroyed() {
		if(LevelLoader.levelNumber == 0) survivalTanksDestroyed++;
		if(Game.options[0] == 1) camera.setShaking(Camera.NORMAL_SHAKE_DURATION);
		System.out.println("Enemy tank destroyed");
	}
	
	public void saveGameObjects(){
		clearSave();
		try {
			for(int i = 0; i < walls.size(); i++) savedWalls.add((Wall) walls.get(i).clone());
			for(int i = 0; i < tanks.size(); i++) savedTanks.add((Tank) tanks.get(i).clone());
			for(int i = 0; i < projectiles.size(); i++) savedProjectiles.add((Projectile) projectiles.get(i).clone());
			for(int i = 0; i < helpers.size(); i++) savedHelpers.add((GameObject) helpers.get(i).clone());
			for(int i = 0; i < hazards.size(); i++) savedHazards.add((GameObject) hazards.get(i).clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
	}
	
	public void revertGameObjects() {
		clearObjects();
		try {
			for(int i = 0; i < savedWalls.size(); i++) walls.add((Wall) savedWalls.get(i).clone());
			for(int i = 0; i < savedTanks.size(); i++) tanks.add((Tank) savedTanks.get(i).clone());
			for(int i = 0; i < savedProjectiles.size(); i++) projectiles.add((Projectile) savedProjectiles.get(i).clone());
			for(int i = 0; i < savedHelpers.size(); i++) helpers.add((GameObject) savedHelpers.get(i).clone());
			for(int i = 0; i < savedHazards.size(); i++) hazards.add((GameObject) savedHazards.get(i).clone());
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		for(int i = 0; i < tanks.size(); i++) tanks.get(i).refreshReferencedObjects();
	}
	
	public void addWall(Wall wall){
		walls.add(wall);
	}
	
	public void removeWall(Wall wall){
		walls.remove(wall);
	}
	
	public void addTank(Tank tank){
		tanks.add(tank);
	}
	
	public void removeTank(Tank tank){
		tanks.remove(tank);
		Wall tempWall;
		for(int i = 0; i < walls.size(); i++) {
			tempWall = walls.get(i);
			if(tempWall.getId() == ObjectID.BattleDoor && tempWall instanceof BattleDoor) ((BattleDoor) tempWall).onTankDestroyed();
		}
	}
	
	public void addProjectile(Projectile projectile){
		projectiles.add(projectile);
	}
	
	public void removeProjectile(Projectile projectile){
		projectiles.remove(projectile);
	}
	
	public void addHelper(GameObject helper){
		helpers.add(helper);
	}
	
	public void removeHelper(GameObject helper){
		helpers.remove(helper);
	}
	
	public void addHazard(GameObject hazard){
		hazards.add(hazard);
	}
	
	public void removeHazard(GameObject hazard){
		hazards.remove(hazard);
	}
	
	public void addGraphicsElement(GameObject graphicsElement){
		graphics.add(graphicsElement);
	}
	
	public void removeGraphicsElement(GameObject graphicsElement){
		graphics.remove(graphicsElement);
	}
	
	public void clearGraphics() {
		graphics.clear();
	}
	
	public void clearObjects(){
		walls.clear();
		projectiles.clear();
		tanks.clear();
		helpers.clear();
		hazards.clear();
	}
	
	public void clearSave() {
		savedWalls.clear();
		savedProjectiles.clear();
		savedTanks.clear();
		savedHelpers.clear();
		savedHazards.clear();
	}
	
	public void setLevelFinish(LevelFinish levelFinish) {
		this.levelFinish = levelFinish;
	}
	
	public int getSurvivalTanksDestroyed() {
		return survivalTanksDestroyed;
	}

	public void setSurvivalTanksDestroyed(int survivalTanksDestroyed) {
		this.survivalTanksDestroyed = survivalTanksDestroyed;
	}

	public int getExistingTanks() {
		return existingTanks;
	}

	public void setExistingTanks(int existingTanks) {
		this.existingTanks = existingTanks;
	}

	public Player getPlayer(){
		return player;
	}
}
