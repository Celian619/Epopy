package net.epopy.epopy.player.stats;

import net.epopy.epopy.utils.Config;

public class AbstractStats {
	
	protected int diminution, firstObjectif, record, parties, temps;
	protected String recordConfig, partiesConfig, tempsConfig;
	protected Config config;
	
	public void addTemps(final int time) {
		temps += time;
		config.setValue(tempsConfig, temps + "");
		
	}
	
	public int getRecord() {
		return record;
	}
	
	public void setRecord(final int record) {
		this.record = record;
		config.setValue(recordConfig, record + "");
	}
	
	public void addPartie() {
		parties++;
		config.setValue(partiesConfig, parties + "");
	}
	
	public int getParties() {
		return parties;
	}

	public int getTemps() {
		return temps;
	}
	
	public String getObjectifString() {
		int secondes = getObjectif();
		int minutes = 0;
		while (secondes > 60) {
			secondes -= 60;
			minutes++;
		}
		if (minutes != 0)
			return minutes + (minutes == 1 ? " minute " : " minutes ") + secondes;
		else
			return secondes + (secondes < 2 ? " seconde " : " secondes ");
	}

	public int getObjectif() {
		return firstObjectif - diminution * parties;
	}
	
}
