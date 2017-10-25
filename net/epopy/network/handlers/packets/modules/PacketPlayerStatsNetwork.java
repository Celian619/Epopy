package net.epopy.network.handlers.packets.modules;

import net.epopy.network.games.waitingroom.AddPlayersMenu;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerStatsNetwork extends PacketAbstract {

	public PacketPlayerStatsNetwork(){};

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String players = dataBuffer.getString();
		String friends = dataBuffer.getString();
		String friendsOnline = dataBuffer.getString();

		boolean add = Boolean.parseBoolean(dataBuffer.getString());

		if(!friends.equals("nodata")) {
			for(String friendName : friends.split(":")) {
				if(AddPlayersMenu.friends.contains(friendName)) 
					AddPlayersMenu.friends.remove(friendName);
				if(add) AddPlayersMenu.friends.add(friendName);
			}
		}
		if(!players.equals("nodata")) {
			for(String playersName : players.split(":")) {
				if(AddPlayersMenu.playersNetwork.contains(playersName)) 
					AddPlayersMenu.playersNetwork.remove(playersName);
				if(add) AddPlayersMenu.playersNetwork.add(playersName);
			}
		}
		if(!friendsOnline.equals("nodata")) {
			for(String friendOnlineName : friendsOnline.split(":")) {
				if(AddPlayersMenu.friendsOnline.contains(friendOnlineName)) 
					AddPlayersMenu.friendsOnline.remove(friendOnlineName);
				if(add) AddPlayersMenu.friendsOnline.add(friendOnlineName);
			}
		}
	/*	System.out.println("Network: " + AddPlayersMenu.playersNetwork.toString());
		System.out.println("Friends: " +AddPlayersMenu.friends.toString());
		System.out.println("Friends online: " +AddPlayersMenu.friendsOnline.toString());
		System.out.println("-------------------------------");*/
		AddPlayersMenu.reload();
	}
}

