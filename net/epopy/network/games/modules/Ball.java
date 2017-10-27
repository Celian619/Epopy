package net.epopy.network.games.modules;

import static net.epopy.epopy.display.components.ComponentsHelper.drawCircle;

import net.epopy.network.NetworkPlayer;

public class Ball {

	private float[] color;
	private Location3D location3d;
	private String name;

	private double lastX = 0;
	private double lastY = 0;
	private int stay = 0;

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

	public void render() {
		if(getLocation().getX() == lastX && getLocation().getY() == lastY) {
			stay++;
			if(stay > 10) {
				stay = 0;
				NetworkPlayer.getGame().removeBall(getName());
			}
		} else 
			drawCircle(getLocation().getX(), getLocation().getY(), 5, 10, getColor());
		this.lastX = location3d.getX();
		this.lastY = location3d.getY();
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
