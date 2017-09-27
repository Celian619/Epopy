package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.tank.TankMenuEnd;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketGameStatus extends PacketAbstract {

	public static String WAITING_MESSAGE = "";

	public PacketGameStatus() {
	}

	public PacketGameStatus(final String name, final String data) {
		packet.put(name);
		packet.put(data);
		packet.flip();
	}

	@Override
	public void process(final NetworkPlayerHandlers networkPlayerHandlers, final DataBuffer dataBuffer) {
		GameStatus gameStatus = GameStatus.valueOf(dataBuffer.getString().toUpperCase());
		if (NetworkPlayer.getGame().getGameStatus() != gameStatus)
			NetworkPlayer.getGame().setGameStatus(gameStatus);
		switch (gameStatus) {
		case WAITING:
			WAITING_MESSAGE = dataBuffer.getString();
			;
			break;
		case IN_GAME:
			// infos pendant le jeu
			break;
		case END:
			String topCoins = dataBuffer.getString();
			String topVictimes = dataBuffer.getString();
			String topPoints = dataBuffer.getString();
			String playerStats = dataBuffer.getString();
			String teamWinner = dataBuffer.getString();

			System.out.println("[GAME-MANAGER] Fin de partie !");
			TankMenuEnd.TEAM_WINNER = NetworkPlayer.getGame().getTeam(teamWinner.toUpperCase());
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
