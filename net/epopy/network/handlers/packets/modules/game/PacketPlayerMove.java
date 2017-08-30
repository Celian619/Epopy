package net.epopy.network.handlers.packets.modules.game;

import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerMove extends PacketAbstract {

	
	public PacketPlayerMove() {}
	
	public PacketPlayerMove(boolean back, boolean avancer, boolean droite, boolean gauche, double direction) {
		packet.put(back ? 1 : 0);
		packet.put(avancer ? 1 : 0);
		packet.put(droite ? 1 : 0);
		packet.put(gauche ? 1 : 0);
		packet.put(direction);
		packet.flip();
	}
	
	public PacketPlayerMove(int back, int avancer, double direction) {
		packet.put(back);
		packet.put(avancer);
		packet.put(0);
		packet.put(0);
		packet.put(0.0);
		packet.put(direction);
		packet.flip();
	}
	public PacketPlayerMove(double direction) {
		packet.put(0);
		packet.put(0);
		packet.put(0);
		packet.put(0);
		packet.put(0.0);
		packet.put(direction);
		packet.flip();
	}
	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
	}
}
