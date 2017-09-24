package net.epopy.epopy.games.deco;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.deco.menus.DecoGame;
import net.epopy.epopy.games.deco.menus.DecoOptions;
import net.epopy.epopy.games.gestion.AbstractGame;

public class Deco extends AbstractGame {
	
	@Override
	public void onEnable() {
		menuGame = new DecoGame();
		menuOptions = new DecoOptions();
		menuOptions.onEnable();
		setStatus(false);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.DECO_BG;
	}
	
	@Override
	public String getName() {
		return "Deco";
	}
	
}
