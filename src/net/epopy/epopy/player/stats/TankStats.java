package net.epopy.epopy.player.stats;

import java.text.SimpleDateFormat;

import net.epopy.epopy.utils.Config;

public class TankStats {

	private int record;
	private Config config;
	private int parties;
	private long tempsLong;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public TankStats(Config config) {
		this.config = config;
		record = Integer.parseInt(config.getData("tank_record"));
		parties = Integer.parseInt(config.getData("tank_parties"));

		tempsLong = Long.parseLong(config.getData("tank_temps"));
	}

	public String getTemps() {
		if (tempsLong <= 0)
			return "00:00:00";
		return timeFormat.format(tempsLong - 3600000);
	}

	public void addTemps(long start) {
		tempsLong += start * 1000;
		config.setValue("tank_temps", String.valueOf(tempsLong));
	}

	public long getTempsLong() {
		return tempsLong;
	}

	public int getObjectif() {
		return 10;//  10 tirs sur le bot
	}
	
	public String getObjectifString() {
		return "Tuer plus de " + getObjectif() + " fois le bot";
	}
	
	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties = parties + 1;
		config.setValue("tank_parties", String.valueOf(parties));
	}

	public int getRecord() {
		return record;
	}

	public String getRecordString() {
		if(record == 0)
			return "?";
		return "Tué " + record + " fois";
	}

	public void setRecord(int tir) {
		record = tir;
		config.setValue("tank_record", String.valueOf(record));
	}
}
