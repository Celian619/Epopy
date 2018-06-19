package net.epopy.network.games.modules;

public class Team {
	
	private int points;
	private final String name;
	private final float[] color;
	private final Location3D spawn;

	public Team(final String name, final float[] color, final Location3D spawn) {
		this.name = name;
		this.color = color;
		this.spawn = spawn;
		points = 0;
	}

	public void setPoints(final int points) {
		this.points = points;
	}

	/*
	 * Getters
	 */

	public String getName() {
		return name;
	}

	public float[] getColor() {
		return color;
	}

	public int getPoints() {
		return points;
	}

	public Location3D getSpawnLocation() {
		return spawn;
	}

}
