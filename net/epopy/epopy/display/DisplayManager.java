package net.epopy.epopy.display;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glViewport;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.util.glu.GLU;

import de.matthiasmann.twl.utils.PNGDecoder;
import net.epopy.epopy.Main;
import net.epopy.epopy.utils.Input;

public class DisplayManager {
	
	public DisplayManager(final int width, final int height, final String title, boolean fullscreen, final boolean lastDisplaymode) {
		
		try {
			if (lastDisplaymode) fullscreen = Main.getPlayer().getLastDisplayWasFullScreen();
			
			if (!fullscreen)
				Display.setDisplayMode(new DisplayMode(width, height));
			else {
				Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
				Display.setFullscreen(true);
			}
			
			Display.setTitle(title);
			
			String path = "/net/epopy/epopy/display/res/main/";
			Display.setIcon(new ByteBuffer[] {
					getByteBuffer(getClass().getResource(path + "logo16.png")),
					getByteBuffer(getClass().getResource(path + "logo32.png")),
					getByteBuffer(getClass().getResource(path + "logo32.png")),
			});
			
			Display.create();

			Display.setVSyncEnabled(true);
			Display.setResizable(true);
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
		Main.setDisplayManager(this);
	}
	
	public void render() {
		view2D();
		
		if (Main.getGameManager() != null)
			Main.getGameManager().render();
		Input.update();
		Display.update();
		Display.sync(60);
	}
	
	public void update() {
		if (Main.getGameManager() != null)
			Main.getGameManager().update();
	}
	
	private void view2D() {
		glClearColor(1, 1, 1, 1);
		glClear(GL_COLOR_BUFFER_BIT);
		
		glViewport(0, 0, Display.getWidth(), Display.getHeight());
		
		glMatrixMode(GL_PROJECTION);
		
		glLoadIdentity();
		
		GLU.gluOrtho2D(0, Display.getWidth(), Display.getHeight(), 0);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
		
		glEnable(GL_TEXTURE_2D);
		
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
	}
	
	private static ByteBuffer getByteBuffer(final URL url) {
		InputStream is = null;
		try {
			is = url.openStream();
			PNGDecoder decoder = new PNGDecoder(is);
			ByteBuffer bb = ByteBuffer.allocateDirect(decoder.getWidth() * decoder.getHeight() * 4);
			decoder.decode(bb, decoder.getWidth() * 4, PNGDecoder.Format.RGBA);
			bb.flip();
			return bb;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
}
