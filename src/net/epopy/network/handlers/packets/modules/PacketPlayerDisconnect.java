package net.epopy.network.handlers.packets.modules;

import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerDisconnect extends PacketAbstract {

	
	public PacketPlayerDisconnect() {
		
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		//System.out.println("- PacketPlayerDisconnect- ");
		//TODO System.out.println("leave: " + dataBuffer.getString());
	}
}
