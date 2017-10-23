package net.epopy.epopy.player.guis;

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
import net.epopy.epopy.display.DisplayManager;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.display.components.NotificationGui;
import net.epopy.epopy.display.components.TextAreaGui;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Config;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.WebPage;
import net.epopy.network.NetworkPlayer;
import net.epopy.network.utils.NetworkStatus;

public class RegisterPlayerNetworkMenu {

	public RegisterPlayerNetworkMenu() {
		Config config = Main.getPlayer().getConfig();
		if (!Display.isCreated())
			new DisplayManager((int) (1920 / 1.5), (int) (1080 / 1.5), "Epopy", Boolean.parseBoolean(config.getData("display_fullscreen", "true")), false);
			
		TextAreaGui username = new TextAreaGui(675, 325 + 30, true, "");
		TextAreaGui mdp = new TextAreaGui(675, 325 + 145 + 2 + 30, true, "", true);
		username.setEnter(true);
		
		ButtonGui twitterButton = new ButtonGui(Textures.LOGO_TWITTER_OFF, Textures.LOGO_TWITTER_ON);
		ButtonGui facebookButton = new ButtonGui(Textures.LOGO_FACEBOOK_OFF, Textures.LOGO_FACEBOOK_ON);
		ButtonGui webButton = new ButtonGui(Textures.LOGO_WEB_OFF, Textures.LOGO_WEB_ON);
		
		ButtonGui connexionButton = new ButtonGui("ME CONNECTER", new float[] { 1, 1, 1, 1 }, 50, false);
		ButtonGui inscriptionButton = new ButtonGui("Mot de passe oublié / m'inscrire", new float[] { 0.7f, 0.7f, 0.7f, 1 }, 20, false);
		
		ButtonGui keepUserButton = new ButtonGui(Textures.MAIN_CONNEXION_NETWORK_BOX_OFF, Textures.MAIN_CONNEXION_NETWORK_BOX_ON);
		
		ButtonGui retourButton = new ButtonGui(Textures.GAME_MENU_USERS_RETOUR_OFF, Textures.GAME_MENU_USERS_RETOUR_ON);
		
		boolean keepUser = Boolean.parseBoolean(config.getData("keepConnexion", "true"));
		if (!config.getData("username", "null").equals("null")) {
			username.addText(config.getData("username", "null"));
			mdp.addText(config.getData("password", "null"));
		}
		
		boolean initPlayer = false;
		
		while (true) {
			if (!Display.isVisible()) {
				Display.update();
				Display.sync(4);
				continue;
			}
			
			view2D();
			
			Textures.MAIN_CONNEXION_NETWORK_BG.renderBackground();
			
			/*
			 * text area
			 */
			username.update(9999);
			username.render();
			mdp.update(9999);
			mdp.render();
			
			ComponentsHelper.drawText("Pseudo ou Email", 655, 325 + 2 - 10, 30);
			ComponentsHelper.drawText("Mot de passe", 655, 325 + 145 + 2 - 10, 30);
			
			connexionButton.update(AbstractGameMenu.defaultWidth / 2, AbstractGameMenu.defaultHeight / 2 + 200, PositionWidth.MILIEU, PositionHeight.HAUT, 330, 50);
			connexionButton.render();
			
			inscriptionButton.update(AbstractGameMenu.defaultWidth / 2 - 10, AbstractGameMenu.defaultHeight / 2 + 250, PositionWidth.MILIEU, PositionHeight.HAUT);
			inscriptionButton.render();
			
			retourButton.update(1440, 130, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, 50);
			retourButton.render();
			
			if (retourButton.isClicked()) {
				NotificationGui.clear();
				new ChooseGameTypeMenu();
				break;
			}
			
			/**
			 * Lien
			 */
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
			else if (inscriptionButton.isClicked())
				new WebPage(WebPage.WEB_PAGE_EPOPY_USER);
				
			/**
			 * Rester connexion
			 */
			ComponentsHelper.drawText("Rester connecté", 680, 325 + 145 * 2 + 1, 27);
			keepUserButton.update(647, 617, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, 30);
			keepUserButton.render();
			if (keepUserButton.isClicked()) {
				keepUser = !keepUser;
				config.setValue("keepConnexion", String.valueOf(keepUser));
				if (!keepUser) {
					config.setValue("username", "null");
					config.setValue("password", "null");
				}
			}
			if (keepUser)
				ComponentsHelper.renderTexture(Textures.MAIN_CONNEXION_NETWORK_BOX_CHECK, 655, 612, 30, 30);
				
			int time = 12;
			int tempsRestant = (int) ((System.currentTimeMillis() - Long.parseLong(config.getData("last_connection_time"))) / 1000);
			if (tempsRestant < 0) {
				config.setValue("last_connection_time", String.valueOf(System.currentTimeMillis() + 1000));
				tempsRestant = 0;
			} else if (tempsRestant < time)
				ComponentsHelper.drawText("Vous pouvez vous reconnecter dans " + (time - tempsRestant) + " seconde" + (time - tempsRestant > 1 ? "s" : ""), AbstractGameMenu.defaultWidth / 2 + 20, AbstractGameMenu.defaultHeight / 2 + 150, PositionWidth.MILIEU, PositionHeight.HAUT, 40,
						new float[] { 1, 0, 0, 1 });
			else {
				connexionButton.setOver(true);
				connexionButton.textColor = new float[] { 1, 1, 1, 1 };
			}
			if (connexionButton.isClicked() || Input.getKeyDown(28)) {
				if (keepUser && username.getText().length() > 0 && mdp.getText().length() > 0) {
					config.setValue("username", username.getText());
					config.setValue("password", mdp.getText());
				}
				if (tempsRestant >= time) {
					ComponentsHelper.drawText("Connexion en cours..", AbstractGameMenu.defaultWidth / 2 + 20, AbstractGameMenu.defaultHeight / 2 + 130, PositionWidth.MILIEU, PositionHeight.HAUT, 60, new float[] { 1, 0, 0, 1 });
					NotificationGui.render();
					Display.update();
					
					player = new NetworkPlayer(username.getText(), mdp.getText());
					networkStatus = player.getNetworkPlayerHandlersWaitingRoom().getNetworkStatus();
				}
			}
			
			if (networkStatus != null) {
				if (networkStatus.equals(NetworkStatus.USER_VALID)) {
					connexionButton.textColor = new float[] { 0.5f, 0.5f, 0.5f, 1 };
					connexionButton.setOver(false);
					config.setValue("last_connection_time", String.valueOf(System.currentTimeMillis()));
					
					initPlayer = true;
					break;
				} else if (networkStatus.equals(NetworkStatus.USER_WAITING_CONFIRMATION)) {
					networkStatus = player.getNetworkPlayerHandlersWaitingRoom().getNetworkStatus();
					ComponentsHelper.drawText("Connexion en cours..", AbstractGameMenu.defaultWidth / 2 + 20, AbstractGameMenu.defaultHeight / 2 + 130, PositionWidth.MILIEU, PositionHeight.HAUT, 60, new float[] { 1, 0, 0, 1 });
				} else {
					// anti-spam
					connexionButton.textColor = new float[] { 0.5f, 0.5f, 0.5f, 1 };
					connexionButton.setOver(false);
					
					config.setValue("last_connection_time", String.valueOf(System.currentTimeMillis()));
					
					String message = "";
					String infos = "( Pour plus d'informations veuillez nous contacter, @EpopyOfficiel/Epopy.fr )";
					if (networkStatus.equals(NetworkStatus.USER_NO_VALID))
						message = "Identifiant incorrect !";
					else if (networkStatus.equals(NetworkStatus.SERVER_OFFLINE))
						message = "Les serveurs sont éteints";
					else if (networkStatus.equals(NetworkStatus.SERVER_FULL))
						message = "Les serveurs sont pleins !";
					else if (networkStatus.equals(NetworkStatus.SERVER_MAINTENANCE))
						message = "Les serveurs sont cours de maintenance !";
					else if (networkStatus.equals(NetworkStatus.USER_ALREADY_CONNECTED))
						message = "Vous êtes déjà connnecté !";
					else if (networkStatus.equals(NetworkStatus.USER_OLD_VERSION)) {
						message = "Vous avez une ancienne version !";
						infos = "( Veuillez faire la mise à jour )";
					}
					
					new NotificationGui(message, infos, 3, new float[] { 1, 0, 0, 1 }, true);
					networkStatus = null;
				}
			}
			Input.checkInputFullscreen();
			NotificationGui.render();
			Input.update();
			Display.update();
			Display.sync(60);
			
			if (Display.isCloseRequested())
				Main.exit();
				
		}
		if (initPlayer)
			player.init();
	}

	NetworkPlayer player = null;
	NetworkStatus networkStatus = null;

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
