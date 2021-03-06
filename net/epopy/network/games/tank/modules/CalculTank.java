package net.epopy.network.games.tank.modules;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Location3D;
import net.epopy.network.games.modules.PlayerNetwork;
import net.epopy.network.games.modules.Team;
import net.epopy.network.games.tank.Tank;
import net.epopy.network.games.tank.TankBoutique;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerLocation;

public class CalculTank {

	public static int getDirectionMouse(final double x, final double y) {
		double Xa = Mouse.getX() / (double) Display.getWidth() * AbstractGameMenu.defaultWidth - x;
		double Ya = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * AbstractGameMenu.defaultHeight - y;
		return (int) ((Ya < 0 ? -1 : 1) * Math.toDegrees(Math.acos(Xa / Math.sqrt(Xa * Xa + Ya * Ya))));
	}

	public static boolean isMouseDistanceNear(final Location3D location) {
		double Xa = Mouse.getX() / (double) Display.getWidth() * AbstractGameMenu.defaultWidth;
		double Ya = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * AbstractGameMenu.defaultHeight;
		Location3D locMouse = new Location3D(Xa, Ya, 0.0, 0.0, 0.0);

		return distance(locMouse, location) <= 25;// TODO changer par la suite la taille du tank 25 = taille
	}

	public static double distance(final Location3D player, final Location3D target) {
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

	public static boolean intersects(final Rectangle rect1, final Rectangle rect2) {
		
		double right1 = rect1.getX() + rect1.getWidth();
		double bottom1 = rect1.getY() + rect1.getHeight();
		double right2 = rect2.getX() + rect2.getWidth();
		double bottom2 = rect2.getY() + rect2.getHeight();

		return rect1.getX() < right2
				&& rect1.getY() < bottom2
				&& right1 > rect2.getX()
				&& bottom1 > rect2.getY();
	}

	public static boolean isCollisionTank(final int x, final int y, final PlayerNetwork target) {
		return intersects(new Rectangle(x, y, 58, 50), new Rectangle((int) target.getLocation().getX(), (int) target.getLocation().getY(), 64, 56));
	}

	public static void moove(final boolean back) {
		PlayerNetwork player = NetworkPlayer.getGame().getPlayer(NetworkPlayer.getNetworkPlayer().getName());
		Location3D location = player.getLocation();

		double speed = 1.8;
		int level = TankBoutique.LEVEL_VITESSE;

		switch (level) {
			case 0:
			speed = 1.8;
				break;
			case 1:
			speed = 1.9;
				break;
			case 2:
			speed = 2.1;
				break;
			case 3:
			speed = 2.2;
				break;
			case 4:
			speed = 2.3;
				break;
		}
		
		double x = deplacedX(location, back ? -speed : speed);
		double y = deplacedY(location, back ? -speed : speed);

		player.addPrintTexture();

		Team team = player.getTeam();

		boolean isInBase = false;
		if (team.getName().equals("RED")) {// TODO revoir pour les autres map
			if (x < 334 && y < 285)
				return;
			else if (x > 1621 && y > 730) isInBase = true;
		} else if (team.getName().equals("BLUE")) {
			if (x > 1621 && y > 730)
				return;
			else if (x < 334 && y < 285) isInBase = true;
		}

		if (isInBase) {
			if (Tank.balls < getMunitions())
				Tank.balls = getMunitions();
		}
		boolean b = !hasCollisionTank(player, (int) x, (int) y);
		
		if (!isCollision((int) x, (int) y)) {
			if (!isInBase)
				if (b) location.setPos(x, y, 0);
			else location.setPos(x, y, 0);
		} else if (!isCollision((int) x, (int) location.getY())) {
			if (!isInBase)
				if (b) location.setPos(x, location.getY(), 0);
			else location.setPos(x, location.getY(), 0);
		} else if (!isCollision((int) location.getX(), (int) y)) {
			if (!isInBase)
				if (b) location.setPos(location.getX(), y, 0);
			else location.setPos(location.getX(), y, 0);
		}

		Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerLocation(location.getX(), location.getY()));
	}

	private static boolean hasCollisionTank(final PlayerNetwork player, final int x, final int y) {
		for (PlayerNetwork target : NetworkPlayer.getGame().getPlayers()) {
			if (target != player) {
				if (!isCollisionTank(x, y, target)) {
					// ok pas de collision
				} else if (!isCollisionTank(x, (int) player.getLocation().getY(), target)) {
					player.getLocation().setPos(x, player.getLocation().getY(), 0);
					return true;
				} else if (!isCollisionTank((int) player.getLocation().getX(), y, target)) {
					player.getLocation().setPos(player.getLocation().getX(), y, 0);
					return true;
				} else return true;
			}
		}
		return false;
	}
	
	public static int getMunitions() {
		switch (TankBoutique.LEVEL_MUNITIONS) {
			case 0:
			return 4;
			case 1:
			return 7;
			case 2:
			return 10;
			case 3:
			return 13;
			case 4:
			return 16;
			default:
			return 4;
		}
		
	}
}
