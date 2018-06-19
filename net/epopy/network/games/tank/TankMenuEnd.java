package net.epopy.network.games.tank;

import static net.epopy.epopy.display.components.ComponentsHelper.drawLine;
import static net.epopy.epopy.display.components.ComponentsHelper.drawQuad;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.games.modules.Team;
import net.epopy.network.games.waitingroom.WaitingRoom;

public class TankMenuEnd {

	public static int PLAYER_KILLS, PLAYER_MORT, PLAYER_POINTS, PLAYER_COINS;
	public static String[] TOP_COINS, TOP_VICTIMES, TOP_POINTS;
	public static Team TEAM_WINNER;

	private static ButtonGui quitterButton = new ButtonGui("Quitter", new float[] { 0.8f, 0, 0, 1 }, 50, false);
	
	private final float[] colorTop = new float[] { 1, 1, 0, 1 };
	
	private int w1Quitter, w2Quitter;

	public TankMenuEnd() {
		TOP_COINS = new String[] { "", "", "" };
		TOP_VICTIMES = new String[] { "", "", "" };
		TOP_POINTS = new String[] { "", "", "" };
		PLAYER_KILLS = PLAYER_MORT = PLAYER_POINTS = PLAYER_COINS = 0;

		TEAM_WINNER = null;
	}

	public void update() {
		
		quitterButton.update(1670, 920, PosWidth.MILIEU, PosHeight.HAUT, 130, 80);
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

	public void render() {

		getDefaultBackGround().renderBackground();
		Textures.NETWORK_GAME_END_BG.renderBackground();
		/*
		 * Nav bar
		 */

		// Bas
		drawQuad(1449, 1000, w1Quitter == 280 ? 287 : w1Quitter, 2);
		// Haut
		drawQuad(1920, 905, w2Quitter == 280 ? -287 : -w2Quitter, 2);
		
		quitterButton.render();
		int mid = AbstractGameMenu.defaultWidth / 2;
		/*
		 * Header
		 */
		if (TEAM_WINNER != null) {
			drawText("Victoire des", mid - 70 - (TEAM_WINNER.getName().equals("RED") ? 20 : 0), 171, PosWidth.MILIEU, PosHeight.HAUT, 48);
			drawText(TEAM_WINNER.getName().equals("RED") ? "Rouges" : TEAM_WINNER.getName().equals("BLUE") ? "Bleus" : TEAM_WINNER.getName(), mid + 140, 171, PosWidth.MILIEU, PosHeight.HAUT, 48, TEAM_WINNER.getColor());
		} else drawText("Match nul !", mid, 171, PosWidth.MILIEU, PosHeight.HAUT, 48);

		drawText("Récapitulatif", mid, 250, PosWidth.MILIEU, PosHeight.HAUT, 35, new float[] { 0, 1, 0, 1 });
		drawLine(860, 290, 1060, 290, 1);

		/*
		 * Classement des Meilleur joueurs
		 */
		drawText("Meilleur joueurs", mid - 400, 330, PosWidth.MILIEU, PosHeight.HAUT, 35);
		drawText("© Points obtenus ©", mid - 400, 380, PosWidth.MILIEU, PosHeight.HAUT, 30, colorTop);
		for (int i = 1; i < 4; i++) {
			if (!TOP_POINTS[i - 1].equals("null"))
				drawText(TOP_POINTS[i - 1], mid - 400, 390 + 30 * i, PosWidth.MILIEU, PosHeight.HAUT, 28);
		}
		// top pour celui qui à tuer le plus de personne
		drawText("Φ Top victimes Φ", mid - 400, 515, PosWidth.MILIEU, PosHeight.HAUT, 30, colorTop);
		for (int i = 1; i < 4; i++) {
			if (!TOP_VICTIMES[i - 1].equals("null"))
				drawText(TOP_VICTIMES[i - 1], mid - 400, 520 + 30 * i, PosWidth.MILIEU, PosHeight.HAUT, 28);
		}
		/**
		 * total de points captures pour l'equipe ComponentsHelper.drawText("• Points obtenus •", AbstractGameMenu.defaultWidth / 2 - 400,
		 * 643, PositionWidth.MILIEU, PositionHeight.HAUT, 30, colorTop); for (int i = 1; i < 4; i++) { if (!TOP_POINTS[i -
		 * 1].equals("null")) ComponentsHelper.drawText(TOP_POINTS[i - 1], AbstractGameMenu.defaultWidth / 2 - 400, 650 + 30 * i,
		 * PositionWidth.MILIEU, PositionHeight.HAUT, 28); }
		 */
		/*
		 * Statistique du joueur
		 */
		drawText("Mes statistiques", mid + 400, 330, PosWidth.MILIEU, PosHeight.HAUT, 35);
		drawText("Victimes: " + PLAYER_KILLS, mid + 400, 370, PosWidth.MILIEU, PosHeight.HAUT, 28);
		drawText("Morts: " + PLAYER_MORT, mid + 400, 400, PosWidth.MILIEU, PosHeight.HAUT, 28);
		drawText("Coins gagnés: " + PLAYER_COINS, mid + 400, 430, PosWidth.MILIEU, PosHeight.HAUT, 28);
		drawText("Points obtenus: " + PLAYER_POINTS, mid + 400, 460, PosWidth.MILIEU, PosHeight.HAUT, 28);

	}
	
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_GAME_TANK_MAP;
	}

}
