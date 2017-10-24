package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerLeave extends PacketAbstract {

	public PacketPlayerLeave() {
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String playerName = dataBuffer.getString();
		System.out.print(playerName);
		AbstractGameNetwork game = NetworkPlayer.getGame();
		if(game != null && game.containsPlayer(playerName)) 
			game.removePlayer(playerName);
	}
}
