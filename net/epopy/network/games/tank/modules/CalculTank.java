package net.epopy.network.games.tank.modules;

import java.awt.geom.Rectangle2D;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

import de.matthiasmann.twl.Rect;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Location3D;
import net.epopy.network.games.modules.PlayerNetwork;
import net.epopy.network.games.modules.Team;
import net.epopy.network.games.tank.Tank;
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

	public static void moove(final boolean back) {
		PlayerNetwork player = NetworkPlayer.getGame().getPlayer(NetworkPlayer.getNetworkPlayer().getName());
		Location3D location = player.getLocation();

		double x = deplacedX(location, back ? -Tank.TANK_SPEED : Tank.TANK_SPEED);
		double y = deplacedY(location, back ? -Tank.TANK_SPEED : Tank.TANK_SPEED);

		player.addPrintTexture();

		Team team = player.getTeam();

		for(PlayerNetwork target : NetworkPlayer.getGame().getPlayers()) {
			if(target != player) {
				//if(new Rectangle((int)x, (int)y, 58, 50).intersects(new Rectangle((int)target.getLocation().getX(), (int)target.getLocation().getY(), 64, 56)))
				if(distance(new Location3D(x, y, 0, 0, 0), target.getLocation()) < 62)	
					return;
			}
		}

		if(team.getName().equals("RED")) {//TODO revoir pour les autres map
			if(x < 334 && y < 285) 
				return;
		} else if(team.getName().equals("BLUE")) {
			if(x > 1621 && y > 730) 
				return;
		}

		if (!isCollision((int) x, (int) y)) {
			location.setPos(x, y, 0);
		} else if (!isCollision((int) x, (int) location.getY())) {
			location.setPos(x, location.getY(), 0);
		} else if (!isCollision((int) location.getX(), (int) y))
			location.setPos(location.getX(), y, 0);

		Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), 0.0));
	}
}
