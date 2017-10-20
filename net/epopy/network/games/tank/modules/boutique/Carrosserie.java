package net.epopy.network.games.tank.modules.boutique;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;

public class Carrosserie extends Model {

	public Carrosserie() {
		List<Level> levelsRegister = new ArrayList<>(10);
		levelsRegister.add(new Level(0, 250));
		levelsRegister.add(new Level(1, 500));
		levelsRegister.add(new Level(2, 1500));
		levelsRegister.add(new Level(3, 1700));
		levelsRegister.add(new Level(4, 2300));

		this.levels = levelsRegister;
		this.maxLevel = this.levels.get(levels.size()-1);
	}

	@Override
	public String getName() {
		return "Carrosserie";
	}

	@Override
	public void update() {
		if(getLevel() == getMaxLevel()) 
			return;	

		buttonBuy.update(815, 465, PositionWidth.MILIEU, PositionHeight.HAUT);

		if(buttonBuy.isClicked()) 
			buy();
	}

	@Override
	public void render() {
		int x = 815;
		ComponentsHelper.drawText(getName() + " (" + getLevel().getLevel() + "/" + getMaxLevel().getLevel() + ")", x, 410, PositionWidth.MILIEU, PositionHeight.HAUT, 30);
		ComponentsHelper.renderTexture(Textures.NETWORK_BOUTIQUE_TANK_CARROSSERIE, x-85, 300, 170, 104);

		if(getLevel() == getMaxLevel()) 
			return;	
		ComponentsHelper.drawText("(" + getPrice() + " coins)", x-5, 498, PositionWidth.MILIEU, PositionHeight.HAUT, 20, new float[]{1, 0.1f, 0.1f, 1});
		buttonBuy.render();
	}


}
