package net.epopy.epopy.games.placeinvader.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawLine;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;
import static net.epopy.epopy.display.components.ComponentsHelper.renderTexture;
import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.player.stats.PlaceInvaderStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class PlaceInvaderGame extends AbstractGameMenu {
	
	private static final int robotSize = 80;
	private static final int playerSize = 70;
	private static final int yPlayer = defaultHeight - 100;
	private double xPlayer;
	
	private boolean playerShot;
	private int level;
	private int scoreLevel;
	private int score;
	private int life;
	
	private int shooted;
	
	private List<robot> robots;
	private List<Location> locBalleP;
	private List<Location> locBalleR;
	private boolean stats;
	private static int timer;
	private boolean pauseScreen;
	
	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound() && !Audios.PLACEINVADER.isRunning())
			Audios.PLACEINVADER.start(true).setVolume(0.2f);
			
		playerShot = gameOver = stats = false;
		shooted = scoreLevel = score = timer = 0;
		level = 1;
		life = 3;
		robots = new ArrayList<robot>();
		
		locBalleP = new ArrayList<Location>();
		locBalleR = new ArrayList<Location>();
		
		xPlayer = defaultWidth / 2;
		pause.startPause(5);
		Mouse.setGrabbed(true);
	}
	
	@Override
	public void update() {
		if (!pauseScreen && pause.isFinish() && !gameOver) {
			timer++;
		}
		if (pause.isFinish() && !gameOver) {
			if (Input.getKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
					Mouse.setGrabbed(true);
				} else {
					pauseScreen = true;
					Mouse.setGrabbed(false);
				}
			}
		}

		if (gameOver) {
			if (rejouerButton.isClicked())
				onEnable();
			return;
		}
		
		if (pauseScreen) {
			if (reprendreButton.isClicked()) {
				pause.startPause(3);
				pauseScreen = false;
				Mouse.setGrabbed(true);
			}
		}
		
		if (!pause.isFinish() || pauseScreen) return;

		if (life < 0) {
			gameOver = true;
			Mouse.setGrabbed(false);
		}
		
		if (Input.isKeyDown(PlaceInvaderOptions.KEY_RIGHT)) {// aller a droite
			
			xPlayer += 10;
			if (xPlayer + playerSize / 2 > defaultWidth) {
				xPlayer = defaultWidth - playerSize / 2;
			}
			
		}
		if (Input.isKeyDown(PlaceInvaderOptions.KEY_LEFT)) {// aller a gauche
			
			xPlayer -= 10;
			if (xPlayer < playerSize / 2) {
				xPlayer = playerSize / 2;
			}
			
		}
		
		if (Input.isButtonDown(0)) {// tirer
			if (!playerShot) {
				playerShot = true;
				
				locBalleP.add(new Location(xPlayer - 2, yPlayer));
			}
		} else if (playerShot) {
			playerShot = false;
		}
		
		for (int i = locBalleR.size() - 1; i >= 0; i--) {// si le joueur se fait shoot
			Location locBR = locBalleR.get(i);
			if (Math.abs(xPlayer - locBR.getX()) + Math.abs(yPlayer - locBR.getY()) <= playerSize / 2) {
				robots.clear();
				locBalleP.clear();
				locBalleR.clear();
				life--;
				shooted = 50;
				break;
			}
		}
		
		for (int i = locBalleP.size() - 1; i >= 0; i--) {
			Location loc = locBalleP.get(i);
			if (loc.getY() <= 1) {
				locBalleP.remove(i);
				Random r = new Random();
				loc.setX(playerSize / 2 + r.nextInt(defaultWidth - playerSize));
				locBalleR.add(loc);// rebonds du joueur
			} else
				loc.remove(0, 10);
				
		}
		
		if (!locBalleR.isEmpty())
			for (int i = locBalleR.size() - 1; i >= 0; i--) {
			Location loc = locBalleR.get(i);
			if (loc.getY() >= defaultHeight - 1) {
				locBalleR.remove(i);
			} else
				loc.add(0, 10);
				
		}
		
		if (robots.size() + scoreLevel < level && new Random().nextInt(30) == 1) new robot();
		
		for (int i = robots.size() - 1; i >= 0; i--) {
			if (robots.isEmpty()) break;
			robots.get(i).update();
		}
		
		if (scoreLevel == level) {
			level++;
			scoreLevel = 0;
		}
		if (shooted > 0) shooted--;
	}
	
	@Override
	public void render() {
		Textures.PLACEINVADER_LEVEL_BG.renderBackground();
		
		if (shooted > 0 && (shooted - 5) / 10 == shooted / 10) glColor4f(1, 0.2f, 0.2f, 1);
		renderTexture(Textures.PLACEINVADER_ROCKET, xPlayer - playerSize / 2, yPlayer, playerSize, 100);
		glColor4f(1, 1, 1, 1);
		
		if (pauseScreen) {
			renderEchap(true);
			return;
		} else if (gameOver) {
			renderTexture(Textures.PLACEINVADER_EXPLOSION, xPlayer - playerSize / 2 - 18, yPlayer, 100, 100);
			
			boolean record = false;
			if (!stats) {
				stats = true;
				PlaceInvaderStats placeInvaderStats = Main.getPlayer().getPlaceInvaderStats();
				placeInvaderStats.addPartie();
				placeInvaderStats.addTemps(timer / 60);

				if (score >= placeInvaderStats.getRecord()) {
					placeInvaderStats.setRecord(score);
					record = true;
				}
				
				if (score >= placeInvaderStats.getObjectif()) {
					if (Main.getPlayer().getLevel() <= GameList.PLACEINVADER.getID())
						Main.getPlayer().setLevel(GameList.PLACEINVADER.getID() + 1);
				}
			}
			
			renderEchap(false, score + "", record);
			
			if (rejouerButton.isClicked())
				onEnable();
			return;
		} else if (!pause.isFinish()) {
			if (Input.getKeyDown(Keyboard.KEY_RETURN)) {
				pause.stopPause();
				return;
			}
			if (pause.getTimePauseTotal() == 5) {

				Textures.GAME_STARTING_BG.renderBackground();
				
				int x = 1093;
				int y = 400;
				float[] orange = new float[] { 1, 0.5f, 0, 1 };
				float[] white = new float[] { 1, 1, 1, 1 };
				float[] grey = new float[] { 0.8f, 0.8f, 0.8f, 1 };
				drawText("CONTROLES", x, y - 40, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, orange);
				drawText("Droite", x - 80, y + 5, PositionWidth.MILIEU, PositionHeight.HAUT, 25, white);
				drawText("Gauche", x - 80, y + 150, PositionWidth.MILIEU, PositionHeight.HAUT, 25, white);
				
				drawText(Input.getKeyName(PlaceInvaderOptions.KEY_RIGHT), x - 28 - 80, y + 40, 50, white);
				drawText(Input.getKeyName(PlaceInvaderOptions.KEY_LEFT), x - 28 - 80, y + 150 + 30, 50, white);
				
				drawText("Tir", x + 80, y + 5, PositionWidth.MILIEU, PositionHeight.HAUT, 25, white);
				
				renderTexture(Textures.GAME_GAUCHE_SOURIS, x - 30 + 60, y + 45, 100, 100);
				
				drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, orange);
				drawText("Obtenir plus de", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, grey);
				drawText("100 points.", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, grey);
				
				drawText(pause.getPauseString(), 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, white);
				return;
			} else
				pause.showRestartChrono();
		}
		
		for (int i = robots.size() - 1; i >= 0; i--) {
			if (robots.isEmpty()) break;
			robot rob = robots.get(i);
			renderTexture(Textures.PLACEINVADER_SPACESHIP, rob.loc.getX() - robotSize / 2, rob.loc.getY() - robotSize / 2, robotSize, robotSize);
			if (rob.dead) {
				robots.remove(rob);
				renderTexture(Textures.PLACEINVADER_EXPLOSION, rob.loc.getX() - robotSize / 2, rob.loc.getY() - robotSize / 2, robotSize + 10, robotSize);
			}
		}
		
		for (Location loc : locBalleP)
			drawLine(loc.getX(), loc.getY(), loc.getX(), loc.getY() - 30, 5, new float[] { 0, 1, 0, 1 });
			
		for (Location loc : locBalleR)
			drawLine(loc.getX(), loc.getY(), loc.getX(), loc.getY() + 30, 5, new float[] { 1, 0, 0, 1 });
			
		drawText(score + " point" + (score < 2 ? "" : "s"), 30, 30, 30);
		
		for (int i = 0; i < life; i++)
			renderTexture(Textures.PLACEINVADER_ROCKET, defaultWidth - (65 + i * 60), 40, 35, 50);
			
	}
	
	public class robot {
		private static final double speedRobot = 0.8;
		private final Random r = new Random();
		Location loc;
		Boolean left;
		int limitX;
		boolean dead;
		
		public robot() {
			left = r.nextBoolean();
			loc = new Location(r.nextInt(defaultWidth - robotSize * 2) + robotSize, r.nextInt(defaultHeight / 2));
			limitX = (int) loc.getX();
			dead = false;
			robots.add(this);
		}
		
		public void update() {
			double xR = loc.getX();
			double yR = loc.getY();
			
			if (left) {
				xR -= speedRobot;
				if (xR < limitX || xR < 0 + robotSize) {
					left = false;
					limitX = (int) (xR + r.nextInt(500));
				}
				
			} else {
				xR += speedRobot;
				if (xR > limitX || xR + robotSize > defaultWidth) {
					left = true;
					limitX = (int) (xR - r.nextInt(500));
				}
			}
			yR += speedRobot;
			
			loc.setPos(xR, yR);
			
			if (yR > defaultHeight - playerSize) {
				robots.clear();
				life--;
			}
			
			for (int i = locBalleP.size() - 1; i >= 0; i--) {
				Location locBP = locBalleP.get(i);
				if (loc.distance(locBP) <= robotSize / 2) {
					dead = true;
					locBalleP.remove(locBP);
					score++;
					scoreLevel++;
				}
			}
			
			if (r.nextInt(200) == 1) {// tire !
				locBalleR.add(new Location(loc.getX() - 1, loc.getY() + 10));
				
			}
			
		}
		
	}
	
}
