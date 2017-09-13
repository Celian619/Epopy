package net.epopy.epopy.games.ping;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.ping.menus.PingGame;
import net.epopy.epopy.games.ping.menus.PingOptions;

public class Ping extends AbstractGame {
	
	@Override
	public void onEnable() {
		menuGame = new PingGame();
		menuOptions = new PingOptions();
		menuOptions.onEnable();
		setStatus(false);
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
