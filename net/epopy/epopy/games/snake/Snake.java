package net.epopy.epopy.games.snake;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.snake.menus.SnakeGame;
import net.epopy.epopy.games.snake.menus.SnakeOptions;

public class Snake extends AbstractGame {
	
	@Override
	public void onEnable() {
		menuGame = new SnakeGame();
		menuOptions = new SnakeOptions();
		setStatus(false);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.SNAKE_BG;
	}
	
	@Override
	public String getName() {
		return "Snake";
	}

}
