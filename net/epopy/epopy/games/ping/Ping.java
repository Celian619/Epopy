package net.epopy.epopy.games.ping;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.games.ping.menus.PingGame;
import net.epopy.epopy.games.ping.menus.PingOptions;

public class Ping extends AbstractGame {
	
	@Override
	public void onEnable() {
		menuGame = new PingGame();
		menuOptions = new PingOptions();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_PING_BG;
	}
	
	@Override
	public String getName() {
		return "Ping";
	}
	
}
