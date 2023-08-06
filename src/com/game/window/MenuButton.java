package com.game.window;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.game.framework.Handler;
import com.game.framework.Texture;
import com.game.objs.Projectile;

public class MenuButton extends MouseAdapter{
	
	private final int buttonSprites = 9;
	private boolean visible = true;
	private int hovering = 0;
	private Rectangle bounds;
	private float x;
	private float y;
	private float width;
	private float height;
	private String text;
	private boolean mouseDownOnButton = false;
	private boolean bulletSelection = false;
	private int yesNoButton = 2;
	
	private Texture tex = Game.getTexture();
	Game game;
	Handler handler;
	Menu menu;
	Font font;
	
	public MenuButton(float x, float y, float width, float height, String text, Font font, Game game, Handler handler, Menu menu) {
		this.x = x;
		this.y = y;
		this.text = text;
		this.width = width;
		this.height = height;
		this.font = font;
		this.game = game;
		this.handler = handler;
		this.menu = menu;
		bounds = new Rectangle((int) x, (int) y, (int) width * 32, (int) height * 32);
	}
	
	public void tick() {
		Point mouse = new Point(MouseInfo.getPointerInfo().getLocation());
		mouse.translate((int) -game.getLocationOnScreen().getX(), (int) -game.getLocationOnScreen().getY());
		if(bounds.contains(mouse)) hovering = buttonSprites;
		else hovering = 0;
		if(bulletSelection) collision();
	}
	
	public void render(Graphics g) {
		if(visible) {
			//genius rendering system
			for(int yy = (int) y; yy < (int)  y + height * 32; yy += 32) {
				for(int xx = (int) x; xx < (int) x + width * 32; xx += 32) {
					if(yy == y) {
						if(xx == x) g.drawImage(tex.menuSprites[0 + hovering], xx, yy, null);
						else if(xx >= x + (width * 32) - 32) g.drawImage(tex.menuSprites[1 + hovering], xx, yy, null);
						else g.drawImage(tex.menuSprites[2 + hovering], xx, yy, null);
					}
					else if(yy >= y + (height * 32) - 32) {
						if(xx == x) g.drawImage(tex.menuSprites[5 + hovering], xx, yy, null);
						else if(xx >= x + (width * 32) - 32) g.drawImage(tex.menuSprites[6 + hovering], xx, yy, null);
						else  g.drawImage(tex.menuSprites[8 + hovering], xx, yy, null);
					}
					else {
						if(xx == x) g.drawImage(tex.menuSprites[7 + hovering], xx, yy, null);
						else if(xx >= x + (width * 32) - 32) g.drawImage(tex.menuSprites[3 + hovering], xx, yy, null);
						else g.drawImage(tex.menuSprites[4 + hovering], xx, yy, null);
					}
				}
			}
			
			g.setFont(font);
			if(yesNoButton == 2) { 
				Point p = HUD.centerText(g, text, bounds, font);
				g.drawString(text, (int) p.getX(), (int) p.getY()); 
			}
			else {
				String yesNo;
				if(yesNoButton == 0) yesNo = "No";
				else yesNo = "Yes";
				Point p = HUD.centerText(g, yesNo, bounds, font);
				g.drawString(yesNo, (int) p.getX(), (int) p.getY()); 
			}
		}
	}

	private void collision() {
		Projectile tempProjectile;
		for(int i = 0; i < handler.projectiles.size(); i++) {
			tempProjectile = handler.projectiles.get(i);
			if(bounds.intersects(tempProjectile.getBounds())) {
				handler.projectiles.clear();
				menu.buttonPressed(this);
			}
		}
	}
	
	public void mousePressed(MouseEvent e){
		if(visible) {
			Point mouse = new Point(e.getX(), e.getY());	
			if(bounds.contains(mouse)) mouseDownOnButton = true;
		}
	}
	
	public void mouseReleased(MouseEvent e) {
		if(mouseDownOnButton && visible) {
			mouseDownOnButton = false;
			if(!bulletSelection) {
				if(yesNoButton != 2) yesNoButton =  1 - yesNoButton; 
				menu.buttonPressed(this);
			}
			else menu.shootMenuTanks();
		}
	}
	
	public Rectangle getBounds() {
		return bounds;
	}

	public void setBounds(Rectangle bounds) {
		this.bounds = bounds;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public boolean isBulletSelection() {
		return bulletSelection;
	}

	public void setBulletSelection(boolean bulletSelection) {
		this.bulletSelection = bulletSelection;
	}

	public int getHovering() {
		return hovering;
	}
	
	public String getName() {
		return text;
	}
	
	public void setName(String name) {
		this.text = name;
	}
	
	public int getYesNoButton() {
		return yesNoButton;
	}

	public void setYesNoButton(int yesNoButton) {
		this.yesNoButton = yesNoButton;
	}
}
