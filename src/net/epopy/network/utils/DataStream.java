package net.epopy.network.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.epopy.network.handlers.packets.Packets;

public class DataStream {

	public static void writePacket(OutputStream outputStream, byte[] data) throws IOException {
		if (data.length > Packets.MAX_SIZE)
			throw new RuntimeException("PacketAbstract size: " + data.length + "  MAX SIZE: " + Packets.MAX_SIZE);

		outputStream.write(data);
		outputStream.flush();
	}

	public static byte[] readPacket(InputStream inputStream) throws IOException {
		byte[] data = new byte[Packets.MAX_SIZE];
		inputStream.read(data, 0, data.length);		
		return data;
	}
}
