package net.epopy.network;

import net.epopy.network.display.DisplayManager;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.waitingroom.WaitingRoom;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.modules.servermanager.PacketGetIPServers;
import net.epopy.network.utils.PlayerStats;

public class NetworkPlayer {
	
	private static NetworkPlayer networkPlayer;
	private static AbstractGameNetwork game;
	
	private final PlayerStats tankStats = new PlayerStats();
	private final String password;
	
	private String name;
	private NetworkPlayerHandlers networkPlayerHandlersWaitingRoom, networkPlayerHandlersGame;
	
	public NetworkPlayer(final String name, final String password) {
		this.name = name;
		this.password = password;
		networkPlayer = this;
		new PacketGetIPServers();
		networkPlayerHandlersWaitingRoom = new NetworkPlayerHandlers(this, PacketGetIPServers.MATCH_MAKING_IP, PacketGetIPServers.MATCH_MAKING_PORT, false);
	}

	/*
	 * -- GETTERS --
	 */

	/**
	 * Donne le password du joueur
	 *
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	/*
	 * Donne les stats du tank
	 */
	public PlayerStats getTankStats() {
		return tankStats;
	}
	
	/**
	 * Donne le nom du joueur
	 *
	 * @return string < 16
	 */
	public String getName() {
		return name;
	}

	public NetworkPlayerHandlers getNetworkPlayerHandlersWaitingRoom() {
		return networkPlayerHandlersWaitingRoom;
	}

	public NetworkPlayerHandlers getNetworkPlayerHandlersGame() {
		return networkPlayerHandlersGame;
	}

	/**
	 * -- Fonctions & Setters --
	 *
	 */
	public void connectWaitingRoom(final String ip, final int port) {
		networkPlayerHandlersWaitingRoom = new NetworkPlayerHandlers(this, ip, port, false);
	}
	
	public void connectGame(final String ip, final int port) {
		networkPlayerHandlersGame = new NetworkPlayerHandlers(this, ip, port, true);
	}

	public void setPseudo(final String pseudo) {
		name = pseudo;
	}
	
	public void init() {
		setGame(new WaitingRoom());
		new DisplayManager();
	}
	
	/**
	 * STATIC
	 */
	public static NetworkPlayer getNetworkPlayer() {
		return networkPlayer;
	}
	
	public static AbstractGameNetwork getGame() {
		return game;
	}
	
	public static AbstractGameNetwork setGame(final AbstractGameNetwork abstractGameNetwork) {
		abstractGameNetwork.onEnable();
		game = abstractGameNetwork;
		return game;
	}
}
