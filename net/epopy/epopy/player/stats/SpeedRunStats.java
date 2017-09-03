package net.epopy.epopy.player.stats;

import java.text.SimpleDateFormat;

import net.epopy.epopy.utils.Config;

public class SpeedRunStats {
	
	private long record;
	private Config config;
	private int parties;
	private long tempsLong;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public SpeedRunStats(Config config) {
		this.config = config;
		record = Long.parseLong(config.getData("speedrun_record"));
		parties = Integer.parseInt(config.getData("speedrun_parties"));
		
		tempsLong = Long.parseLong(config.getData("speedrun_temps"));
	}
	
	public String getTemps() {
		if (tempsLong <= 0)
			return "00:00:00";
		return timeFormat.format(tempsLong - 3600000);
	}
	
	public void addTemps(long start) {
		tempsLong += start * 1000;
		config.setValue("speedrun_temps", String.valueOf(tempsLong));
	}
	
	public long getRecord() {
		return record;
	}
	
	public String getRecordString() {
		if (record <= 0)
			return "00:00:00";
		return timeFormat.format(record - 3600000);
	}
	
	public void setRecord(long time) {
		record = time * 1000;
		config.setValue("speedrun_record", String.valueOf(record));
	}
	
	public long getTempsLong() {
		return tempsLong;
	}
	
	public long getObjectif() {
		return 1000 * 60 + 1000*20;//1 minute 20
	}

	public String getObjectifString() {
		return "Tenir plus d'une minute !";
	}

	public int getParties() {
		return parties;
	}
	
	public void addPartie() {
		parties = parties + 1;
		config.setValue("speedrun_parties", String.valueOf(parties));
	}
	
}
