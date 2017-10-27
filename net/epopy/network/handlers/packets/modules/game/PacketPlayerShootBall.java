package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.modules.Location3D;
import net.epopy.network.games.modules.Team;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerShootBall extends PacketAbstract {

	public PacketPlayerShootBall() {}
	public String getName() {
		return "PacketPlayerShootBall";
	}
	public PacketPlayerShootBall(Location3D location) {
		packet.put(location.getX());
		packet.put(location.getY());
		packet.put(location.getZ());
		packet.put(location.getYaw());
		packet.put(location.getDirection());//TODO mmm
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		Team team = NetworkPlayer.getGame().getTeam(dataBuffer.getString().toUpperCase());
		String name = dataBuffer.getString();
		double x = dataBuffer.getDouble();
		double y = dataBuffer.getDouble();
		AbstractGameNetwork game = NetworkPlayer.getGame();
		if(x != -1.0 && team != null) 
			game.updateBall(name, team.getColor(), new Location3D(x, y, 0, 0, 0));
		else {
			game.removeBall(name);
		}
	}
}
