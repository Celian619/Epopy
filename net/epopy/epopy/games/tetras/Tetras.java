package net.epopy.epopy.games.tetras;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.tetras.menus.TetrasGame;
import net.epopy.epopy.games.tetras.menus.TetrasOptions;

public class Tetras extends AbstractGame {

	@Override
	public void onEnable() {
		menuGame = new TetrasGame();
		menuOptions = new TetrasOptions();
		setStatus(false);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.TETRAS_BG;
	}

	@Override
	public String getName() {
		return "Tetras";
	}

}
