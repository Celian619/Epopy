package net.epopy.network.handlers.packets.modules;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.epopy.epopy.display.Textures;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.GameListNetwork;
import net.epopy.network.games.waitingroom.WaitingRoom;
import net.epopy.network.games.waitingroom.modules.WaitingRoomBuilder;
import net.epopy.network.games.waitingroom.modules.WaitingRoomBuilder.WaitingRoomStatus;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.game.PacketPlayerJoin;
import net.epopy.network.utils.Callback;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerWaitingRoom extends PacketAbstract {
	
	public PacketPlayerWaitingRoom() {
	}
	
	public PacketPlayerWaitingRoom(final NetworkPlayer player, final String targetName, final PacketWaitingRoomType packetType) {
		packet.put(player.getName());
		packet.put(targetName);
		packet.put(packetType.name());
		packet.put("false");
		packet.flip();
	}
	
	public PacketPlayerWaitingRoom(final NetworkPlayer player, final String targetName, final PacketWaitingRoomType packetType, final boolean returnMessage) {
		packet.put(player.getName());
		packet.put(targetName);
		packet.put(packetType.name());
		packet.put(String.valueOf(returnMessage));
		packet.flip();
	}
	
	@Override
	public void process(final NetworkPlayerHandlers networkPlayerHandlers, final DataBuffer dataBuffer) {
		PacketWaitingRoomType type = PacketWaitingRoomType.valueOf(dataBuffer.getString().toUpperCase());
		switch (type) {
			case GET:
			WaitingRoomBuilder waitingRoom = WaitingRoom.waitingRoom;
			waitingRoom.clear();
			waitingRoom.setWaitinRoomStatus(WaitingRoomStatus.WAITING);
			waitingRoom.setIdGame(dataBuffer.getInt());
			WaitingRoom.game = GameListNetwork.getGameByID(waitingRoom.getIdGame());
			waitingRoom.setLeader(dataBuffer.getString());
			List<String> playerGetImage = new ArrayList<>(4);
			for (int i = 0; i < 5; i++) {
				String name = dataBuffer.getString();
				if (name == null || name.equals(""))
					break;
				waitingRoom.addPlayer(name);
				if (!WaitingRoom.userProfilTexture.containsKey(name) && !playerGetImage.contains(name))
					playerGetImage.add(name);
			}
			/*
			 * Demande au serveur les textures (profil) des joueurs
			 */
			if (!WaitingRoom.userProfilTexture.containsKey(waitingRoom.getLeader()) && !playerGetImage.contains(waitingRoom.getLeader()))
				playerGetImage.add(waitingRoom.getLeader());
				
			if (playerGetImage.size() > 0) {
				new PacketPlayerServerImage(playerGetImage, new Callback() {
					
					@Override
					public <T> void callback(final T reponse) {

						@SuppressWarnings("unchecked")
						Map<String, Textures> texture = (Map<String, Textures>) reponse;
						WaitingRoom.userProfilTexture.putAll(texture);
					}
				});
			}
				break;
			case GAME_STATUS:
			WaitingRoomStatus waitingRoomStatus = WaitingRoomStatus.valueOf(dataBuffer.getString().toUpperCase());
			WaitingRoom.waitingRoom.setWaitinRoomStatus(waitingRoomStatus);
				break;
			case GAME_IP:
			String data = dataBuffer.getString();
			String ip = data.split(":")[0];
			int port = Integer.parseInt(data.split(":")[1]);
			String team = data.split(":")[2];
			NetworkPlayer.getNetworkPlayer().connectGame(ip, port);
			NetworkPlayer.getGame().clear();
			
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerJoin(team));
						}
					}, 1000);
			new java.util.Timer().schedule(
					new java.util.TimerTask() {
						@Override
						public void run() {
							NetworkPlayer.setGame(GameListNetwork.getGameByID(WaitingRoom.waitingRoom.getIdGame()).getAbstractGame());
						}
					}, 2000);
				break;
			default:
			System.out.println("default");
				break;
		}
		
	}
	
	public enum PacketWaitingRoomType {
		CHANGE_ID_GAME(),
		GET(),
		ADD(),
		START(),
		CANCEL_START(),
		GAME_STATUS(),
		GAME_IP(),
		SEND_INVITE(),
		RECEICED_INVITE(),
		REFUSE_INVITE(),
		ACCEPT_INVITE(),
		REMOVE(),;
	}
	
}
