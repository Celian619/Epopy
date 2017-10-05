package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class SnakeStats {
	
	private int pts;
	private final Config config;
	private int parties;
	private int temps;
	
	public SnakeStats(final Config config) {
		this.config = config;
		pts = Integer.parseInt(config.getData("snake_pts"));
		parties = Integer.parseInt(config.getData("snake_parties"));
		
		if (Integer.parseInt(config.getData("configUpgrade", "0")) == 0) {
			config.setValue("snake_temps", Integer.parseInt(config.getData("snake_temps")) / 1000 + "");
		}
		
		temps = Integer.parseInt(config.getData("snake_temps"));

	}
	
	public int getTemps() {

		return temps;
	}
	
	public void addTemps(final int temps) {
		this.temps += temps;
		config.setValue("snake_temps", this.temps + "");
	}
	
	public int getObjectif() {
		return 100;
	}
	
	public int getParties() {
		return parties;
	}
	
	public void addPartie() {
		parties++;
		config.setValue("snake_parties", parties + "");
	}
	
	public int getRecord() {
		return pts;
	}
	
	public void setRecord(final int pts) {
		this.pts = pts;
		config.setValue("snake_pts", pts + "");
	}
}
