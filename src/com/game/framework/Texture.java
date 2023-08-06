package com.game.framework;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {

	private BufferedImage tankSheet;
	private BufferedImage bulletSheet;
	private BufferedImage levelSheet;
	private BufferedImage hudSheet;
	private BufferedImage explosionSheet;
	private BufferedImage menuSheet;
	public BufferedImage[] levelSprites = new BufferedImage[9];
	public BufferedImage[] tankSprites = new BufferedImage[23];
	public BufferedImage[] bulletSprites = new BufferedImage[5];
	public BufferedImage[] hudSprites = new BufferedImage[5];
	public BufferedImage[] explosionSprites = new BufferedImage[7];
	public BufferedImage[] menuSprites = new BufferedImage[18];
	
	public Texture(){
		tankSheet = loadImage("res/tank_sheet.png");
		bulletSheet = loadImage("res/bullet_sheet.png");
		levelSheet = loadImage("res/level_sheet.png");
		hudSheet = loadImage("res/hud_sheet.png");
		explosionSheet = loadImage("res/explosion_sheet.png");
		menuSheet= loadImage("res/menu_sheet.png");
		getTextures();
	}
	
	private void getTextures(){
		tankSprites[0] = tankSheet.getSubimage(0, 0, 45, 47); //player tank
		tankSprites[1] = tankSheet.getSubimage(45, 0, 21, 47); //player cannon
		tankSprites[2] = tankSheet.getSubimage(66, 0, 45, 47); //red tank
		tankSprites[3] = tankSheet.getSubimage(111, 0, 21, 47); //red cannon
		tankSprites[4] = tankSheet.getSubimage(132, 0, 45, 47); //yellow tank
		tankSprites[5] = tankSheet.getSubimage(177, 0, 21, 47); //yellow cannon
		tankSprites[6] = tankSheet.getSubimage(0, 47, 45, 47); //purple tank
		tankSprites[7] = tankSheet.getSubimage(45, 47, 21, 47); //purple cannon
		tankSprites[8] = tankSheet.getSubimage(66, 47, 45, 47); //black tank
		tankSprites[9] = tankSheet.getSubimage(111, 47, 21, 47); //black cannon
		tankSprites[10] = tankSheet.getSubimage(132, 47, 45, 47); //white tank
		tankSprites[11] = tankSheet.getSubimage(177, 47, 21, 47); //white cannon
		tankSprites[12] = tankSheet.getSubimage(0, 94, 45, 47); //gray tank
		tankSprites[13] = tankSheet.getSubimage(45, 94, 21, 47); //gray cannon
		tankSprites[14] = tankSheet.getSubimage(66, 94, 45, 47); //orange tank
		tankSprites[15] = tankSheet.getSubimage(111, 94, 21, 47); //orange cannon
		tankSprites[16] = tankSheet.getSubimage(132, 94, 45, 47); //green tank
		tankSprites[17] = tankSheet.getSubimage(177, 94, 21, 47); //green cannon
		tankSprites[18] = tankSheet.getSubimage(0, 141, 45, 47); //blue tank
		tankSprites[19] = tankSheet.getSubimage(45, 141, 21, 47); //blue cannon
		tankSprites[20] = tankSheet.getSubimage(66, 141, 45, 47); //boss tank
		tankSprites[21] = tankSheet.getSubimage(111, 141, 21, 47); //boss cannon
		tankSprites[22] = tankSheet.getSubimage(132, 141, 8, 1); //trail
		
		bulletSprites[0] = bulletSheet.getSubimage(0, 0, 8, 12); //basic
		bulletSprites[1] = bulletSheet.getSubimage(8, 0, 8, 12); //fast
		bulletSprites[2] = bulletSheet.getSubimage(16, 0, 8, 12); //freeze
		bulletSprites[3] = bulletSheet.getSubimage(24, 0, 8, 12); //bouncy
		bulletSprites[4] = bulletSheet.getSubimage(32, 0, 8, 12); //targeting
		
		levelSprites[0] = levelSheet.getSubimage(0, 0, 16, 16); //wall (use x2 size)
		levelSprites[1] = levelSheet.getSubimage(16, 0, 16, 16); //closed door (use x2 size)
		levelSprites[2] = levelSheet.getSubimage(32, 0, 16, 16); //open door (use x2 size)
		levelSprites[3] = levelSheet.getSubimage(48, 0, 16, 16); //level end (use x2 size)
		levelSprites[4] = levelSheet.getSubimage(0, 16, 16, 16); //land mine
		levelSprites[5] = levelSheet.getSubimage(16, 16, 32, 16); //ammo box
		levelSprites[6] = levelSheet.getSubimage(0,  32, 64, 64); //checkpoint
		levelSprites[7] = levelSheet.getSubimage(48, 16, 16, 16); //broken turret
		levelSprites[8] = levelSheet.getSubimage(64, 0, 64, 64); //closed checkpoint
		
		hudSprites[0] = hudSheet.getSubimage(0, 52, 32, 32); //life heart
		hudSprites[1] = hudSheet.getSubimage(32, 52, 32, 32); //lost life heart
		hudSprites[2] = hudSheet.getSubimage(0, 0, 52, 52); //hotbar slot
		hudSprites[3] = hudSheet.getSubimage(52, 0, 52, 52); //selected hotbar slot
		hudSprites[4] = hudSheet.getSubimage(64, 52, 32, 32); //Mouse icon
		
		explosionSprites[0] = explosionSheet.getSubimage(0, 0, 80, 80); //frame 1
		explosionSprites[1] = explosionSheet.getSubimage(80, 0, 80, 80); //frame 2
		explosionSprites[2] = explosionSheet.getSubimage(160, 0, 80, 80); //frame 3
		explosionSprites[3] = explosionSheet.getSubimage(0, 80, 80, 80); //frame 4
		explosionSprites[4] = explosionSheet.getSubimage(80, 80, 80, 80); //frame 5
		explosionSprites[5] = explosionSheet.getSubimage(160, 80, 80, 80); //frame 6
		explosionSprites[6] = explosionSheet.getSubimage(0, 160, 80, 80); //frame 7
		
		menuSprites[0] = menuSheet.getSubimage(0, 0, 32, 32); //curve 1
		menuSprites[1] = menuSheet.getSubimage(32, 0, 32, 32); //curve 2
		menuSprites[2] = menuSheet.getSubimage(64, 0, 32, 32); //border 1
		menuSprites[3] = menuSheet.getSubimage(96, 0, 32, 32); //border 2
		menuSprites[4] = menuSheet.getSubimage(128, 0, 32, 32); //center
		menuSprites[5] = menuSheet.getSubimage(0, 32, 32, 32); //curve 3
		menuSprites[6] = menuSheet.getSubimage(32, 32, 32, 32); //curve 4
		menuSprites[7] = menuSheet.getSubimage(64, 32, 32, 32); //border 3
		menuSprites[8] = menuSheet.getSubimage(96, 32, 32, 32); //border 4
		menuSprites[9] = menuSheet.getSubimage(0, 64, 32, 32); //curve 1 light
		menuSprites[10] = menuSheet.getSubimage(32, 64, 32, 32); //curve 2 light
		menuSprites[11] = menuSheet.getSubimage(64, 64, 32, 32); //border 1 light
		menuSprites[12] = menuSheet.getSubimage(96, 64, 32, 32); //border 2 light
		menuSprites[13] = menuSheet.getSubimage(128, 64, 32, 32); //center light
		menuSprites[14] = menuSheet.getSubimage(0, 96, 32, 32); //curve 3 light
		menuSprites[15] = menuSheet.getSubimage(32, 96, 32, 32); //curve 4 light
		menuSprites[16] = menuSheet.getSubimage(64, 96, 32, 32); //border 3 light
		menuSprites[17] = menuSheet.getSubimage(96, 96, 32, 32); //border 4 light
	}
	
	public BufferedImage loadImage(String path){
		try {
			return ImageIO.read(new File(path));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
