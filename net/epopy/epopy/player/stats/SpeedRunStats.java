package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class SpeedRunStats extends AbstractStats {
	
	public SpeedRunStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("speedrun_parties", "0"));
		partiesConfig = "speedrun_parties";
		
		record = Integer.parseInt(config.getData("speedrun_record", "0"));
		recordConfig = "speedrun_record";
		
		temps = Integer.parseInt(config.getData("speedrun_temps", "0"));
		tempsConfig = "speedrun_temps";
		
		diminution = 4;
		firstObjectif = 150;
	}

}
