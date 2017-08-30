package net.epopy.epopy.audio;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audios {
	
	private static String PATH = "/net/epopy/epopy/audio/res/";

	public static Audios LOBBY = new Audios("menu");
	public static Audios NEW_GAME = new Audios("yew");
	
	private Clip clip;
	public Audios(String name) {
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
	public void start(float volume) {
		if (!clip.isRunning()) {
			clip.start();
			setVolume(volume);
			clip.loop(Integer.MAX_VALUE);
		}
	}
	
	public void start(boolean loop) {
		if (!clip.isRunning()) {
			clip.start();
			if(loop)
				clip.loop(Integer.MAX_VALUE);
		}
	}
	
	public void start() {
		start(false);
	}
 
	public void stop() {
		if(clip.isRunning())
			clip.stop();
	}
	
	public void setVolume(float volume) {
		if (volume < 0f || volume > 1f)
			throw new IllegalArgumentException("Volume not valid: " + volume);
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		gainControl.setValue(20f * (float) Math.log10(volume));
	}

	/*
	 * Getters
	 */
	public float getVolume() {
		FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
		return (float) Math.pow(10f, gainControl.getValue() / 20f);
	}
}
