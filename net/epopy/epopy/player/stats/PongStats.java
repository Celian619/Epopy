package net.epopy.epopy.player.stats;

import java.text.SimpleDateFormat;

import net.epopy.epopy.utils.Config;

public class PongStats {

	private long record;
	private final Config config;
	private int parties;
	private long tempsLong;
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public PongStats(final Config config) {
		this.config = config;
		record = Long.parseLong(config.getData("ping_record"));
		parties = Integer.parseInt(config.getData("ping_parties"));

		tempsLong = Long.parseLong(config.getData("ping_temps"));
	}

	public String getTemps() {
		if (tempsLong <= 0)
			return "00:00:00";
		return timeFormat.format(tempsLong - 3600000);
	}

	public void addTemps(final long start) {
		tempsLong += start * 1000;
		config.setValue("ping_temps", String.valueOf(tempsLong));
	}

	public long getRecord() {
		return record;
	}

	public String getRecordString() {
		if (record <= 0)
			return "00:00:00";
		return timeFormat.format(record - 3600000);
	}

	public void setRecord(final long time) {
		record = time * 1000;
		config.setValue("ping_record", String.valueOf(record));
	}

	public long getTempsLong() {
		return tempsLong;
	}

	public long getObjectif() {
		return 1000 * 60 + 1000 * 20;// 1 minute 20
	}
	
	public String getObjectifString() {
		return "Tenir plus d'une minute !";
	}
	
	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties = parties + 1;
		config.setValue("ping_parties", String.valueOf(parties));
	}

}
