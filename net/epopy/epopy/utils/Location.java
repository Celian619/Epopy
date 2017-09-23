package net.epopy.epopy.utils;

import java.util.LinkedList;
import java.util.List;

public class Location {
	
	private double x;
	private double y;
	private int direction;
	private int maxSizeX;
	private int maxSizeY;
	private boolean isMax;
	private boolean isDirection;

	public Location(final double x, final double y, final int direction) {
		this.direction = direction;
		isDirection = true;
		this.x = x;
		this.y = y;
	}
	
	public Location(final double x, final double y, final int direction, final int maxSizeX, final int maxSizeY) {
		this.direction = direction;
		isDirection = true;
		this.x = x;
		this.y = y;

		isMax = true;
		this.maxSizeX = maxSizeX;
		this.maxSizeY = maxSizeY;
	}
	
	public Location(final double x, final double y) {
		
		this.x = x;
		this.y = y;
	}

	public Location(final double x, final double y, final int maxSizeX, final int maxSizeY) {
		this.x = x;
		this.y = y;
		isMax = true;
		this.maxSizeX = maxSizeX;
		this.maxSizeY = maxSizeY;
	}
	
	public Location(final Location loc) {
		x = loc.getX();
		y = loc.getY();
	}

	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public int getDirection() {
		return direction;
	}

	public void setX(final double x) {
		this.x = x;
	}
	
	public void setY(final double y) {
		this.y = y;
	}

	public void setDirection(final int direction) {
		this.direction = direction;
	}

	@Override
	public Location clone() {
		if (isDirection && isMax) {
			return new Location(x, y, direction, maxSizeX, maxSizeY);
		} else if (isMax) {
			return new Location(x, y, maxSizeX, maxSizeY);
		} else if (isDirection) {
			return new Location(x, y, direction);
		} else
			return new Location(x, y);
	}

	public Location setPos(double x, double y) {

		if (isMax) {
			x = normalizeX((int) x);
			y = normalizeY((int) y);
		}
		
		this.x = x;
		this.y = y;
		
		return this;
	}
	
	public Location setPos(double x, double y, final double direction) {

		if (isMax) {
			x = normalizeX(x);
			y = normalizeY(y);
		}
		
		this.x = x;
		this.y = y;
		this.direction = (int) direction;
		
		return this;
	}

	public List<Location> getNears(final int distance) {
		List<Location> nears = new LinkedList<Location>();
		for (int xF = (int) (x - distance); xF <= x + distance; xF++) {
			for (int yF = (int) (y - distance); yF <= y + distance; yF++) {
				Location loc = new Location(xF, yF);
				double dist = distance(loc);
				if (dist <= distance && dist != 0)
					nears.add(loc);
			}

		}
		return nears;
		
	}
	
	public double distance(final Location loc) {
		double distanceX = loc.getX() - x;
		double distanceY = loc.getY() - y;
		
		return Math.abs(distanceX) + Math.abs(distanceY);
	}

	public Location getNearest(final List<Location> locs) {
		Location returnLoc = locs.get(0);
		int i = 1000000;// distance la plus petite

		for (Location loc : locs) {
			if (distance(loc) < i) {
				returnLoc = loc;
				i = (int) distance(loc);
			}
		}
		
		return returnLoc;
	}
	
	public double getNearestDistance(final List<Location> locs) {
		double i = 1000000;// distance la plus petite

		for (Location loc : locs) {
			if (distance(loc) < i) {
				i = distance(loc);
			}
		}
		
		return i;
	}

	public Location add(final double x, final double y) {

		double finalX = this.x + x;
		double finalY = this.y + y;

		if (isMax) {
			finalX = normalizeX(finalX);
			finalY = normalizeY(finalY);
			if (finalX < this.x && x < 0 || finalX > this.x && x > 0) {
				finalX = this.x + x;
			}
			if (finalY < this.y && y < 0 || finalY > this.y && y > 0) {
				finalY = this.y + y;
			}
		}

		this.x = finalX;
		this.y = finalY;
		
		return this;
	}
	
	public Location remove(final double x, final double y) {
		double finalX = this.x - x;
		double finalY = this.y - y;

		if (isMax) {
			finalX = normalizeX(finalX);
			finalY = normalizeY(finalY);
		}

		this.x = finalX;
		this.y = finalY;
		
		return this;
	}
	
	public boolean equals(final Location loc) {
		return x == loc.getX() && y == loc.getY();
	}
	
	private double normalizeX(double x) {
		if (x > maxSizeX) x -= maxSizeX;// bordure
		else if (x < 0) x += maxSizeX;
		return x;
	}

	private double normalizeY(double y) {
		if (y > maxSizeY) y -= maxSizeY;// bordure
		else if (y < 0) y += maxSizeY;
		return y;
	}

}
