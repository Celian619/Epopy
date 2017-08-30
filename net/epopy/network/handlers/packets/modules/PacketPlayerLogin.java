package net.epopy.network.handlers.packets.modules;

import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;
import net.epopy.network.utils.NetworkStatus;

public class PacketPlayerLogin extends PacketAbstract {

	public PacketPlayerLogin() {
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		NetworkStatus status = NetworkStatus.valueOf(dataBuffer.getString());
		if(status != null)  
			networkPlayerHandlers.setNetworkStatus(status);	
		networkPlayerHandlers.getNetworkPlayer().setPseudo(dataBuffer.getString());
	}
}
