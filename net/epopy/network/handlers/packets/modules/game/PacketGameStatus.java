package net.epopy.network.handlers.packets.modules.game;

import net.epopy.epopy.Main;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Location3D;
import net.epopy.network.games.tank.TankMenuEnd;
import net.epopy.network.games.waitingroom.WaitingRoom;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.utils.DataBuffer;

public class PacketGameStatus extends PacketAbstract {

	public static String WAITING_MESSAGE = "";

	public PacketGameStatus() {
	}
	public String getName() {
		return "PacketGameStatus";
	}
	public PacketGameStatus(final String name, final String data) {
		packet.put(name);
		packet.put(data);
		packet.flip();
	}

	@Override
	public void process(final NetworkPlayerHandlers networkPlayerHandlers, final DataBuffer dataBuffer) {
		GameStatus gameStatus = GameStatus.valueOf(dataBuffer.getString().toUpperCase());

		NetworkPlayer.getGame().setGameStatus(gameStatus);

		switch (gameStatus) {
		case WAITING:
			WAITING_MESSAGE = dataBuffer.getString();
			if(WAITING_MESSAGE.equals("Lancement dans 00:02")) {
				// infos pendant le jeu
				Location3D location = NetworkPlayer.getGame().getPlayer(NetworkPlayer.getNetworkPlayer().getName()).getLocation();
				Location3D teamLoc =  NetworkPlayer.getGame().getPlayer(NetworkPlayer.getNetworkPlayer().getName()).getTeam().getSpawnLocation();
				location.set(teamLoc.copy());
				Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersGame(), new PacketPlayerLocation(location.getX(), location.getY()));
			}
			break;
		case IN_GAME:
			break;
		case END:
			String topCoins = dataBuffer.getString();
			String topVictimes = dataBuffer.getString();
			String topPoints = dataBuffer.getString();
			String playerStats = dataBuffer.getString();
			String teamWinner = dataBuffer.getString();

			System.out.println("[GAME-MANAGER] Fin de partie !");
			if(!teamWinner.equals("null"))
				TankMenuEnd.TEAM_WINNER = NetworkPlayer.getGame().getTeam(teamWinner.toUpperCase());
			else
				TankMenuEnd.TEAM_WINNER = null;
			TankMenuEnd.TOP_COINS = new String[] { topCoins.split(":")[0], topCoins.split(":")[1], topCoins.split(":")[2] };
			TankMenuEnd.TOP_VICTIMES = new String[] { topVictimes.split(":")[0], topVictimes.split(":")[1], topVictimes.split(":")[2] };
			TankMenuEnd.TOP_POINTS = new String[] { topPoints.split(":")[0], topPoints.split(":")[1], topPoints.split(":")[2] };
			TankMenuEnd.TOP_POINTS = new String[] { topPoints.split(":")[0], topPoints.split(":")[1], topPoints.split(":")[2] };
			TankMenuEnd.PLAYER_KILLS = Integer.parseInt(playerStats.split(":")[0]);
			TankMenuEnd.PLAYER_MORT = Integer.parseInt(playerStats.split(":")[1]);
			TankMenuEnd.PLAYER_COINS = Integer.parseInt(playerStats.split(":")[2]);
			TankMenuEnd.PLAYER_POINTS = Integer.parseInt(playerStats.split(":")[3]);

			break;
		default:
			break;
		}
	}

	public enum GameStatus {
		WAITING(),
		IN_GAME(),
		END(),
	}
}
