package net.epopy.network.games.waitingroom;

import static net.epopy.epopy.display.components.ComponentsHelper.drawLine;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;
import static net.epopy.epopy.display.components.ComponentsHelper.renderTexture;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.display.components.TextAreaGui;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.handlers.packets.Packets;
import net.epopy.network.handlers.packets.modules.PacketPlayerWaitingRoom;
import net.epopy.network.handlers.packets.modules.PacketPlayerWaitingRoom.PacketWaitingRoomType;

public class AddPlayersMenu extends AbstractGameNetwork {

	// liste d'information
	public static List<String> friends = new LinkedList<>(), friendsOnline = new LinkedList<>(), playersNetwork = new LinkedList<>(), addfriendsButtons = new ArrayList<>(5);

	private static int colone, sizeTextArea;// nbr de lettre dans la recherche de joueur. Cette variable permet de savoir si il l a modif
	private static TextAreaGui searchPlayer;
	private static ButtonGui validPlayers, retourWaitingRoom;
	private static List<ButtonGui> friendsButtons = new LinkedList<>();// tous les amis à render

	private final int ecartLigne = 40, maxColone = 3, ecartX = 450, defaultX = 730;
	private final Map<String, ButtonGui> playersRender = new TreeMap<>();// list des joueurs du network à render

	private int clickTime, lastX = 770, ySearchPlayer = 300;

	@Override
	public void onEnable() {
		validPlayers = new ButtonGui("Valider", new float[] { 0, 0.7f, 0, 1 }, 50, false);
		searchPlayer = new TextAreaGui(1082, 225, true, "");
		retourWaitingRoom = new ButtonGui(Textures.GAME_MENU_USERS_RETOUR_OFF, Textures.GAME_MENU_USERS_RETOUR_ON);

	}

	/**
	 * Fonction pour reload les pages d'amis
	 */
	public static void reload() {
		// remove tous les joueurs qui sont déconnécter entre temps
		List<String> buttonsOnlinePlayers = new ArrayList<>();
		for (ButtonGui button : friendsButtons) {
			if (!friendsOnline.contains(button.text)) {
				friendsButtons.remove(button);
			} else
				buttonsOnlinePlayers.add(button.text);
		}

		// ajouts les amis en lignes
		for (String friend : friendsOnline) {
			if (!buttonsOnlinePlayers.contains(friend)) {
				if (friendsButtons.size() <= 45) // TODO pages
					friendsButtons.add(new ButtonGui(friend, new float[] { 1, 1, 1, 1 }, 30, false));
			}
		}
	}

	/**
	 * Fonction pour reset (la bare de recherche, les amis cochés...)
	 */
	public static void reset() {
		friendsButtons.clear();
		reload();
		addfriendsButtons.clear();
		sizeTextArea = 0;
		searchPlayer.setText("");
		colone = 0;
	}

	@Override
	public void update() {
		// text ara pour chercher un joueur
		searchPlayer.update(18);
		// button pour envoyer les requets d'invitation
		validPlayers.update(1265, 900, PosWidth.MILIEU, PosHeight.HAUT, 170, 50);
		// button pour retourner à la salle d'attente
		retourWaitingRoom.update(1803, 115, PosWidth.GAUCHE, PosHeight.MILIEU, 30, 30);

		if (retourWaitingRoom.isClicked()) {
			reset();
			WaitingRoom.showAddPlayersMenu = false;
			return;
		}

		// variables for friends
		int y = 300;
		int x = defaultX;
		int idfriendsButtons = 0;

		/**
		 * Handler quand il click sur le button valider
		 */
		if (validPlayers.isClicked()) {
			if (addfriendsButtons.size() > 0) {
				for (String target : addfriendsButtons)
					Packets.sendPacket(NetworkPlayer.getNetworkPlayer().getNetworkPlayerHandlersWaitingRoom(), new PacketPlayerWaitingRoom(NetworkPlayer.getNetworkPlayer(), target, PacketWaitingRoomType.ADD));
				new NotificationGui("WAITING ROOM", "Invitation envoyée !", 2, new float[] { 1, 1, 1, 1 }, false);
			}
			reset();
			validPlayers.setClicked(false);
			// on retourne au menu de la waiting room
			WaitingRoom.showAddPlayersMenu = false;
			return;
		}

		/**
		 * On check si il a update la barre de recherche (ex: Ro | update: Roro -> donc on update la recherche)
		 */
		if (sizeTextArea != searchPlayer.getText().length()) {
			sizeTextArea = searchPlayer.getText().length();
			playersRender.clear();
			ySearchPlayer = y;
			lastX = defaultX;
			clickTime = 0;
		}

		// check si le joueur peut reclicker sur un button
		if (clickTime > 0)
			clickTime--;

		/**
		 * Si la barre de recherche n'est pas vide on cherche sur la network le joueur demandé
		 */
		if (!searchPlayer.getText().isEmpty()) {
			for (String pls : playersNetwork) {
				if (pls.toUpperCase().contains(searchPlayer.getText().toUpperCase()) && !WaitingRoom.waitingRoom.getPlayers().contains(pls) && !WaitingRoom.waitingRoom.getLeader().equals(pls)) {
					/*
					 * on check si le joueur n'est pas dans la waiting room puis on check si je joueur n'est pas déjà afficher, si oui on
					 * update si non on l'ajoute
					 */

					if (!playersRender.containsKey(pls)) {
						if (playersRender.size() < 45) {
							ButtonGui button = new ButtonGui(pls, new float[] { 1, 1, 1, 1 }, 30, false);
							if (lastX != x) {
								if (lastX >= 2080) {
									lastX = defaultX;
									ySearchPlayer += ecartLigne;
								}
								button.update(lastX, ySearchPlayer, null, null);
								lastX += ecartX;
							} else {
								button.update(lastX, ySearchPlayer, null, null);
								lastX += ecartX;
							}
							playersRender.put(pls, button);
						} // TODO page
					} else {
						if (playersRender.containsKey(pls)) {
							ButtonGui button = playersRender.get(pls);
							button.update(0, 0, null, null);
							if (addfriendsButtons.contains(button.text)) {
								if (button.textColor[0] == 1) {
									button.textColor = new float[] { 0, 1, 0, 1 };
								}
							}
							if (button.isClicked()) {
								button.setClicked(false);
								if (clickTime == 0) {
									clickTime = 10;
									if (addfriendsButtons.contains(button.text)) {
										button.textColor = new float[] { 1, 1, 1, 1 };
										addfriendsButtons.remove(button.text);
									} else {
										if (addfriendsButtons.size() + WaitingRoom.waitingRoom.getPlayers().size() <= WaitingRoom.MAX_PLAYERS) {
											button.textColor = new float[] { 0, 0.7f, 0, 1 };
											addfriendsButtons.add(button.text);
										} else
											new NotificationGui("WAITING ROOM", "Plus assez de place !", 1, new float[] { 1, 0, 0, 1 }, true);
									}
								}
							}
						}
					}
				} else if (playersRender.containsKey(pls))
					playersRender.remove(pls);

			}
			return;
		}

		while (idfriendsButtons < friendsButtons.size())

		{ // TODO page par la suite
			ButtonGui button = friendsButtons.get(idfriendsButtons);
			if (WaitingRoom.waitingRoom.getPlayers().contains(button.text) || WaitingRoom.waitingRoom.getLeader().equals(button.text)) {
				friendsButtons.remove(button);
				if (addfriendsButtons.contains(button.text))
					addfriendsButtons.remove(button.text);
			} else {
				if (colone == maxColone) {
					colone = 0;
					y += ecartLigne;
					x = defaultX;
				}
				colone++;

				button.update(x, y, null, null);

				if (button.isClicked()) {
					button.setClicked(false);
					if (clickTime == 0) {
						clickTime = 10;
						if (addfriendsButtons.contains(button.text)) {
							button.textColor = new float[] { 1, 1, 1, 1 };
							addfriendsButtons.remove(button.text);
						} else {
							if (addfriendsButtons.size() + WaitingRoom.waitingRoom.getPlayers().size() <= WaitingRoom.MAX_PLAYERS) {
								button.textColor = new float[] { 0, 0.7f, 0, 1 };
								addfriendsButtons.add(button.text);
							} else {
								// Plus assez de place !
								new NotificationGui("WAITING ROOM", "Plus assez de place !", 1, new float[] { 1, 0, 0, 1 }, true);
							}
						}
					}
				}
				idfriendsButtons++;
				x += ecartX;
			}
		}

	}

	@Override
	public void render() {
		getDefaultBackGround().renderBackground();
		renderTexture(Textures.NETWORK_SEARCH_ZONE, 1060, 230, 383 + 10, 32 + 10);
		/*
		 * HEADER
		 */
		searchPlayer.render();
		validPlayers.render();
		retourWaitingRoom.render();

		if (searchPlayer.getText().length() == 0)
			drawText("Rechercher un joueur", 1135, 225 + 10, PosWidth.GAUCHE, PosHeight.HAUT, 24, new float[] { 0.7f, 0.7f, 0.7f, 1 });
		drawText("AJOUTER DES JOUEURS", 1250, 160, PosWidth.MILIEU, PosHeight.HAUT, 50);
		/**
		 * LIST des joueurs
		 */
		// sur le networl
		if (!searchPlayer.getText().isEmpty()) {
			for (ButtonGui buttonGui : playersRender.values())
				buttonGui.render();
		} else {
			// dans ses amis
			for (ButtonGui friend : friendsButtons) {
				if (friend.textColor[0] == 0)
					drawLine(friend.xx - 10, friend.yy + 18, friend.xx - 23, friend.yy + 18, 1);
				friend.render();
			}
		}

		int[] ligneId = new int[] { 0, 0, 0 };
		for (ButtonGui friend : !searchPlayer.getText().isEmpty() ? playersRender.values() : friendsButtons) {
			if (friend.xx == defaultX) {
				ligneId[0]++;
			} else if (friend.xx == defaultX + ecartX)
				ligneId[1]++;
			if (friend.xx == defaultX + ecartX * 2)
				ligneId[2]++;
		}
		if (ligneId[0] != 0)// -15
			drawLine(715, 300, 715, 300 + ecartLigne * ligneId[0], 1);
		if (ligneId[1] != 0)
			drawLine(715 + ecartX, 300, 715 + ecartX, 300 + ecartLigne * ligneId[1], 1);
		if (ligneId[2] != 0)
			drawLine(715 + ecartX * 2, 300, 715 + ecartX * 2, 300 + ecartLigne * ligneId[2], 1);
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_WAITING_ROOM_BG;
	}
}
