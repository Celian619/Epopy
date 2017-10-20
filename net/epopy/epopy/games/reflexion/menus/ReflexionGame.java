package net.epopy.epopy.games.reflexion.menus;

import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.reflexion.Reflexion;

public class ReflexionGame extends AbstractGameMenu {
	
	private static int choice = 0;
	
	@Override
	public void onEnable() {
		switch (choice) {
			case 0:
			Morpion m = new Morpion();
			m.onEnable();
			Reflexion.setMenuGame(m);
				break;
				
			default:
				break;
		}
		
	}
	
	@Override
	public void update() {
		System.out.println("t");
	}
	
	@Override
	public void render() {
	
	}
	
}
