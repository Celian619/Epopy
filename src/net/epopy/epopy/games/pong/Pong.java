package net.epopy.epopy.games.pong;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameLogger;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.games.pong.menus.PongGame;
import net.epopy.epopy.games.pong.menus.PongOptions;

public class Pong extends AbstractGame {
	
	@Override
	public void onEnable() {
		gameLogger = new GameLogger(getName());
		menuGame = new PongGame();
		menuOptions = new PongOptions();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_PONG_BG;
	}
	
	@Override
	public String getName() {
		return "Pong";
	}
	
}
