package net.epopy.network.games.tank.modules.boutique;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;

public class Vitesse extends Model {
	
	public Vitesse() {
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
		return "Vitesse";
	}

	@Override
	public void update() {
		buttonBuy.update(1706, 465, PositionWidth.MILIEU, PositionHeight.HAUT);
		
		if(buttonBuy.isClicked()) {
			System.out.println("request buy: " + getName());
		}
	}

	@Override
	public void render() {
		int x = 1706;
		ComponentsHelper.drawText(getName() + " (" + getLevel().getLevel() + "/" + getMaxLevel().getLevel() + ")", x, 410, PositionWidth.MILIEU, PositionHeight.HAUT, 30);
		ComponentsHelper.drawText("(" + getPrice() + " coins)", x-5, 498, PositionWidth.MILIEU, PositionHeight.HAUT, 20, new float[]{1, 0.1f, 0.1f, 1});
		
		ComponentsHelper.renderTexture(Textures.NETWORK_BOUTIQUE_TANK_VITESSE, x-53, 300, 100, 100);
		
		buttonBuy.render();
	}

	
}
