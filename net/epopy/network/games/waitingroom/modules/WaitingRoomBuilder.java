package net.epopy.network.games.waitingroom.modules;

import java.util.List;

public class WaitingRoomBuilder {

	private String leader;
	private List<String> players;
	private int idGame;
	private WaitingRoomStatus waitingRoomStatus = WaitingRoomStatus.WAITING;
	
	public WaitingRoomBuilder(String leader, List<String> players, int idGame) {
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
	
	public List<String> removePlayer(String player) {
		if(players.contains(player))
			players.remove(player);
		return players;
	}

	public List<String> addPlayer(String player) {
		if(!players.contains(player))
			players.add(player);
		return players;
	}
	
	public String setLeader(String leader) {
		return this.leader = leader;
	}
	
	public void setWaitinRoomStatus(WaitingRoomStatus waitingRoomStatus) {
		this.waitingRoomStatus = waitingRoomStatus;
	}
	
	public int setIdGame(int idGame) {
		return this.idGame = idGame;
	}
	
	public enum WaitingRoomStatus {
		SEARCH(),
		WAITING(),
		MATCH_FOUND(),
	}
	
}
