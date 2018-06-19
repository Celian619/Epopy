package net.epopy.epopy.games.gestion;

import static net.epopy.epopy.display.components.ComponentsHelper.drawQuad;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.utils.Pause;
import net.epopy.epopy.player.stats.AbstractStats;

public abstract class AbstractGameMenu {
	
	protected ButtonGui rejouerButton = new ButtonGui("Rejouer", new float[] { 0, 1, 0, 1 }, 50, false);
	protected ButtonGui reprendreButton = new ButtonGui("Reprendre", new float[] { 0, 1, 0, 1 }, 50, false);
	protected ButtonGui quitterButton = new ButtonGui("Quitter", new float[] { 1, 1, 1, 1 }, 50, false);

	private static ButtonGui sound = new ButtonGui(Textures.MENU_SOUND_ON, Textures.MENU_BTN_SOUND_ON);
	private static ButtonGui sound_moins = new ButtonGui("-", new float[] { 1, 1, 1, 1 }, 30, false);
	private static ButtonGui sound_plus = new ButtonGui("+", new float[] { 1, 1, 1, 1 }, 30, false);
	
	protected static Audios song = Audios.PING;
	protected static AbstractStats gameStats;

	private int w1Quitter, w2Quitter, w1Button, w2Button;

	public void renderEchap(final boolean pause) {
		renderEchap(pause, "", false);
	}
	
	public void renderEchap(final boolean pause, final String score, final boolean record) {
		renderEchap(pause, score, record ? "Record !" : "Score");
	}
	
	public void renderEchap(final boolean pause, final String subtitle, final String title) {

		Textures.GAME_ECHAP_BANDE.renderBackground();
		if (!pause) {
			drawText(title, defaultWidth / 2 + (subtitle.equals("") ? -7 : 30), defaultHeight / 2 - (subtitle.equals("") ? 0 : 40), PosWidth.MILIEU, PosHeight.MILIEU, 90, new float[] { 1, 1, 1, 1 });
			if (!subtitle.equals(""))
				drawText(subtitle, defaultWidth / 2 + 30, defaultHeight / 2 + 40, PosWidth.MILIEU, PosHeight.MILIEU, 50, new float[] { 1, 1, 1, 0.8f });
		}
		ButtonGui button = pause ? reprendreButton : rejouerButton;
		button.update(pause ? 1560 : 1590, 200, PosWidth.MILIEU, PosHeight.HAUT, 300, 50);
		quitterButton.update(450 - 55, 795, PosWidth.MILIEU, PosHeight.HAUT, 150, 50);

		sound.update(10, 10, PosWidth.GAUCHE, PosHeight.HAUT, 65, 65);
		sound_moins.update(80, 20, PosWidth.GAUCHE, PosHeight.HAUT, 30, 30);
		sound_plus.update(123, 21, PosWidth.GAUCHE, PosHeight.HAUT, 30, 30);

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
			Main.getPlayer().setSoundStatus(!Main.getPlayer().hasSound(), false);
			
			if (Main.getPlayer().hasSound()) song.start(true).setVolume(0.3f);
			
			sound.setClicked(false);
		}
		String text = "Objectif " + (gameStats.getRecord() > gameStats.getObjectif() ? "réussi !" : ": " + gameStats.getObjectifString());
		float[] color = gameStats.getRecord() > gameStats.getObjectif() ? new float[] { 0, 1, 0, 1 } : new float[] { 1, 0.5f, 0, 1 };
		
		drawText(text, defaultWidth - 5, defaultHeight - 5, PosWidth.DROITE, PosHeight.BAS, 40, color);
		
		if (sound != null) {
			sound.render();
			sound_moins.render();
			sound_plus.render();
			drawText(String.valueOf(Audios.VOLUME_VALUE), Audios.VOLUME_VALUE == 10 ? 92 : 100, 20, PosWidth.GAUCHE, PosHeight.HAUT, 30, new float[] { 1, 1, 1, 1 });
		}

		button.render();
		quitterButton.render();

		if (quitterButton.isOn()) {
			if (w1Quitter > 0)
				w1Quitter -= 40;
			else w1Quitter = 0;
			if (w2Quitter > 0)
				w2Quitter -= 40;
			else w2Quitter = 0;
		} else {
			if (w1Quitter < 520)
				w1Quitter += 20;
			else w1Quitter = 520;
			if (w2Quitter < 520)
				w2Quitter += 20;
			else w2Quitter = 520;
		}

		if (button.isOn()) {
			if (w1Button > 0)
				w1Button -= 40;
			else w1Button = 0;
			if (w2Button > 0)
				w2Button -= 40;
			else w2Button = 0;
		} else {
			if (w1Button < 520)
				w1Button += 20;
			else w1Button = 520;
			if (w2Button < 520)
				w2Button += 20;
			else w2Button = 520;
		}
		// solo bar bas
		drawQuad(42 - 17, 880, w1Quitter == 520 ? 523 : w1Quitter, 2);
		drawQuad(780 - 17, 770, w2Quitter == 520 ? -530 : -w2Quitter, 2);

		// button bars
		drawQuad(1390 - 17, 180, w1Button == 520 ? 523 : w1Button, 2);
		drawQuad(1730 - 17, 190 + 90, w2Button == 520 ? -530 : -w2Button, 2);

		if (quitterButton.isClicked()) {
			Audios.stopAll();
			Textures.unloadTextures();
			Main.getGameManager().getGameEnable().setStatus(false);
		}
	}

	public boolean gameOver;
	public boolean win;

	/**
	 * This int is the default size in width of the window
	 */
	public static int defaultWidth = 1920;

	/**
	 * This int is the default size in height of the window
	 */
	public static int defaultHeight = 1080;

	/*
	 * This object is for create a pause in game
	 */
	public static Pause pause = new Pause();

	/**
	 * This function is the activation function which is launched at the beginning of each game.
	 */
	public abstract void onEnable();

	/**
	 * This function is used to update all the mecanics of the game
	 */
	public abstract void update();

	/**
	 * This function is used to update the textures.
	 */
	public abstract void render();

}
