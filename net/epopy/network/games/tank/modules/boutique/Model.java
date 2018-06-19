package net.epopy.network.games.tank.modules.boutique;

import java.util.List;

import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.PacketPlayerShopBuy;

public abstract class Model {
	
	public List<Level> levels;
	public Level currentLevel, maxLevel;
	public ButtonGui buttonBuy = new ButtonGui("Améliorer", new float[] { 0, 1, 0, 1 }, 30);
	
	public int getPrice() {
		return getLevel().getPrice();
	}
	
	public Level getLevel() {
		return currentLevel;
	}
	
	public Level getMaxLevel() {
		return maxLevel;
	}
	
	public void buy() {
		if (NetworkPlayer.getNetworkPlayer().getTankStats().getCoins() >= getPrice()) {
			// TODO changer tank pour que ça soit universele
			Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerShopBuy("tank", getName()));
			new NotificationGui("BOUTIQUE", getName() + " - Niveau: " + (getLevel().getLevel() + 1), 3, new float[] { 0, 1, 0, 1 }, false);
		} else
			new NotificationGui("BOUTIQUE", "Il vous manque " + (getPrice() - NetworkPlayer.getNetworkPlayer().getTankStats().getCoins()) + "coins !", 2, new float[] { 1, 0, 0, 1 }, true);
			
	}
	
	public abstract String getName();
	
	public abstract void update();
	
	public abstract void render();
}
