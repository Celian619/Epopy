package net.epopy.epopy.games.placeinvader;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameLogger;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.games.placeinvader.menus.PlaceInvaderGame;
import net.epopy.epopy.games.placeinvader.menus.PlaceInvaderOptions;

public class PlaceInvader extends AbstractGame {
	@Override
	public void onEnable() {
		gameLogger = new GameLogger(getName());
		
		menuGame = new PlaceInvaderGame();
		menuOptions = new PlaceInvaderOptions();
		menuOptions.onEnable();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_PLACEINVADER_BG;
	}
	
	@Override
	public String getName() {
		return "PlaceInvader";
	}
}
