package net.epopy.network.games.tank;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Team;
import net.epopy.network.games.waitingroom.WaitingRoom;

public class TankMenuEnd {
	
	// team winner
	public static Team TEAM_WINNER = null;
	
	public static String[] TOP_COINS = new String[] { "", "", "" };
	public static String[] TOP_VICTIMES = new String[] { "", "", "" };
	public static String[] TOP_POINTS = new String[] { "", "", "" };
	
	// player stats
	public static int PLAYER_KILLS = 0;
	public static int PLAYER_MORT = 0;
	public static int PLAYER_POINTS = 0;
	public static int PLAYER_COINS = 0;
	
	private static ButtonGui quitterButton = new ButtonGui("Quitter", new float[] { 0.8f, 0, 0, 1 }, 50, false);
	private int w1Quitter = 0;
	private int w2Quitter = 0;
	
	public TankMenuEnd() {
		TOP_COINS = new String[] { "", "", "" };
		TOP_VICTIMES = new String[] { "", "", "" };
		TOP_POINTS = new String[] { "", "", "" };
		PLAYER_KILLS = 0;
		PLAYER_MORT = 0;
		PLAYER_POINTS = 0;
		PLAYER_COINS = 0;
		TEAM_WINNER = null;
	}
	
	public void update() {
		quitterButton.update(1550 + 120, 840 + 80, PositionWidth.MILIEU, PositionHeight.HAUT, 130, 80);
		if (quitterButton.isOn()) {
			if (w1Quitter > 0)
				w1Quitter -= 20;
			else w1Quitter = 0;
			if (w2Quitter > 0)
				w2Quitter -= 20;
			else w2Quitter = 0;
		} else {
			if (w1Quitter < 280)
				w1Quitter += 20;
			else w1Quitter = 280;
			if (w2Quitter < 280)
				w2Quitter += 20;
			else w2Quitter = 280;
		}
		
		if (quitterButton.isClicked()) NetworkPlayer.setGame(new WaitingRoom());
		
	}
	
	private final float[] colorTop = new float[] { 1, 1, 0, 1 };
	
	public void render() {
		
		getDefaultBackGround().renderBackground();
		Textures.NETWORK_GAME_END_BG.renderBackground();
		/*
		 * Header
		 */
		ComponentsHelper.drawText("Victoire des", AbstractGameMenu.defaultWidth / 2 - 40 - 30 - (TEAM_WINNER.getName().equals("RED") ? 20 : 0), 171, PositionWidth.MILIEU, PositionHeight.HAUT, 48);
		if (TEAM_WINNER != null)
			ComponentsHelper.drawText(TEAM_WINNER.getName().equals("RED") ? "Rouges" : TEAM_WINNER.getName().equals("BLUE") ? "bleus" : TEAM_WINNER.getName(), AbstractGameMenu.defaultWidth / 2 + 170 - 30, 171, PositionWidth.MILIEU, PositionHeight.HAUT, 48, TEAM_WINNER.getColor());
		ComponentsHelper.drawText("Récapitulatif", AbstractGameMenu.defaultWidth / 2, 250, PositionWidth.MILIEU, PositionHeight.HAUT, 35, new float[] { 0, 1, 0, 1 });
		ComponentsHelper.drawLine(860, 290, 1060, 290, 1);
		
		/*
		 * Classement des Meilleur joueurs
		 */
		ComponentsHelper.drawText("Meilleur joueurs", AbstractGameMenu.defaultWidth / 2 - 400, 330, PositionWidth.MILIEU, PositionHeight.HAUT, 35);
		ComponentsHelper.drawText("© Top coins ©", AbstractGameMenu.defaultWidth / 2 - 400, 380, PositionWidth.MILIEU, PositionHeight.HAUT, 30, colorTop);
		for (int i = 1; i < 4; i++) {
			if (!TOP_COINS[i - 1].equals("null"))
				ComponentsHelper.drawText(TOP_COINS[i - 1], AbstractGameMenu.defaultWidth / 2 - 400, 390 + 30 * i, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		}
		// top pour celui qui à tuer le plus de personne
		ComponentsHelper.drawText("Φ Top victimes Φ", AbstractGameMenu.defaultWidth / 2 - 400, 515, PositionWidth.MILIEU, PositionHeight.HAUT, 30, colorTop);
		for (int i = 1; i < 4; i++) {
			if (!TOP_VICTIMES[i - 1].equals("null"))
				ComponentsHelper.drawText(TOP_VICTIMES[i - 1], AbstractGameMenu.defaultWidth / 2 - 400, 520 + 30 * i, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		}
		// total de points captures pour l'equipe
		ComponentsHelper.drawText("• Points obtenus •", AbstractGameMenu.defaultWidth / 2 - 400, 643, PositionWidth.MILIEU, PositionHeight.HAUT, 30, colorTop);
		for (int i = 1; i < 4; i++) {
			if (!TOP_POINTS[i - 1].equals("null"))
				ComponentsHelper.drawText(TOP_POINTS[i - 1], AbstractGameMenu.defaultWidth / 2 - 400, 650 + 30 * i, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		}
		/*
		 * Statistique du joueur
		 */
		ComponentsHelper.drawText("Mes statistiques", AbstractGameMenu.defaultWidth / 2 + 400, 330, PositionWidth.MILIEU, PositionHeight.HAUT, 35);
		int xStats = 340;
		ComponentsHelper.drawText("Victimes: " + PLAYER_KILLS, AbstractGameMenu.defaultWidth / 2 + 400, xStats + 30, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		ComponentsHelper.drawText("Morts: " + PLAYER_MORT, AbstractGameMenu.defaultWidth / 2 + 400, xStats + 30 * 2, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		ComponentsHelper.drawText("Coins gagnés: " + PLAYER_COINS, AbstractGameMenu.defaultWidth / 2 + 400, xStats + 30 * 3, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		ComponentsHelper.drawText("Points obtenus: " + PLAYER_POINTS, AbstractGameMenu.defaultWidth / 2 + 400, xStats + 30 * 4, PositionWidth.MILIEU, PositionHeight.HAUT, 28);
		
		/*
		 * Nav bar
		 */
		
		// Bas
		ComponentsHelper.drawQuad(1297 + 18 + 134, 930 - 10 + 80, w1Quitter == 280 ? 287 : w1Quitter, 2);
		// Haut
		ComponentsHelper.drawQuad(1768 + 18 + 134, 835 - 10 + 80, w2Quitter == 280 ? -287 : -w2Quitter, 2);
		
		quitterButton.render();
		
	}
	
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_GAME_TANK_MAP;
	}
}
