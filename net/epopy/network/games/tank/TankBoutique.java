package net.epopy.network.games.tank;

import static net.epopy.epopy.display.components.ComponentsHelper.drawLine;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import java.util.ArrayList;
import java.util.List;

import net.epopy.epopy.display.Textures;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.tank.modules.boutique.Canon;
import net.epopy.network.games.tank.modules.boutique.Carrosserie;
import net.epopy.network.games.tank.modules.boutique.Model;
import net.epopy.network.games.tank.modules.boutique.Munitions;
import net.epopy.network.games.tank.modules.boutique.Vitesse;

public class TankBoutique extends AbstractGameNetwork {

	public static List<Model> models = new ArrayList<>(4);

	public static int LEVEL_VITESSE = 0;
	public static int LEVEL_CANON = 0;
	public static int LEVEL_MUNITIONS = 0;
	
	public static void loadModels() {
		models.add(new Carrosserie());
		models.add(new Canon());
		models.add(new Munitions());
		models.add(new Vitesse());
	}
	
	@Override
	public void onEnable() {
	
	}

	@Override
	public void update() {
		for (Model model : models)
			model.update();
	}

	@Override
	public void render() {
		drawText("Tank", 1197, 150, 48);
		drawLine(950, 588, 1554, 588, 1);
		drawText("Equipement", 1136, 600, 48);

		for (Model model : models)
			model.render();
	}

	@Override
	public Textures getDefaultBackGround() {
		return null;
	}

}
