package net.epopy.epopy.games.snake;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.GameLogger;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.games.snake.menus.SnakeGame;
import net.epopy.epopy.games.snake.menus.SnakeOptions;

public class Snake extends AbstractGame {

	@Override
	public void onEnable() {
		gameLogger = new GameLogger(getName());

		menuGame = new SnakeGame();
		menuOptions = new SnakeOptions();

		setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.GAME_SNAKE_BG;
	}

	@Override
	public String getName() {
		return "Snake";
	}
	
}
