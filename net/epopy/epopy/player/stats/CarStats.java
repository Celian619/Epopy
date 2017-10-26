package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class CarStats {
	
	private int record;
	private final Config config;
	private int parties;
	private int temps;
	
	public CarStats(final Config config) {
		this.config = config;
		
		parties = Integer.parseInt(config.getData("car_parties", "0"));
		
		temps = Integer.parseInt(config.getData("car_temps", "0"));
		
		record = Integer.parseInt(config.getData("car_record", "0"));
		
	}
	
	public void addTemps(final int temps) {
		this.temps += temps;
		config.setValue("car_temps", this.temps + "");
	}
	
	public int getTemps() {
		return temps;
	}
	
	public int getObjectif() {
		return 60;// 60 s
	}

	public String getObjectifString() {
		return "Finir en moins d'une minute !";
	}

	public int getParties() {
		return parties;
	}
	
	public void addPartie() {
		parties++;
		config.setValue("car_parties", parties + "");
	}
	
	public int getRecord() {
		return record;
	}

	public void setRecord(final int time) {
		record = time;
		config.setValue("car_record", record + "");
	}
}
