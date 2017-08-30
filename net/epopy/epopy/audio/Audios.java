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

public class Audios {

	private static String PATH = "/net/epopy/epopy/audio/res/";

	public static Audios LOBBY = new Audios("menu");
	public static Audios NEW_GAME = new Audios("level");
	public static Audios PONG = new Audios("pong");
	public static Audios CAR = new Audios("car");
	public static Audios SNAKE = new Audios("snake");
	public static Audios TANK = new Audios("tank");
	public static Audios PLACEINVADER = new Audios("place_invader");
	public static Audios SPEEDRUN = new Audios("speedrun");

	private Clip clip;

	private static List<Audios> audios = new ArrayList<Audios>();

	public Audios(final String name) {
		try {
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource(PATH + name + ".wav"));
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Fonctions
	 */
	
	public void start(final boolean loop) {

		audios.add(this);
		if (!clip.isRunning()) {
			clip.start();
			if (loop)
				clip.loop(Integer.MAX_VALUE);
		}
	}

	public void stop() {
		if (clip.isRunning())
			clip.stop();
	}

	public static void stopAll() {
		for (Audios audio : audios) {
			audio.stop();
		}
	}

	public static void changeVolume(final float volume) {
		for (Audios audio : audios) {
			if (volume < 0f || volume > 1f)
				throw new IllegalArgumentException("Volume not valid: " + volume);
			FloatControl gainControl = (FloatControl) audio.clip.getControl(FloatControl.Type.MASTER_GAIN);
			gainControl.setValue(20f * (float) Math.log10(volume));
			
		}
	}
	
}
