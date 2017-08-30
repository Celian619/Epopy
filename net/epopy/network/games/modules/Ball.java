package net.epopy.network.games.modules;

public class Ball {

	private float[] color;
	private Location3D location3d;
	private String name;
	
	public Ball(String name, float[] color, Location3D location3d) {
		this.name = name;
		this.color = color;
		this.location3d = location3d;
	}

	/*
	 * GETTERS
	 */
	
	public float[] getColor() {
		return color;
	}

	public Location3D getLocation() {
		return location3d;
	}

	public String getName() {
		return name;
	}

	/*
	 * SETTERS
	 */
	
	public void setColor(float[] color) {
		this.color = color;
	}

	public void setLocation3d(Location3D location3d) {
		this.location3d = location3d;
	}

	public void setName(String name) {
		this.name = name;
	}
}
