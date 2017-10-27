package net.epopy.epopy.audio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import net.epopy.epopy.Main;

public class Audios {

	// le volume du joueur
	public static int VOLUME_VALUE = Main.getPlayer().getSoundLevel(); // min 1 | max 10 | default 5

	private static String PATH = "/net/epopy/epopy/audio/res/";
	private static List<Audios> audios = new ArrayList<Audios>(10);
	
	public static Audios LOBBY = new Audios("menu");
	public static Audios NEW_GAME = new Audios("level");
	public static Audios PING = new Audios("ping");
	public static Audios CAR = new Audios("car");
	public static Audios SNAKE = new Audios("snake");
	public static Audios TANK = new Audios("tank");
	public static Audios PLACEINVADER = new Audios("place_invader");
	public static Audios SPEEDRUN = new Audios("speedrun");
	public static Audios TETRIS = new Audios("tetris");
	
	private Clip clip;
	private float volume;
	private float vec = 110 - VOLUME_VALUE * 10;// 0 < plus c'est fort
	private final String name;
	private boolean running = false;
	
	public Audios(final String name) {
		this.name = name;
	}

	/*
	 * Fonctions
	 */

	public Audios start(final boolean loop) {
		stopAll();
		running = true;

		AudioInputStream audioIn;
		try {
			audioIn = AudioSystem.getAudioInputStream(getClass().getResource(PATH + name + ".wav"));
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}

		clip.start();
		if (loop)
			clip.loop(Integer.MAX_VALUE);

		audios.add(this);
		
		return this;
	}

	public void stop() {
		if (running) {
			clip.close();
			running = false;
		}
	}

	public static void stopAll() {
		for (Audios audio : audios) {
			if (audio.isRunning())
				audio.stop();
		}
		audios.clear();
	}

	public static void pauseAll() {
		for (Audios audio : audios) {
			if (audio.isRunning())
				audio.clip.stop();
		}
	}
	
	public static void resumeAll() {
		for (Audios audio : audios) {
			if (audio.isRunning())
				audio.clip.start();
		}
	}

	public boolean isRunning() {
		return running;
	}

	/*
	 * Fonction pour update le volume du clip quand le joueur changer son master volume
	 */
	private void updateVolume() {
		vec = 110 - VOLUME_VALUE * 10;
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		float value = vec * (float) Math.log10(volume);
		gainControl.setValue(value <= -80 ? -70.0f : value);
	}

	/*
	 * Fonction pour changer le volume default
	 */
	public Audios setVolume(final float volume) {
		if (running) {
			this.volume = volume;
			updateVolume();
		}
		return this;
	}

	public float getVolume() {
		return (float) Math.pow(10f, ((FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN)).getValue() / vec);
	}
	
	/**
	 * Fonction pour recalculer tous les volumes quand le joueur changer de volume
	 *
	 * @param
	 */
	public static void updateAllVolume() {
		for (Audios audio : audios)
			audio.updateVolume();
	}

	/**
	 * Pour set le volumes de tous les clips
	 *
	 * @param volume
	 */
	public static void setVolumeAll(final float volume) {
		for (Audios audio : audios) {
			if (volume < 0 || volume > 1)
				throw new IllegalArgumentException("Volume not valid: " + volume);
			FloatControl gainControl = (FloatControl) audio.clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(volume);
		}
	}
}
