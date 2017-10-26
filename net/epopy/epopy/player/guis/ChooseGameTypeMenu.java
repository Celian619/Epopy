package net.epopy.epopy.player.guis;

import static net.epopy.epopy.display.components.ComponentsHelper.drawQuad;
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

import java.io.File;

import org.lwjgl.opengl.Display;
import org.lwjgl.util.glu.GLU;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.DisplayManager;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameManager;
import net.epopy.epopy.player.Player;
import net.epopy.epopy.utils.Config;
import net.epopy.epopy.utils.FileUtils;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.WebPage;

public class ChooseGameTypeMenu {
	
	private int w1_Solo = 0;
	private int w2_Solo = 0;
	private int w1_Multi = 0;
	private int w2_Multi = 0;
	
	public ChooseGameTypeMenu() {
		if (Main.getPlayer() == null)
			Main.setPlayer(new Player("localhost"));

		if (!Display.isCreated()) {
			Config c = Main.getPlayer().getConfig();
			int width = Integer.parseInt(c.getData("display_width", (int) (1920 / 1.5) + ""));
			int height = Integer.parseInt(c.getData("display_height", (int) (1080 / 1.5) + ""));
			
			new DisplayManager(width, height, "Epopy", Boolean.parseBoolean(Main.getPlayer().getConfig().getData("display_fullscreen")), false);
		} else {
			
			Textures.unloadTextures();
		}
		
		ButtonGui soloButton = new ButtonGui("SOLO", new float[] { 1, 1, 1, 1 }, 50, false);
		ButtonGui multiButton = new ButtonGui("MULTIJOUEUR", new float[] { 1, 1, 1, 1 }, 50, false);
		
		ButtonGui twitterButton = new ButtonGui(Textures.LOGO_TWITTER_OFF, Textures.LOGO_TWITTER_ON);
		ButtonGui facebookButton = new ButtonGui(Textures.LOGO_FACEBOOK_OFF, Textures.LOGO_FACEBOOK_ON);
		ButtonGui webButton = new ButtonGui(Textures.LOGO_WEB_OFF, Textures.LOGO_WEB_ON);
		
		ButtonGui quitterButton = new ButtonGui(Textures.GAME_MENU_QUITTER_OFF, Textures.GAME_MENU_QUITTER_ON);
		
		while (true) {
			view2D();
			Textures.MAIN_BG.renderBackground();
			
			/*
			 * Buttons
			 */
			soloButton.update(430, 795, PositionWidth.MILIEU, PositionHeight.HAUT, 150, 70);
			multiButton.update(1565, 200, PositionWidth.MILIEU, PositionHeight.HAUT, 300, 70);
			
			if (Display.isFullscreen()) {
				quitterButton.update(AbstractGameMenu.defaultWidth - 12, 12, PositionWidth.DROITE, PositionHeight.HAUT, 20, 20);
				quitterButton.render();
				if (quitterButton.isClicked())
					Main.exit();
			}
			if (soloButton.isOn()) {
				if (w1_Solo > 40)
					w1_Solo -= 40;
				else w1_Solo = 0;
				if (w2_Solo > 40)
					w2_Solo -= 40;
				else w2_Solo = 0;
			} else {
				if (w1_Solo < 500)
					w1_Solo += 20;
				else w1_Solo = 520;
				if (w2_Solo < 500)
					w2_Solo += 20;
				else w2_Solo = 520;
			}
			
			if (multiButton.isOn()) {
				if (w1_Multi > 40)
					w1_Multi -= 40;
				else w1_Multi = 0;
				if (w2_Multi > 40)
					w2_Multi -= 40;
				else w2_Multi = 0;
			} else {
				if (w1_Multi < 500)
					w1_Multi += 20;
				else w1_Multi = 520;
				if (w2_Multi < 500)
					w2_Multi += 20;
				else w2_Multi = 520;
			}
			
			// solo bar bas
			drawQuad(42, 880, w1_Solo == 520 ? 523 : w1_Solo, 2);
			drawQuad(780, 770, w2_Solo == 520 ? -530 : -w2_Solo, 2);
			
			// multi bars
			drawQuad(1390, 180, w1_Multi == 520 ? 523 : w1_Multi, 2);
			drawQuad(1730, 190 + 90, w2_Multi == 520 ? -530 : -w2_Multi, 2);
			
			soloButton.render();
			multiButton.render();
			
			twitterButton.update(AbstractGameMenu.defaultWidth - 60, AbstractGameMenu.defaultHeight - 10, PositionWidth.GAUCHE, PositionHeight.BAS, 50, 50);
			twitterButton.render();
			
			facebookButton.update(AbstractGameMenu.defaultWidth - 120, AbstractGameMenu.defaultHeight - 10, PositionWidth.GAUCHE, PositionHeight.BAS, 50, 50);
			facebookButton.render();
			
			webButton.update(AbstractGameMenu.defaultWidth - 60 * 3, AbstractGameMenu.defaultHeight - 10, PositionWidth.GAUCHE, PositionHeight.BAS, 50, 50);
			webButton.render();
			
			if (twitterButton.isClicked())
				new WebPage(WebPage.WEB_PAGE_TWITTER_EPOPY);
			else if (facebookButton.isClicked())
				new WebPage(WebPage.WEB_PAGE_FACEBOOK_EPOPY);
			else if (webButton.isClicked())
				new WebPage(WebPage.WEB_PAGE_EPOPY);
			NotificationGui.render();
			Input.checkInputFullscreen();
			Input.update();
			Display.update();
			Display.sync(60);
			
			if (Display.isCloseRequested())
				Main.exit();
			else if (soloButton.isClicked()) {
				Textures.unloadTextures();
				Main.setGameManager(new GameManager());
				break;
				
			} else if (multiButton.isClicked()) {
				Textures.unloadTextures();
				/*
				 * TODO new RegisterPlayerNetworkMenu(); break;
				 */
				// new NotificationGui("Le multijoueur arrive bientôt !", "( Pour plus d'information, @EpopyOfficiel / Epopy.fr )", 5, new
				// float[] { 1, 0, 0, 1 }, true);
				new RegisterPlayerNetworkMenu();
				break;
			}
		}
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
