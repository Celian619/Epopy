package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class TetrasStats {
	
	private int pts;
	private int parties;
	private int temps;
	private final Config config;

	public TetrasStats(final Config config) {
		this.config = config;
		pts = Integer.parseInt(config.getData("tetras_pts", "0"));
		parties = Integer.parseInt(config.getData("tetras_parties", "0"));
		temps = Integer.parseInt(config.getData("tetras_temps", "0"));
		
	}

	public int getTemps() {
		return temps;
	}

	public void addTemps(final int temps) {
		this.temps += temps;
		config.setValue("tetras_temps", this.temps + "");
	}

	public int getObjectif() {
		return 100;
	}

	public int getParties() {
		return parties;
	}

	public void addPartie() {
		parties++;
		config.setValue("tetras_parties", parties + "");
	}

	public int getRecord() {
		return pts;
	}

	public void setRecord(final int pts) {
		this.pts = pts;
		config.setValue("tetras_pts", pts + "");
	}

}
