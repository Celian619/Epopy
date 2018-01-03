package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class TetrasStats extends AbstractStats {
	
	public TetrasStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("tetras_parties", "0"));
		partiesConfig = "tetras_parties";
		
		record = Integer.parseInt(config.getData("tetras_pts", "0"));
		recordConfig = "tetras_pts";
		
		temps = Integer.parseInt(config.getData("tetras_temps", "0"));
		tempsConfig = "tetras_temps";
		
		diminution = 5;
		firstObjectif = 150;
	}

	@Override
	public String getObjectifString() {
		return getObjectif() + " points";
	}
}
