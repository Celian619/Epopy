package net.epopy.epopy.games.deco;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.deco.menus.DecoGame;
import net.epopy.epopy.games.deco.menus.DecoOptions;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameLogger;
import net.epopy.epopy.games.gestion.GameStatus;

public class Deco extends AbstractGame {

	@Override
	public void onEnable() {
		gameLogger = new GameLogger(getName());

		menuGame = new DecoGame();
		menuOptions = new DecoOptions();
		menuOptions.onEnable();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_DECO_BG;
	}

	@Override
	public String getName() {
		return "Deco";
	}

}
