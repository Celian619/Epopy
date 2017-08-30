package net.epopy.epopy.games.entities;

import java.awt.Rectangle;

import org.lwjgl.opengl.Display;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;

public class Entity {

	private String name;
	private double xx;
	private double yy;
	private double ww;
	private double hh;

	private double x = xx;
	private double y = yy;
	private double w = ww;
	private double h = hh;

	private boolean updateX;
	private boolean updateY;
	private Textures texture;

	public Entity(String name, double x, double y, double w, double h) {
		this.name = name;
		xx = x;
		yy = y;
		ww = w;
		hh = h;
	}

	public Entity(Textures texture, String name, double x, double y, double w, double h, boolean updateX, boolean updateY) {
		this.texture = texture;
		this.name = name;
		xx = x;
		yy = y;
		ww = w;
		hh = h;
		this.updateX = updateX;
		this.updateY = updateY;
	}

	public void render() {
		if (texture != null)
			ComponentsHelper.renderTexture(texture, (int) x, (int) y, (int) w, (int) h);
	}

	public void update() {
		x = getResponsiveX(xx);
		y = getResponsiveY(yy);
		w = getResponsiveX(ww);
		h = getResponsiveY(hh);
	}

	public void update(double x, double y) {
		if (updateX)
			this.x = getResponsiveX(xx + x);
		else
			this.x = getResponsiveX(xx);

		if (updateY)
			this.y = getResponsiveY(y);
		else
			this.y = getResponsiveY(yy);

		w = getResponsiveX(ww);
		h = getResponsiveY(hh);
	}

	public CollisionType hasCollision(Entity entity) {
		Rectangle c = new Rectangle((int) getX(), (int) getY(), (int) getW(), (int) getY());
		Rectangle c2 = new Rectangle((int) entity.getX(), (int) entity.getY(), (int) entity.getW(), (int) entity.getY());
		boolean collision = c.intersects(c2);// collisionCheck(c, c2);
		if (collision) {

			if (entity.getY() < getY() && entity.getX() + getW() - 20 > getX()) {
				if (getY() < entity.getY())
					return CollisionType.BELOW;
				else
					return CollisionType.UP;
			} else {
				if (entity.getY() >= getY()) {
					if (getX() < entity.getX())
						return CollisionType.BACKWARD;
					else
						return CollisionType.FORWARD;
				}
			}

		}
		return CollisionType.NO_COLLISION;
	}

	public String getName() {
		return name;
	}

	public double getXX() {
		return xx;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		xx = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		yy = y;
	}

	public double getW() {
		return w;
	}

	public void setW(double w) {
		ww = w;
	}

	public double getH() {
		return h;
	}

	public void setH(double h) {
		hh = h;
	}

	private int getResponsiveX(double size) {
		return (int) (size / 1000 * Display.getWidth());
	}

	private int getResponsiveY(double size) {
		return (int) (size / 630 * Display.getHeight());
	}
	
	@SuppressWarnings("unused")
	private boolean collisionCheck(Rectangle rect1, Rectangle rect2) {
		return hitCheck(rect1.x, rect1.y, rect2) ||
				hitCheck(rect1.x + rect1.width, rect1.y, rect2) ||
				hitCheck(rect1.x, rect1.y + rect1.height, rect2) ||
				hitCheck(rect1.x + rect1.width,
						rect1.y + rect1.height, rect2);
	}

	private boolean hitCheck(int x, int y, Rectangle rect) {
		return x >= rect.x && x <= rect.x + rect.width &&
				y >= rect.y && y <= rect.y + rect.height;
	}

	public enum CollisionType {
		BACKWARD(), // <- en arrier
		FORWARD(), // -> en avant
		UP(), // /\ dessus
		BELOW(), // \/ en desous
		NO_COLLISION();
	}
}
