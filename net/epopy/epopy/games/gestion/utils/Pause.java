package net.epopy.epopy.games.gestion.utils;

import java.util.Calendar;
import java.util.Date;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class Pause {

	private long startPause; // début de la pause
	private boolean isFinish; // savoir si la pause est en cours ou fini
	private int timePause; // durée de la pause
	private int timeTotal;// durée de la pause total
	private boolean isStarted;

	/**
	 * Class pour crée une pause facilement
	 *
	 * Exemple: Pause pause = new Pause(); pause.startPause(20); while (!pause.isFinish()) { System.out.println("Pause: " +
	 * pause.getPauseString()); }
	 *
	 */
	public Pause() {
		isFinish = true;
	}

	/**
	 * Lancement de la pause
	 *
	 * @param timePause
	 *            la durée de la pause en secondes
	 */
	public void startPause(final int timePause) {
		startPause = System.currentTimeMillis();
		this.timePause = timePause;
		timeTotal = timePause;
		isFinish = false;
		isStarted = true;
	}

	public boolean isStarted() {
		return isStarted;
	}

	/**
	 * Donne la durée de la pause total
	 *
	 * @return la durée en secondes
	 */
	public int getTimePauseTotal() {
		return timeTotal;
	}

	/**
	 * Donne la durée de la pause
	 *
	 * @return la durée en secondes
	 */
	public int getTimePause() {
		return timePause;
	}

	/**
	 * Pour arrêter le pause
	 */
	public void stopPause() {
		isFinish = true;
	}
	
	/**
	 * Fonction qui permet de savoir si la pause fini
	 *
	 * @return false = pause en cours | true = pause terminée
	 */
	public boolean isFinish() {
		return isFinish;
	}

	/**
	 * Donne le pause en joli format
	 *
	 * @return le temps qu il reste de la pause en secondes (ex: 03,04...)
	 */

	@SuppressWarnings("deprecation")
	public String getPauseString() {
		Date timeDiff = new Date(Calendar.getInstance().getTimeInMillis() - startPause - 3600000);
		
		int secondes = timePause - timeDiff.getSeconds();
		
		if (secondes == 0)
			return "GO";
		else if (secondes > 0)
			return (secondes < 10 ? "0" : "") + secondes;
		else if (secondes < 0)
			isFinish = true;

		return "";
	}

	@SuppressWarnings("deprecation")
	public void showRestartChrono() {
		Date timeDiff = new Date(Calendar.getInstance().getTimeInMillis() - startPause - 3600000);
		
		int secondes = timePause - timeDiff.getSeconds();
		if (secondes == 1)
			ComponentsHelper.renderTexture(Textures.GAME_CHRONO_1, AbstractGameMenu.defaultWidth / 2 - 64, AbstractGameMenu.defaultHeight / 2 - 64, 128, 128);
		else if (secondes == 2)
			ComponentsHelper.renderTexture(Textures.GAME_CHRONO_2, AbstractGameMenu.defaultWidth / 2 - 64, AbstractGameMenu.defaultHeight / 2 - 64, 128, 128);
		else if (secondes == 3)
			ComponentsHelper.renderTexture(Textures.GAME_CHRONO_3, AbstractGameMenu.defaultWidth / 2 - 64, AbstractGameMenu.defaultHeight / 2 - 64, 128, 128);
		else if (secondes < 0)
			isFinish = true;
	}
}
