package net.epopy.epopy.display;

import static org.lwjgl.opengl.EXTFramebufferObject.glGenerateMipmapEXT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NEAREST;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

public class Textures {

	private static List<Textures> textures = new ArrayList<Textures>(91);
	
	public static Textures NETWORK_WAITING_ROOM_BG_MATCHMAKING = load("network/waitingroom/bg_matchmaking");
	public static Textures NETWORK_WAITING_ROOM_ADD = load("network/waitingroom/add");
	public static Textures NETWORK_WAITING_ROOM_IMAGE_USER_DEFAULT = load("network/waitingroom/user");
	public static Textures NETWORK_WAITING_ROOM_BG = load("network/waitingroom/bg");
	public static Textures NETWORK_SEARCH_ZONE = load("network/search_zone");
	public static Textures NETWORK_WAITING_SHOP_ON = load("network/waitingroom/shop_on");
	public static Textures NETWORK_WAITING_SHOP_OFF = load("network/waitingroom/shop_off");
	public static Textures NETWORK_WAITING_LODING_MAP_TANK = load("network/waitingroom/chargement");
	
	// network game
	public static Textures NETWORK_GAME_TANK_MAP = load("network/games/map-tank-default");
	public static Textures NETWORK_GAME_TANK_ZONE_SMALL = load("network/games/zone-tank-small");
	public static Textures NETWORK_GAME_END_BG = load("network/games/end_bg");

	public static Textures MENU_SOUND_ON = load("games/sound_on");
	public static Textures MENU_BTN_SOUND_ON = load("games/btn_sound_on");
	public static Textures MENU_SOUND_OFF = load("games/sound_off");
	
	public static Textures FONT = load("font");
	public static Textures MAIN_BG = load("main/bg");
	public static Textures MAIN_NOTIF = load("main/notif_bg");

	public static Textures GAME_ECHAP_BANDE = load("games/menu/echap/echap_band");
	public static Textures GAME_STARTING_BG = load("games/starting_game_bg");
	public static Textures GAME_GAUCHE_SOURIS = load("games/gauche_souris");

	public static Textures MAIN_CONNEXION_NETWORK_BG = load("main/network/bg");
	public static Textures MAIN_CONNEXION_NETWORK_BOX_OFF = load("main/network/box_off");
	public static Textures MAIN_CONNEXION_NETWORK_BOX_ON = load("main/network/box_on");
	public static Textures MAIN_CONNEXION_NETWORK_BOX_CHECK = load("main/network/check");

	public static Textures LOGO_TWITTER_ON = load("main/twitter_on");
	public static Textures LOGO_TWITTER_OFF = load("main/twitter_off");
	public static Textures LOGO_FACEBOOK_ON = load("main/facebook_on");
	public static Textures LOGO_FACEBOOK_OFF = load("main/facebook_off");
	public static Textures LOGO_WEB_OFF = load("main/web_off");
	public static Textures LOGO_WEB_ON = load("main/web_on");

	public static Textures GAME_TOUCHE_VIERGE = load("games/touche_vierge");
	public static Textures GAME_MOUSE_DOWN = load("games/mouse_down");
	public static Textures GAME_MOUSE_UP = load("games/mouse_up");
	
	public static Textures GAME_CHRONO_1 = load("games/chrono_1");
	public static Textures GAME_CHRONO_2 = load("games/chrono_2");
	public static Textures GAME_CHRONO_3 = load("games/chrono_3");
	
	// ping
	public static Textures GAME_PING_BG = load("games/ping/bg");
	public static Textures GAME_PING_LEVEL_BG = load("games/ping/level/bg");

	// car
	public static Textures GAME_CAR_VOITURE = load("games/car/voiture");
	public static Textures GAME_CAR_BG = load("games/car/bg");

	// tank
	public static Textures TANK_BG = load("games/tank/bg");
	public static Textures TANK_LEVELBG = load("games/tank/levelbg");
	public static Textures TANK_SMOKE = load("games/tank/smoke");
	public static Textures TANK_WALL = load("games/tank/wall");
	public static Textures TANK_TANK1 = load("games/tank/tank");
	public static Textures TANK_TANK2 = load("games/tank/tank2");
	public static Textures TANK_TANKPRINT = load("games/tank/tankPrint");
	
	// tetras
	public static Textures TETRAS_BG = load("games/tetras/bg");
	public static Textures TETRAS_BLOCK = load("games/tetras/block");

	// snake
	public static Textures SNAKE_BG = load("games/snake/bg");
	public static Textures SNAKE_LEVEL_BG = load("games/snake/levels/bg");
	public static Textures SNAKE_FOOD = load("games/snake/levels/food");
	public static Textures SNAKE_FOOD2 = load("games/snake/levels/food-2");
	public static Textures SNAKE_FOOD3 = load("games/snake/levels/food-3");
	public static Textures SNAKE_TETE = load("games/snake/levels/tete");
	public static Textures SNAKE_CORP = load("games/snake/levels/corp");
	
	// place invader
	public static Textures PLACEINVADER_BG = load("games/placeinvader/bg");
	public static Textures PLACEINVADER_LEVEL_BG = load("games/placeinvader/levelbg");
	public static Textures PLACEINVADER_SPACESHIP = load("games/placeinvader/spaceShip");
	public static Textures PLACEINVADER_ROCKET = load("games/placeinvader/rocket");
	public static Textures PLACEINVADER_EXPLOSION = load("games/placeinvader/explosion");

	// speedrun
	public static Textures SPEEDRUN_BG = load("games/speedrun/bg");
	public static Textures SPEEDRUN_LEVELBG = load("games/speedrun/levelbg");
	public static Textures SPEEDRUN_LAMPADAIRE = load("games/speedrun/lampadaire");
	public static Textures SPEEDRUN_MAN1 = load("games/speedrun/man-1");
	public static Textures SPEEDRUN_MAN2 = load("games/speedrun/man-2");
	public static Textures SPEEDRUN_MAN3 = load("games/speedrun/man-3");
	public static Textures SPEEDRUN_MAN4 = load("games/speedrun/man-4");
	public static Textures SPEEDRUN_MAN5 = load("games/speedrun/man-5");
	public static Textures SPEEDRUN_MAN6 = load("games/speedrun/man-6");
	public static Textures SPEEDRUN_MAN7 = load("games/speedrun/man-7");
	public static Textures SPEEDRUN_MAN8 = load("games/speedrun/man-8");
	public static Textures SPEEDRUN_MANSOL1 = load("games/speedrun/sol-1");
	public static Textures SPEEDRUN_MANSOL2 = load("games/speedrun/sol-2");
	public static Textures SPEEDRUN_OTHERMEN1 = load("games/speedrun/other-men-1");
	public static Textures SPEEDRUN_OTHERMEN2 = load("games/speedrun/other-men-2");
	public static Textures SPEEDRUN_OTHERMEN3 = load("games/speedrun/other-men-3");
	public static Textures SPEEDRUN_OTHERMEN4 = load("games/speedrun/other-men-4");
	public static Textures SPEEDRUN_OTHERMEN5 = load("games/speedrun/other-men-5");
	public static Textures SPEEDRUN_OTHERMEN6 = load("games/speedrun/other-men-6");
	public static Textures SPEEDRUN_OTHERMEN7 = load("games/speedrun/other-men-7");
	public static Textures SPEEDRUN_OTHERMEN8 = load("games/speedrun/other-men-8");
	public static Textures SPEEDRUN_OTHERMENSOL1 = load("games/speedrun/other-sol-1");
	public static Textures SPEEDRUN_OTHERMENSOL2 = load("games/speedrun/other-sol-2");
	
	// general
	public static Textures GAME_BACKGROUND_80OPACITY = load("games/noir_80opacity");
	public static Textures GAME_MENU_USERS_RETOUR_OFF = load("games/menu/users/retour_off");
	public static Textures GAME_MENU_USERS_RETOUR_ON = load("games/menu/users/retour_on");
	public static Textures GAME_MENU_OFF = load("games/menu/menu_off");
	public static Textures GAME_MENU_ON = load("games/menu/menu_on");
	public static Textures GAME_MENU_BG = load("games/menu/bg");
	public static Textures GAME_MENU_GAUCHE_OFF = load("games/menu/gauche_off");
	public static Textures GAME_MENU_GAUCHE_ON = load("games/menu/gauche_on");
	public static Textures GAME_MENU_DROITE_OFF = load("games/menu/droite_off");
	public static Textures GAME_MENU_DROITE_ON = load("games/menu/droite_on");
	public static Textures GAME_MENU_QUITTER_OFF = load("games/menu/quitter_off");
	public static Textures GAME_MENU_INFOS = load("games/menu/infos");
	public static Textures GAME_MENU_JOUER_OFF = load("games/menu/jouer_off");
	public static Textures GAME_MENU_JOUER_ON = load("games/menu/jouer_on");
	public static Textures GAME_MENU_QUITTER_ON = load("games/menu/quitter_on");
	public static Textures GAME_MENU_SCOTCH = load("games/menu/scotch");
	public static Textures GAME_MENU_BG_STATS = load("games/menu/bg_stats");

	private int width, height;
	private int id = -12;
	public String path;
	public BufferedImage bufferedImage;

	private Textures(final String path) {
		this.path = path;
		textures.add(this);
	}

	public Textures(final BufferedImage bufferedImage) {
		this.bufferedImage = bufferedImage;
		textures.add(this);
	}

	public static Textures load(final String path) {
		
		return new Textures("/net/epopy/epopy/display/res/" + path + ".png");
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}
	
	public String getPath() {
		return path;
	}

	public void bind() {
		if (id == -12) {
			
			try {

				if (bufferedImage == null)
					bufferedImage = ImageIO.read(Textures.class.getResource(path));
				BufferedImage image = bufferedImage;

				if (image == null)
					System.err.println("[ERROR] Texture not found: " + path);

				width = image.getWidth();
				height = image.getHeight();

				int[] pixels = new int[width * height];
				image.getRGB(0, 0, width, height, pixels, 0, width);

				IntBuffer buffer = BufferUtils.createIntBuffer(width * height * 4);

				int[] data = new int[pixels.length];
				for (int i = 0; i < data.length; i++) {
					int a = (pixels[i] & 0xff000000) >> 24;
					int r = (pixels[i] & 0xff0000) >> 16;
					int g = (pixels[i] & 0xff00) >> 8;
					int b = pixels[i] & 0xff;

					data[i] = a << 24 | b << 16 | g << 8 | r;
				}
				buffer.put(data);
				buffer.flip();

				glBindTexture(GL_TEXTURE_2D, id = glGenTextures());

				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
				glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

				glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);

				unbind();
			} catch (IOException e) {
				e.printStackTrace();
				System.err.println("[ERROR] Texture not load: " + path);
			}
		}
		glBindTexture(GL_TEXTURE_2D, id);

	}

	private static void unbind() {
		glBindTexture(GL_TEXTURE_2D, 0);
		
	}

	public static void unloadTextures() {
		unbind();
		for (Textures t : textures) {
			if (t.id != -12) {
				GL11.glDeleteTextures(t.id);
				t.id = -12;
			}
		}

	}

	public void renderBackground() {
		renderTexture(Display.getWidth(), Display.getHeight());
	}

	public BufferedImage getBuffImage() {
		if (id == -12)
			bind();
		return bufferedImage;
	}

	public void renderTexture(final float w, final float h) {
		glEnable(GL_TEXTURE_2D);
		bind();
		
		glGenerateMipmapEXT(GL_TEXTURE_2D);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
		glMatrixMode(GL_TEXTURE);
		glLoadIdentity();
		glScalef(1, 1, 1);
		glMatrixMode(GL_MODELVIEW);
		
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0);
		glVertex2f(0, 0);
		glTexCoord2f(1, 0);
		glVertex2f(w, 0);
		glTexCoord2f(1, 1);
		glVertex2f(w, h);
		glTexCoord2f(0, 1);
		glVertex2f(0, h);
		glEnd();
		unbind();
	}

}
