package net.epopy.epopy.games.gestion;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.Textures;

public abstract class AbstractGame {
	
	private boolean inGame = false;

	// menu
	public AbstractGameMenu menuGame;
	public AbstractGameMenu menuOptions;
	/*
	 * --> Pour cree un module de jeu Copier un autre module et changer les menus 'menuMain' (dans la fonction onEnable();) et changer le
	 * nom du jeu dans le 'getName();'
	 *
	 *
	 */
	
	/*
	 * Quand le module se load
	 */
	public abstract void onEnable();
	
	public AbstractGameMenu getMenuOptions() {
		return menuOptions;
	}

	public void setStatus(final boolean inGame) {
		this.inGame = inGame;
		if (getActualAbstract() != null)
			getActualAbstract().onEnable();
	}
	
	public boolean getStatus() {
		return inGame;
	}
	
	public abstract Textures getDefaultBackGround();
	
	/*
	 * Get le nom du jeu
	 */
	public abstract String getName();

	/*
	 * update le jeu
	 */
	public void update() {
		if (getActualAbstract() == null)
			Main.getGameManager().getMenu().update();
		else
			getActualAbstract().update();
			
	}
	
	/*
	 * Render le jeu
	 */
	public void render() {
		if (getActualAbstract() == null)
			Main.getGameManager().getMenu().render();
		else
			getActualAbstract().render();
	}

	private AbstractGameMenu getActualAbstract() {
		return inGame ? menuGame : null;
	}
	
}
