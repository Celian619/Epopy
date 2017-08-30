package net.epopy.network.games.modules;

public class Team {

	private String name;
	private float[] color;
	private Location3D spawn;
	private int points;
	
	public Team(String name, float[] color, Location3D spawn) {
		this.name = name;
		this.color = color;
		this.spawn = spawn;
		this.points = 0;
	}
	
	public void setPoints(int points) {
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
