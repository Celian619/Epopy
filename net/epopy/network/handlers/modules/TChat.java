package net.epopy.network.handlers.modules;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.TextAreaGui;
import net.epopy.epopy.utils.Input;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.PacketPlayerTChat;
import net.epopy.network.handlers.packets.modules.PacketPlayerTChat.PacketTChatType;

public class TChat {


	private TextAreaGui textArea;
	private int x, y, maxWidth;
	private static List<String> messages = new LinkedList<>();
	private static int MAX_MESSAGE = 12;

	public TChat(int x, int y, int maxWidth) {
		this.x = x;
		this.y = y;
		this.maxWidth = maxWidth;
		this.textArea = new TextAreaGui(x, y, true, "");
		textArea.setEnter(true);
		textArea.setAccesCaratereSpecial(true);
	}

	/*
	 * Fonctions
	 */
	public static void addMessage(String sender, String message) {//»
		messages.add(sender.equals("SERVER") ? "&e" + message : sender + " » " + message);
	
		if(messages.size() > MAX_MESSAGE)
			messageFirst++;

	}

	public void clear() {
		messages.clear();
		messageFirst = 1;
	}
	
	public void update() {
		textArea.update(maxWidth);

		if(Input.getKeyDown(28)) {
			Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerTChat(NetworkPlayer.getNetworkPlayer().getName(), textArea.getText(), PacketTChatType.WAITING_ROOM));
			textArea.setText("");
			textArea.setEnter(true);
		}

	}
	//dernier message qu'il veut afficher 
	private static int messageFirst = 1;
	private float[] serverColor = new float[]{1, 1, 0, 1};
	private float[] defaultColor = new float[]{1, 1, 1, 1};
	
	public void render() {
		int x = this.x - 15;
		int y = this.y - 50;

		//ComponentsHelper.drawQuad(x + 995, y - 17, 15, 60, new float[]{1, 1, 1, 0.7f});

		List<String> messagesList = new ArrayList<>(messages.size());
		for(int i = messageFirst; i < (messageFirst + MAX_MESSAGE); i++) {
			if(messages.size() >= i)
				messagesList.add(messages.get(i-1));
			else break;
		}
		Collections.reverse(messagesList);

		for(String message : messagesList) {
			if(message.contains("&e"))
				ComponentsHelper.drawText(message.replace("&e", ""), x, y, 30, serverColor);
			else
				ComponentsHelper.drawText(message, x, y, 30, defaultColor);
			y -= 30;
		}

		textArea.render();
	}
}
