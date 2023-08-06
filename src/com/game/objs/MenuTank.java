package com.game.objs;

import com.game.framework.Handler;
import com.game.framework.ObjectID;
import com.game.framework.Tank;
import com.game.window.Game;
import com.game.window.Menu;

public class MenuTank extends Tank{

	private final int cannonTurnSpeed = 3;
	private boolean[] hoverButtons;
	private int[] buttonY;
	private int targetX, targetY;
	
	Handler handler;
	Menu menu;
	
	public MenuTank(float x, float y, ObjectID id, Handler handler, Menu menu) {
		super(x, y, id, handler);
		this.handler = handler;
		this.menu = menu;
		hoverButtons = menu.getHoverButtons();
		buttonY = new int[hoverButtons.length];
		for(int i = 0; i < hoverButtons.length; i++) {
			buttonY[i] = menu.FIRST_BUTTON_Y + i * (menu.MENU_BUTTON_HEIGHT + menu.BUTTON_SPACING) * menu.BLOCK_SIZE + menu.BLOCK_SIZE;
		}
		if(id == ObjectID.LeftMenuTank) {
			tankRotation = 90;
			setCannonRotation(90);
		}
		else if(id == ObjectID.RightMenuTank) { 
			tankRotation = 270;
			setCannonRotation(270);
		}
	}

	@Override
	public void tick() {
		pointCannon();
		super.tick();
	}

	private void pointCannon() {
		for(int i = 0; i < hoverButtons.length; i++) if(hoverButtons[i]) targetY = buttonY[i];
		if(id == ObjectID.LeftMenuTank) targetX = (((Game.WIDTH / menu.BLOCK_SIZE) - menu.MENU_BUTTON_WIDTH) / 2) * menu.BLOCK_SIZE;
		else if(id == ObjectID.RightMenuTank) targetX = (((Game.WIDTH / menu.BLOCK_SIZE) - menu.MENU_BUTTON_WIDTH) / 2 + menu.MENU_BUTTON_WIDTH) * menu.BLOCK_SIZE;
		else System.out.println("Invalid tank ID for MenuTank error: " + id);
		float targetRotation = ((float) Math.toDegrees(Math.atan((targetY - (y + 11)) / (targetX - (x + 22)))) + 90);
		if(id == ObjectID.RightMenuTank) targetRotation += 180;
		if(Math.abs(getCannonRotation() - targetRotation) < 1) {}
		else if(targetRotation - getCannonRotation() > 3) setCannonRotation(getCannonRotation() + cannonTurnSpeed);
		else if(getCannonRotation() - targetRotation > 3) setCannonRotation(getCannonRotation() - cannonTurnSpeed);
	}
}
