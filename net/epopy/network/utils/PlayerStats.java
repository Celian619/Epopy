package net.epopy.network.utils;

public class PlayerStats {
	
	private boolean hasBooster;
	private int coins;
	private String booster = "null", playTime = "0h et 0min", parties = "0/0", rank = "";

	public PlayerStats() {
	}
	
	public int getCoins() {
		return coins;
	}
	
	public void setCoins(final int coins) {
		this.coins = coins;
	}

	public String getRank() {
		return rank;
	}
	
	public void setRank(final String rank) {
		this.rank = rank;
	}
	
	public String getBooster() {
		return booster;
	}
	
	public boolean hasBooster() {
		return hasBooster;
	}

	public void setBooster(final String booster) {
		this.booster = booster;
		hasBooster = !booster.equals("null");
	}
	
	public String getPlayTime() {
		return playTime;
	}
	
	public void setPlayTime(final String playTime) {
		this.playTime = playTime;
	}
	
	public String getParties() {
		return parties;
	}
	
	public void setParties(final String parties) {
		this.parties = parties;
	}
	
}
