package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class PingStats {

	private int record;
	private final Config config;
	private int parties;
	private int temps;

	public PingStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("ping_parties"));
		
		record = Integer.parseInt(config.getData("ping_record"));

		temps = Integer.parseInt(config.getData("ping_temps"));

	}

	public void addTemps(final int time) {
		temps += time;
		config.setValue("ping_temps", temps + "");

	}

	public int getRecord() {
		return record;
	}
	
	public void setRecord(final int record) {
		this.record = record;
		config.setValue("ping_record", record + "");
	}

	public int getTemps() {
		return temps;
	}

	public int getObjectif() {
		return 80;// 1 minute 20
	}
	
	public String getObjectifString() {
		return "Tenir plus d'une minute !";
	}
	
	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties++;
		config.setValue("ping_parties", parties + "");
	}

}
