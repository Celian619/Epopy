package net.epopy.network.handlers.packets.modules;

import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.handlers.packets.modules.PacketPlayerRequest.RequestType;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerMessage extends PacketAbstract {
	
	public PacketPlayerMessage() {
	}
	
	@Override
	public void process(final NetworkPlayerHandlers networkPlayerHandlers, final DataBuffer dataBuffer) {
		dataBuffer.getString();
		RequestType type = RequestType.valueOf(dataBuffer.getString().toUpperCase());
		String message = dataBuffer.getString();
		boolean error = Boolean.parseBoolean(dataBuffer.getString());
		float[] color = error ? new float[] { 1, 0, 0, 1 } : new float[] { 1, 1, 1, 1 };
		
		switch (type) {
			case FRIENDS:
			new NotificationGui("FRIENDS", message, 2, color, error);
				break;
			case WAITING_ROOM:
			new NotificationGui("WAITING ROOM", message, 2, color, error);
				break;
			default:
			new NotificationGui(message, "", 2, new float[] { 0, 0, 1, 1 }, error);
				break;
		}
		
	}
}
