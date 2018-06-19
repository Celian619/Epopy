package net.epopy.network.games.waitingroom.modules;

import java.util.List;

public class WaitingRoomBuilder {
	
	private int idGame;
	private String leader;
	private final List<String> players;
	private WaitingRoomStatus waitingRoomStatus = WaitingRoomStatus.WAITING;

	public WaitingRoomBuilder(final String leader, final List<String> players, final int idGame) {
		this.leader = leader;
		this.players = players;
		this.idGame = idGame;
	}
	
	/**
	 * GETTERS
	 */

	public List<String> getPlayers() {
		return players;
	}
	
	public String getLeader() {
		return leader;
	}
	
	public int getIdGame() {
		return idGame;
	}

	public WaitingRoomStatus getWaitingRoomStatus() {
		return waitingRoomStatus;
	}

	/*
	 * SETTERS
	 */

	public void clear() {
		players.clear();
	}

	public List<String> removePlayer(final String player) {
		if (players.contains(player))
			players.remove(player);
		return players;
	}
	
	public List<String> addPlayer(final String player) {
		if (!players.contains(player))
			players.add(player);
		return players;
	}

	public String setLeader(final String leader) {
		return this.leader = leader;
	}

	public void setWaitinRoomStatus(final WaitingRoomStatus waitingRoomStatus) {
		this.waitingRoomStatus = waitingRoomStatus;
	}

	public int setIdGame(final int idGame) {
		return this.idGame = idGame;
	}

	public enum WaitingRoomStatus {
		SEARCH(),
		WAITING(),
		MATCH_FOUND(),
	}

}
