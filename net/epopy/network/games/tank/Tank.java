package net.epopy.network.games.tank;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

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
import net.epopy.network.handlers.packets.modules.game.PacketPlayerDirection;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerShootBall;

public class Tank extends AbstractGameNetwork {

	public static int TANK_SIZE = 25;
	public static MapLoader MAP;

	private TankMenuEnd tankMenuEnd;
	public static boolean unloadTexture = false;

	private boolean shoot = false;
	public static int balls = CalculTank.getMunitions();

	public Tank() {
		tankMenuEnd = new TankMenuEnd();
	}

	@Override
	public void onEnable() { 

	}

	private int timeReload;
	@Override
	public void update() {
		PlayerNetwork player = getPlayer(NetworkPlayer.getNetworkPlayer().getName());
		if(player != null) {
			Display.setTitle("Epopy - " + player.getName());

			if(getGameStatus().equals(GameStatus.IN_GAME) || getGameStatus().equals(GameStatus.WAITING)) {
				if (player != null ) {
					// rotation du tank
					int rotationSpeed = 5;
					int directionMouse = CalculTank.getDirectionMouse(player.getLocation().getX(), player.getLocation().getY());
					int directionPlayer = player.getLocation().getDirection();

					if (!CalculTank.isMouseDistanceNear(player.getLocation())) {
						if (Math.abs(directionMouse - directionPlayer) <= rotationSpeed) {
							if (player.getLocation().getDirection() != directionMouse) {
								player.getLocation().setDirection(directionMouse);
								Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerDirection(directionMouse));
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
								Packets.sendPacketUDP(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerDirection(directionPlayer));
							}
						}
					}

					// inputs
					if (Input.isKeyDown(Keyboard.KEY_S)) 
						CalculTank.moove(true);
					else if (Input.isKeyDown(Keyboard.KEY_Z)) 
						CalculTank.moove(false);

					if(timeReload <= 0) {
						if(balls > 0) {
							if (Input.isButtonDown(0)) {
								if (!shoot) {
									timeReload = getTankReload();//TODO time tank
									Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerShootBall(player.getLocation()));
									shoot = true;
									balls--;
								}
							} else
								shoot = false;		
						}
					} else 
						timeReload--;

				}
			} else if(getGameStatus().equals(GameStatus.END)) {
				if(tankMenuEnd != null)tankMenuEnd.update();
			}
		}
	}
	
	private static float[] colorReload = new float[]{0, 0, 0, 1};
	@Override
	public void render() {
		if(unloadTexture) {
			Textures.unloadTextures();
			unloadTexture = false;
		}
		
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
			if(balls <= 0) ComponentsHelper.drawText("Retourner à votre base, pour vous recharger en munitions.", 1920/2, 1030, PositionWidth.MILIEU, PositionHeight.HAUT, 30, new float[]{1, 0.1f, 0.1f, 1});
		} else if(getGameStatus().equals(GameStatus.WAITING)) {
			ComponentsHelper.drawText(PacketGameStatus.WAITING_MESSAGE, AbstractGameMenu.defaultWidth / 2 + 10,  40, PositionWidth.MILIEU, PositionHeight.MILIEU, 18, new float[]{1, 1, 1, 1});
			if(balls <= 0) ComponentsHelper.drawText("Retourner à votre base, pour vous recharger en munitions.", 1920/2, 1030, PositionWidth.MILIEU, PositionHeight.HAUT, 30, new float[]{1, 0.1f, 0.1f, 1});
		} else if(getGameStatus().equals(GameStatus.END)) {
			if(tankMenuEnd != null)tankMenuEnd.render();
			else tankMenuEnd = new TankMenuEnd();
		}
	
		
		if(timeReload > 0) {
			PlayerNetwork player = getPlayer(NetworkPlayer.getNetworkPlayer().getName());
			if(player != null) {
				double y = player.getLocation().getY()-40;
				ComponentsHelper.drawLine(player.getLocation().getX(), y, player.getLocation().getX() + timeReload, y, 2, colorReload);
				ComponentsHelper.drawLine(player.getLocation().getX(), y, player.getLocation().getX() - timeReload, y, 2, colorReload);
			}
		}
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_GAME_TANK_MAP;
	}

	public int getTankReload() {
		int level = TankBoutique.LEVEL_CANON;
		if(level == 0) 
			return 50;
		if(level == 1) 
			return 45;
		if(level == 2) 
			return 40;
		if(level == 3) 
			return 35;
		if(level == 4) 
			return 30;
		return 50;
	}

}
