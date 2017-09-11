package net.epopy.epopy.player;

import java.io.File;
import java.io.IOException;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.player.stats.CarStats;
import net.epopy.epopy.player.stats.PingStats;
import net.epopy.epopy.player.stats.PlaceInvaderStats;
import net.epopy.epopy.player.stats.SnakeStats;
import net.epopy.epopy.player.stats.SpeedRunStats;
import net.epopy.epopy.player.stats.TankStats;
import net.epopy.epopy.utils.Config;
import net.epopy.epopy.utils.FileUtils;

public class Player {
	
	// config du joueur
	private Config config;
	private boolean sound = true;
	
	// key data
	private int level;
	private int lastGameID = 0;
	private final String name;
	
	// stats games
	private SnakeStats snakeStats;
	private PingStats pingStats;
	private CarStats carStats;
	private TankStats tankStats;
	private PlaceInvaderStats placeInvaderStats;
	private SpeedRunStats speedRunStats;
	
	public Player(final String name) {
		
		this.name = name;
		File profil = new File(FileUtils.PATH_FOLDER + name + ".txt");
		try {
			if (profil.createNewFile())
				new NewPlayer(name);
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Fonction pour supprimer le joueur
	 */
	public void reset() {
		new NewPlayer(name);
		init();
	}
	
	public Config getConfig() {
		return config;
	}
	
	public void init() {
		File profil = new File(FileUtils.PATH_FOLDER + name + ".txt");
		config = new Config(profil);
		level = Integer.parseInt(config.getData("level"));
		carStats = new CarStats(config);
		snakeStats = new SnakeStats(config);
		pingStats = new PingStats(config);
		speedRunStats = new SpeedRunStats(config);
		placeInvaderStats = new PlaceInvaderStats(config);
		tankStats = new TankStats(config);
		sound = Boolean.valueOf(config.getData("sound", "true"));
	}

	public int getSoundLevel() {
		return Integer.parseInt(config.getData("sound_volume", "5"));
	}
	
	public void setSoundLevel(final int value) {
		config.setValue("sound_volume", String.valueOf(value));
	}
	
	public boolean getLastDisplayWasFullScreen() {
		return Boolean.valueOf(Main.getConfig("infos").getData("display_fullscreen"));
	}
	
	public void setDisplayFullScreen(final boolean fullscreen) {
		Main.getConfig("infos").setValue("display_fullscreen", fullscreen + "");
	}
	
	public String getName() {
		return name;
	}
	
	public String getDataAccountCreate() {
		return config.getData("account_create_at");
	}
	
	public boolean hasSound() {
		return sound;
	}
	
	public void setSoundStatus(final boolean sound) {
		config.setValue("sound", String.valueOf(sound));
		if (!sound)
			Audios.stopAll();
		else
			Audios.LOBBY.start(true).setVolume(0.4f);
		this.sound = sound;
	}
	
	public int getLastGame() {
		if (lastGameID == 0)
			lastGameID = Integer.parseInt(config.getData("last_game"));
		return lastGameID;
	}
	
	public void setLastGame(final int id) {
		
		lastGameID = id;
		config.setValue("last_game", String.valueOf(id));
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(final int level) {
		if (this.level < level) {
			new NotificationGui("• Objectif réussi •", "Vous venez de débloquer un nouveau jeu !", 5, new float[] { 1, 1, 1, 1 }, false);
			Audios.NEW_GAME.start(false).setVolume(0.5f);
		}
		this.level = level;
		config.setValue("level", String.valueOf(level));
	}
	
	public SpeedRunStats getSpeedRunStats() {
		return speedRunStats;
	}
	
	public PlaceInvaderStats getPlaceInvaderStats() {
		return placeInvaderStats;
	}

	public PingStats getPingStats() {
		return pingStats;
	}
	
	public SnakeStats getSnakeStats() {
		return snakeStats;
	}
	
	public CarStats getCarStats() {
		return carStats;
	}
	
	public TankStats getTankStats() {
		return tankStats;
	}
}
