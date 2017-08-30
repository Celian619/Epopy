package net.epopy.network.handlers.packets.modules.servermanager;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import net.epopy.network.utils.DataBuffer;
import net.epopy.network.utils.DataStream;
import net.epopy.network.utils.EnvType;

public class PacketGetIPServers {
	
	// Server macht making
	public static String MATCH_MAKING_IP;
	public static int MATCH_MAKING_PORT;
	
	// server texture
	public static String TEXTURE_IP;
	public static int TEXTURE_PORT;
	
	// server manager
	public static String MANAGER_IP = "86.192.80.89";
	public static int MANAGER_PORT = 25565;
	
	public PacketGetIPServers() {
		// TODO pour le dev
		try {
			if (InetAddress.getLocalHost().getHostAddress().equals("192.168.1.15"))
				MANAGER_IP = "192.168.1.15";
				
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		}
		
		try {
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(MANAGER_IP, MANAGER_PORT), 1000);
			socket.setTcpNoDelay(true);
			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			DataBuffer packet = new DataBuffer(512);
			packet.put(getClass().getSimpleName());
			packet.put(EnvType.PROD.name());// changer par la suite
			packet.flip();
			dataOutputStream.write(packet.getData());
			dataOutputStream.flush();
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			
			try {
				byte[] bytes = DataStream.readPacket(dataInputStream);
				DataBuffer data = new DataBuffer(bytes);
				String packetName = data.getString();
				if (packetName.equals(getClass().getSimpleName())) {
					String dataMatchmaking = data.getString();
					String dataTexture = data.getString();
					
					MATCH_MAKING_IP = dataMatchmaking.split(":")[0];
					MATCH_MAKING_PORT = Integer.parseInt(dataMatchmaking.split(":")[1]);
					
					TEXTURE_IP = dataTexture.split(":")[0];
					TEXTURE_PORT = Integer.parseInt(dataTexture.split(":")[1]);
					
					// System.out.println("IPS: " + MATCH_MAKING_IP + ":" + MANAGER_PORT + " " + TEXTURE_IP + ":" + TEXTURE_PORT);
				}
			} catch (Exception ex) {
			}
			
		} catch (IOException e) {
		}
	}
}
