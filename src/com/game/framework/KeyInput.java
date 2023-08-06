package com.game.framework;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import com.game.objs.Player;
import com.game.window.Game;
import com.game.window.HUD;
import com.game.window.Menu;

public class KeyInput extends KeyAdapter{

	private boolean[] keysDown = new boolean[4];
	
	Handler handler;
	HUD hud;
	Menu menu;
	Player player;
	
	public KeyInput(Handler handler, HUD hud, Menu menu){
		this.handler = handler;
		this.menu = menu;
		this.hud = hud;
		player = handler.getPlayer();
		for(int i = 0; i < keysDown.length; i++) keysDown[i] = false;
	}
	
	public void keyPressed(KeyEvent e){		
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP){
			if(Game.options[1] == 0) player.setForwardSpeed(player.moveSpeed);
			else {
				if(keysDown[2] == true) player.setTankRotation(315);
				else if(keysDown[3] == true) player.setTankRotation(45);
				else player.setTankRotation(0);
				player.setForwardSpeed(player.moveSpeed);
			}
			keysDown[0] = true;
		}
		else if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN){
			if(Game.options[1] == 0) player.setForwardSpeed(-player.moveSpeed);
			else {
				if(keysDown[2] == true) player.setTankRotation(225);
				else if(keysDown[3] == true) player.setTankRotation(135);
				else player.setTankRotation(180);
				player.setForwardSpeed(player.moveSpeed);
			}
			keysDown[1] = true;
		}
		else if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT){
			if(Game.options[1] == 0) player.setRotationSpeed(-player.turnSpeed);
			else {
				if(keysDown[0] == true) player.setTankRotation(315);
				else if(keysDown[1] == true) player.setTankRotation(225);
				else player.setTankRotation(270);
				player.setForwardSpeed(player.moveSpeed);
			}
			keysDown[2] = true;
		}
		else if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT){
			if(Game.options[1] == 0) player.setRotationSpeed(player.turnSpeed);
			else {
				if(keysDown[0] == true) player.setTankRotation(45);
				else if(keysDown[1] == true) player.setTankRotation(135);
				else player.setTankRotation(90);
				player.setForwardSpeed(player.moveSpeed);
			}
			keysDown[3] = true;
		}
		else if(key == KeyEvent.VK_SPACE) player.shootCannon();
		else if(key == KeyEvent.VK_T) player.collectBullets();
		else if(key == KeyEvent.VK_ESCAPE) {
			if(Game.gameState == GameState.Game) {
				Game.gameState = GameState.Paused; 
				menu.pause();
			}
		}
		else{
			switch(key){
			case KeyEvent.VK_1:
				hud.setSelectedSlot(0);
				break;
			case KeyEvent.VK_2:
				hud.setSelectedSlot(1);
				break;
			case KeyEvent.VK_3:
				hud.setSelectedSlot(2);
				break;
			case KeyEvent.VK_4:
				hud.setSelectedSlot(3);
				break;
			case KeyEvent.VK_5:
				hud.setSelectedSlot(4);
				break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_W) keysDown[0] = false;
		else if(key == KeyEvent.VK_S) keysDown[1] = false;
		else if(key == KeyEvent.VK_A) keysDown[2] = false;
		else if(key == KeyEvent.VK_D) keysDown[3] = false;	
		else if(key == KeyEvent.VK_UP) keysDown[0] = false;
		else if(key == KeyEvent.VK_DOWN) keysDown[1] = false;
		else if(key == KeyEvent.VK_LEFT) keysDown[2] = false;
		else if(key == KeyEvent.VK_RIGHT) keysDown[3] = false;
		
		if(Game.options[1] == 0) {
			if(!keysDown[0] && !keysDown[1]){
				player.setForwardSpeed(0);
			}
			if(!keysDown[2] && !keysDown[3]){
				player.setRotationSpeed(0);
			}
		}
		else {
			player.setForwardSpeed(0);
			if(keysDown[0] == true){	
				if(keysDown[2] == true) player.setTankRotation(315);
				else if(keysDown[3] == true) player.setTankRotation(45);
				else player.setTankRotation(0);
				player.setForwardSpeed(player.moveSpeed);		
			}
			else if(keysDown[1] == true){		
				if(keysDown[2] == true) player.setTankRotation(225);
				else if(keysDown[3] == true) player.setTankRotation(135);
				else player.setTankRotation(180);
				player.setForwardSpeed(player.moveSpeed);	
			}
			else if(keysDown[2] == true){	
				if(keysDown[0] == true) player.setTankRotation(315);
				else if(keysDown[1] == true) player.setTankRotation(225);
				else player.setTankRotation(270);
				player.setForwardSpeed(player.moveSpeed);		
			}
			else if(keysDown[3] == true){		
				if(keysDown[0] == true) player.setTankRotation(45);
				else if(keysDown[1] == true) player.setTankRotation(135);
				else player.setTankRotation(90);
				player.setForwardSpeed(player.moveSpeed);
			}
		}
	}
}
