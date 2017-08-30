package net.epopy.epopy.games.gestion;

import java.lang.reflect.InvocationTargetException;

import net.epopy.epopy.games.car.Car;
import net.epopy.epopy.games.placeinvader.PlaceInvader;
import net.epopy.epopy.games.pong.Pong;
import net.epopy.epopy.games.snake.Snake;
import net.epopy.epopy.games.speedrun.SpeedRun;
import net.epopy.epopy.games.tank.Tank;

public enum GameList {

	PONG(1, Pong.class),
	CAR(2, Car.class),
	SNAKE(3, Snake.class),
	TANK(4, Tank.class),
	PLACEINVADER(5, PlaceInvader.class),
	SPEEDRUN(6, SpeedRun.class)
	// EATMAN(5, Eatman.class),
	// MARIO(6, Mario.class),
	// si id > 1000, il n'est plus visible
	//DECO(6, Deco.class),
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
