package net.epopy.network.games.tank;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.tank.modules.boutique.Carrosserie;
import net.epopy.network.games.tank.modules.boutique.Model;

public class TankBoutique extends AbstractGameNetwork {

	private List<Model> models = new ArrayList<>(10);

	@Override
	public void onEnable() {
		models.add(new Carrosserie());
	}

	@Override
	public void update() {
		for(Model model : models)
			model.update();
	}

	@Override
	public void render() {
		ComponentsHelper.drawText("Tank", 1197, 150, 48);
		ComponentsHelper.drawLine(950, 588, 1554, 588, 1);
		ComponentsHelper.drawText("Equipement", 1136, 600, 48);
	
		for(Model model : models)
			model.render();
	}

	@Override
	public Textures getDefaultBackGround() {
		return null;
	}

}
