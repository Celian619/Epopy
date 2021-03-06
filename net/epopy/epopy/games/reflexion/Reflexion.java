package net.epopy.epopy.games.reflexion;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.reflexion.menus.ReflexionGame;
import net.epopy.epopy.games.reflexion.menus.ReflexionOptions;

public class Reflexion extends AbstractGame {

	@Override
	public void onEnable() {
		menuGame = new ReflexionGame();
		menuOptions = new ReflexionOptions();
		setStatus(false);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.REFLEXION_BG;
	}
	
	@Override
	public String getName() {
		return "Reflexion";
	}
	
}
