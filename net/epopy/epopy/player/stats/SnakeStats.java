package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class SnakeStats extends AbstractStats {

	public SnakeStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("snake_parties", "0"));
		partiesConfig = "snake_parties";
		
		record = Integer.parseInt(config.getData("snake_record", "0"));
		recordConfig = "snake_record";
		
		temps = Integer.parseInt(config.getData("snake_temps", "0"));
		tempsConfig = "snake_temps";
		
		diminution = 5;
		firstObjectif = 150;
	}

	@Override
	public String getObjectifString() {
		return getObjectif() + " points";
	}
	
}
