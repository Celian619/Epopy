package net.epopy.network.games.modules;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;

public class PlayerNetwork {
	
	private final String name;
	private Location3D location;
	private final Team team;
	
	private int hp;
	private int printTexture;

	public PlayerNetwork(final String name, final Team team, int hp) {
		this.name = name;
		this.team = team;
		this.hp = hp;
		location = new Location3D(team.getSpawnLocation().getX(), team.getSpawnLocation().getY(), team.getSpawnLocation().getZ(), team.getSpawnLocation().getYaw(), team.getSpawnLocation().getPitch());
	}
	
	/*
	 * Getters
	 */
	
	//donne sa vie
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
		// GL11.glColor4f(team.getColor()[0], team.getColor()[1],team.getColor()[2], 1);

		Textures texture = printTexture <= 5 ? Textures.TANK_TANK2 : Textures.TANK_TANK1;
		if (printTexture >= 10) printTexture = 0;
		
		ComponentsHelper.drawText(getName() + " - " + getHP() + " HP", getLocation().getX(), getLocation().getY() - 75, PositionWidth.MILIEU, PositionHeight.HAUT, 30, team.getColor());
		ComponentsHelper.renderTexture(texture,  (getLocation().getX() - 26),  (getLocation().getY() - 24), 64, 56, getLocation().getDirection());

	}
}
