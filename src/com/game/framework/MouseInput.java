package com.game.framework;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import com.game.objs.Player;
import com.game.window.Game;
import com.game.window.HUD;

public class MouseInput extends MouseAdapter implements MouseWheelListener{
	
	public static final int MOUSE_ICON_WIDTH = 32;
	
	Point prevMouse;
	Robot robot;
	Game game;
	Handler handler;
	HUD hud;
	Player player;
	
	public MouseInput(Game game, Handler handler, HUD hud){
		player = handler.getPlayer();
		this.handler = handler;
		this.hud = hud;
		this.game =  game;
		try {
			robot = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	public void tick() {
		if(Game.options[3] == 1) {
			Point mouse = new Point(MouseInfo.getPointerInfo().getLocation());
			Rectangle gameWindow = new Rectangle((int) game.getLocationOnScreen().getX(), (int) game.getLocationOnScreen().getY(), Game.WIDTH, Game.HEIGHT - 30);
			if(!gameWindow.contains(mouse)) robot.mouseMove((int) prevMouse.getX(), (int) prevMouse.getY());
			else prevMouse = mouse;
		}
	}
	
	public void mousePressed(MouseEvent e){
		player.shootCannon();
	}
	
	public void mouseReleased(MouseEvent e){
		
	}
	
	 public void mouseWheelMoved(MouseWheelEvent e) {
		 int notches = e.getWheelRotation();
		 hud.setSelectedSlot(hud.getSelectedSlot() + notches);
	 }
}
