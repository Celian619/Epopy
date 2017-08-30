package net.epopy.network.handlers.packets.modules;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;

import net.epopy.epopy.display.Textures;
import net.epopy.network.handlers.packets.modules.servermanager.PacketGetIPServers;
import net.epopy.network.utils.Callback;

public class PacketPlayerServerImage {

	public PacketPlayerServerImage(List<String> targets, Callback callback) {
		try {	
			Socket socket = new Socket();
			socket.connect(new InetSocketAddress(PacketGetIPServers.TEXTURE_IP, PacketGetIPServers.TEXTURE_PORT), 1000);
			socket.setTcpNoDelay(true);

			Map<String, Textures> textures = new HashMap<>(targets.size());

			DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
			StringBuilder data = new StringBuilder();
			for(String targetName : targets) 
				data.append(targetName).append(":");
			dataOutputStream.writeUTF(data.toString());
			dataOutputStream.flush();
			
			int dataSend = targets.size();
			int dataReceive = 0;
			while(dataSend != dataReceive) {
				final long startTime = System.nanoTime();
				/*
				 * Infos sur l'image
				 */
				DataInputStream din = new DataInputStream(socket.getInputStream());
				int valid = din.readInt();
				String imageName = din.readUTF();
				if(valid == 1) {
					/**
					 * Lecture de l'image reçu
					 */
					//InputStream inputStream = socket.getInputStream();
					byte[] sizeAr = new byte[4];
					din.read(sizeAr);
					int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
					byte[] imageAr = new byte[size];
					din.read(imageAr);

					/**
					 * Creation de l'image
					 */
					BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageAr));
					final long duration = System.nanoTime() - startTime;
					System.out.println("------PacketPlayerServerImage------");
					System.out.println("Received " + imageName +" -> " + "Taille:" + image.getHeight() + "x" + image.getWidth() + " | en " + duration / 10000000 + "s");
					textures.put(imageName, new Textures(image));
					//	ImageIO.write(image, "jpg", new File("C:\\Users\\SEVEN\\AppData\\Roaming\\.Epopy\\" + imageName + ".jpg"));
				} else {
					System.out.println("------PacketPlayerServerImage------");
					System.out.println("Received " + imageName + " -> default texture");
					textures.put(imageName, Textures.NETWORK_WAITING_ROOM_IMAGE_USER_DEFAULT);
				} 
				dataReceive++;
			}
			if(callback != null) 
				callback.callback(textures);
			socket.close();
		} catch (Exception e) {
			Map<String, Textures> textures = new HashMap<>(targets.size());
			for(String target : targets)
				textures.put(target, Textures.NETWORK_WAITING_ROOM_IMAGE_USER_DEFAULT);
			if(callback != null) 
				callback.callback(textures);
		}
	}	
}
