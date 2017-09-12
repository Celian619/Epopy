package net.epopy.epopy.games.gestion;

import java.util.HashMap;
import java.util.Map;

import net.epopy.epopy.Main;
import net.epopy.epopy.games.gestion.menus.GameMenu;

public class GameManager {

	private final Map<Integer, AbstractGame> games = new HashMap<>(GameList.values().length);
	private AbstractGame gameEnable;
	private final AbstractGameMenu menuGames;

	public GameManager() {
		for (GameList gameList : GameList.values())
			games.put(gameList.getID(), gameList.getAbstractGame());
			
		setGameEnable(games.get(Main.getPlayer().getLastGame()));

		/**
		 * Menus globals
		 */
		menuGames = new GameMenu();
		menuGames.onEnable();

		getGameEnable().setStatus(GameStatus.MENU_CHOOSE_GAMES);
	}

	public AbstractGame getAbstractGame(final int id) {
		if (games.containsKey(id))
			return games.get(id);
		return null;
	}

	public AbstractGame getGameEnable() {
		return gameEnable;
	}

	/**
	 * Donne la liste des tous les jeux sous forme de menu
	 *
	 * @return un menu
	 */
	public AbstractGameMenu getMenu() {
		return menuGames;
		
	}

	public void setGameEnable(final AbstractGame gameToEnable) {
		gameEnable = gameToEnable;
		gameEnable.onEnable();
	}

	public void update() {
		if (gameEnable != null)
			gameEnable.update();
	}

	public void render() {
		if (gameEnable != null)
			gameEnable.render();

	}
}
