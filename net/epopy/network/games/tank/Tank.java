package net.epopy.network.games.tank;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.modules.Ball;
import net.epopy.network.games.modules.PlayerNetwork;
import net.epopy.network.games.tank.modules.CalculTank;
import net.epopy.network.games.tank.modules.MapLoader;
import net.epopy.network.games.tank.modules.Zone;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.game.PacketGameStatus;
import net.epopy.network.handlers.packets.modules.game.PacketGameStatus.GameStatus;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerMove;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerShootBall;

public class Tank extends AbstractGameNetwork {

	public static int TANK_SIZE = 25;
	public static int TANK_SPEED = 2;
	public static MapLoader MAP;
	
	private boolean shoot = false;

	private TankMenuEnd tankMenuEnd;

	public Tank() {
		tankMenuEnd = new TankMenuEnd();
	}

	@Override
	public void onEnable() { 
		MAP = new MapLoader("/net/epopy/epopy/display/res/network/games/map-tank-default-game.png");
	}

	@Override
	public void update() {
		PlayerNetwork player = getPlayer(NetworkPlayer.getNetworkPlayer().getName());
		if(getGameStatus().equals(GameStatus.IN_GAME) || getGameStatus().equals(GameStatus.WAITING)) {
			if (player != null ) {
				// rotation du tank
				int rotationSpeed = 5;
				int directionMouse = CalculTank.getDirectionMouse(player.getLocation().getX(), player.getLocation().getY());
				int directionPlayer = (int) player.getLocation().getDirection();

				if (!CalculTank.isMouseDistanceNear(player.getLocation())) {
					if (Math.abs(directionMouse - directionPlayer) <= rotationSpeed) {
						if (player.getLocation().getDirection() != directionMouse) {
							player.getLocation().setDirection(directionMouse);
							Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerMove(directionMouse));
						}
					} else {
						boolean directionInverse = Math.abs(directionMouse - directionPlayer) > 180;
						if (directionPlayer < directionMouse) {
							if (directionInverse) directionPlayer -= rotationSpeed;
							else directionPlayer += rotationSpeed;
						} else {
							if (directionInverse) directionPlayer += rotationSpeed;
							else directionPlayer -= rotationSpeed;
						}
						if (directionPlayer >= 180) directionPlayer -= 360;
						else if (directionPlayer < -180) directionPlayer += 360;
						if (player.getLocation().getDirection() != directionPlayer) {
							player.getLocation().setDirection(directionPlayer);
							Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerMove(directionPlayer));
						}
					}
				}

				
				// inputs
				if (Input.isKeyDown(Keyboard.KEY_S)) 
					CalculTank.moove(true);
				else if (Input.isKeyDown(Keyboard.KEY_Z)) 
					CalculTank.moove(false);
			//		Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerMove(0, 1, player.getLocation().getPitch()));

				if (Input.isButtonDown(0)) {
					if (!shoot) {
						Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerShootBall(player.getLocation()));
						shoot = true;
					}
				} else
					shoot = false;
			}
		} else if(getGameStatus().equals(GameStatus.IN_GAME)) {
			if(tankMenuEnd != null)tankMenuEnd.update();
		}
	}

	@Override
	public void render() {

		getDefaultBackGround().renderBackground();

		for(Zone zone : getZones())
			zone.render();

		for (PlayerNetwork player : getPlayers())
			player.render();

		for (Ball ball : getBalls())
			ComponentsHelper.drawCircle(ball.getLocation().getX(), ball.getLocation().getY(), 5, 10, ball.getColor());
		
		if(getGameStatus().equals(GameStatus.IN_GAME)) {
			ComponentsHelper.drawText(String.valueOf(getTeam("BLUE").getPoints()), AbstractGameMenu.defaultWidth / 2-10, 20, PositionWidth.DROITE, PositionHeight.HAUT, 30, getTeam("BLUE").getColor());
			ComponentsHelper.drawText(String.valueOf(getTeam("RED").getPoints()), AbstractGameMenu.defaultWidth / 2+20, 20, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, getTeam("RED").getColor());	
		} else if(getGameStatus().equals(GameStatus.WAITING)) {
			ComponentsHelper.drawText(PacketGameStatus.WAITING_MESSAGE, AbstractGameMenu.defaultWidth / 2 + 10,  40, PositionWidth.MILIEU, PositionHeight.MILIEU, 18, new float[]{1, 1, 1, 1});
		} else if(getGameStatus().equals(GameStatus.END)) {
			if(tankMenuEnd != null)tankMenuEnd.render();
		}
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_GAME_TANK_MAP;
	}
}
