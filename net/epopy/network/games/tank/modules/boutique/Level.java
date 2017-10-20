package net.epopy.network.games.tank.modules.boutique;

public class Level {

	private int levelId, price;
	
	public Level(int levelId, int price) {
		this.levelId = levelId;
		this.price = price;
	}
	
	public int getLevel() {
		return levelId;
	}
	
	public int getPrice() {
		return price;
	}
	
}
