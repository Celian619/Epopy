package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class PingStats extends AbstractStats {
	
	public PingStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("ping_parties", "0"));
		partiesConfig = "ping_parties";
		
		record = Integer.parseInt(config.getData("ping_record", "0"));
		recordConfig = "ping_record";
		
		temps = Integer.parseInt(config.getData("ping_temps", "0"));
		tempsConfig = "ping_temps";
		
		diminution = 2;
		firstObjectif = 90;
	}

}
