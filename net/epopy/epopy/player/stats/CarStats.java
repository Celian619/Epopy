package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class CarStats extends AbstractStats {

	public CarStats(final Config config) {
		this.config = config;
		parties = Integer.parseInt(config.getData("car_parties", "0"));
		partiesConfig = "car_parties";

		record = Integer.parseInt(config.getData("car_record", "0"));
		recordConfig = "car_record";

		temps = Integer.parseInt(config.getData("car_temps", "0"));
		tempsConfig = "car_temps";

		diminution = 1;
		firstObjectif = 54;
	}
	
}
