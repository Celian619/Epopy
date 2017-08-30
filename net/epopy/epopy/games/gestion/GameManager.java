package net.epopy.epopy.games.gestion;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import net.epopy.epopy.Main;
import net.epopy.epopy.games.gestion.menus.GameMenu;
import net.epopy.epopy.games.gestion.menus.GameOptionsMenu;

public class GameManager {
	
	private Map<Integer, AbstractGame> games = new HashMap<>(GameList.values().length);
	private AbstractGame gameEnable;
	private AbstractGameMenu menuGames;
	private AbstractGameMenu menuUsers;
	private AbstractGameMenu options;
	
	public GameManager() {
		for (GameList gameList : GameList.values())
			games.put(gameList.getID(), gameList.getAbstractGame());
			
		System.out.println("GameList:");
		for (Entry<Integer, AbstractGame> game : games.entrySet())
			System.out.println(" " + game.getKey() + " - " + game.getValue().getName());
			
		setGameEnable(games.get(Main.getPlayer().getLastGame()));
		
		/**
		 * Menus globals
		 */
		menuGames = new GameMenu();
		menuGames.onEnable();

		options = new GameOptionsMenu();
		options.onEnable();
		
		getGameEnable().setStatus(GameStatus.MENU_CHOOSE_GAMES);		
	}
	
	public AbstractGame getAbstractGame(int id) {
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
		if (getGameEnable().getGameStatus() == GameStatus.MENU_CHOOSE_GAMES)
			return menuGames;
		else if (getGameEnable().getGameStatus() == GameStatus.MENU_CHOOSE_USERS)
			return menuUsers;
		else if (getGameEnable().getGameStatus() == GameStatus.OPTIONS)
			return options;
		return null;
	}
	
	public void setGameEnable(AbstractGame gameToEnable) {
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
