package net.epopy.network.games.tank.modules;

import org.lwjgl.opengl.GL11;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;

public class Zone {
	
	private final int zoneID;
	private float[] color;
	
	public Zone(final int zoneID) {
		color = new float[] { 1, 1, 1, 1 };
		this.zoneID = zoneID;
	}
	
	/*
	 * Getters
	 */
	public int getZoneID() {
		return zoneID;
	}
	
	/*
	 * Fonctions & Setters
	 */
	public void setColor(final float[] color) {
		this.color = color;
	}
	
	public void render() {
		GL11.glColor4f(color[0], color[1], color[2], color[3]);
		if (zoneID == 1)
			ComponentsHelper.renderTexture(Textures.NETWORK_GAME_TANK_ZONE_SMALL, 138, 682, 258, 270);
		else if (zoneID == 2)
			ComponentsHelper.renderTexture(Textures.NETWORK_GAME_TANK_ZONE_SMALL, 1535, 50, 258, 270, 150);
		GL11.glColor4f(1, 1, 1, 1);
	}
	
}
