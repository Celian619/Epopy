package net.epopy.network.handlers;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.network.Logger;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.display.DisplayManager;
import net.epopy.network.games.waitingroom.WaitingRoom;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.PacketPlayerDisconnect;
import net.epopy.network.handlers.packets.modules.PacketPlayerLogin;
import net.epopy.network.utils.DataBuffer;
import net.epopy.network.utils.DataStream;
import net.epopy.network.utils.NetworkStatus;

public class NetworkPlayerHandlers implements Runnable {

	private final NetworkPlayer player;

	// network
	private Socket socket;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;

	private NetworkStatus networkStatus;
	private ConcurrentLinkedQueue<PacketAbstract> sendQueue = new ConcurrentLinkedQueue<>();
	
	private NetworkPlayerHandlersUDP networkPlayerHandlersUDP;

	public NetworkPlayerHandlers(final NetworkPlayer player, final String ip, final int port, final boolean udp) {
		this.player = player;
		networkStatus = connect(ip, port);
		if (!networkStatus.equals(NetworkStatus.SERVER_OFFLINE))
			Packets.sendPacket(this, new PacketPlayerLogin(Main.getVersion()));
		if (udp)
			networkPlayerHandlersUDP = new NetworkPlayerHandlersUDP(this, ip, port);
	}

	/*
	 * -- Getters --
	 */

	public Socket getSocket() {
		return socket;
	}

	public NetworkPlayerHandlersUDP getServerUDP() {
		return networkPlayerHandlersUDP;
	}

	/**
	 * donne le status du network
	 *
	 * @return
	 */
	public NetworkStatus getNetworkStatus() {
		return networkStatus;
	}

	public DataOutputStream getDataOutputStream() {
		return dataOutputStream;
	}

	public NetworkPlayer getNetworkPlayer() {
		return player;
	}

	/*
	 * Setters
	 */

	public void disconnect() {
		Packets.sendPacket(this, new PacketPlayerDisconnect());
		try {
			socket.close();
		} catch (IOException e) {
		}
	}

	/**
	 *
	 * Connection a un server
	 *
	 * @param ip
	 *            du serveur
	 * @param port
	 *            du serveur
	 * @return true = connexion possible | false = connexion impossible
	 */
	private NetworkStatus connect(final String ip, final int port) {
		if (ip == null)
			return NetworkStatus.SERVER_OFFLINE;
		try {
			socket = new Socket();
			socket.connect(new InetSocketAddress(ip, port), 4000);// 10s de time out
			System.out.println("Connect: " + ip + " " + port);
			socket.setTcpNoDelay(true);
		//	socket.setKeepAlive(true);
			new ClientSendThread(this);
			//this.socket.setTrafficClass(0x10);
		//	this.socket.setReuseAddress(false);
			//this.socket.setSoLinger(false, 0);

			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			new Packets();

			new Thread(this, "tcp-" + socket.getLocalPort()).start();
			
			return NetworkStatus.USER_WAITING_CONFIRMATION;
		} catch (IOException e) {
			return NetworkStatus.SERVER_OFFLINE;
		}
	}

	@Override
	public void run() {
		while (socket != null) {
			try {
				byte[] bytes = DataStream.readPacket(dataInputStream);
				DataBuffer data = new DataBuffer(bytes);

				PacketAbstract packet = Packets.getPacket(data.getString());
				if (packet != null) {
					packet.process(this, data);
				} else 
					break;
			} catch (Exception ex) {
				ex.printStackTrace();
				break;
			}
		}
		stop();
	}

	public void stop() {
		if (networkPlayerHandlersUDP == null) {
			new NotificationGui("Les serveurs se sont éteints", "( Pour plus d'informations veuillez nous contacter, @EpopyOfficiel/Epopy.fr )", 4, new float[] { 1, 0, 0, 1 }, false);
			NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom().disconnect();
			DisplayManager.exitMulti();
		} else {
			Logger.info("Server game has been stopped");
			//TODO voir pour send que quand il y a vraiment besoin
				NetworkPlayer.setGame(new WaitingRoom());
			//new NotificationGui("Votre serveur de jeu vient de s'éteindre", "( Pour plus d'informations veuillez nous contacter, @EpopyOfficiel/Epopy.fr )", 4, new float[] { 1, 0, 0, 1 }, false);
		}
	}

	public void setNetworkStatus(final NetworkStatus networkStatus) {
		this.networkStatus = networkStatus;
	}
	
	public ConcurrentLinkedQueue<PacketAbstract> getSendingQueue() {
		return sendQueue;
	}
	public void send(PacketAbstract packet) {
		sendQueue.add(packet);
	}
	
	public class ClientSendThread implements Runnable {
		private NetworkPlayerHandlers client;

		public ClientSendThread(NetworkPlayerHandlers client) {
			this.client = client;
			new Thread(this).start();
		}

		public void stop() {
			System.out.println("stop thread");
		}

		@Override
		public void run() {
			while (client.getSocket() != null && !client.getSocket().isClosed()) { 
				while (!client.getSendingQueue().isEmpty()) {
					try {
						PacketAbstract packet = client.getSendingQueue().poll();
						if (packet != null) {

							byte[] packetData = packet.getPacket().getData();
							if (packetData.length >= 0 ) {
								client.getDataOutputStream().write(packetData);
								client.getDataOutputStream().flush();
							//	System.out.println(client.getName() + " <- send " + packet.getName());
							} 
						}
					} catch (IOException e) {
						e.printStackTrace();
						stop();
					}
				}
			}
			stop();

		}
	}
}
