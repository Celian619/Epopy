package net.epopy.network.handlers.packets.modules;

import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;
import net.epopy.network.utils.PlayerStats;

public class PacketPlayerStats extends PacketAbstract {

	public PacketPlayerStats() {
		packet.flip();
	}
	public String getName() {
		return "PacketPlayerStats";
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String name = dataBuffer.getString();
		int coins = dataBuffer.getInt();//100
		String booster = dataBuffer.getString();//Booster x2 / Booster x0.2...
		String rank = dataBuffer.getString(); //beginner ...
		String time = dataBuffer.getString();//10 h 03min
		String parties = dataBuffer.getString();//x gagnés / x total
		
		if(name.equals("tank")) {
			PlayerStats tankStats = networkPlayerHandlers.getNetworkPlayer().getTankStats();
			if(coins != -1) 
				tankStats.setCoins(coins);
			if(!booster.equals("-1")) 
				tankStats.setBooster(booster);
			if(!rank.equals("-1")) 
				tankStats.setRank(rank);
			if(!time.equals("-1")) 
				tankStats.setPlayTime(time);
			if(!parties.equals("-1")) 
				tankStats.setParties(parties);
		}
	}
}
