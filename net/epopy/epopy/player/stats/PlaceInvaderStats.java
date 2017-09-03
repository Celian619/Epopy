package net.epopy.epopy.player.stats;

import java.text.SimpleDateFormat;

import net.epopy.epopy.utils.Config;

public class PlaceInvaderStats {
	
	private int pts;
	private int parties;
	private long tempsLong;
	private Config config;
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");;

	public PlaceInvaderStats(Config config) {
		this.config = config;
		this.pts = Integer.parseInt(config.getData("plainv_pts"));
		this.parties = Integer.parseInt(config.getData("plainv_parties"));
		this.tempsLong = Long.parseLong(config.getData("plainv_temps"));
	}

	public String getTemps() {
		if (tempsLong <= 0)
			return "00:00:00";
		return timeFormat.format(tempsLong - 3600000);
	}

	public void addTemps(long start) {
		this.tempsLong += start * 1000;
		config.setValue("plainv_temps", String.valueOf(tempsLong));
	}

	public int getObjectif() {
		return 100;
	}

	public int getParties() {
		return parties;
	}

	public void addPartie() {
		this.parties = parties + 1;
		config.setValue("plainv_parties", String.valueOf(parties));
	}

	public int getRecord() {
		return pts;
	}

	public void setRecord(int pts) {
		this.pts = pts;
		config.setValue("plainv_pts", String.valueOf(pts));
	}
}
