package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.PlayerNetwork;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerLocation extends PacketAbstract {

	public PacketPlayerLocation() {}
	public String getName() {
		return "PacketPlayerLocation";
	}
	/**public PacketPlayerLocation(double x, double y, double z, double yaw, double pitch) {
		packet.put(x);
		packet.put(y);
		packet.put(z);
		packet.put(yaw);
		packet.put(pitch);
		packet.flip();
	}*/

	public PacketPlayerLocation(double x, double y) {
		packet.put(x);
		packet.put(y);
		//		packet.put(0.0);
		//	packet.put(0.0);
		//packet.put(direction);
		packet.flip();
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String playerName = dataBuffer.getString();
		double x = dataBuffer.getDouble();
		double y = dataBuffer.getDouble();
		//double z = dataBuffer.getDouble();
		//double yaw = dataBuffer.getDouble();
		//double pitch = dataBuffer.getDouble();

		if(NetworkPlayer.getGame().containsPlayer(playerName)) {
			PlayerNetwork player = NetworkPlayer.getGame().getPlayer(playerName);	
			//	if(pitch == -1)
			player.getLocation().set(x, y);
			//	else
			//		player.getLocation().set(x, y, z, yaw, pitch);
			player.addPrintTexture();
		}
	}
}
