package net.epopy.epopy.player;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.player.stats.CarStats;
import net.epopy.epopy.player.stats.MarioStats;
import net.epopy.epopy.player.stats.PongStats;
import net.epopy.epopy.player.stats.SnakeStats;
import net.epopy.epopy.player.stats.TankStats;
import net.epopy.epopy.utils.Config;
import net.epopy.epopy.utils.FileUtils;

public class Player {
	
	// config du joueur
	private Config config;
	
	// key data
	private int level;
	private int lastGameID = 0;
	private String name;
	
	private boolean sound = true;
	
	// stats games
	private MarioStats marioStats;
	private SnakeStats snakeStats;
	private PongStats pongStats;
	private CarStats carStats;
	private TankStats tankStats;
	
	public Player(String name) {		
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
		marioStats = new MarioStats(config);
		carStats = new CarStats(config);
		snakeStats = new SnakeStats(config);
		pongStats = new PongStats(config);
		tankStats = new TankStats(config);
		sound = Boolean.valueOf(config.getData("sound", "true"));
	}
	
	/**
	 * Donne la dernier dimension de l'ecran du joueur
	 *
	 * @return x, y , isfullscreen
	 */
	public boolean getLastDisplayWasFullScreen() {
		return Boolean.valueOf(Main.getConfig("infos").getData("display_fullscreen"));
	}
	
	public void setDisplayFullScreen(boolean fullscreen) {
		Main.getConfig("infos").setValue("display_fullscreen", fullscreen + "");
	}
	
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public String getTotalTemps() {
		long tempsTotal = 0;
		
		tempsTotal += Long.parseLong(config.getData("pong_temps"));
		tempsTotal += Long.parseLong(config.getData("snake_temps"));
		tempsTotal += Long.parseLong(config.getData("car_temps"));
		tempsTotal += Long.parseLong(config.getData("tank_temps"));
		
		if (tempsTotal != 0)
			return timeFormat.format(tempsTotal - 3600000);
		else
			return "00:00:00";
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
	
	public void setSoundStatus(boolean sound) {
		//config.setValue("sound", String.valueOf(sound));
		this.sound = sound;
	}
	
	public boolean getSound() {
		return sound;
	}
	
	public int getLastGame() {
		if (lastGameID == 0)
			lastGameID = Integer.parseInt(config.getData("last_game"));
		return lastGameID;
	}
	
	public void setLastGame(int id) {
		lastGameID = id;
		config.setValue("last_game", String.valueOf(id));
	}
	
	public boolean hasNewGame() {
		return true;
	}

	public int getLevel() {
		return level;
	}
	
	public void setLevel(int level) {
		if(this.level < level) {
			new NotificationGui("• Objectif réussi •", "Vous venez de débloquer un nouveau jeu !", 5, new float[]{1, 1f, 1, 1}, false);
			Audios.NEW_GAME.start(0.1f);
		}
		this.level = level;
		config.setValue("level", String.valueOf(level));
	}
	
	public MarioStats getMarioStats() {
		return marioStats;
	}
	
	public PongStats getPongStats() {
		return pongStats;
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
