package net.epopy.network.games.waitingroom;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.GameListNetwork;
import net.epopy.network.games.tank.TankBoutique;

public class Boutique extends AbstractGameNetwork {
	
	private static ButtonGui retourWaitingRoom;
	
	private static TankBoutique tankBoutique = new TankBoutique();
	
	@Override
	public void onEnable() {
		retourWaitingRoom = new ButtonGui(Textures.GAME_MENU_USERS_RETOUR_OFF, Textures.GAME_MENU_USERS_RETOUR_ON);
	}
	
	@Override
	public void update() {
		retourWaitingRoom.update(1803, 115, PositionWidth.GAUCHE, PositionHeight.MILIEU, 30, 30);
		
		if (retourWaitingRoom.isClicked()) {
			WaitingRoom.showBoutiqueMenu = false;
			return;
		}

		if (WaitingRoom.waitingRoom.getIdGame() == GameListNetwork.TANK.getID())
			tankBoutique.update();
			
	}
	
	@Override
	public void render() {
		getDefaultBackGround().renderBackground();
		retourWaitingRoom.render();
		
		if (WaitingRoom.waitingRoom.getIdGame() == GameListNetwork.TANK.getID())
			tankBoutique.render();
			
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_WAITING_ROOM_BG;
	}
	
}
