package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class MarioStats {
	
	@SuppressWarnings("unused")
	private Config config;
	private int etoile;
	private int xp;

	public MarioStats(Config config) {
		this.config = config;
		etoile = Integer.parseInt(config.getData("mario_etoile"));
		xp = Integer.parseInt(config.getData("mario_xp"));
	}

	public int getEtoile() {
		return etoile;
	}

	public int getXp() {
		return xp;
	}

	public int getMaxXp() {
		return 1245;
	}

	public int getMaxEtoile() {
		return 3;
	}

}
