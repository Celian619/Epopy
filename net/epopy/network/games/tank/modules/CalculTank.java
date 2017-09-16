package net.epopy.network.games.tank.modules;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Location3D;
import net.epopy.network.games.tank.Tank;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerLocation;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerMove;

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

	private static double deplacedX(final Location3D loc, final double speed) {
		return loc.getX() + speed * Math.cos(Math.toRadians(loc.getDirection()));
	}

	private static double deplacedY(final Location3D loc, final double speed) {
		return loc.getY() + speed * Math.sin(Math.toRadians(loc.getDirection()));
	}

	public static boolean isCollision(final int x, final int y) {
		return Tank.MAP.distanceBlocks[1920 * y + x] < Tank.TANK_SIZE + 1;
	}

	public static void moove(boolean back) {
		Location3D location = NetworkPlayer.getGame().getPlayer(NetworkPlayer.getNetworkPlayer().getName()).getLocation();
		double x = deplacedX(location, back ? -Tank.TANK_SPEED : Tank.TANK_SPEED);
		double y = deplacedY(location, back ? -Tank.TANK_SPEED : Tank.TANK_SPEED);
		NetworkPlayer.getGame().getPlayer(NetworkPlayer.getNetworkPlayer().getName()).addPrintTexture();
		//TODO base
		
		if (!isCollision((int) x, (int) y)) {
			location.setPos(x, y, 0);
		} else if (!isCollision((int) x, (int) location.getY())) {
			location.setPos(x, location.getY(), 0);
		} else if (!isCollision((int) location.getX(), (int) y)) 
			location.setPos(location.getX(), y, 0);
		
		Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), 
				new PacketPlayerLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch()));
	}
}
