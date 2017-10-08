package net.epopy.epopy.games.gestion;

import java.lang.reflect.InvocationTargetException;

import net.epopy.epopy.games.car.Car;
import net.epopy.epopy.games.ping.Ping;
import net.epopy.epopy.games.placeinvader.PlaceInvader;
import net.epopy.epopy.games.snake.Snake;
import net.epopy.epopy.games.speedrun.SpeedRun;
import net.epopy.epopy.games.tank.Tank;

public enum GameList {
	
	PING(1, Ping.class),
	CAR(2, Car.class),
	SNAKE(3, Snake.class),
	PLACEINVADER(4, PlaceInvader.class),
	TANK(5, Tank.class),
	SPEEDRUN(6, SpeedRun.class),
	// TETRAS(7, Tetras.class),
	// EATMAN(8, Eatman.class),
	// MARIO(9, Mario.class),
	// si id > 1000, il n'est plus visible
	;
	
	private int id;
	private Class<?> clazz;

	/**
	 *
	 * Class qui regroupe tous les retros du jeu
	 *
	 * @param id
	 *            du jeux (ex: id=1 -> ça sera le premier jeu proposé)
	 * @param clazz
	 *            class du jeu
	 */
	private GameList(final int id, final Class<?> clazz) {
		this.id = id;
		this.clazz = clazz;
	}

	public int getID() {
		return id;
	}
	
	public AbstractGame getAbstractGame() {
		try {
			return (AbstractGame) clazz.getConstructor().newInstance();
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
