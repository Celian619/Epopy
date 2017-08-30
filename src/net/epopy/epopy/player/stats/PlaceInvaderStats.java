package net.epopy.epopy.player.stats;

import java.text.SimpleDateFormat;

import net.epopy.epopy.utils.Config;

public class PlaceInvaderStats {

	private int record;
	private Config config;
	private int parties;
	private long tempsLong;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public PlaceInvaderStats(Config config) {
		this.config = config;
		record = Integer.parseInt(config.getData("placeinvader_record"));
		parties = Integer.parseInt(config.getData("placeinvader_parties"));

		tempsLong = Long.parseLong(config.getData("placeinvader_temps"));
	}

	public String getTemps() {
		if (tempsLong <= 0)
			return "00:00:00";
		return timeFormat.format(tempsLong - 3600000);
	}

	public void addTemps(long start) {
		tempsLong += start * 1000;
		config.setValue("placeinvader_temps", String.valueOf(tempsLong));
	}

	public long getTempsLong() {
		return tempsLong;
	}

	public double getObjectif() {
		return 100;
	}
	
	public String getObjectifString() {
		return "Avoir plus de" + getObjectif() + " points";
	}
	
	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties = parties + 1;
		config.setValue("placeinvader_parties", String.valueOf(parties));
	}

	public int getRecord() {
		return record;
	}

	public String getRecordString() {
		if(record == 0)
			return "0 point";
		return record + " point" +(record < 2 ? "" : "s");
	}

	public void setRecord(int tir) {
		record = tir;
		config.setValue("placeinvader_record", String.valueOf(record));
	}
}
