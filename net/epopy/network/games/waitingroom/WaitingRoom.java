package net.epopy.network.games.waitingroom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.opengl.GL11;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.utils.WebPage;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.GameListNetwork;
import net.epopy.network.games.waitingroom.modules.WaitingRoomBuilder;
import net.epopy.network.games.waitingroom.modules.WaitingRoomBuilder.WaitingRoomStatus;
import net.epopy.network.handlers.modules.TChat;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.PacketPlayerFriends;
import net.epopy.network.handlers.packets.modules.PacketPlayerFriends.PacketFriendsType;
import net.epopy.network.handlers.packets.modules.PacketPlayerWaitingRoom;
import net.epopy.network.handlers.packets.modules.PacketPlayerWaitingRoom.PacketWaitingRoomType;
import net.epopy.network.utils.PlayerStats;

public class WaitingRoom extends AbstractGameNetwork {
	
	private ButtonGui gauche;
	private ButtonGui droite;
	private ButtonGui jouer;
	private ButtonGui quitterWaitingRoom;
	private ButtonGui leaderInfosButton;
	private List<ButtonGui> addsButtons;
	private ButtonGui shopButton;
	
	/**
	 * Modules
	 */
	public static Map<String, Textures> userProfilTexture = new HashMap<>();
	public static GameListNetwork game;
	public static WaitingRoomBuilder waitingRoom = new WaitingRoomBuilder("", new ArrayList<>(0), 1);
	public static TChat tChat = new TChat(772, 850, 54);
	
	/*
	 * Static variables
	 */
	private static AbstractGameNetwork addPlayersMenu = new AddPlayersMenu();
	private static AbstractGameNetwork boutiqueMenu = new Boutique();
	public static int MAX_PLAYERS = 4; // +1 avec le leader
	public static boolean showAddPlayersMenu;
	public static boolean showBoutiqueMenu;
	
	@Override
	public void onEnable() {
		// public/private statics
		game = GameListNetwork.TANK;
		addPlayersMenu.onEnable();
		boutiqueMenu.onEnable();

		// private
		gauche = new ButtonGui(Textures.GAME_MENU_GAUCHE_OFF, Textures.GAME_MENU_GAUCHE_ON);
		droite = new ButtonGui(Textures.GAME_MENU_DROITE_OFF, Textures.GAME_MENU_DROITE_ON);
		shopButton = new ButtonGui(Textures.NETWORK_WAITING_SHOP_OFF, Textures.NETWORK_WAITING_SHOP_ON);
		leaderInfosButton = new ButtonGui(Textures.NETWORK_WAITING_ROOM_ADD, Textures.NETWORK_WAITING_ROOM_ADD);
		quitterWaitingRoom = new ButtonGui(Textures.GAME_MENU_QUITTER_OFF, Textures.GAME_MENU_QUITTER_ON);
		jouer = new ButtonGui("JOUER", new float[] { 0, 1, 0, 1 }, 70, false);
		
		/**
		 * Buttons 'ajouter' des joueurs
		 */
		addsButtons = new ArrayList<>(4);
		for (int i = 0; i < MAX_PLAYERS; i++)
			addsButtons.add(new ButtonGui(Textures.NETWORK_WAITING_ROOM_ADD, Textures.NETWORK_WAITING_ROOM_ADD));
			
		// recupère la waiting room du joueur
		Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), NetworkPlayer.getNetworkPlayer().getName(), PacketWaitingRoomType.GET));
	}
	
	@Override
	public void update() {
		
		/*
		 * Start
		 */
		
		if (waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName())) {
			if (waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.WAITING) && jouer.text.equals("ANNULER")) {
				jouer.setText("JOUER");
				jouer.textColor = new float[] { 0, 1, 0, 1 };
				jouer.xx = -1;
			} else if (waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.SEARCH) && jouer.text.equals("JOUER")) {
				jouer.setText("ANNULER");
				jouer.textColor = new float[] { 1, 0, 0, 1 };
				jouer.xx = -1;
			}
			
			jouer.update(290, 790, PositionWidth.MILIEU, PositionHeight.HAUT);
			if (jouer.isClicked() && !waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.MATCH_FOUND)) {
				Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(),
						new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), NetworkPlayer.getNetworkPlayer().getName(), waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.WAITING) ? PacketWaitingRoomType.START : PacketWaitingRoomType.CANCEL_START));
				jouer.setClicked(false);
			}
		}
		
		/**
		 * Pour changer le jeu de la salle d'attente
		 */
		gauche.update(200 - 10, 85, PositionWidth.DROITE, PositionHeight.MILIEU, 165 / 2, 148 / 2);
		droite.update(400 - 10, 85, PositionWidth.GAUCHE, PositionHeight.MILIEU, 165 / 2, 148 / 2);
		
		if (GameListNetwork.values().length > 1 && waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.WAITING)) {
			if (waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName())) {
				if (gauche.isClicked()) {
					if (game.getID() > 1) {
						game = GameListNetwork.getGameByID(game.getID() - 1);
					} else game = GameListNetwork.getGameByID(GameListNetwork.getGamesSize());
					
					Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), String.valueOf(game.getID()), PacketWaitingRoomType.CHANGE_ID_GAME));
				} else if (droite.isClicked()) {
					if (game.getID() < GameListNetwork.getGamesSize()) {
						game = GameListNetwork.getGameByID(game.getID() + 1);
					} else game = GameListNetwork.getGameByID(1);
					Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), String.valueOf(game.getID()), PacketWaitingRoomType.CHANGE_ID_GAME));
				}
			}
		}
		
		/**
		 * Si le menu ajouter des joueurs est activé
		 */
		if (showAddPlayersMenu || showBoutiqueMenu) {
			if (showAddPlayersMenu)
				addPlayersMenu.update();
			if (showBoutiqueMenu)
				boutiqueMenu.update();
			return;
		}
		
		// tchat update
		tChat.update();
		
		shopButton.update(1760, 95, PositionWidth.GAUCHE, PositionHeight.HAUT, 40, 40);
		
		if (shopButton.isClicked()) {
			showBoutiqueMenu = true;
		}
		
		/*
		 * Pour quiter la waiting room, donc aller tous seul dans une autre waiting room
		 */
		quitterWaitingRoom.update(1806, 115, PositionWidth.GAUCHE, PositionHeight.MILIEU, 50 / 2, 50 / 2);
		
		// on check si il n'est pas tous seul dans la waiting room, pour éviter les requets
		if (quitterWaitingRoom.isClicked() && waitingRoom.getPlayers().size() > 0) {
			Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), NetworkPlayer.getNetworkPlayer().getName(), PacketWaitingRoomType.REMOVE));
			quitterWaitingRoom.setClicked(false);
		}
		/**
		 * Add players button
		 */
		if (!addsButtons.isEmpty()) {
			for (int i = waitingRoom.getPlayers().size(); i < MAX_PLAYERS; i++) {
				addsButtons.get(i).update(835 + 210 * (i + 1), 180, PositionWidth.MILIEU, PositionHeight.HAUT, 154, 154);
				if (addsButtons.get(i).isClicked()) {
					showAddPlayersMenu = true;
					break;
				}
			}
		}
		leaderInfosButton.update(835, 180, PositionWidth.MILIEU, PositionHeight.HAUT, 154, 154);
	}
	
	@Override
	public void render() {
		
		if (showAddPlayersMenu || showBoutiqueMenu) {
			if (showAddPlayersMenu)
				addPlayersMenu.render();
			if (showBoutiqueMenu)
				boutiqueMenu.render();
			renderInfosRoom();
			jouer.render();
			return;
		}
		
		getDefaultBackGround().renderBackground();
		
		// render la croix pour quitter la waiting room
		quitterWaitingRoom.render();
		
		// render les infos de la waiting room, rank, jeu, map...
		renderInfosRoom();

		if (waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName()) && !waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.MATCH_FOUND))
			jouer.render();
		else {
			if (waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.SEARCH))
				ComponentsHelper.drawText("Recherche en cours...", 290, 790, PositionWidth.MILIEU, PositionHeight.HAUT, 30);
			else if (waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.WAITING))
				ComponentsHelper.drawText("En attente...", 290, 790, PositionWidth.MILIEU, PositionHeight.HAUT, 30);
			if (waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.MATCH_FOUND))
				ComponentsHelper.drawText("Match trouvé !", 290, 790, PositionWidth.MILIEU, PositionHeight.HAUT, 30, new float[] { 0, 1, 0, 1 });
		}
		tChat.render();
		shopButton.render();
		
		/*
		 * ----CASE----
		 */
		
		/**
		 * Render les buttons add players
		 */
		for (int i = waitingRoom.getPlayers().size(); i < MAX_PLAYERS; i++) {
			if (addsButtons.get(i).isOn())
				GL11.glColor4f(0.7f, 0.7f, 0.7f, 1);
			addsButtons.get(i).render();
			GL11.glColor4f(1, 1, 1, 1);
		}
		
		/*
		 * Case du leader
		 */
		ComponentsHelper.drawText(waitingRoom.getLeader(), 840, 350, PositionWidth.MILIEU, PositionHeight.HAUT, 30);
		if (waitingRoom != null) {
			ComponentsHelper.renderTexture(userProfilTexture.containsKey(waitingRoom.getLeader()) ? userProfilTexture.get(waitingRoom.getLeader()) : Textures.NETWORK_WAITING_ROOM_IMAGE_USER_DEFAULT, 756, 174, 157, 157);
			if (leaderInfosButton.isOn() && !waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName())) {
				ComponentsHelper.drawQuad(756, 173, 158, 158, new float[] { 0, 0, 0, 0.8f });
				renderInfosPlayer(leaderInfosButton, waitingRoom.getLeader(), waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName()));
			}
		}
		
		/**
		 * Render Avatar + actions sur les joueurs (kick, profil, ..)
		 */
		for (int i = 0; i < waitingRoom.getPlayers().size(); i++) {
			String name = waitingRoom.getPlayers().get(i);
			ComponentsHelper.drawText(name, 835 + 210 * (i + 1), 350, PositionWidth.MILIEU, PositionHeight.HAUT, 30);
			ComponentsHelper.renderTexture(userProfilTexture.containsKey(name) ? userProfilTexture.get(name) : Textures.NETWORK_WAITING_ROOM_IMAGE_USER_DEFAULT, 756 + 210 * (i + 1), 173, 157, 157);
			/*
			 * render les infos du joueur demandé
			 */
			ButtonGui button = addsButtons.get(i);
			button.update(835 + 210 * (i + 1), 180, PositionWidth.MILIEU, PositionHeight.HAUT, 154, 154);
			if (button.isOn() && !name.equals(NetworkPlayer.getNetworkPlayer().getName())) {
				ComponentsHelper.drawQuad(756 + 210 * (i + 1), 173, 158, 158, new float[] { 0, 0, 0, 0.8f });
				renderInfosPlayer(button, name, waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName()));
			}
		}
	}
	
	/**
	 * Render les infos + options d'un joueur joueur
	 *
	 * @param button
	 *            du joueur
	 */
	private final ButtonGui showProfil = new ButtonGui("Voir le profil", new float[] { 1, 1, 1, 1 }, 22, false);
	private final ButtonGui sendFriendRequest = new ButtonGui("Ajouter en ami", new float[] { 1, 1, 1, 1 }, 22, false);
	private final ButtonGui sendMessage = new ButtonGui("Message privée", new float[] { 1, 1, 1, 1 }, 22, false);
	private final ButtonGui kickPlayer = new ButtonGui("Exclure", new float[] { 1, 0, 0, 1 }, 22, false);
	
	private void renderInfosPlayer(final ButtonGui button, final String playerName, final boolean leader) {
		int x = button.xx + 80;
		int y = button.yy + 55;
		
		showProfil.xx = -1;
		showProfil.update(x, y - 30, PositionWidth.MILIEU, PositionHeight.HAUT);
		showProfil.render();
		if (showProfil.isClicked()) {
			new WebPage(WebPage.WEB_PAGE_EPOPY_USER + "/" + playerName);
			showProfil.setClicked(false);
		}
		
		if (!AddPlayersMenu.friends.contains(playerName)) {
			sendFriendRequest.xx = -1;
			sendFriendRequest.update(x, y, PositionWidth.MILIEU, PositionHeight.HAUT);
			sendFriendRequest.render();
			if (sendFriendRequest.isClicked()) {
				Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerFriends(NetworkPlayer.getNetworkPlayer(), playerName, PacketFriendsType.ADD));
				sendFriendRequest.setClicked(false);
			}
		}
		
		sendMessage.xx = -1;
		sendMessage.update(x, y + 30, PositionWidth.MILIEU, PositionHeight.HAUT);
		sendMessage.render();
		if (sendMessage.isClicked()) {
			System.out.println("message privée to " + playerName);
			sendMessage.setClicked(false);
		}
		
		if (leader) {
			kickPlayer.xx = -1;
			kickPlayer.update(x, y + 70, PositionWidth.MILIEU, PositionHeight.HAUT);
			kickPlayer.render();
			if (kickPlayer.isClicked()) {
				Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), playerName, PacketWaitingRoomType.REMOVE));
				kickPlayer.setClicked(false);
			}
		}
	}
	
	private void renderInfosRoom() {
		/**
		 * Le jeu de la waiting room (infos)
		 */
		
		ComponentsHelper.drawText(game.getAbstractGame().getName().toUpperCase(), 300 - 10, 55, PositionWidth.MILIEU, PositionHeight.HAUT, 50);
		if (game.getAbstractGame().getDefaultBackGround() != null)
			ComponentsHelper.renderTexture(game.getAbstractGame().getDefaultBackGround(), 0, 166, 582, 332);
			
		/*
		 * Stats du joueur
		 */
		//TODO changer quand il y aura d'autre jeu
		PlayerStats tankStats = NetworkPlayer.getNetworkPlayer().getTankStats();
		ComponentsHelper.drawText("Coins: " + tankStats.getCoins() + (tankStats.hasBooster() ? " - " + tankStats.getBooster() : ""), 10, 500, PositionWidth.GAUCHE, PositionHeight.HAUT, 24);
		
		ComponentsHelper.drawText("Parties: " + tankStats.getParties(), 10, 555, PositionWidth.GAUCHE, PositionHeight.HAUT, 24);
		
		ComponentsHelper.drawText("Heures: " + tankStats.getPlayTime(), 350, 500, PositionWidth.GAUCHE, PositionHeight.HAUT, 24);
	
		ComponentsHelper.drawText("Rank: " + tankStats.getRank(), 350, 555, PositionWidth.GAUCHE, PositionHeight.HAUT, 24);
		boolean waiting = waitingRoom.getWaitingRoomStatus().equals(WaitingRoomStatus.WAITING);
		if (waitingRoom.getLeader().equals(NetworkPlayer.getNetworkPlayer().getName()) && waiting) {
			droite.render();
			gauche.render();
		}
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_WAITING_ROOM_BG_MATCHMAKING;
	}
	
}
