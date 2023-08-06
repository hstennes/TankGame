package com.game.window;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.game.framework.AIHelper;
import com.game.framework.Handler;
import com.game.framework.LevelLoader;
import com.game.framework.ObjectID;
import com.game.framework.SurvivalMode;
import com.game.framework.Texture;
import com.game.objs.Player;

public class HUD {
	
	public static int LC_MESSAGE_DURATION = 240;
	private int[] availableBullets;
	private ObjectID[] items = new ObjectID[5];
	private int selectedSlot;
	private int hotbarSlots;
	private String message = "";
	private int messageDuration;
	private int levelCompleteMessage = -1;
	private int blink;
	
	Texture tex = Game.getTexture();
	Font hotbarFont;
	Font messageFont;
	Font scoreFont;
	Player player;
	Game game;
	Handler handler;
	Menu menu;
	
	public HUD(Game game, Handler handler, Menu menu, int[] availableBullets){
		player = handler.getPlayer();
		this.game = game;
		this.handler = handler;
		this.menu = menu;
		hotbarFont = new Font("Monospaced", Font.PLAIN, 15);
		messageFont = new Font("Monospaced", Font.BOLD, 75);
		scoreFont = new Font("Monospaced", Font.BOLD, 30);
		this.availableBullets = availableBullets;
		refreshItems();
	}
	
	public void tick() {
		if(messageDuration > 0) messageDuration--;
		if(levelCompleteMessage > 0 && levelCompleteMessage != -1) {
			levelCompleteMessage--;
			blink++;
			if(blink > 80) {
				blink = 0;
				dispMessage("Mission Complete", 40);
			}
		}
		else if(levelCompleteMessage == 0){
			menu.levelComplete();
		}
	}
	
	public void render(Graphics g){
		g.setFont(hotbarFont);
		g.setColor(Color.BLACK);
		Point center;
		for(int i = 0; i < hotbarSlots; i++){
			center = centerText(g, Integer.toString(availableBullets[0]), new Rectangle(10 + 60 * i, 60, 52, 30), hotbarFont);
			if(i != selectedSlot) g.drawImage(tex.hudSprites[2], 10 + 60 * i, 10, null);
			else g.drawImage(tex.hudSprites[3], 10 + 60 * i, 10, null);
			switch(items[i]) {
			case BasicBullet:
				g.drawImage(tex.bulletSprites[0], 32 + 60 * i, 30, null);
				g.drawString(Integer.toString(availableBullets[0]), (int) center.getX(), (int) center.getY());
				break;
			case FastBullet:
				g.drawImage(tex.bulletSprites[1], 32 + 60 * i, 30, null);	
				g.drawString(Integer.toString(availableBullets[1]), (int) center.getX(), (int) center.getY());				
				break;
			case EmpBullet:
				g.drawImage(tex.bulletSprites[2], 32 + 60 * i, 30, null);	
				g.drawString(Integer.toString(availableBullets[2]), (int) center.getX(), (int) center.getY());
				break;
			case BouncyBullet:
				g.drawImage(tex.bulletSprites[3], 32 + 60 * i, 30, null);
				g.drawString(Integer.toString(availableBullets[3]), (int) center.getX(), (int) center.getY());
				break;
			case TargetingBullet:
				g.drawImage(tex.bulletSprites[4], 32 + 60 * i, 30, null);
				g.drawString(Integer.toString(availableBullets[4]), (int) center.getX(), (int) center.getY());
				break;
			default:
				break;
			}
		}
		
		if(Game.options[2] == 1) g.drawString(Integer.toString(Game.currentFps), Game.WIDTH - 30, Game.HEIGHT - 30);
		
		for(int i = 0; i < player.getLives(); i++){
			g.drawImage(tex.hudSprites[0], Game.WIDTH - 120 + 40 * i, 10, null);
		}
		
		for(int i = 0; i < 3 - player.getLives(); i++){
			g.drawImage(tex.hudSprites[1], Game.WIDTH - 120 + 40 * player.getLives() + 40 * i, 10, null);
		}
		
		if(messageDuration > 0) {
			g.setColor(Color.WHITE);
			g.setFont(messageFont);
			Point p = centerText(g, message, new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT), messageFont);
			g.drawString(message, (int) p.getX(), (int) p.getY());
		}
		
		if(LevelLoader.levelNumber == 0) {
			SurvivalMode survival = game.getSurvival();
			if(survival != null) {
				g.setColor(Color.WHITE);
				g.setFont(scoreFont);
				Point p = centerText(g, "Tanks destroyed: " + handler.getSurvivalTanksDestroyed() + "  " + "Highscore: " + survival.getHighscore(), new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT), scoreFont);
				g.drawString("Tanks destroyed: " + handler.getSurvivalTanksDestroyed() + "  " + "Highscore: " + survival.getHighscore(), (int) p.getX(), 50);
				p = centerText(g, "Enemy tanks in world: " + handler.getExistingTanks(), new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT), scoreFont);
				g.drawString("Enemy tanks in world: " + handler.getExistingTanks(), (int) p.getX(), 100);
			}
		}
	}
	
	public void refreshItems() {
		hotbarSlots = 0;
		for(int i = 0; i < 5; i++){
			if(availableBullets[i] != 0) {
				switch(i) {
				case 0:
					items[hotbarSlots] = ObjectID.BasicBullet;
					break;
				case 1:
					items[hotbarSlots] = ObjectID.FastBullet;
					break;
				case 2:
					items[hotbarSlots] = ObjectID.EmpBullet;
					break;
				case 3:
					items[hotbarSlots] = ObjectID.BouncyBullet;
					break;
				case 4:
					items[hotbarSlots] = ObjectID.TargetingBullet;
					break;
				}
				hotbarSlots++;
			}
		}
	}
	
	public void dispMessage(String message, int duration) {
		this.message = message;
		messageDuration = duration;
	}
	
	public void survivalComplete() {
		int score = handler.getSurvivalTanksDestroyed();
		int highscore = game.getSurvival().getHighscore();
		if(score > highscore) dispMessage("New Highscore: " + score, 360);
		else dispMessage("Final score: " + score, 360);
	}
	
	public static Point centerText(Graphics g, String text, Rectangle rect, Font font) {
	    FontMetrics metrics = g.getFontMetrics(font);
	    int x = rect.x + (rect.width - metrics.stringWidth(text)) / 2;
	    int y = rect.y + ((rect.height - metrics.getHeight()) / 2) + metrics.getAscent();
	    return new Point(x, y);
	}
	
	public void levelComplete(int messageTime) {
		levelCompleteMessage = messageTime;
		blink = 80;
	}

	public int getSelectedSlot() {
		return selectedSlot;
	}

	public void setSelectedSlot(int selectedSlot) {
		selectedSlot = (int) AIHelper.clamp(selectedSlot, 0, hotbarSlots - 1);
		this.selectedSlot = selectedSlot;
	}
	
	public int getHotbarSlots() {
		return hotbarSlots;
	}

	public void setHotbarSlots(int hotbarSlots) {
		this.hotbarSlots = hotbarSlots;
	}
	
	public int getLevelCompleteMessage() {
		return levelCompleteMessage;
	}

	public int[] getAvailableBullets() {
		return availableBullets;
	}

	public ObjectID[] getItems() {
		return items;
	}
}
