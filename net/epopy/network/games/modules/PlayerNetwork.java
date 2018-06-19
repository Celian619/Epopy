package net.epopy.network.games.modules;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.tank.Tank;

public class PlayerNetwork {
	
	private final String name;
	private final Team team;
	
	private int hp, printTexture;
	private Location3D location;

	public PlayerNetwork(final String name, final Team team, final int hp) {
		this.name = name;
		this.team = team;
		this.hp = hp;
		location = new Location3D(team.getSpawnLocation().getX(), team.getSpawnLocation().getY(), team.getSpawnLocation().getZ(), team.getSpawnLocation().getYaw(), team.getSpawnLocation().getPitch());
	}
	
	/*
	 * Getters
	 */
	
	// donne sa vie
	public int getHP() {
		return hp;
	}
	
	public int getPrintTexture() {
		return printTexture;
	}
	
	public String getName() {
		return name;
	}
	
	public Location3D getLocation() {
		return location;
	}
	
	public Team getTeam() {
		return team;
	}
	
	/*
	 * Setters
	 */
	public void setLocation(final Location3D location) {
		this.location = location;
	}
	
	public void setPrintTexture(final int print) {
		printTexture = print;
	}
	
	public void setHP(final int hp) {
		this.hp = hp;
	}
	
	public void addPrintTexture() {
		printTexture++;
	}
	
	/*
	 * Fonction
	 */
	public void render() {
		Textures texture = printTexture <= 5 ? Textures.TANK_TANK2 : Textures.TANK_TANK1;
		if (printTexture >= 10) printTexture = 0;
		
		if (getName().equals(NetworkPlayer.getNetworkPlayer().getName()))
			ComponentsHelper.drawText(Tank.balls + " Obus | " + getName() + " - " + getHP() + " HP", getLocation().getX(), getLocation().getY() - 75, PosWidth.MILIEU, PosHeight.HAUT, 30, team.getColor());
		else
			ComponentsHelper.drawText(getName() + " - " + getHP() + " HP", getLocation().getX(), getLocation().getY() - 75, PosWidth.MILIEU, PosHeight.HAUT, 30, team.getColor());
			
		ComponentsHelper.renderTexture(texture, getLocation().getX() - 26, getLocation().getY() - 24, 64, 56, getLocation().getDirection());
		
	}
}
