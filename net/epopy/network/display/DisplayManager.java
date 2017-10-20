package net.epopy.network.display;

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

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.player.guis.RegisterPlayerNetworkMenu;
import net.epopy.epopy.utils.Input;
import net.epopy.network.NetworkPlayer;

public class DisplayManager {

	private static boolean running = true;
	
	public DisplayManager() {
		NotificationGui.clear();
		/**int FRAME_CAP = 9000;//max
		long lastTickTime = System.nanoTime();
		long lastRenderTime = System.nanoTime();

		double tickTime = 1000000000.0 / 60.0;
		double renderTime = 1000000000.0 / FRAME_CAP;

		int ticks = 0;
		int frames = 0;

		long timer = System.currentTimeMillis();

		while (!Display.isCloseRequested()) {
			if (System.nanoTime() - lastTickTime > tickTime) {
				lastTickTime += tickTime;
				Input.checkInputFullscreen();
				update();
				ticks++;
			} else if (System.nanoTime() - lastRenderTime > renderTime) {
				lastRenderTime += renderTime;
				render();
				frames++;
			} else {
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}				
			}

			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				Display.setTitle("Epopy - " + ticks + " ticks, " + frames + " fps");
				ticks = 0;
				frames = 0;
			}
		}*/
		running = true;
		Display.setVSyncEnabled(true);
		while (running) {
			if (!Display.isVisible()) {
				Display.update();
				Display.sync(4);
				continue;
			}
			if(Display.isCloseRequested())
				Main.exit();
			Input.checkInputFullscreen();
			update();
			render();
		}
		
		new RegisterPlayerNetworkMenu();
	}
	
	public static void exitMulti() {
		running = false;
	}
	
	public void update() {
		//update game
		NetworkPlayer.getGame().update();
	}

	public void render() {
		view2D();
		//render game
		NetworkPlayer.getGame().render();
		NotificationGui.render();
		Input.update();
		Display.update();
		Display.sync(60);
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


}
