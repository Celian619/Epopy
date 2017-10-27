package net.epopy.network.handlers.packets.modules;

import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.modules.TChat;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerTChat extends PacketAbstract {
	
	public PacketPlayerTChat() {}
	public String getName() {
		return "PacketPlayerTChat";
	}
	
	public PacketPlayerTChat(String sender, String message, PacketTChatType packetTChatType) {
		packet.put(sender);
		packet.put("ALL");
		packet.put(message);
		packet.put(packetTChatType.name());
		packet.flip();
	}
	public PacketPlayerTChat(String sender, String target, String message, PacketTChatType packetTChatType) {
		packet.put(sender);
		packet.put(target);
		packet.put(message);
		packet.put(packetTChatType.name());
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String senderName = dataBuffer.getString();
		String targetName = dataBuffer.getString();
		String message = dataBuffer.getString();
		PacketTChatType type = PacketTChatType.valueOf(dataBuffer.getString().toUpperCase());
	/**	switch (type) {
		case PRIVATE:
			break;
		case WAITING_ROOM:
			WaitingRoom.tChat.addMessage(senderName, message);
			break;
		case GAME:
			break;
		default:
			System.out.println("[TChat] Sender:" + senderName + " Target:" + targetName + " Message:" + message);
			break;
		}*/
		if(type == PacketTChatType.WAITING_ROOM) 
			TChat.addMessage(senderName, message);
	}

	public enum PacketTChatType {
		WAITING_ROOM(),
		PRIVATE(),
		GAME(),
	}
}
