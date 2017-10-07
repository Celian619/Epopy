package net.epopy.epopy;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.DisplayManager;
import net.epopy.epopy.games.gestion.GameManager;
import net.epopy.epopy.player.Player;
import net.epopy.epopy.player.guis.ChooseGameTypeMenu;
import net.epopy.epopy.utils.Config;
import net.epopy.epopy.utils.FileUtils;
import net.epopy.epopy.utils.Input;
import net.epopy.sdk.security.Encryptor;

public class Main {
	
	private static DisplayManager displayManager;
	private static GameManager gameManager;
	private static Player player;
	
	// configs
	private static Map<String, Config> configs = new TreeMap<>();
	
	// encryp
	private static String KEY = "E1BB465D57CAE7ACDBBE8091F9CE83DF";
	private static String ALGORITMO = "AES/CBC/PKCS5Padding";
	private static String CODIFICACION = "UTF-8";
	private static Encryptor encryptor = new Encryptor(KEY, ALGORITMO, CODIFICACION);
	private static int slow = 0;
	private static boolean wasInvisible = false;
	
	public static void main(final String[] args) {
		FileUtils.checkFiles();
		
		new ChooseGameTypeMenu();
		Display.setResizable(true);
		
		loop();
	}
	
	private static void loop() {
		while (!Display.isCloseRequested()) {
			if (!Display.isVisible()) {

				Display.update();
				Display.sync(4);
				
				Audios.pauseAll();
				if (!wasInvisible) wasInvisible = true;
				continue;
			} else if (wasInvisible) {
				Audios.resumeAll();
				wasInvisible = false;
			}
			Input.checkInputFullscreen();
			displayManager.update();
			displayManager.render();
			
			// gestion du slow
			if (Input.isKeyDown(Keyboard.KEY_END) && Input.isKeyDown(Keyboard.KEY_DELETE)) {
				if (slow == 0) slow++;
				if (slow == 2) slow++;
			} else if (slow == 1) {
				slow++;
			} else if (slow == 3) slow = 0;
			
			if (slow != 0) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			// fin slow
		}
		
		exit();
		
	}
	
	public static void exit() {
		Config c = getPlayer().getConfig();
		c.setValue("display_width", Display.getWidth() + "");
		c.setValue("display_height", Display.getHeight() + "");
		c.setValue("display_x", Display.getX() + "");
		c.setValue("display_y", Display.getY() + "");
		c.setValue("display_fullscreen", Display.isFullscreen() + "");
		Display.destroy();
		System.exit(0);
	}

	/*
	 * Setters
	 */
	public static void setGameManager(final GameManager gameManager) {
		Main.gameManager = gameManager;
	}
	
	public static void setPlayer(final Player player) {
		Main.player = player;
	}
	
	public static void setDisplayManager(final DisplayManager displayManager) {
		Main.displayManager = displayManager;
	}
	
	public static void setConfig(final String key, final Config config) {
		configs.put(key, config);
	}
	
	/*
	 * Getters
	 */
	public static Player getPlayer() {
		return player;
	}
	
	public static GameManager getGameManager() {
		return gameManager;
	}
	
	public static DisplayManager getDisplayManager() {
		return displayManager;
	}
	
	public static Encryptor getEncryptor() {
		return encryptor;
	}
	
	public static Config getConfig(final String key) {
		return configs.get(key);
	}
	
}
