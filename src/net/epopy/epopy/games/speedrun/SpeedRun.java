package net.epopy.epopy.games.speedrun;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameLogger;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.games.speedrun.menus.SpeedRunGame;
import net.epopy.epopy.games.speedrun.menus.SpeedRunOptions;

public class SpeedRun extends AbstractGame {

	@Override
	public void onEnable() {
		gameLogger = new GameLogger(getName());
		menuGame = new SpeedRunGame();
		menuOptions = new SpeedRunOptions();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_SPEEDRUN_BG;
	}

	@Override
	public String getName() {
		return "SpeedRun";
	}

}
