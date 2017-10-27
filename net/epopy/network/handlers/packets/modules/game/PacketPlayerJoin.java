package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.modules.Location3D;
import net.epopy.network.games.modules.Team;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerJoin extends PacketAbstract {

	public PacketPlayerJoin() {}

	public PacketPlayerJoin(String team) {
		packet.put(team);
		packet.flip();
	}
	public String getName() {
		return "PacketPlayerJoin";
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String playerName = dataBuffer.getString();
		String playerTeamName = dataBuffer.getString();
		String teamName = dataBuffer.getString();
		AbstractGameNetwork game = NetworkPlayer.getGame();
		//ajoute la teams
		if(!teamName.equals("null")) {
			if(!game.containsTeam(teamName)) {
				double x = dataBuffer.getDouble();
				double y = dataBuffer.getDouble();
				double z = dataBuffer.getDouble();
				double pitch = dataBuffer.getDouble();
				double yaw = dataBuffer.getDouble();
				float[] color = new float[]{dataBuffer.getFloat(),  dataBuffer.getFloat(), dataBuffer.getFloat(), 1};
				game.addTeam(teamName, new Team(teamName, color, new Location3D(x, y, z, yaw, pitch)));
				System.out.println("[Server - Team] Register team: " + teamName);
			} else 	System.out.println("[Server - Team] Team alreay register: " + teamName);
		}
		if(game.containsTeam(playerTeamName) && !game.containsPlayer(playerTeamName)) {
			game.addPlayer(playerName, playerTeamName, dataBuffer.getInt());
			System.out.println("[Server - Team] " + playerName + " join -> " + playerTeamName);
		}
	}
}
