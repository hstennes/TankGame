
package com.game.window;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import com.game.framework.GameState;
import com.game.framework.Handler;
import com.game.framework.LevelLoader;
import com.game.framework.ObjectID;
import com.game.framework.Texture;
import com.game.objs.MenuTank;

public class Menu{

	public final int BLOCK_SIZE = 32;
	public final int FIRST_BUTTON_Y = 160;
	public final int BUTTON_SPACING = 1;
	public final int MENU_BUTTON_WIDTH = 14;
	public final int MENU_BUTTON_HEIGHT = 3;
	public final int BACK_BUTTON_WIDTH = 6;
	public final int BACK_BUTTON_HEIGHT = 3;
	public final int LEVEL_BUTTON_SIZE = 3;
	public final int SCREEN_WIDTH = Game.WIDTH / 32;
	public final int MENU_BUTTONS = 5;
	public final int LEVELS = 15;
	private String currentTitle = "Tanks!";
	private boolean showHelp = false;
	private boolean showOptionLables = false;
	private boolean[] hoverButtons = new boolean[MENU_BUTTONS];
	private String[] help = new String[9];
	private String[] options = new String[Game.NUM_OPTIONS];
	MenuButton[] mainMenu = new MenuButton[MENU_BUTTONS];
	MenuButton[] levelSelect = new MenuButton[LEVELS];
	MenuButton[] pauseMenu = new MenuButton[3];
	MenuButton[] optionsMenu = new MenuButton[Game.NUM_OPTIONS];
	MenuButton[] missionComplete = new MenuButton[3];
	MenuButton[] activeButtons;
	MenuButton backButton;
	
	MenuTank leftTank;
	MenuTank rightTank;
	Texture tex = Game.getTexture();
	Font buttonFont;
	Font titleFont;
	Font helpFont;
	Font optionFont;
	Game game;
	Handler handler;
	LevelLoader levelLoader;
	
	public Menu(Game game, Handler handler, LevelLoader levelLoader) {
		this.game = game;
		this.handler = handler;
		this.levelLoader = levelLoader;
		buttonFont = new Font("Monospaced", Font.BOLD, 50);
		titleFont = new Font("Monospaced", Font.BOLD, 80);
		helpFont = new Font("Monospaced", Font.BOLD, 25);
		optionFont = new Font("Monospaced", Font.BOLD, 30);
		
		mainMenu[0] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Missions", buttonFont, game, handler, this);
		mainMenu[1] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 32, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Survival", buttonFont, game, handler, this);
		mainMenu[2] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 64, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Options", buttonFont, game, handler, this);
		mainMenu[3] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 96, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Help", buttonFont, game, handler, this);
		mainMenu[4] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 128, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Quit", buttonFont, game, handler, this);
		for(int i = 0; i < mainMenu.length; i++) mainMenu[i].setBulletSelection(true);
		
		for(int i = 0; i < levelSelect.length; i++) {
			if(i < LEVELS / 3) levelSelect[i] = new MenuButton(i * 128 + (Game.WIDTH - 640) / 2, 175, LEVEL_BUTTON_SIZE, LEVEL_BUTTON_SIZE, Integer.toString(i + 1), buttonFont, game, handler, this);
			else if(i < LEVELS / 3 * 2) levelSelect[i] = new MenuButton((i - 5) * 128 + (Game.WIDTH - 640) / 2, 303, LEVEL_BUTTON_SIZE, LEVEL_BUTTON_SIZE, Integer.toString(i + 1), buttonFont, game, handler, this);
			else levelSelect[i] = new MenuButton((i - 10) * 128 + (Game.WIDTH - 640) / 2, 431, LEVEL_BUTTON_SIZE, LEVEL_BUTTON_SIZE, Integer.toString(i + 1), buttonFont, game, handler, this);
		}
		
		pauseMenu[0] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Resume", buttonFont, game, handler, this);
		pauseMenu[1] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 32, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Help", buttonFont, game, handler, this);
		pauseMenu[2] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 64, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Main menu", buttonFont, game, handler, this);
		
		optionsMenu[0] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, 50 + FIRST_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "o0", buttonFont, game, handler, this);
		optionsMenu[1] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, 50 + FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 40, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "o1", buttonFont, game, handler, this);
		optionsMenu[2] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, 50 + FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 80, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "o2", buttonFont, game, handler, this);
		optionsMenu[3] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, 50 + FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 120, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "o3", buttonFont, game, handler, this);
		for(int i = 0; i < optionsMenu.length; i++) optionsMenu[i].setYesNoButton(Game.options[i]);
		
		missionComplete[0] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Next mission", buttonFont, game, handler, this);
		missionComplete[1] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 32, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Replay mission", buttonFont, game, handler, this);
		missionComplete[2] = new MenuButton((Game.WIDTH - (MENU_BUTTON_WIDTH * 32)) / 2, FIRST_BUTTON_Y + (BUTTON_SPACING + MENU_BUTTON_HEIGHT) * 64, MENU_BUTTON_WIDTH, MENU_BUTTON_HEIGHT, "Main menu", buttonFont, game, handler, this);
		
		backButton = new MenuButton((Game.WIDTH - BACK_BUTTON_WIDTH * 32) / 2, Game.HEIGHT - BACK_BUTTON_HEIGHT * BLOCK_SIZE - 50, BACK_BUTTON_WIDTH, BACK_BUTTON_HEIGHT, "Back", buttonFont, game, handler, this);
		backButton.setVisible(false);
		
		game.addMouseListener(backButton);
		for(int i = 0; i < mainMenu.length; i++) game.addMouseListener(mainMenu[i]);
		for(int i = 0; i < levelSelect.length; i++) game.addMouseListener(levelSelect[i]);
		for(int i = 0; i < pauseMenu.length; i++) game.addMouseListener(pauseMenu[i]);
		for(int i = 0; i < missionComplete.length; i++) game.addMouseListener(missionComplete[i]);
		for(int i = 0; i < optionsMenu.length; i++) game.addMouseListener(optionsMenu[i]);
		
		leftTank = new MenuTank(300, Game.HEIGHT / 2, ObjectID.LeftMenuTank, handler, this);
		rightTank = new MenuTank(Game.WIDTH - 350, Game.HEIGHT / 2, ObjectID.RightMenuTank, handler, this);
		
		help[0] = "Complete each level by reaching the checkered flag";
		help[1] = "Aim and shoot the cannon at enemy tanks using the mouse";
		help[2] = "Open red doors by destroying all tanks near them";
		help[3] = "Try to conserve bullets while fighting";
		help[4] = "Drive over checkpoints to save the level in its current state";
		help[5] = "Press T near ammo boxes to get a refil on bullets";
		help[6] = "Dodge incoming enemy bullets";
		help[7] = "Avoid driving over land mines";
		help[8] = "Don't give up no matter how my times you die!";
		
		options[0] = "Camera shake";
		options[1] = "Straight driving mode";
		options[2] = "Show FPS";
		options[3] = "Keep mouse in game window";
		
		setActiveButtons(mainMenu);
	}
	
	public void tick() {
		for(int i = 0; i < mainMenu.length; i++) {
			if(mainMenu[i].getHovering() > 0) hoverButtons[i] = true;
			else hoverButtons[i] = false;
		}
		for(int i = 0; i < activeButtons.length; i++) activeButtons[i].tick();
		backButton.tick();
		if(Game.gameState == GameState.MainMenu) {
			leftTank.tick();
			rightTank.tick();
		}
	}

	public void render(Graphics g) {
		Point centerTitle = HUD.centerText(g, currentTitle, new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT), titleFont);
		g.setFont(titleFont);
		g.drawString(currentTitle, (int) centerTitle.getX(), 100);
		for(int i = 0; i < activeButtons.length; i++) {
			activeButtons[i].render(g);
		}
		backButton.render(g);
		if(Game.gameState == GameState.MainMenu) {
			leftTank.render(g);
			rightTank.render(g);
		}
		else if(showHelp) {
			g.setFont(helpFont);
			Point centerText;
			for(int i = 0; i < help.length; i++) {
				centerText = HUD.centerText(g, help[i], new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT), helpFont);
				g.drawString(help[i], (int) centerText.getX(), i * 50 + 175);
			}
		}
		else if(showOptionLables) {
			g.setFont(optionFont);
			Point centerText;
			for(int i = 0; i < options.length; i++) {
				centerText = HUD.centerText(g, options[i], new Rectangle(0, 0, Game.WIDTH, Game.HEIGHT), optionFont);
				g.drawString(options[i], (int) centerText.getX(), i * 160 + 200);
			}
		}
	}
	
	public void buttonPressed(MenuButton button) {
		handler.clearGraphics();
		String buttonName = button.getName();
		System.out.println("Menu button \"" + buttonName + "\" was pressed");
		
		if(Game.gameState == GameState.MainMenu) {
			switch(buttonName) {
			case "Missions":
				Game.gameState = GameState.LevelSelect;
				currentTitle = "Missions";
				setActiveButtons(levelSelect);
				backButton.setVisible(true);
				break;
			case "Survival":
				currentTitle = "";
				setActiveButtons(null);
				levelLoader.enterGame(0);
				break;
			case "Options":
				Game.gameState = GameState.Options;
				currentTitle = "Options";
				showOptionLables = true;  
				setActiveButtons(optionsMenu);
				backButton.setVisible(true);
				break;
			case "Help":
				Game.gameState = GameState.Help;
				currentTitle = "How To Play";
				showHelp = true;
				setActiveButtons(null);
				backButton.setVisible(true);
				break;
			case "Quit":
				System.exit(0);
				break;
			}
		}
		else if(Game.gameState == GameState.LevelSelect) {
			if(buttonName == "Back") {
				Game.gameState = GameState.MainMenu;
				currentTitle = "Tanks!";
				setActiveButtons(mainMenu);
				backButton.setVisible(false);
			}
			else {
				currentTitle = "";
				setActiveButtons(null);
				backButton.setVisible(false);
				levelLoader.enterGame(Integer.parseInt(buttonName));
			}
		}
		else if(Game.gameState == GameState.Help) {
			if(buttonName == "Back") {
				Game.gameState = GameState.MainMenu;
				currentTitle = "Tanks!";
				setActiveButtons(mainMenu);
				showHelp = false;
				backButton.setVisible(false);
			}
		}
		else if(Game.gameState == GameState.Options) {
			if(buttonName == "Back") {
				Game.gameState = GameState.MainMenu;
				currentTitle = "Tanks!";
				setActiveButtons(mainMenu);
				showOptionLables = false;
				backButton.setVisible(false);
				Game.saveCurrentOptions();
			}
			else {
				if(button.getYesNoButton() != 2) {
					int optionNumber = Integer.parseInt(new String(buttonName.replaceFirst("o", "")));
					Game.options[optionNumber] = button.getYesNoButton();
				}
				else System.out.println("YesNoButton Error: Options buttons must be YesNoButtons");
			}
		}
		else if(Game.gameState == GameState.Paused) {
			if(activeButtons == pauseMenu) {
				if(buttonName == "Resume") {
					Game.gameState = GameState.Game;
					setActiveButtons(null);
				}
				else if(buttonName == "Help") {
					setActiveButtons(null);
					backButton.setVisible(true);
					showHelp = true;
				}
				else if(buttonName == "Main menu") {
					handler.clearObjects();
					Game.gameState = GameState.MainMenu;
					currentTitle = "Tanks!";
					setActiveButtons(mainMenu);
					backButton.setVisible(false);
					handler.setLevelFinish(null);
				}
			}
			else if(activeButtons == missionComplete) {
				if(buttonName == "Next mission") {
					Game.gameState = GameState.Game;
					levelLoader.enterGame(LevelLoader.levelNumber + 1);
					setActiveButtons(null);
				}
				else if(buttonName == "Replay mission") {
					Game.gameState = GameState.Game;
					levelLoader.enterGame(LevelLoader.levelNumber);
					setActiveButtons(null);
				}
				else if(buttonName == "Main menu") {
					handler.clearObjects();
					Game.gameState = GameState.MainMenu;
					currentTitle = "Tanks!";
					setActiveButtons(mainMenu);
					backButton.setVisible(false);
					handler.setLevelFinish(null);
				}
			}
			else if(buttonName == "Back") pause();
		}
	}
	
	public void setActiveButtons(MenuButton[] buttons) {
		if(buttons == null) activeButtons = new MenuButton[0];
		else activeButtons = buttons;
		for(int i = 0; i < mainMenu.length; i++) mainMenu[i].setVisible(false);
		for(int i = 0; i < levelSelect.length; i++) levelSelect[i].setVisible(false);
		for(int i = 0; i < pauseMenu.length; i++) pauseMenu[i].setVisible(false);
		for(int i = 0; i < optionsMenu.length; i++) optionsMenu[i].setVisible(false);
		for(int i = 0; i < missionComplete.length; i++) missionComplete[i].setVisible(false);
		for(int i = 0; i < activeButtons.length; i++) activeButtons[i].setVisible(true);
	}
	
	public void pause() {
		Game.gameState = GameState.Paused;
		currentTitle = "Game Paused";
		showHelp = false;
		setActiveButtons(pauseMenu);
		backButton.setVisible(false);
	}
	
	public void levelComplete() {
		Game.gameState = GameState.Paused;
		currentTitle = "Mission Complete!";
		setActiveButtons(missionComplete);
		backButton.setVisible(false);
	}
	
	public void shootMenuTanks() {
		leftTank.shootCannon(ObjectID.FastBullet);
		rightTank.shootCannon(ObjectID.FastBullet);
	}

	public boolean[] getHoverButtons() {
		return hoverButtons;
	}

	public void setHoverButtons(boolean[] hoverButtons) {
		this.hoverButtons = hoverButtons;
	}
}
