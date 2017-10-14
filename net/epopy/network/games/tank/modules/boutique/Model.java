package net.epopy.network.games.tank.modules.boutique;

import java.util.List;

import net.epopy.epopy.display.components.ButtonGui;

public abstract class Model {

	public List<Level> levels;
	public Level currentLevel;
	public Level maxLevel;
	public ButtonGui buttonBuy = new ButtonGui("Améliorer", new float[]{0, 1, 0, 1}, 30);;
	
	public int getPrice() {
		return getLevel().getPrice();
	}
	
	public Level getLevel() {
		return currentLevel;
	}

	public Level getMaxLevel() {
		return maxLevel;
	}

	public void upLevel() {
		//TODO
	}

	public abstract String getName();

	public abstract void update();

	public abstract void render();
}
