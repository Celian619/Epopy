package net.epopy.network;

import net.epopy.network.display.DisplayManager;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.waitingroom.WaitingRoom;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.modules.servermanager.PacketGetIPServers;

public class NetworkPlayer {
	
	private String name;
	private String password;

	private NetworkPlayerHandlers networkPlayerHandlersWaitingRoom;
	private NetworkPlayerHandlers networkPlayerHandlersGame;
	
	private static NetworkPlayer networkPlayer;
	private static AbstractGameNetwork game;
	
	public NetworkPlayer(String name, String password) {
		this.name = name; 	
		this.password = password;
		networkPlayer = this;
		new PacketGetIPServers();
		this.networkPlayerHandlersWaitingRoom = new NetworkPlayerHandlers(this, PacketGetIPServers.MATCH_MAKING_IP, PacketGetIPServers.MATCH_MAKING_PORT, false);
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
	public void connectWaitingRoom(String ip, int port) {
		this.networkPlayerHandlersWaitingRoom = new NetworkPlayerHandlers(this, ip, port, false);
	}
	
	public void connectGame(String ip, int port) {
		this.networkPlayerHandlersGame = new NetworkPlayerHandlers(this, ip, port, true);
	}

	public void setPseudo(String pseudo) {
		this.name = pseudo;
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
	
	public static AbstractGameNetwork setGame(AbstractGameNetwork abstractGameNetwork) {
		abstractGameNetwork.onEnable();
		game = abstractGameNetwork;
		return game;
	}
}
