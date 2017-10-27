package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Team;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketTeamPoints extends PacketAbstract {

	public PacketTeamPoints(){}
	public String getName() {
		return "PacketTeamPoints";
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		Team team = NetworkPlayer.getGame().getTeam(dataBuffer.getString());
		team.setPoints(dataBuffer.getInt());
	}
}
