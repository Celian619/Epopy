package net.epopy.network.games;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.TreeMap;

import net.epopy.network.games.tank.Tank;

public enum GameListNetwork {

	//CAR(1, Car.class),
	TANK(1, Tank.class);

	private static Map<Integer, GameListNetwork> gamesList = new TreeMap<>();

	public static GameListNetwork getGameByID(int id) {
		initGamesList();
		return gamesList.get(id);
	}
	public static int getGamesSize() {
		initGamesList();
		return gamesList.size();
	}
	
	private static void initGamesList() {
		if(gamesList.isEmpty()) {
			for(GameListNetwork game : values())
				gamesList.put(game.getID(), game);
		}
	}
	
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
	private GameListNetwork(int id, Class<?> clazz) {
		this.id = id;
		this.clazz = clazz;
	}

	public int getID() {
		return id;
	}

	public AbstractGameNetwork getAbstractGame() {
		try {
			return (AbstractGameNetwork) clazz.getConstructor().newInstance();
		} catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
}
