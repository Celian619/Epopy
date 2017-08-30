package net.epopy.network.handlers.packets.modules;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerFriends extends PacketAbstract {

	
	public PacketPlayerFriends(){}

	public PacketPlayerFriends(NetworkPlayer player, String targetName, PacketFriendsType packetFriendsType) {
		packet.put(player.getName());
		packet.put(targetName);
		packet.put(packetFriendsType.name());
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		PacketFriendsType type = PacketFriendsType.valueOf(dataBuffer.getString().toUpperCase());
		switch(type) {
		case GET:
			for(int i = 0; i < 10; i++) {
				String name = dataBuffer.getString();
				if(name == null)
					break;
				System.out.println("-" + name);
			}
			break;
		case ACCEPT_INVITE:
			System.out.println(dataBuffer.getString() + " accepter l'invite");
			break;
		case REFUSE_INVITE:
			System.out.println(dataBuffer.getString() + " refuser l'invite");
			break;
		case ADD:
			System.out.println("Invite de : " + dataBuffer.getString());
			break;
		case REMOVE:
			System.out.println(dataBuffer.getString() + " vous à remove de ses amis");
			break;
		default:
			break;
		}

	}


	public enum PacketFriendsType {
		GET(),
		ADD(),
		REFUSE_INVITE(),
		ACCEPT_INVITE(),
		REMOVE(),
		;
	}
}
