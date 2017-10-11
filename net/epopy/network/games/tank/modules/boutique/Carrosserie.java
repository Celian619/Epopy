package net.epopy.network.games.tank.modules.boutique;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;

public class Carrosserie extends Model {

	public Carrosserie() {
		List<Level> levelsRegister = new ArrayList<>(10);
		levelsRegister.add(new Level(1, 250));
		levelsRegister.add(new Level(2, 250));
		levelsRegister.add(new Level(3, 250));
		
		this.levels = levelsRegister;
		this.maxLevel = this.levels.get(levels.size()-1);
		int playerLevel = 0;//TODO bdd
		this.currentLevel = this.levels.get(playerLevel);
	}

	@Override
	public String getName() {
		return "Carrosserie";
	}

	@Override
	public void update() {
		buttonBuy.update(800, 180, PositionWidth.GAUCHE, PositionHeight.HAUT);
	}

	@Override
	public void render() {
		ComponentsHelper.drawText(getLevel().getLevel() + "/" + getMaxLevel().getLevel() + "  - " + getPrice(), 800, 210, 30);
		
		buttonBuy.render();
	}

	
}
