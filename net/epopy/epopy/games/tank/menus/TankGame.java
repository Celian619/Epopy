package net.epopy.epopy.games.tank.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawCircle;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;
import static net.epopy.epopy.display.components.ComponentsHelper.renderTexture;
import static org.lwjgl.opengl.GL11.glColor4f;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.player.stats.TankStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class TankGame extends AbstractGameMenu {
	
	private static final int maxRebonds = 3;
	
	private static final int tankSize = 25;
	private static final double speedTank = 2;
	private static final int maxBalles = 5;
	
	private Location locPlayer;
	private Location locRobot;
	
	private List<Location> tankPrintP;
	private List<Location> tankPrintR;
	private List<Ball> balles;
	
	private int tankAspectP;
	private int tankAspectR;
	private int print;
	private int[] distanceBlocks;
	
	private static int timer;
	
	private double speedRobot;
	
	private int cooldownRobot;
	private int robotBallesNbr;
	private int directionMax;
	private int recul;
	private int damaged;
	private int damage;
	
	private boolean tir;
	private boolean win;
	private boolean stats;
	private boolean record;
	private boolean pauseScreen;
	
	/*
	 *
	 * Lancement de Tank avec le save de tout les pixels a moins de tanksize pixels d'un mur dans la variable blocksPos. Ce qui permet de
	 * faire des collisions propres et optimisées
	 *
	 */
	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound() && !Audios.TANK.isRunning())
			Audios.TANK.start(true).setVolume(0.3f);
		song = Audios.TANK;
		cooldownRobot = 25;
		
		damaged = damage = robotBallesNbr = directionMax = print = tankAspectR = tankAspectP = timer = 0;
		
		speedRobot = speedTank;
		recul = -10;
		
		record = gameOver = stats = tir = pauseScreen = win = false;

		locPlayer = new Location(150, defaultHeight / 2, 0);
		locRobot = new Location(1740, defaultHeight / 2, 180);
		
		tankPrintP = new LinkedList<Location>();
		tankPrintR = new LinkedList<Location>();
		balles = new LinkedList<Ball>();
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource(Textures.TANK_LEVELBG.getPath()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		int width = img.getWidth();
		
		int nbrOfPixels = img.getHeight() * img.getWidth();
		distanceBlocks = new int[nbrOfPixels];
		
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int argb = img.getRGB(x, y);
				if ((argb >> 16 & 0xff) == 82 && (argb >> 8 & 0xff) == 68 && (argb & 0xff) == 60) {
					distanceBlocks[width * y + x] = 1;
				}
			}
		}
		
		while (true) {
			
			int[] added = new int[nbrOfPixels];
			
			int modifs = 0;
			for (int n = width; n < nbrOfPixels - width; n++) {
				
				if (distanceBlocks[n] == 0) {
					int max1 = Math.max(distanceBlocks[n + 1], distanceBlocks[n - 1]);
					int max2 = Math.max(distanceBlocks[n + width], distanceBlocks[n - width]);
					int maxTotal = Math.max(max1, max2);
					
					if (maxTotal > 0) {
						added[n] = maxTotal + 1;
						modifs++;
					}
					
				}
			}
			
			for (int n = width; n < nbrOfPixels - width; n++) {
				if (distanceBlocks[n] == 0)
					distanceBlocks[n] = added[n];
			}
			
			if (modifs == 0) break;
			pause.startPause(5);
		}
	}
	
	/*
	 *
	 * Update de la position tank avec sauvegarde de ses dernieres positions
	 *
	 *
	 */

	@Override
	public void update() {
		
		if (pause.isFinish() && !gameOver && !pauseScreen) {
			timer++;
		}
		if (pause.isFinish() && !gameOver) {
			if (Input.getKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
				} else {
					pauseScreen = true;
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
			}
		}
		
		if (pauseScreen || !pause.isFinish() || gameOver)
			return;
			
		print++;
		if (print >= 15) {
			
			tankPrintP.add(0, locPlayer.clone());
			
			if (tankPrintP.size() > 10)
				tankPrintP.remove(tankPrintP.size() - 1);
				
			tankPrintR.add(0, locRobot.clone());
			
			if (tankPrintR.size() > 10)
				tankPrintR.remove(tankPrintR.size() - 1);
				
			print = 0;
			
		}
		
		if (!isMouseDistanceNear()) {
			
			// rotation du tank
			int rotationSpeed = 5;
			int directionM = getDirectionMouse();
			int directionPlayer = locPlayer.getDirection();
			
			if (Math.abs(directionM - directionPlayer) <= rotationSpeed) {
				locPlayer.setDirection(directionM);
			} else {
				
				boolean directionInverse = Math.abs(directionM - directionPlayer) > 180;
				
				if (directionPlayer < directionM) {
					if (directionInverse) directionPlayer -= rotationSpeed;
					else directionPlayer += rotationSpeed;
				} else {
					if (directionInverse) directionPlayer += rotationSpeed;
					else directionPlayer -= rotationSpeed;
				}
				
				if (directionPlayer >= 180) directionPlayer -= 360;
				else if (directionPlayer < -180) directionPlayer += 360;
				
				locPlayer.setDirection(directionPlayer);
			}
			
			// controle avant-arriere du tank
			if (Input.isKeyDown(TankOptions.KEY_UP)) {
				movePlayer(false);
			} else if (Input.isKeyDown(TankOptions.KEY_DOWN)) {
				movePlayer(true);
			}
			
		}
		// tir
		if (Input.isButtonDown(0)) {
			if (!tir && damaged == 0) {
				balles.add(new Ball(locPlayer.clone(), true, false));
				
				if (balles.size() - robotBallesNbr > maxBalles) {// tir alors qu'il y a trop de balles
					for (int i = 0; i < balles.size(); i++) {
						Ball b = balles.get(i);
						if (b.isPlayerBall()) {
							balles.remove(b);
							break;
						}
					}
				}
				tir = true;
			}
		} else {
			tir = false;
		}
		
		for (int i = balles.size() - 1; i >= 0; i--) {
			if (balles.size() > i)// Si suppr les balles
				balles.get(i).refreshPos();
		}
		
		playRobot();
	}
	
	@Override
	public void render() {
		Textures.TANK_LEVELBG.renderBackground();
		
		if (!gameOver && !win && !pauseScreen && pause.isFinish())
			drawText("Score: " + damage, defaultWidth / 2, defaultHeight / 2 + 35, PosWidth.MILIEU, PosHeight.HAUT, 40, new float[] { 0.7f, 0.7f, 0.7f, 1 });
			
		// empreintes du tank
		float f = 0.20f;
		for (Location loc : tankPrintP) {
			glColor4f(0.32f, 0.26f, 1, f);
			renderTexture(Textures.TANK_TANKPRINT, loc.getX() - 32, loc.getY() - 28, 64, 56, loc.getDirection());
			f -= 0.02f;
		}
		
		f = 0.20f;
		for (Location loc : tankPrintR) {
			glColor4f(1, 0f, 0f, f);
			renderTexture(Textures.TANK_TANKPRINT, loc.getX() - 32, loc.getY() - 28, 64, 56, loc.getDirection());
			f -= 0.02f;
		}
		
		glColor4f(1, 1, 1, 1);
		
		for (Ball b : balles) {
			if (b.isPlayerBall())
				drawCircle(b.getLocation().getX(), b.getLocation().getY(), 5, 10, new float[] { 0f, 0f, 1, 1 });
			else
				drawCircle(b.getLocation().getX(), b.getLocation().getY(), 5, 10, new float[] { 1, 0f, 0f, 1 });
				
		}
		
		// Affiche le joueur
		Textures texturePlayer = tankAspectP <= 5 ? Textures.TANK_TANK2 : Textures.TANK_TANK1;
		if (tankAspectP == 10) tankAspectP = 0;
		
		renderTexture(texturePlayer, locPlayer.getX() - 32, locPlayer.getY() - 28, 64, 56, locPlayer.getDirection());
		// Affiche le Bot
		Textures textureBot = tankAspectR <= 5 ? Textures.TANK_TANK2 : Textures.TANK_TANK1;
		if (tankAspectR == 10) tankAspectR = 0;
		
		if (damaged > 0) {// a été touché
			damaged--;
			glColor4f(1, 0f, 0f, 0.5f);
		}
		
		renderTexture(textureBot, locRobot.getX() - 32, locRobot.getY() - 28, 64, 56, locRobot.getDirection());
		
		glColor4f(1, 1, 1, 1);
		if (!pause.isFinish()) {
			if (Input.getKeyDown(Keyboard.KEY_RETURN)) {
				pause.stopPause();
				return;
			}
			// debut du jeu
			if (pause.getTimePauseTotal() == 5) {
				Textures.GAME_STARTING_BG.renderBackground();

				float[] orange = new float[] { 1, 0.5f, 0, 1 };
				float[] white = new float[] { 1, 1, 1, 1 };
				float[] grey = new float[] { 0.8f, 0.8f, 0.8f, 1 };
				
				drawText("CONTROLES", 1093, 430, PosWidth.MILIEU, PosHeight.MILIEU, 30, orange);
				drawText("Avancer", 1093, 410, PosWidth.MILIEU, PosHeight.HAUT, 25, white);
				drawText("Reculer", 1093, 540, PosWidth.MILIEU, PosHeight.HAUT, 25, white);
				
				drawText(Input.getKeyName(TankOptions.KEY_UP), 1081, 438, 50, white);
				drawText(Input.getKeyName(TankOptions.KEY_DOWN), 1081, 568, 50, white);

				drawText("OBJECTIF", 660, 495, PosWidth.GAUCHE, PosHeight.HAUT, 30, orange);
				drawText("Tuer plus de", 710, 600, PosWidth.MILIEU, PosHeight.HAUT, 25, grey);
				drawText(Main.getPlayer().getTankStats().getObjectif() + " fois le bot.", 710, 630, PosWidth.MILIEU, PosHeight.HAUT, 25, grey);
				
				drawText(pause.getPauseString(), 660, 335, PosWidth.GAUCHE, PosHeight.HAUT, 100, white);
			} else
				pause.showRestartChrono();
		}
		
		if (pauseScreen)
			renderEchap(true);
			
		if (gameOver) {
			if (!stats) {
				stats = true;
				TankStats tankStats = Main.getPlayer().getTankStats();
				tankStats.addPartie();
				tankStats.addTemps(timer / 60);
				if (tankStats.getRecord() == 0 || damage > tankStats.getRecord()) {
					tankStats.setRecord(damage);
					record = true;
				}
				
				if (tankStats.getRecord() >= tankStats.getObjectif())
					if (Main.getPlayer().getLevel() <= GameList.TANK.getID())
						Main.getPlayer().setLevel(GameList.TANK.getID() + 1);
						
			}
			renderEchap(false, +damage + "", record);
		}
		
	}
	
	/*
	 *
	 * Methodes utilisees
	 *
	 */
	
	private void playRobot() {
		Location nearBalleLoc = null;
		for (Ball b : balles) {
			Location locB = b.getLocation();
			if (b.isPlayerBall() && locB.distance(locRobot) < 100) {
				Location loc = new Location(deplacedX(locB, Ball.speedBall), deplacedX(locB, Ball.speedBall));
				if (loc.distance(locRobot) < locB.distance(locRobot)) {
					nearBalleLoc = b.getLocation();
					break;
				}
				
			}
		}
		
		if (nearBalleLoc != null) {
			
			double Xa = nearBalleLoc.getX() - locRobot.getX();
			double Ya = nearBalleLoc.getY() - locRobot.getY();
			
			int directionBalle = (int) ((Ya < 0 ? -1 : 1) * Math.toDegrees(Math.acos(Xa / Math.sqrt(Xa * Xa + Ya * Ya))));
			
			int directionR = locRobot.getDirection();
			directionR += (directionBalle - directionR) / 10.0;
			locRobot.setDirection(directionR);
			recul -= 5;
			
			shootRobot();
			
		} else {
			
			double Xa = locPlayer.getX() - locRobot.getX();
			double Ya = locPlayer.getY() - locRobot.getY();
			
			int directionP = (int) ((Ya < 0 ? -1 : 1) * Math.toDegrees(Math.acos(Xa / Math.sqrt(Xa * Xa + Ya * Ya))));
			
			speedRobot += (2 * new Random().nextDouble() - 1) / 5;// -0.1 à 0.1
			
			if (speedRobot < speedTank - 1) speedRobot = speedTank - 1;
			if (speedRobot > speedTank + 1) speedRobot = speedTank + 1;
			
			double bestDirection = getBestDirectionRobot(speedRobot);
			
			int directionR = locRobot.getDirection();
			
			bestDirection = directionR + (bestDirection - directionR) / 20.0;
			
			double rotationSpeed = 1;
			if (damaged == 0 && !isWall(locRobot, locPlayer, directionP)) {
				
				shootRobot();
				if (distanceBlocks[1920 * (int) locRobot.getY() + (int) locRobot.getX()] < 35)
					recul = new Random().nextInt(100);
				else
					recul++;
				rotationSpeed = 5;
			} else {
				
				if (damaged == 0 && rebondKill()) shootRobot();
				
				if (recul > -10) recul--;
			}
			
			if (bestDirection > 180)
				bestDirection -= 360;
			if (bestDirection < -180)
				bestDirection += 360;
				
			boolean directionInverse = Math.abs(directionP - bestDirection) > 180;
			
			if (bestDirection < directionP) {
				if (directionInverse) bestDirection -= rotationSpeed;
				else bestDirection += rotationSpeed;
			} else {
				if (directionInverse) bestDirection += rotationSpeed;
				else bestDirection -= rotationSpeed;
			}
			
			if (bestDirection > 180)
				bestDirection -= 360;
			if (bestDirection < -180)
				bestDirection += 360;
				
			directionMax = Math.max(directionMax, locRobot.getDirection());
			
			locRobot.setDirection((int) bestDirection);
		}
		// speed = speed / 2;
		
		// le robot avance toujours
		double x = deplacedX(locRobot, (recul > 0 ? -1 : 1) * speedRobot);
		double y = deplacedY(locRobot, (recul > 0 ? -1 : 1) * speedRobot);
		
		if (!isCollision((int) x, (int) y)) {
			locRobot.setPos(x, y);
		} else if (!isCollision((int) x, (int) locRobot.getY())) {
			locRobot.setPos(x, locRobot.getY());
		} else if (!isCollision((int) locRobot.getX(), (int) y)) {
			locRobot.setPos(locRobot.getX(), y);
		}
		
		tankAspectR++;
		
	}
	
	private boolean rebondKill() {
		
		Ball b = new Ball(locRobot.clone(), false, true);
		while (!b.isTestFinish)
			b.refreshPos();
			
		return b.testResult;
	}
	
	private void shootRobot() {
		if (cooldownRobot <= 0) {
			
			if (robotBallesNbr < maxBalles) {// tir alors qu'il y a trop de balles
				robotBallesNbr++;
				balles.add(new Ball(locRobot.clone(), false, false));
				cooldownRobot = 25;
			}
			
		} else {
			cooldownRobot--;
		}
	}
	
	private int getBestDirectionRobot(final double speed) {
		
		int bestGood = -1;
		int bestDirection = locRobot.getDirection();
		// si distance est tjr pareil (mur continuer les recherche de direction
		for (int i = 0; i < 180; i++) {// fait -1 1, -2 2 ... -179 179;
			for (int n = 1; n > -2; n -= 2) {
				
				int direction = locRobot.getDirection() + n * i;
				
				if (direction > 360)
					direction -= 360;
				if (direction < -360)
					direction += 360;
					
				Location locTo = locRobot.clone();
				locTo.setDirection(direction);
				
				int dist = distanceBlocks[1920 * (int) deplacedY(locTo, speed) + (int) deplacedX(locTo, speed)];
				
				if (dist > bestGood) {
					bestDirection = direction;
					bestGood = dist;
					
				}
				
				if (i > 30 && bestGood > 35) {// dans un rayon de 30 + 30 ° sauf si mur
					
					return bestDirection;
				}
				
			}
		}
		return bestDirection;
	}
	
	private boolean isWall(final Location locR, final Location locTarget, final int direction) {
		
		Location locTest = locR.clone();
		locTest.setDirection(direction);
		final double xT = locTarget.getX();
		final double yT = locTarget.getY();
		
		while (Math.abs(xT - locTest.getX()) > tankSize || Math.abs(yT - locTest.getY()) > tankSize) {
			if (isBallCollision((int) locTest.getX(), (int) locTest.getY())) return true;
			locTest.setPos(deplacedX(locTest, 1), deplacedY(locTest, 1));
		}
		
		return false;
	}
	
	private void movePlayer(final boolean backward) {
		double x = deplacedX(locPlayer, backward ? -speedTank : speedTank);
		double y = deplacedY(locPlayer, backward ? -speedTank : speedTank);
		
		tankAspectP++;
		if (!isCollision((int) x, (int) y)) {
			locPlayer.setPos(x, y);
		} else if (!isCollision((int) x, (int) locPlayer.getY())) {
			locPlayer.setPos(x, locPlayer.getY());
		} else if (!isCollision((int) locPlayer.getX(), (int) y)) {
			locPlayer.setPos(locPlayer.getX(), y);
		} else {
			tankAspectP--;// pas bouge
		}
		
	}

	// collision pour le rebond des balles
	protected boolean isBallCollision(final int x, final int y) {
		return distanceBlocks[1920 * y + x] == 1;
	}
	
	// collision pour les tank (mur)
	private boolean isCollision(final int x, final int y) {
		return distanceBlocks[1920 * y + x] < tankSize + 3;
	}
	
	private int getDirectionMouse() {
		double Xa = Mouse.getX() / (double) Display.getWidth() * defaultWidth - locPlayer.getX();
		double Ya = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight - locPlayer.getY();
		return (int) ((Ya < 0 ? -1 : 1) * Math.toDegrees(Math.acos(Xa / Math.sqrt(Xa * Xa + Ya * Ya))));
	}
	
	private boolean isMouseDistanceNear() {
		double Xa = Mouse.getX() / (double) Display.getWidth() * defaultWidth;
		double Ya = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight;
		Location locMouse = new Location(Xa, Ya);
		
		return locMouse.distance(locPlayer) <= tankSize;
	}
	
	private double deplacedX(final Location loc, final double speed) {
		return loc.getX() + speed * Math.cos(Math.toRadians(loc.getDirection()));
	}
	
	private double deplacedY(final Location loc, final double speed) {
		return loc.getY() + speed * Math.sin(Math.toRadians(loc.getDirection()));
	}
	
	class Ball {
		private static final int speedBall = 8;
		private final Location loc;
		private final boolean playerOwn;
		
		private final boolean testBall;
		private boolean isTestFinish;
		private boolean testResult;
		private int rebonds = 0;
		
		public Ball(final Location location, final boolean playerOwn, final boolean isTest) {
			loc = location;
			this.playerOwn = playerOwn;
			testBall = isTest;
			if (isTest) {
				isTestFinish = false;
				testResult = false;
			}
		}
		
		public Location getLocation() {
			return loc;
		}
		
		public void refreshPos() {
			
			double x = deplacedX(loc, speedBall);
			double y = deplacedY(loc, speedBall);
			
			if (!isBallCollision((int) x, (int) y)) {
				loc.setPos(x, y);
			} else if (!isBallCollision((int) x, (int) loc.getY())) {// plafond
				int direction = 360 - loc.getDirection();
				if (direction >= 180) direction -= 360;
				loc.setDirection(direction);
				rebonds++;
			} else if (!isBallCollision((int) loc.getX(), (int) y)) {// mur
				int direction = 540 - loc.getDirection();
				if (direction >= 180) direction -= 360;
				loc.setDirection(direction);
				rebonds++;
			} else {
				int direction = loc.getDirection() - 180;
				if (direction <= -180) direction += 360;
				loc.setDirection(direction);
			}
			
			if (playerOwn) {
				if (loc.distance(locRobot) < tankSize) {
					if (damaged == 0) {
						damage++;
						damaged = 500;
					}
					balles.remove(this);
				} else if (loc.distance(locPlayer) < tankSize && rebonds > 0)
					balles.remove(this);
				else if (rebonds > maxRebonds)
					balles.remove(this);
					
			} else {
				if (loc.distance(locPlayer) < tankSize) {
					if (testBall) {
						isTestFinish = true;
						testResult = true;
						return;
					}
					
					gameOver = true;
				} else if (loc.distance(locRobot) < tankSize && rebonds > 0) {
					
					if (testBall) {
						isTestFinish = true;
						testResult = false;
						return;
					}
					
					balles.remove(this);
					robotBallesNbr--;
				} else if (rebonds > maxRebonds) {
					
					if (testBall) {
						isTestFinish = true;
						testResult = false;
						
						return;
					}
					
					balles.remove(this);
					robotBallesNbr--;
				}
			}
			
			if (balles.contains(this) && balles.size() > 1 && !testBall) {// pas encore supprimé
				for (int i = balles.size() - 1; i >= 0; i--) {
					Ball b = balles.get(i);
					if (b != this) {
						if (b.getLocation().distance(loc) < 10) {
							balles.remove(this);
							balles.remove(b);
							if (!playerOwn) robotBallesNbr--;
							if (!b.playerOwn) robotBallesNbr--;
							
							break;
						}
					}
				}
			}
			
		}
		
		public boolean isPlayerBall() {
			return playerOwn;
		}
		
	}
	
}
