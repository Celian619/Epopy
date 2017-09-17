package net.epopy.epopy.games.tank;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.tank.menus.TankGame;
import net.epopy.epopy.games.tank.menus.TankOptions;

public class Tank extends AbstractGame {

	@Override
	public void onEnable() {
		menuGame = new TankGame();
		menuOptions = new TankOptions();
		setStatus(false);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_TANK_WALL;
	}

	@Override
	public String getName() {
		return "Tank";
	}

}
