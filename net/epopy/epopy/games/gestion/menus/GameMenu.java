package net.epopy.epopy.games.gestion.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawQuad;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;
import static net.epopy.epopy.display.components.ComponentsHelper.renderTexture;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.player.Player;
import net.epopy.epopy.player.guis.ChooseGameTypeMenu;
import net.epopy.epopy.player.stats.CarStats;
import net.epopy.epopy.player.stats.PingStats;
import net.epopy.epopy.player.stats.PlaceInvaderStats;
import net.epopy.epopy.player.stats.SnakeStats;
import net.epopy.epopy.player.stats.SpeedRunStats;
import net.epopy.epopy.player.stats.TankStats;
import net.epopy.epopy.player.stats.TetrasStats;
import net.epopy.epopy.utils.WebPage;

public class GameMenu extends AbstractGameMenu {

	private String name = null;
	private AbstractGame game;
	private static ButtonGui retour = new ButtonGui(Textures.GAME_MENU_USERS_RETOUR_OFF, Textures.GAME_MENU_USERS_RETOUR_ON);;
	private static ButtonGui gauche = new ButtonGui(Textures.GAME_MENU_GAUCHE_OFF, Textures.GAME_MENU_GAUCHE_ON);
	private static ButtonGui droite = new ButtonGui(Textures.GAME_MENU_DROITE_OFF, Textures.GAME_MENU_DROITE_ON);;
	private static ButtonGui jouer = new ButtonGui("Jouer", new float[] { 0, 0.7f, 0, 1 }, 50, false);
	private static ButtonGui stats = new ButtonGui("Stats", new float[] { 1, 1, 1, 1 }, 30, false);
	private static ButtonGui options = new ButtonGui("Options", new float[] { 1, 1, 1, 1 }, 30, false);
	private static ButtonGui quitterMenu = new ButtonGui(Textures.GAME_MENU_QUITTER_OFF, Textures.GAME_MENU_QUITTER_ON);
	private static ButtonGui sound = new ButtonGui(Textures.MENU_SOUND_ON, Textures.MENU_BTN_SOUND_ON);

	private static ButtonGui sound_moins = new ButtonGui("-", new float[] { 1, 1, 1, 1 }, 30, false);
	private static ButtonGui sound_plus = new ButtonGui("+", new float[] { 1, 1, 1, 1 }, 30, false);

	private static ButtonGui twitterButton = new ButtonGui(Textures.LOGO_TWITTER_OFF, Textures.LOGO_TWITTER_ON);

	private boolean showStats = false;
	private boolean showOptions = false;

	private int w1Jouer = 0;
	private int w2Jouer = 0;

	@Override
	public void onEnable() {

	}

	private float i = 0;
	boolean soundCrescendo;

	@Override
	public void update() {
		
		if (!Audios.LOBBY.isRunning() && Main.getPlayer().hasSound()) {
			Audios.LOBBY.start(true).setVolume(0.1f);
			soundCrescendo = true;
			i = 0;
		}

		if (soundCrescendo) {
			if (i == 10)
				Audios.LOBBY.setVolume(0.15f);
			else if (i == 20)
				Audios.LOBBY.setVolume(0.2f);
			else if (i == 40)
				Audios.LOBBY.setVolume(0.25f);
			else if (i == 80) {
				Audios.LOBBY.setVolume(0.3f);
				soundCrescendo = false;
			}
			i++;
		}
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

		/*
		 * sound
		 */

		sound.update(10, 10, PositionWidth.GAUCHE, PositionHeight.HAUT, 65, 65);
		sound_moins.update(80, 20, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, 30);
		sound_plus.update(123, 21, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, 30);
		twitterButton.update(AbstractGameMenu.defaultWidth - 60, AbstractGameMenu.defaultHeight - 10, PositionWidth.GAUCHE, PositionHeight.BAS, 50, 50);

		if (sound_moins.isClicked()) {
			if (Audios.VOLUME_VALUE > 1) {
				Audios.VOLUME_VALUE -= 1;
				Main.getPlayer().setSoundLevel(Audios.VOLUME_VALUE);
				Audios.updateAllVolume();
			}
		}

		if (sound_plus.isClicked()) {
			if (Audios.VOLUME_VALUE < 10) {
				Audios.VOLUME_VALUE += 1;
				Main.getPlayer().setSoundLevel(Audios.VOLUME_VALUE);
				Audios.updateAllVolume();
			}
		}

		if (!Main.getPlayer().hasSound()) {
			sound.textureOff = Textures.MENU_SOUND_OFF;
			sound.textureOn = Textures.MENU_SOUND_OFF;
		} else {
			sound.textureOff = Textures.MENU_SOUND_ON;
			sound.textureOn = Textures.MENU_SOUND_ON;
		}

		if (sound.isClicked()) {
			Main.getPlayer().setSoundStatus(!Main.getPlayer().hasSound());
			sound.setClicked(false);
		}
		if (twitterButton.isClicked()) {// tweeter avec @EpopyOfficiel
			new WebPage("https://twitter.com/intent/tweet?screen_name=EpopyOfficiel&ref_src=twsrc%5Etfw");
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

		if (stats.isClicked() && !name.equals("? ? ? ?")) {
			showStats = true;
			showOptions = false;
		}
		if (options.isClicked() && !name.equals("? ? ? ?")) {
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

		if (jouer.isClicked()) {
			showStats = false;
			showOptions = false;

			Main.getGameManager().setGameEnable(game);
			game.setStatus(true);
			Main.getPlayer().setLastGame(GameList.valueOf(name.toUpperCase()).getID());
			Audios.LOBBY.stop();
		}
		if (droite.isClicked() && game != null && Main.getPlayer().getLevel() >= GameList.valueOf(game.getName().toUpperCase()).getID() + 1) {
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
				}
			}
		}
		if (gauche.isClicked() && GameList.valueOf(game.getName().toUpperCase()).getID() - 1 != 0) {
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
		float[] white = new float[] { 1, 1, 1, 1 };
		if (name == null) {
			game = Main.getGameManager().getGameEnable();
			name = game.getName();
			game.onEnable();
			if (game.getMenuOptions() != null)
				game.getMenuOptions().onEnable();
		}
		
		Textures.GAME_MENU_BG.renderBackground();
		if (twitterButton.isOn())
			drawText("Partage ton expérience de jeu !", AbstractGameMenu.defaultWidth - 70, AbstractGameMenu.defaultHeight - 24, PositionWidth.DROITE, PositionHeight.BAS, 20);
		if (sound != null) {
			sound.render();
			sound_moins.render();
			sound_plus.render();
			drawText(String.valueOf(Audios.VOLUME_VALUE), Audios.VOLUME_VALUE == 10 ? 92 : 100, 20, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, white);
		}
		
		if (showStats) {
			Textures.GAME_MENU_BG_STATS.renderBackground();
			quitterMenu.render();
			drawText("Statistiques", AbstractGameMenu.defaultWidth / 2, 299, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, white);
			
			if (game != null) {
				String temps = "";
				String record = "";
				String parties = "";
				String nom = name.toLowerCase();
				Player p = Main.getPlayer();
				if (GameList.SNAKE.toString().toLowerCase().equals(nom)) {
					SnakeStats snakeStats = p.getSnakeStats();
					temps = changeTpsTxt(snakeStats.getTemps());
					record = snakeStats.getRecord() + " point" + (snakeStats.getRecord() <= 1 ? "" : "s");
					parties = snakeStats.getParties() + "";
				} else if (GameList.PING.toString().toLowerCase().equals(nom)) {
					PingStats pingStats = p.getPingStats();
					temps = changeTpsTxt(pingStats.getTemps());
					record = pingStats.getRecord() + "s";
					parties = pingStats.getParties() + "";
				} else if (GameList.CAR.toString().toLowerCase().equals(nom)) {
					CarStats carStats = p.getCarStats();
					temps = changeTpsTxt(carStats.getTemps());
					record = carStats.getRecord() + "s";
					parties = carStats.getParties() + "";
				} else if (GameList.TANK.toString().toLowerCase().equals(nom)) {
					TankStats tankStats = p.getTankStats();
					temps = changeTpsTxt(tankStats.getTemps());
					record = tankStats.getRecordString();
					parties = tankStats.getParties() + "";
				} else if (GameList.PLACEINVADER.toString().toLowerCase().equals(nom)) {
					PlaceInvaderStats placeInvaderStats = p.getPlaceInvaderStats();
					temps = changeTpsTxt(placeInvaderStats.getTemps());
					record = placeInvaderStats.getRecord() + " point" + (placeInvaderStats.getRecord() <= 1 ? "" : "s");
					parties = placeInvaderStats.getParties() + "";
				} else if (GameList.SPEEDRUN.toString().toLowerCase().equals(nom)) {
					SpeedRunStats speedRunStats = p.getSpeedRunStats();
					temps = changeTpsTxt(speedRunStats.getTemps());
					record = speedRunStats.getRecord() + "s";
					parties = speedRunStats.getParties() + "";
				} else if (GameList.TETRAS.toString().toLowerCase().equals(nom)) {
					TetrasStats tetrasStats = p.getTetrasStats();
					temps = changeTpsTxt(tetrasStats.getTemps());
					record = tetrasStats.getRecord() + " point" + (tetrasStats.getRecord() <= 1 ? "" : "s");
					parties = tetrasStats.getParties() + "";
				}

				float[] green = new float[] { 0, 0.7f, 0, 1 };
				// temps
				drawText("Temps de jeu", 930, 368, PositionWidth.DROITE, PositionHeight.MILIEU, 30, white);
				drawText(temps, 990, 368, PositionWidth.GAUCHE, PositionHeight.MILIEU, 30, green);
				// nombre de partie jouées
				drawText("Parties jouées", 930, 547, PositionWidth.DROITE, PositionHeight.MILIEU, 30, white);
				drawText(parties, 990, 547, PositionWidth.GAUCHE, PositionHeight.MILIEU, 30, green);
				// dernier record
				drawText("Record", 930, 736, PositionWidth.DROITE, PositionHeight.MILIEU, 30, white);
				drawText(record, 990, 736, PositionWidth.GAUCHE, PositionHeight.MILIEU, 30, green);
			}
			
		} else if (showOptions) {
			Textures.GAME_MENU_BG_STATS.renderBackground();
			quitterMenu.render();
			drawText("Options", AbstractGameMenu.defaultWidth / 2, 299, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, white);
			
			if (game != null && game.getMenuOptions() != null)
				game.getMenuOptions().render();
		} else {
			if (game != null) {
				renderTexture(game.getDefaultBackGround(), 483, 267, 958, 539);
			}
		}
		
		Textures.GAME_MENU_SCOTCH.renderBackground();
		// Bas
		drawQuad(1297 + 18, 930 - 10, w1Jouer == 280 ? 287 : w1Jouer, 2);
		// Haut
		drawQuad(1768 + 18, 835 - 10, w2Jouer == 280 ? -287 : -w2Jouer, 2);
		
		jouer.render();
		stats.render();
		options.render();
		twitterButton.render();
		
		retour.render();
		
		if (GameList.valueOf(game.getName().toUpperCase()).getID() - 1 != 0)
			gauche.render();
			
		if (Main.getPlayer().getLevel() > GameList.valueOf(game.getName().toUpperCase()).getID()) {
			droite.render();
			
		}
		
		drawText(name, 370, 200, PositionWidth.MILIEU, PositionHeight.MILIEU, 65);
		NotificationGui.render();
		
	}
	
	public String changeTpsTxt(int seconds) {
		int minutes = 0;
		int hours = 0;
		while (seconds >= 60) {
			minutes++;
			seconds -= 60;
		}
		while (minutes >= 60) {
			hours++;
			minutes -= 60;
		}

		return (hours < 10 ? "0" : "") + hours + ":" + (minutes < 10 ? "0" : "") + minutes + ":" + (seconds < 10 ? "0" : "") + +seconds;
	}

}
