package net.epopy.epopy.games.speedrun.menus;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Timer;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.player.stats.SpeedRunStats;
import net.epopy.epopy.utils.Input;

public class SpeedRunGame extends AbstractGameMenu {
	private static final int menSize = 100;
	
	private double decors;
	private int playerWalk;
	private int playerSneak;
	private int height;
	private int propulsion;
	private int newRobot;
	private double level;
	private boolean sneak;
	List<Robot> robots = new LinkedList<Robot>();
	List<Integer> lampadairesX = new LinkedList<Integer>();
	
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	private static Timer timer;
	private boolean addStats;

	private boolean pauseScreen;
	private boolean paused;
	
	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound() && !Audios.SPEEDRUN.isRunning())
			Audios.SPEEDRUN.start(true).setVolume(0.2f);
		sneak = addStats = gameOver = pauseScreen = false;

		decors = 0.0;
		playerWalk = height = 1;
		newRobot = 50;
		level = 12;
		playerSneak = propulsion = 0;
		robots.clear();
		lampadairesX.clear();
		
		paused = true;
		timer = new Timer();
		timer.pause();
		pause.startPause(5);
	}

	@Override
	public void update() {
		Timer.tick();

		if (pause.isFinish() && !gameOver) {
			if (Input.getKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
				} else {
					pauseScreen = true;
					timer.pause();
					paused = true;
				}
			}
		}

		if (gameOver) {
			if (rejouerButton.isClicked())
				onEnable();
		}

		if (pauseScreen) {
			if (reprendreButton.isClicked()) {
				pause.startPause(3);
				pauseScreen = false;
				Mouse.setGrabbed(true);
			}
		}

		if (pauseScreen || !pause.isFinish() || gameOver) {
			timer.pause();
			return;
		}

		if (paused) {
			timer.resume();
			paused = false;
		}

		if (Input.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}
		level += 0.005;
		if (Input.isKeyDown(SpeedRunOptions.KEY_SNEAK)) {
			sneak = true;
		} else {
			if (sneak) sneak = false;

			if (Input.isKeyDown(SpeedRunOptions.KEY_JUMP)) {
				if (height == 1) {// au sol
					height += 5;
					propulsion = 22;
				} else {
					propulsion++;
				}

			}
		}
		if (height > 1) {
			height += propulsion;
			propulsion -= 2;
		} else {
			height = 1;
		}
		decors -= (float) level / 8;
		if (decors < 0) decors += 1920;

		playerWalk++;
		if (playerWalk > 80) playerWalk = 9;

		playerSneak++;
		if (playerSneak > 10) playerSneak = 0;

		for (int i = robots.size() - 1; i >= 0; i--) {
			Robot r = robots.get(i);

			if (r.x > 0 && r.x < 130) {
				if (r.normalORsneakORlampadaire == 0) {
					if (!sneak) {
						gameOver = true;
						System.out.println("GameOver");
					}
				} else if (r.normalORsneakORlampadaire == 1) {
					if (height < 60) {
						gameOver = true;
						System.out.println("GameOver");
					}
				} else {// lampadaire
					if (height > 130 || sneak) {
						gameOver = true;
						System.out.println("GameOver");
					}
				}

			}
			r.move();
		}

		if (newRobot > 0) newRobot--;
		else {
			robots.add(new Robot());
			newRobot = defaultWidth / (int) level;
		}

	}
	
	@Override
	public void render() {
		
		ComponentsHelper.renderTexture(Textures.SPEEDRUN_BG, decors, 0, 1920, 1080);
		ComponentsHelper.renderTexture(Textures.SPEEDRUN_BG, decors - 1920, 0, 1920, 1080);
		
		for (Robot r : robots) {
			if (r.normalORsneakORlampadaire == 2) {// lampadaire
				drawWithOmbre(Textures.SPEEDRUN_LAMPADAIRE, r.x, defaultHeight / 2 - 180, 100, 300);
			} else if (r.normalORsneakORlampadaire == 1) {// sneak
				if ((r.walk - 5) / 10 == r.walk / 10)// l'unité n'est pas 1/2/3/4/5
					drawWithOmbre(Textures.SPEEDRUN_OTHERMENSOL1, r.x, defaultHeight / 2 + 62, 140, 60);
				else drawWithOmbre(Textures.SPEEDRUN_OTHERMENSOL2, r.x, defaultHeight / 2 + 62, 140, 60);
			} else {// normal
				Textures robotTexture;
				switch (r.walk / 10) {
					case 2:
					robotTexture = Textures.SPEEDRUN_OTHERMEN2;
						break;
					case 3:
					robotTexture = Textures.SPEEDRUN_OTHERMEN3;
						break;
					case 4:
					robotTexture = Textures.SPEEDRUN_OTHERMEN4;
						break;
					case 5:
					robotTexture = Textures.SPEEDRUN_OTHERMEN5;
						break;
					case 6:
					robotTexture = Textures.SPEEDRUN_OTHERMEN6;
						break;
					case 7:
					robotTexture = Textures.SPEEDRUN_OTHERMEN7;
						break;
					case 8:
					robotTexture = Textures.SPEEDRUN_OTHERMEN8;
						break;
					default:
					robotTexture = Textures.SPEEDRUN_OTHERMEN1;
						break;
				}
				drawWithOmbre(robotTexture, r.x, defaultHeight / 2 - 180, 200, 300);
			}
			
		}
		
		if (sneak) {
			if ((playerWalk - 5) / 10 == playerWalk / 10)// l'unité n'est pas 1/2/3/4/5
				drawWithOmbre(Textures.SPEEDRUN_MANSOL1, 50, defaultHeight / 2 + 62, 140, 60);
			else drawWithOmbre(Textures.SPEEDRUN_MANSOL2, 50, defaultHeight / 2 + 62, 140, 60);
			
		} else {
			Textures playerTexture;
			switch (playerWalk / 10) {
				case 2:
				playerTexture = Textures.SPEEDRUN_MAN2;
					break;
				case 3:
				playerTexture = Textures.SPEEDRUN_MAN3;
					break;
				case 4:
				playerTexture = Textures.SPEEDRUN_MAN4;
					break;
				case 5:
				playerTexture = Textures.SPEEDRUN_MAN5;
					break;
				case 6:
				playerTexture = Textures.SPEEDRUN_MAN6;
					break;
				case 7:
				playerTexture = Textures.SPEEDRUN_MAN7;
					break;
				case 8:
				playerTexture = Textures.SPEEDRUN_MAN8;
					break;
				default:
				playerTexture = Textures.SPEEDRUN_MAN1;
					break;
			}
			ComponentsHelper.renderTexture(playerTexture, 50, defaultHeight / 2 - 27 - height, 100, 150);
			glColor4f(0, 0, 0, 0.2f);
			ComponentsHelper.renderTexture(playerTexture, 50, defaultHeight / 2 - 27 + height + 300, 100, -150);
			glColor4f(1, 1, 1, 1);
		}
		if (!gameOver && !paused && !pauseScreen) {
			if (timer != null) {
				boolean record = (long) timer.getTime() > Main.getPlayer().getSpeedRunStats().getRecord();
				ComponentsHelper.drawText(timeFormat.format((long) timer.getTime() * 1000 - 3600000), defaultWidth / 2, 10, PositionWidth.MILIEU, PositionHeight.HAUT, 40, record ? new float[] { 0.7f, 0, 0, 1 } : new float[] { 0.6f, 0.6f, 0.6f, 1 });
			}
		}

		if (!pause.isFinish()) {
			if (Input.getKeyDown(Keyboard.KEY_RETURN)) {
				pause.stopPause();
				return;
			}
			// debut du jeu
			if (pause.getTimePauseTotal() == 5) {
				Textures.GAME_STARTING_BG.renderBackground();
				int x = 1093;
				int y = 400;
				
				ComponentsHelper.drawText("CONTROLES", x, y - 30, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });
				
				ComponentsHelper.drawText("Jump", x, y + 10, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Sneak", x, y + 140, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - 30, y + 45, 60, 60);
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - 30, y + 140 + 35, 60, 60);
				ComponentsHelper.drawText(Input.getKeyName(SpeedRunOptions.KEY_JUMP), x + 16 - 30, y + 37, 50, new float[] { 0, 0, 0, 1 });
				ComponentsHelper.drawText(Input.getKeyName(SpeedRunOptions.KEY_SNEAK), x + 16 - 30, y + 130 + 37, 50, new float[] { 0, 0, 0, 1 });
				
				ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
				ComponentsHelper.drawText("Tenir plus d'", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				ComponentsHelper.drawText("1 minute 20", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				
				ComponentsHelper.drawText(pause.getPauseString(), 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, new float[] { 1, 1, 1, 1 });
			} else
				pause.showRestartChrono();
		}

		if (pauseScreen) {
			if (Mouse.isGrabbed())
				Mouse.setGrabbed(false);
			renderEchap(true);
		}
		if (gameOver) {
			if (Mouse.isGrabbed())
				Mouse.setGrabbed(false);
			SpeedRunStats speedRunStats = Main.getPlayer().getSpeedRunStats();
			String timeString = timeFormat.format((long) timer.getTime() * 1000 - 3600000);
			boolean record = timer.getTime() * 1000 > speedRunStats.getRecord();
			renderEchap(false, timeString, record);
			if (!addStats) {
				speedRunStats.addPartie();
				speedRunStats.addTemps((long) timer.getTime());
				// set best score
				if (record)
					speedRunStats.setRecord((long) timer.getTime());
					
				/**
				 * TODO if (timer.getTime() * 1000 > speedRunStats.getObjectif()) { if (Main.getPlayer().getLevel() <=
				 * GameList.SPEEDRUN.getID()) Main.getPlayer().setLevel(GameList.SPEEDRUN.getID() + 1); }
				 */
				
				addStats = true;
			}
		}
	}
	
	private void drawWithOmbre(final Textures texture, final double x, final double y, final int width, final int height) {

		ComponentsHelper.renderTexture(texture, x, y, width, height);
		glColor4f(0, 0, 0, 0.2f);
		ComponentsHelper.renderTexture(texture, x, y + height * 2, width, -height);
		glColor4f(1, 1, 1, 1);
	}
	
	class Robot {
		
		int x;
		int speed;
		int walk;
		int normalORsneakORlampadaire;
		
		public Robot() {
			x = defaultWidth;
			Random r = new Random();
			normalORsneakORlampadaire = r.nextInt(3);
			speed = (int) level / 2;
			walk = r.nextInt(79) + 1;
		}
		
		public void move() {
			x -= speed;
			if (x < -menSize) {
				robots.remove(this);
			}
			
			walk++;
			if (walk > 80) walk = 9;
			
		}
		
	}
	
}