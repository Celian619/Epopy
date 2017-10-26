package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class TankStats {

	private int record;
	private final Config config;
	private int parties;
	private int temps;

	public TankStats(final Config config) {
		this.config = config;
		record = Integer.parseInt(config.getData("tank_record"));
		parties = Integer.parseInt(config.getData("tank_parties"));
		
		temps = Integer.parseInt(config.getData("tank_temps"));

	}

	public int getTemps() {
		return temps;
	}

	public void addTemps(final int temps) {
		this.temps += temps;
		config.setValue("tank_temps", this.temps + "");
	}
	
	public int getObjectif() {
		return 10;// 10 tirs sur le bot
	}
	
	public String getObjectifString() {
		return "Tuer plus de " + getObjectif() + " fois le bot";
	}
	
	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties++;
		config.setValue("tank_parties", parties + "");
	}

	public int getRecord() {
		return record;
	}

	public String getRecordString() {
		return "Tué " + record + " fois";
	}

	public void setRecord(final int tir) {
		record = tir;
		config.setValue("tank_record", tir + "");
	}
}
