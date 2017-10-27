package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.PlayerNetwork;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerDirection extends PacketAbstract {

	
	public PacketPlayerDirection() {}
	public String getName() {
		return "PacketPlayerDirection";
	}
	public PacketPlayerDirection(int direction) {
		packet.put(direction);
		packet.flip();
	}
	
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String name = dataBuffer.getString();
		if(NetworkPlayer.getGame().containsPlayer(name)) {
			PlayerNetwork player = NetworkPlayer.getGame().getPlayer(name);	
			player.getLocation().setDirection(dataBuffer.getInt());
		}
	}
}
