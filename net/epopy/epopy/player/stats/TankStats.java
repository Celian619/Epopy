package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class TankStats extends AbstractStats {
	
	public TankStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("tank_parties", "0"));
		partiesConfig = "tank_parties";
		
		record = Integer.parseInt(config.getData("tank_record", "0"));
		recordConfig = "tank_record";
		
		temps = Integer.parseInt(config.getData("tank_temps", "0"));
		tempsConfig = "tank_temps";
		
		diminution = 1;
		firstObjectif = 15;
	}
	
	@Override
	public String getObjectifString() {
		return getObjectif() + " points";
	}
}
