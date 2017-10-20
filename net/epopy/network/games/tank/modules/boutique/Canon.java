package net.epopy.network.games.tank.modules.boutique;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;

public class Canon extends Model {

	public Canon() {
		List<Level> levelsRegister = new ArrayList<>(10);
		levelsRegister.add(new Level(0, 300));
		levelsRegister.add(new Level(1, 700));
		levelsRegister.add(new Level(2, 1400));
		levelsRegister.add(new Level(3, 2000));
		levelsRegister.add(new Level(4, -1));

		this.levels = levelsRegister;
		this.maxLevel = this.levels.get(levels.size()-1);
	}

	@Override
	public String getName() {
		return "Cadence de tir";
	}

	@Override
	public void update() {
		if(getLevel().getLevel() == getMaxLevel().getLevel()) 
			return;

		buttonBuy.update(1130, 465, PositionWidth.MILIEU, PositionHeight.HAUT);

		if(buttonBuy.isClicked()) 
			buy();
	}

	@Override
	public void render() {
		int x = 1130;
		ComponentsHelper.drawText(getName() + " (" + getLevel().getLevel() + "/" + getMaxLevel().getLevel() + ")", x, 410, PositionWidth.MILIEU, PositionHeight.HAUT, 30);	
		ComponentsHelper.renderTexture(Textures.NETWORK_BOUTIQUE_TANK_CANON, x-75, 360, 150, 20);

		if(getLevel().getLevel() == getMaxLevel().getLevel()) 
			return;	

		ComponentsHelper.drawText("(" + getPrice() + " coins)", x-5, 498, PositionWidth.MILIEU, PositionHeight.HAUT, 20, new float[]{1, 0.1f, 0.1f, 1});
		buttonBuy.render();
	}


}
