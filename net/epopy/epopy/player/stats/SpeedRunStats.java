package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class SpeedRunStats {

	private int record;
	private final Config config;
	private int parties;
	private int temps;

	public SpeedRunStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("speedrun_parties"));

		if (Integer.parseInt(config.getData("configUpgrade", "0")) == 0) {
			config.setValue("speedrun_record", Integer.parseInt(config.getData("speedrun_record")) / 1000 + "");
			config.setValue("speedrun_temps", Integer.parseInt(config.getData("speedrun_temps")) / 1000 + "");
		}

		record = Integer.parseInt(config.getData("speedrun_record"));

		temps = Integer.parseInt(config.getData("speedrun_temps"));

	}

	public int getTemps() {
		return temps;
	}

	public void addTemps(final int tps) {
		temps += tps;
		config.setValue("speedrun_temps", temps + "");
	}

	public int getRecord() {
		return record;
	}
	
	public void setRecord(final int time) {
		record = time;
		config.setValue("speedrun_record", record + "");
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
		config.setValue("speedrun_parties", parties + "");
	}

}
