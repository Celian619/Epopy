package net.epopy.epopy.games.reflexion.menus;

import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class ReflexionGame extends AbstractGameMenu {

	private Morpion morpion;
	private MasterMind master;

	@Override
	public void onEnable() {
		morpion = new Morpion();
		morpion.onEnable();

		master = new MasterMind();
		master.onEnable();

	}
	
	@Override
	public void update() {
		morpion.update();
		master.update();
	}

	@Override
	public void render() {
		morpion.render();
		master.render();
	}

}
