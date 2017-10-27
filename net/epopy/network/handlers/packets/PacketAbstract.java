package net.epopy.network.handlers.packets;

import net.epopy.network.NetworkPlayer;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.utils.DataBuffer;

public abstract class PacketAbstract {

    public abstract void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer);
  
    public DataBuffer packet = new DataBuffer(Packets.MAX_SIZE).put(getName()).put(NetworkPlayer.getNetworkPlayer().getName()).put(NetworkPlayer.getNetworkPlayer().getPassword());
  
    public abstract String getName();
    //return getClass().getSimpleName();
  	public DataBuffer getPacket() {
  		return packet;
  	}
}
