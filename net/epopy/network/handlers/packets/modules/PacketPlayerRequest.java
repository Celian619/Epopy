package net.epopy.network.handlers.packets.modules;

import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.network.games.waitingroom.AddPlayersMenu;
import net.epopy.network.games.waitingroom.WaitingRoom;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.PacketPlayerWaitingRoom.PacketWaitingRoomType;
import net.epopy.network.utils.Callback;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerRequest extends PacketAbstract {

	public PacketPlayerRequest() {}
	public String getName() {
		return "PacketPlayerRequest";
	}
	public PacketPlayerRequest(String senderName, String targetName, RequestType type, boolean reponse) {
		packet.put(senderName);
		packet.put(targetName);
		packet.put(type.name());
		packet.put(String.valueOf(reponse));
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String senderName = dataBuffer.getString();
		String targetName = dataBuffer.getString();
		PacketWaitingRoomType packetType = PacketWaitingRoomType.valueOf(dataBuffer.getString().toUpperCase());
		RequestType type = RequestType.valueOf(dataBuffer.getString().toUpperCase());
		String title = type.equals(RequestType.FRIENDS) ? "FRIENDS" : "WAITING ROOM";

		switch (packetType) {
		case ADD:
			NotificationGui notif = new NotificationGui(title, type.equals(RequestType.FRIENDS) ? senderName + " vous a envoyé une invitation !" : senderName + " vous à invité", 99999, new float[]{1, 1, 1, 1}, false);
			notif.setCallbackReponse(new Callback() {
				@Override
				public <T> void callback(T reponse) {
					boolean accept = (boolean)reponse;
					Packets.sendPacket(networkPlayerHandlers, new PacketPlayerRequest(senderName, targetName, type, accept));
					if(accept && type.equals(RequestType.WAITING_ROOM)) {
						WaitingRoom.tChat.clear();
						WaitingRoom.showAddPlayersMenu = false;
						AddPlayersMenu.reset();
					}
				}
			});
			break;
		default:
			System.out.println("Request: " + senderName +"  " + targetName +"  " + type);
			break;
		}

	}
	/*
	 * ENUM
	 */
	public enum RequestType {
		FRIENDS(),
		WAITING_ROOM();
	}
}
