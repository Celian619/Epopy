package net.epopy.network.handlers.packets.modules;

import net.epopy.network.games.tank.TankBoutique;
import net.epopy.network.games.tank.modules.boutique.Model;
import net.epopy.network.handlers.NetworkPlayerHandlers;
import net.epopy.network.handlers.packets.PacketAbstract;
import net.epopy.network.utils.DataBuffer;

public class PacketPlayerShopBuy extends PacketAbstract {

	public PacketPlayerShopBuy() {}

	public PacketPlayerShopBuy(String gameType, String item) {
		packet.put(gameType);
		packet.put(item);
		packet.flip();
	}

	@Override
	public void process(NetworkPlayerHandlers networkPlayerHandlers, DataBuffer dataBuffer) {
		String gameName = dataBuffer.getString();
		String modelName = dataBuffer.getString();
		if(modelName.contains(":")) {
			int i = 0;
			String level = dataBuffer.getString();
			for(String m : modelName.split(":")) {
				update(gameName, m, Integer.parseInt(level.split(":")[i]));
				i++;
			}
		} else 
			update(gameName, modelName, Integer.parseInt(dataBuffer.getString()));
			
	}

	private void update(String gameName, String modelName, int level) {
		if(gameName.equals("tank")) {
			if(TankBoutique.models.isEmpty()) 
				TankBoutique.loadModels();

			for(Model model : TankBoutique.models) {
				if(model.getName().equals(modelName)) 
					model.currentLevel = model.levels.get(level);
			}
		}
	}


}
