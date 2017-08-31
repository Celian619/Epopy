package net.epopy.epopy;

import java.util.Map;
import java.util.TreeMap;

import org.lwjgl.opengl.Display;

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
	private static Map<String, Config> configs = new TreeMap<>();;
	
	// encryp
	private static String KEY = "E1BB465D57CAE7ACDBBE8091F9CE83DF";
	private static String ALGORITMO = "AES/CBC/PKCS5Padding";
	private static String CODIFICACION = "UTF-8";
	private static Encryptor encryptor = new Encryptor(KEY, ALGORITMO, CODIFICACION);;
	
	public static void main(final String[] args) {
		FileUtils.checkFiles();
		
		new ChooseGameTypeMenu();
		Display.setResizable(true);
		loop();
	}
	
	private static void loop() {
		while (!Display.isCloseRequested()) {
			Input.checkInputFullscreen();
			displayManager.update();
			displayManager.render();
		}
		exit();
	}
	
	public static void exit() {
		Main.getConfig("infos").setValue("display_fullscreen", Display.isFullscreen() + "");
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
