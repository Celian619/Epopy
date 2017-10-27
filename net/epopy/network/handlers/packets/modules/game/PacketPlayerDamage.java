package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.PlayerNetwork;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerDamage extends PacketAbstract {

	public PacketPlayerDamage() {
		packet.flip();
	}
	public String getName() {
		return "PacketPlayerDamage";
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String playerName = dataBuffer.getString();
		int hp = dataBuffer.getInt();
		
		if(NetworkPlayer.getGame().containsPlayer(playerName)) {
			PlayerNetwork player = NetworkPlayer.getGame().getPlayer(playerName);	
			player.setHP(hp);
		}
		
	}
}
