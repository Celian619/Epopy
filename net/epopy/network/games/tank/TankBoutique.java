package net.epopy.network.games.tank;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.network.games.AbstractGameNetwork;

public class TankBoutique extends AbstractGameNetwork {
	
	@Override
	public void onEnable() {
	
	}
	
	@Override
	public void update() {
	
	}
	
	@Override
	public void render() {
		ComponentsHelper.drawText("Tank", 1197, 150, 48);
		ComponentsHelper.drawLine(950, 588, 1554, 588, 1);
		ComponentsHelper.drawText("Equipement", 1136, 600, 48);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return null;
	}
	
}
