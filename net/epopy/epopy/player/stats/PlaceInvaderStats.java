package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class PlaceInvaderStats {
	
	private int pts;
	private int parties;
	private int temps;
	private final Config config;

	public PlaceInvaderStats(final Config config) {
		this.config = config;
		pts = Integer.parseInt(config.getData("plainv_pts"));
		parties = Integer.parseInt(config.getData("plainv_parties"));
		
		try {
			temps = Integer.parseInt(config.getData("plainv_temps"));
		} catch (Exception e) {
			temps = (int) (Long.parseLong(config.getData("plainv_temps")) / 1000);
		}
	}

	public int getTemps() {
		return temps;
	}

	public void addTemps(final int temps) {
		this.temps += temps;
		config.setValue("plainv_temps", this.temps + "");
	}

	public int getObjectif() {
		return 100;
	}

	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties++;
		config.setValue("plainv_parties", parties + "");
	}

	public int getRecord() {
		return pts;
	}

	public void setRecord(final int pts) {
		this.pts = pts;
		config.setValue("plainv_pts", pts + "");
	}
}
