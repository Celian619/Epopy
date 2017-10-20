package net.epopy.network.utils;

public class PlayerStats {

	private int coins = 0;
	private String booster = "null";
	private String playTime = "0h et 0min";
	private String parties = "0/0";
	private boolean hasBooster = false;
	private String rank = "";
	
	public PlayerStats() {}

	public int getCoins() {
		return coins;
	}

	public void setCoins(int coins) {
		this.coins = coins;
	}
	
	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public String getBooster() {
		return booster;
	}

	public boolean hasBooster() {
		return hasBooster;
	}
	
	public void setBooster(String booster) {
		this.booster = booster;
		this.hasBooster = !booster.equals("null");
	}

	public String getPlayTime() {
		return playTime;
	}

	public void setPlayTime(String playTime) {
		this.playTime = playTime;
	}

	public String getParties() {
		return parties;
	}

	public void setParties(String parties) {
		this.parties = parties;
	}




}
