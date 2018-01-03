package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class PlaceInvaderStats extends AbstractStats {

	public PlaceInvaderStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("plainv_parties", "0"));
		partiesConfig = "plainv_parties";

		record = Integer.parseInt(config.getData("plainv_record", "0"));
		recordConfig = "plainv_record";

		temps = Integer.parseInt(config.getData("plainv_temps", "0"));
		tempsConfig = "plainv_temps";

		diminution = 5;
		firstObjectif = 150;
	}
	
	@Override
	public String getObjectifString() {
		return getObjectif() + " points";
	}
	
}
