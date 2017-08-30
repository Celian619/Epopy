package net.epopy.epopy.games.gestion.menus;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.games.gestion.GameStatus;
import net.epopy.epopy.player.guis.ChooseGameTypeMenu;
import net.epopy.epopy.player.stats.CarStats;
import net.epopy.epopy.player.stats.PongStats;
import net.epopy.epopy.player.stats.SnakeStats;
import net.epopy.epopy.player.stats.TankStats;

public class GameMenu extends AbstractGameMenu {

	private String name = null;
	private AbstractGame game;
	private ButtonGui retour;
	private ButtonGui gauche;
	private ButtonGui droite;
	private ButtonGui jouer;
	private ButtonGui user;
	private ButtonGui stats;
	private ButtonGui options;
	private ButtonGui quitterMenu;
	private ButtonGui sound;

	private boolean showStats = false;
	private boolean showOptions = false;

	private int w1Jouer = 0;
	private int w2Jouer = 0;

	@Override
	public void onEnable() {
		retour = new ButtonGui(Textures.GAME_MENU_USERS_RETOUR_OFF, Textures.GAME_MENU_USERS_RETOUR_ON);
		quitterMenu = new ButtonGui(Textures.GAME_MENU_QUITTER_OFF, Textures.GAME_MENU_QUITTER_ON);
		gauche = new ButtonGui(Textures.GAME_MENU_GAUCHE_OFF, Textures.GAME_MENU_GAUCHE_ON);
		droite = new ButtonGui(Textures.GAME_MENU_DROITE_OFF, Textures.GAME_MENU_DROITE_ON);
		jouer = new ButtonGui("Jouer", new float[] { 0, 0.7f, 0, 1 }, 50, false);
		stats = new ButtonGui("Stats", new float[] { 1, 1, 1, 1 }, 30, false);
		options = new ButtonGui("Options", new float[] { 1, 1, 1, 1 }, 30, false);
		user = new ButtonGui(Textures.GAME_MENU_USER_OFF, Textures.GAME_MENU_USER_ON);
		sound = new ButtonGui(Textures.MENU_SOUND_ON, Textures.MENU_BTN_SOUND_ON);
	}

	@Override
	public void update() {

		if (showStats) {
			quitterMenu.update(482 - 13, 263 - 13, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, 30);

		} else if (showOptions) {
			quitterMenu.update(482 - 13, 263 - 13, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, 30);
			if (game != null && game.getMenuOptions() != null)
				game.getMenuOptions().update();
		}

		if (quitterMenu.isClicked()) {
			showStats = false;
			showOptions = false;
		}

		sound.update(10, 10, PositionWidth.GAUCHE, PositionHeight.HAUT, 65, 65);

		if(!Main.getPlayer().getSound()) {
			sound.textureOff = Textures.MENU_SOUND_OFF;
			sound.textureOn = Textures.MENU_SOUND_OFF;
		} else {
			sound.textureOff = Textures.MENU_SOUND_ON;
			sound.textureOn = Textures.MENU_SOUND_ON;
		}

		if(sound.isClicked()) {
			Main.getPlayer().setSoundStatus(!Main.getPlayer().getSound());
			sound.setClicked(false);			
		}

		retour.update(AbstractGameMenu.defaultWidth - 17, 17, PositionWidth.DROITE, PositionHeight.HAUT, 50, 50);
		droite.update(AbstractGameMenu.defaultWidth - 200, AbstractGameMenu.defaultHeight / 2, PositionWidth.DROITE, PositionHeight.MILIEU, 165, 148);
		gauche.update(200, AbstractGameMenu.defaultHeight / 2, PositionWidth.GAUCHE, PositionHeight.MILIEU, 165, 148);

		jouer.update(1550, 840, PositionWidth.MILIEU, PositionHeight.HAUT, 130, 50);

		stats.update(1280, 980, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, 30);
		options.update(1730, 730, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, 30);

		if (stats.isOn())
			stats.setText(" Stats");
		else
			stats.setText("Stats");

		if (options.isOn())
			options.setText(" Options");
		else
			options.setText("Options");

		if (stats.isClicked()&& !name.equals("? ? ? ?")) {
			showStats = true;
			showOptions = false;
		}
		if (options.isClicked()&& !name.equals("? ? ? ?")) {
			showStats = false;
			showOptions = true;
		}

		if (retour.isClicked())
			new ChooseGameTypeMenu();

		if (jouer.isOn()) {
			if (w1Jouer > 0)
				w1Jouer -= 20;
			else w1Jouer = 0;
			if (w2Jouer > 0)
				w2Jouer -= 20;
			else w2Jouer = 0;
		} else {
			if (w1Jouer < 280)
				w1Jouer += 20;
			else w1Jouer = 280;
			if (w2Jouer < 280)
				w2Jouer += 20;
			else w2Jouer = 280;
		}

		if (jouer.isClicked() && !name.equals("? ? ? ?")) {
			showStats = false;
			showOptions = false;

			Main.getGameManager().setGameEnable(game);
			game.setStatus(GameStatus.IN_GAME);
			Main.getPlayer().setLastGame(GameList.valueOf(name.toUpperCase()).getID());
			Audios.LOBBY.stop();
		}
		if (droite.isClicked() && game != null) {
			GameList gameList = GameList.valueOf(game.getName().toUpperCase());
			int id = gameList.getID() + 1;
			AbstractGame nextGame = Main.getGameManager().getAbstractGame(id);
			if (nextGame != null) {
				if (Main.getPlayer().getLevel() >= id) {
					name = Main.getGameManager().getAbstractGame(id).getName();
					game = nextGame;
					game.onEnable();
					if (game.getMenuOptions() != null)
						game.getMenuOptions().onEnable();
				} else {
					name = "? ? ? ?";
					game = null;
				}
			}
		}
		if (gauche.isClicked()) {
			int id = 1;
			AbstractGame nextGame = null;
			if (game == null) {
				id = Main.getPlayer().getLevel();
				game = Main.getGameManager().getAbstractGame(id);
				nextGame = game;
			} else {
				GameList gameList = GameList.valueOf(game.getName().toUpperCase());
				id = gameList.getID() - 1;
				nextGame = Main.getGameManager().getAbstractGame(id);
			}
			if (nextGame != null) {
				name = Main.getGameManager().getAbstractGame(id).getName();
				game = nextGame;
				game.onEnable();
				if (game.getMenuOptions() != null)
					game.getMenuOptions().onEnable();
			}
		}
	}

	@Override
	public void render() {
		if (name == null) {
			game = Main.getGameManager().getGameEnable();
			name = game.getName();
			game.onEnable();
			if (game.getMenuOptions() != null)
				game.getMenuOptions().onEnable();
		}

		Textures.GAME_MENU_BG.renderBackground();

		if(sound != null)
			sound.render();

		if (showStats) {
			Textures.GAME_MENU_BG_STATS.renderBackground();
			quitterMenu.render();
			ComponentsHelper.drawText("Statistiques", AbstractGameMenu.defaultWidth / 2, 299, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 1, 1, 1, 1 });

			if (game != null) {
				String temps = "";
				String record = "";
				String parties = "";

				if (GameList.SNAKE.toString().toLowerCase().equals(name.toLowerCase())) {
					SnakeStats snakeStats = Main.getPlayer().getSnakeStats();
					temps = snakeStats.getTemps();
					record = String.valueOf(snakeStats.getRecord() + " pts");
					parties = String.valueOf(snakeStats.getParties());
				} else if (GameList.PONG.toString().toLowerCase().equals(name.toLowerCase())) {
					PongStats pongStats = Main.getPlayer().getPongStats();
					temps = pongStats.getTemps();
					record = pongStats.getRecordString();
					parties = String.valueOf(pongStats.getParties());
				} else if (GameList.CAR.toString().toLowerCase().equals(name.toLowerCase())) {
					CarStats carStats = Main.getPlayer().getCarStats();
					temps = carStats.getTemps();
					record = carStats.getRecordString();
					parties = String.valueOf(carStats.getParties());
				} else if (GameList.TANK.toString().toLowerCase().equals(name.toLowerCase())) {
					TankStats tankStats = Main.getPlayer().getTankStats();
					temps = tankStats.getTemps();
					record = tankStats.getRecordString();
					parties = String.valueOf(tankStats.getParties());
				}
				// temps
				int lastXTemps = ComponentsHelper.drawText("Temps de jeu", 780 - 10, 353, 30, new float[] { 1, 1f, 1, 1 });
				ComponentsHelper.drawText(temps, lastXTemps + 35, ComponentsHelper.getResponsiveY(353), PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 0, 0.7f, 0, 1 }, false);
				// nombre de partie jouées
				int lastXPartie = ComponentsHelper.drawText("Parties jouées", 770 - 10, 530, 30, new float[] { 1, 1f, 1, 1 });
				ComponentsHelper.drawText(parties, lastXPartie + 35, ComponentsHelper.getResponsiveY(530), PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 0, 0.7f, 0, 1 }, false);
				// dernier record
				int lastXRecord = ComponentsHelper.drawText("Record", 856 - 5, 718, 30, new float[] { 1, 1f, 1, 1 });
				ComponentsHelper.drawText(record, lastXRecord + 30, ComponentsHelper.getResponsiveY(718), PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 0, 0.7f, 0, 1 }, false);
			}

		} else if (showOptions) {
			Textures.GAME_MENU_BG_STATS.renderBackground();
			quitterMenu.render();
			ComponentsHelper.drawText("Options", AbstractGameMenu.defaultWidth / 2, 299, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 1, 1, 1, 1 });

			if (game != null && game.getMenuOptions() != null)
				game.getMenuOptions().render();
		} else {
			if (game != null) {
				ComponentsHelper.renderTexture(game.getDefaultBackGround(), 483, 267, 958, 539);
			}
		}
		if(name.equals("? ? ? ?") && showOptions || name.equals("? ? ? ?") && showStats) {
			showOptions = false;
			showStats = false;
		}
		if(!name.equals("? ? ? ?")) {
			Textures.GAME_MENU_SCOTCH.renderBackground();
			// Bas
			ComponentsHelper.drawQuad(1297 + 18, 930 - 10, w1Jouer == 280 ? 287 : w1Jouer, 2);
			// Haut
			ComponentsHelper.drawQuad(1768 + 18, 835 - 10, w2Jouer == 280 ? -287 : -w2Jouer, 2);

			jouer.render();
			stats.render();
			options.render();
		}

		// user.render();
		retour.render();

		gauche.render();
		droite.render();
		ComponentsHelper.drawText(name, 370, 200, PositionWidth.MILIEU, PositionHeight.MILIEU, 65);
		NotificationGui.render();
	}

}
