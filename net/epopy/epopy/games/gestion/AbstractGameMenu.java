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

public abstract class AbstractGameMenu {

	// buttons

	protected ButtonGui rejouerButton = new ButtonGui("Rejouer", new float[] { 0, 1, 0, 1 }, 50, false);

	protected ButtonGui reprendreButton = new ButtonGui("Reprendre", new float[] { 1, 1, 1, 1 }, 50, false);

	protected ButtonGui quitterButton = new ButtonGui("Quitter", new float[] { 1, 1, 1, 1 }, 50, false);
	
	private int w1Quitter = 0;
	private int w2Quitter = 0;
	private int w1Button = 0;
	private int w2Button = 0;

	public void renderEchap(final boolean pause) {
		renderEchap(pause, "", false);
	}
	
	public void renderEchap(final boolean pause, final String score, final boolean record) {
		renderEchap(pause, score, record ? "Record !" : "Score");
	}
	
	public void renderEchap(final boolean pause, final String subtitle, final String title) {
		// Textures.GAME_BACKGROUND_80OPACITY.renderBackground();
		Textures.GAME_ECHAP_BANDE.renderBackground();
		if (!pause) {
			drawText(title, defaultWidth / 2 + (subtitle.equals("") ? -7 : 30), defaultHeight / 2 - (subtitle.equals("") ? 0 : 40), PosWidth.MILIEU, PosHeight.MILIEU, 90, new float[] { 1, 1, 1, 1 });
			if (!subtitle.equals(""))
				drawText(subtitle, defaultWidth / 2 + 30, defaultHeight / 2 + 40, PosWidth.MILIEU, PosHeight.MILIEU, 50, new float[] { 1, 1, 1, 0.8f });
		}
		ButtonGui button = pause ? reprendreButton : rejouerButton;
		button.update(pause ? 1560 : 1590, 200, PosWidth.MILIEU, PosHeight.HAUT, 300, 50);
		quitterButton.update(450 - 55, 795, PosWidth.MILIEU, PosHeight.HAUT, 150, 50);

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

	public boolean gameOver = false;

	public boolean win = false;

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
