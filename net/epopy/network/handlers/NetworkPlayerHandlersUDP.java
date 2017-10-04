package net.epopy.network.handlers;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.utils.DataBuffer;

public class NetworkPlayerHandlersUDP implements Runnable {

	private int				port;
	private InetAddress		address;
	private DatagramSocket	socket;

	public NetworkPlayerHandlersUDP(NetworkPlayerHandlers networkPlayerHandlers, String address, int port) {
		try {
			this.address = InetAddress.getByName(address);
			this.port = port;
			this.socket = new DatagramSocket(networkPlayerHandlers.getSocket().getLocalPort());
			socket.setReceiveBufferSize(Packets.MAX_SIZE * 30 * 100);
			new Thread(this, "udp-thread").start();
		} catch (SocketException e) {
			System.out.println("SocketException");
			System.out.println("Local port: " + networkPlayerHandlers.getSocket().getLocalPort());
			System.out.println(port);
			e.printStackTrace();
		} catch (UnknownHostException e) {
			System.out.println("UnknownHostException");
			System.out.println("Local port: " + networkPlayerHandlers.getSocket().getLocalPort());
			System.out.println(port);
			e.printStackTrace();
		}
	}

	public void run() {
		while (true) {
			try {
				byte[] bytes = new byte[Packets.MAX_SIZE];
				DatagramPacket receive = new DatagramPacket(bytes, bytes.length);
				socket.receive(receive);
				DataBuffer data = new DataBuffer(receive.getData());
				PacketAbstract packet = Packets.getPacket(data.getString());
				if (packet != null)
					packet.process(null, data);
			} catch (IOException e) {
				break;
			}
		}
		close();
	}

	public void send(byte[] bytes) {
		new Thread("udp-send-thread") {
			public void run() {
				try {
					DatagramPacket packet = new DatagramPacket(bytes, bytes.length, address, port);
					socket.send(packet);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}.start();
	}

	public void close() {
		socket.close();
	}
}