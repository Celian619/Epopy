package net.epopy.epopy.player.stats;

import java.text.SimpleDateFormat;

import net.epopy.epopy.utils.Config;

public class CarStats {

	private double record;
	private Config config;
	private int parties;
	private long tempsLong;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	public CarStats(Config config) {
		this.config = config;
		record = Double.parseDouble(config.getData("car_record"));
		parties = Integer.parseInt(config.getData("car_parties"));

		tempsLong = Long.parseLong(config.getData("car_temps"));
	}

	public String getTemps() {
		if (tempsLong <= 0)
			return "00:00:00";
		return timeFormat.format(tempsLong - 3600000);
	}

	public void addTemps(long start) {
		tempsLong += start * 1000;
		config.setValue("car_temps", String.valueOf(tempsLong));
	}

	public long getTempsLong() {
		return tempsLong;
	}

	public double getObjectif() {
		return 60.99;// 60 s
	}
	
	public String getObjectifString() {
		return "Finir en moins d'une minute !";
	}
	
	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties = parties + 1;
		config.setValue("car_parties", String.valueOf(parties));
	}

	public double getRecord() {
		return record;
	}

	public String getRecordString() {
		if (record <= 0)
			return "00:00:00";
		return timeFormat.format(record * 1000 - 3600000);
	}

	public void setRecord(double time) {
		record = time;
		config.setValue("car_record", String.valueOf(record));
	}
}
