package net.epopy.network.games.modules;

public class Location3D {

	private double x, y, z, yaw, pitch;
	
	public Location3D(double x, double y, double z, double yaw, double pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	public Location3D copy() {
		return new Location3D(x, y, z, yaw, pitch);
	}
	
	/**
	 * Setters
	 */
	
	public Location3D mul(float v) {
		x *= v;
		y *= v;
		z *= v;

		return this;
	}
	
	public Location3D add(Location3D location) {
		this.x -= location.getX();
		this.y -= location.getY();
		this.z -= location.getZ();
		this.yaw -= location.getYaw();
		this.pitch -= location.getPitch();

		return this;
	}
	
	public Location3D add(double x, double y, double z, double yaw, double pitch) {
		this.x += x;
		this.y += y;
		this.z += z;
		this.yaw += yaw;
		this.pitch += pitch;

		return this;
	}
	
	public Location3D sub(double x, double y, double z, double yaw, double pitch) {
		this.x -= x;
		this.y -= y;
		this.z -= z;
		this.yaw -= yaw;
		this.pitch -= pitch;

		return this;
	}
	
	public Location3D sub(Location3D location) {
		this.x -= location.getX();
		this.y -= location.getY();
		this.z -= location.getZ();
		this.yaw -= location.getYaw();
		this.pitch -= location.getPitch();

		return this;
	}
	
	public void setX(double x) {
		this.x = x;
	}

	public void setY(double y) {
		this.y = y;
	}

	public void setZ(double z) {
		this.z = z;
	}

	public void setYaw(double yaw) {
		this.yaw = yaw;
	}

	public void setPitch(double pitch) {
		this.pitch = pitch;
	}
	public void setPos(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	public void set(double x, double y, double z, double yaw, double pitch) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
	}
	/*
	 * Getters
	 */
	
	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public double getYaw() {
		return yaw;
	}

	public double getPitch() {
		return pitch;
	}
	
	
		
}
