package net.epopy.network.games.tank.modules.boutique;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;

public class Munitions extends Model {
	
	public Munitions() {
		List<Level> levelsRegister = new ArrayList<>(10);
		levelsRegister.add(new Level(0, 500));
		levelsRegister.add(new Level(1, 700));
		levelsRegister.add(new Level(2, 1000));
		levelsRegister.add(new Level(3, 1500));
		levelsRegister.add(new Level(4, -1));
		
		this.levels = levelsRegister;
		this.maxLevel = this.levels.get(levels.size()-1);
	}

	@Override
	public String getName() {
		return "Munitions";
	}

	@Override
	public void update() {
		if(getLevel().getLevel() == getMaxLevel().getLevel()) 
			return;	
		
		buttonBuy.update(1445, 465, PosWidth.MILIEU, PosHeight.HAUT);
		if(buttonBuy.isClicked()) 
			buy();
	}

	@Override
	public void render() {
		int x = 1445;
		ComponentsHelper.drawText(getName() + " (" + getLevel().getLevel() + "/" + getMaxLevel().getLevel() + ")", x, 410, PosWidth.MILIEU, PosHeight.HAUT, 30);
		ComponentsHelper.renderTexture(Textures.NETWORK_BOUTIQUE_TANK_MUNITIONS, x-69, 300, 139, 113);
		
		if(getLevel().getLevel() == getMaxLevel().getLevel()) 
			return;	
		
		ComponentsHelper.drawText("(" + getPrice() + " coins)", x-5, 498, PosWidth.MILIEU, PosHeight.HAUT, 20, new float[]{1, 0.1f, 0.1f, 1});	
		buttonBuy.render();
	}

	
}
