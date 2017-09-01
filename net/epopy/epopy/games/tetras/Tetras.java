package net.epopy.epopy.games.tetras;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameLogger;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.games.tetras.menus.TetrasGame;
import net.epopy.epopy.games.tetras.menus.TetrasOptions;

public class Tetras extends AbstractGame {
	
	@Override
	public void onEnable() {
		gameLogger = new GameLogger(getName());
		menuGame = new TetrasGame();
		menuOptions = new TetrasOptions();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_TETRAS_BG;
	}
	
	@Override
	public String getName() {
		return "Tetras";
	}
	
}
