package net.epopy.network.games.tank.modules;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.network.games.modules.Location3D;

public class CalculTank {
	
	public static int getDirectionMouse(double x, double y) {
		double Xa = Mouse.getX() / (double) Display.getWidth() * AbstractGameMenu.defaultWidth - x;
		double Ya = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() *  AbstractGameMenu.defaultHeight - y;
		return (int) ((Ya < 0 ? -1 : 1) * Math.toDegrees(Math.acos(Xa / Math.sqrt(Xa * Xa + Ya * Ya))));
	}
	
	public static boolean isMouseDistanceNear(Location3D location) {
		double Xa = Mouse.getX() / (double) Display.getWidth() *  AbstractGameMenu.defaultWidth;
		double Ya = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() *  AbstractGameMenu.defaultHeight;
		Location3D locMouse = new Location3D(Xa, Ya, 0.0, 0.0, 0.0);

		return distance(locMouse, location) <= 25;//TODO changer par la suite la taille du tank 25 = taille
	}
	
	public static double distance(Location3D player, Location3D target) {
		double distanceX = target.getX() - player.getX();
		double distanceY = target.getY() - player.getY();
		
		return Math.abs(distanceX) + Math.abs(distanceY);
	}
}
