package net.epopy.epopy.games.car;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.car.menus.CarGame;
import net.epopy.epopy.games.car.menus.CarOptions;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameStatus;

public class Car extends AbstractGame {

	@Override
	public void onEnable() {
		
		menuGame = new CarGame();
		menuOptions = new CarOptions();
		menuOptions.onEnable();
		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_CAR_BG;
	}

	@Override
	public String getName() {
		return "Car";
	}

}
