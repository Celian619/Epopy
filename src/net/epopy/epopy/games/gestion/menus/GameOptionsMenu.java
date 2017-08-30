package net.epopy.epopy.games.gestion.menus;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.games.gestion.GameStatus;

public class GameOptionsMenu extends AbstractGameMenu {

	private ButtonGui retour;
	private ButtonGui reset;
	private ButtonGui options;
	private int timeReset = 0;
	private ButtonGui accept;
	private ButtonGui refuse;
	private boolean validation;
	private ButtonGui gauche;
	private ButtonGui droite;

	private AbstractGame game;
	private int gameID;

	@Override
	public void onEnable() {
		retour = new ButtonGui(Textures.GAME_MENU_USERS_RETOUR_OFF, Textures.GAME_MENU_USERS_RETOUR_ON);
		reset = new ButtonGui("Réinitialiser", new float[] { 1, 0, 0, 1 }, 50);
		options = new ButtonGui("Options", new float[] { 1, 1, 1, 1 }, 30 * 2);
		accept = new ButtonGui("Oui", new float[] { 0, 1, 0, 1 }, 30 * 2);
		refuse = new ButtonGui("Non", new float[] { 1, 0, 0, 1 }, 30 * 2);
		gauche = new ButtonGui(Textures.GAME_MENU_GAUCHE_OFF, Textures.GAME_MENU_GAUCHE_ON);
		droite = new ButtonGui(Textures.GAME_MENU_DROITE_OFF, Textures.GAME_MENU_DROITE_ON);
	}

	@Override
	public void update() {
		retour.update(AbstractGameMenu.defaultWidth-17, 17, PositionWidth.DROITE, PositionHeight.HAUT, 50, 50);
		reset.update(140, 450, PositionWidth.GAUCHE, PositionHeight.MILIEU, 5 * 60, 60);
		accept.update(defaultWidth / 2 - 150, defaultHeight / 2 + 50, PositionWidth.GAUCHE, PositionHeight.MILIEU, 2* 60, 60);
		refuse.update(defaultWidth / 2 + 50, defaultHeight / 2 + 50, PositionWidth.GAUCHE, PositionHeight.MILIEU, 2* 60, 60);

		gauche.update(1500-50, 600, PositionWidth.DROITE, PositionHeight.MILIEU, 165/2, 148/2);
		droite.update(1500+150, 600, PositionWidth.GAUCHE, PositionHeight.MILIEU, 165/2, 148/2);

		if (retour.isClicked())
			Main.getGameManager().getGameEnable().setStatus(GameStatus.MENU_CHOOSE_USERS);
		else if (reset.isClicked()) {
			validation = true;
		}

		if(droite.isClicked()) {
			if(Main.getGameManager().getAbstractGame(gameID+1) != null) {
				gameID++;
				game = getGame(gameID);
			}
		}
		if(gauche.isClicked()) {
			if(Main.getGameManager().getAbstractGame(gameID-1) != null) {
				gameID--;
				game = getGame(gameID);
			}
		}
		if(validation) {
			if(refuse.isClicked())
				validation = false;
			if (accept.isClicked()) {
				Main.getPlayer().reset();
				timeReset = 60;
				validation = false;
			}
		}
		if(game != null && game.getMenuOptions() != null)
			game.getMenuOptions().update();
	}

	public AbstractGame getGame(int id) {
		AbstractGame game = GameList.valueOf(Main.getGameManager().getAbstractGame(id).getName().toUpperCase()).getAbstractGame();
		game.onEnable();
		game.getMenuOptions().onEnable();
		return game;
	}
	
	@Override
	public void render() {
		if(game == null) game = getGame(gameID = 1);

		/*
		 * Main
		 */
		Main.getGameManager().getGameEnable().getDefaultBackGround().renderBackground();
		Textures.GAME_BACKGROUND_80OPACITY.renderBackground();
		ComponentsHelper.drawText("Options", defaultWidth / 2, 0, PositionWidth.MILIEU, PositionHeight.HAUT, 100, new float[] { 1, 1f, 1, 1 });

		/*
		 * Title optins
		 */
		int hauteur = 330;
		ComponentsHelper.drawText("Général", 150, hauteur - 30, 70, new float[] { 1, 1f, 1, 1 });
		ComponentsHelper.drawText("Jeux", 1485, hauteur - 30, 70, new float[] { 1, 1f, 1, 1 });

		if(game != null && game.getMenuOptions() != null)
			game.getMenuOptions().render();

		ComponentsHelper.drawText(game.getName(), 1550, 600, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 1, 1, 1, 1 });

		/*
		 * réinitialiser
		 */
		if (validation) {
			ComponentsHelper.drawText("Êtes-vous sûr", defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.BAS, 80, new float[] { 1, 1f, 1, 1 });
			ComponentsHelper.drawText("vouloir réinitialiser ?", defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.HAUT, 80, new float[] { 1, 1f, 1, 1 });
			accept.render();
			refuse.render();
		}
		if (timeReset > 0) {
			timeReset--;
			ComponentsHelper.drawText("Réinitialisation avec succès !", defaultWidth / 2, defaultHeight / 2, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 1, 0f, 0, 1 });
		} 

		/*
		 * buttons
		 */
		reset.render();
		options.render();
		droite.render();
		gauche.render();
		retour.render();
	}

}
