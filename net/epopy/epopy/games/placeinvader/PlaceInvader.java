package net.epopy.epopy.games.placeinvader;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.placeinvader.menus.PlaceInvaderGame;
import net.epopy.epopy.games.placeinvader.menus.PlaceInvaderOptions;

public class PlaceInvader extends AbstractGame {
	
	@Override
	public void onEnable() {
		menuGame = new PlaceInvaderGame();
		menuOptions = new PlaceInvaderOptions();
		menuOptions.onEnable();
		setStatus(false);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.PLACEINVADER_BG;
	}

	@Override
	public String getName() {
		return "PlaceInvader";
	}
}
