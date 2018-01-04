package net.epopy.network.handlers.packets;

import java.util.Map;
import java.util.TreeMap;

import net.epopy.network.Logger;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.modules.PacketPlayerDisconnect;
import net.epopy.network.handlers.packets.modules.PacketPlayerFriends;
import net.epopy.network.handlers.packets.modules.PacketPlayerLogin;
import net.epopy.network.handlers.packets.modules.PacketPlayerMessage;
import net.epopy.network.handlers.packets.modules.PacketPlayerRequest;
import net.epopy.network.handlers.packets.modules.PacketPlayerShopBuy;
import net.epopy.network.handlers.packets.modules.PacketPlayerStats;
import net.epopy.network.handlers.packets.modules.PacketPlayerStatsNetwork;
import net.epopy.network.handlers.packets.modules.PacketPlayerTChat;
import net.epopy.network.handlers.packets.modules.PacketPlayerWaitingRoom;
import net.epopy.network.handlers.packets.modules.game.PacketGameStatus;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerDamage;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerDirection;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerJoin;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerLeave;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerLocation;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerShootBall;
import net.epopy.network.handlers.packets.modules.game.PacketTeamCaptureZone;
import net.epopy.network.handlers.packets.modules.game.PacketTeamPoints;

public class Packets {

	public static int MAX_SIZE = 512;
	private static Map<String, Class<?>> packets;

	public Packets() {
		if (packets == null) {
			packets = new TreeMap<>();
			packets.put("PacketPlayerLogin", PacketPlayerLogin.class);
			packets.put("PacketPlayerFriends", PacketPlayerFriends.class);
			packets.put("PacketPlayerWaitingRoom", PacketPlayerWaitingRoom.class);
			packets.put("PacketPlayerRequest", PacketPlayerRequest.class);
			packets.put("PacketPlayerMessage", PacketPlayerMessage.class);
			packets.put("PacketPlayerDisconnect", PacketPlayerDisconnect.class);
			packets.put("PacketPlayerTChat", PacketPlayerTChat.class);
			packets.put("PacketPlayerStatsNetwork", PacketPlayerStatsNetwork.class);
			packets.put("PacketPlayerStats", PacketPlayerStats.class);
			packets.put("PacketPlayerShopBuy", PacketPlayerShopBuy.class);
			
			// Game
			packets.put("PacketPlayerDirection", PacketPlayerDirection.class);
			packets.put("PacketPlayerLocation", PacketPlayerLocation.class);
			packets.put("PacketPlayerJoin", PacketPlayerJoin.class);
			packets.put("PacketPlayerLeave", PacketPlayerLeave.class);
			packets.put("PacketPlayerShootBall", PacketPlayerShootBall.class);
			packets.put("PacketTeamPoints", PacketTeamPoints.class);
			packets.put("PacketTeamCaptureZone", PacketTeamCaptureZone.class);
			packets.put("PacketGameStatus", PacketGameStatus.class);
			packets.put("PacketPlayerDamage", PacketPlayerDamage.class);
			
			Logger.info("[Packets] init packet (" + packets.size() + ")");
		}
	}

	public static void sendPacket(final NetworkPlayerHandlers clientHandlers, final PacketAbstract packetAbstract) {
		/**if (packetAbstract.getPacket().getData().length > 0 && clientHandlers != null && clientHandlers.getDataOutputStream() != null) {
			try {
				clientHandlers.getDataOutputStream().write(packetAbstract.getPacket().getData());
				clientHandlers.getDataOutputStream().flush();
				// Logger.info("[Packet] Type: send || Name: " + packetAbstract.getName());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}*/
		clientHandlers.send(packetAbstract);
	}

	public static void sendPacketUDP(final NetworkPlayerHandlers clientHandlers, final PacketAbstract packetAbstract) {
		if (packetAbstract.getPacket().getData().length > 0 && clientHandlers != null && clientHandlers.getDataOutputStream() != null)
			clientHandlers.getServerUDP().send(packetAbstract.getPacket().getData());
	}

	public static PacketAbstract getPacket(final String name) {
		if (packets.containsKey(name)) {
			try {
				return (PacketAbstract) packets.get(name).newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

}
