package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketTeamCaptureZone extends PacketAbstract {

	public PacketTeamCaptureZone(){}
	public String getName() {
		return "PacketTeamCaptureZone";
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		int zoneId = dataBuffer.getInt();
		float[] color = new float[]{dataBuffer.getFloat(), dataBuffer.getFloat(), dataBuffer.getFloat(), dataBuffer.getFloat()};
		AbstractGameNetwork game = NetworkPlayer.getGame();
		System.out.println("REcu :" + zoneId);
		if(game.containsZone(zoneId))
			game.getZone(zoneId).setColor(color);
		else
			game.addZone(zoneId);
	}
}
