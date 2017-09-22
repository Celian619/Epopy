package net.epopy.epopy.games.tank.menus;

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
import org.lwjgl.util.Timer;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.player.stats.TankStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class TankGame extends AbstractGameMenu {
	
	static final int maxRebonds = 3;
	static TankGame game;
	
	static final int tankSize = 25;
	private static final double speedTank = 2;
	private static final int maxBalles = 5;
	
	Location locPlayer;
	Location locRobot;
	
	private List<Location> tankPrintP;
	private List<Location> tankPrintR;
	
	private int tankAspectP;
	private int tankAspectR;
	private int print;
	public int[] distanceBlocks;
	
	private static Timer timer;
	
	private double speedRobot;
	
	private int cooldownRobot;
	int robotBallesNbr;
	private int directionMax;
	private int recul;
	int damaged;
	int damage;
	
	private boolean smoke;
	private boolean tir;
	private boolean win;
	private boolean stats;
	private boolean record;
	
	/*
	 *
	 * Lancement de Tank avec le save de tout les pixels a moins de tanksize pixels d'un mur dans la variable blocksPos. Ce qui permet de
	 * faire des collisions propres et optimisées
	 *
	 */
	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound() && !Audios.TANK.isRunning())
			Audios.TANK.start(true).setVolume(0.2f);
		game = this;

		damaged = print = tankAspectR = tankAspectP = timeTamp = damage = 0;
		cooldownRobot = 25;
		robotBallesNbr = 0;
		directionMax = 0;
		speedRobot = speedTank;
		recul = -10;
		
		paused = true;
		record = stats = pauseScreen = win = gameOver = tir = false;
		locPlayer = new Location(150, defaultHeight / 2, 0);
		locRobot = new Location(1750, defaultHeight / 2, 180);
		
		tankPrintP = new LinkedList<Location>();
		tankPrintR = new LinkedList<Location>();
		Ball.balles = new LinkedList<Ball>();
		BufferedImage img = null;
		try {
			img = ImageIO.read(this.getClass().getResource(Textures.GAME_TANK_WALL.getPath()));
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
		}
		pause.startPause(5);
		timer = new Timer();
	}
	
	/*
	 *
	 * Update de la position tank avec sauvegarde de ses dernieres positions
	 *
	 *
	 */
	private int timeTamp;
	private boolean pauseScreen;
	private boolean paused;
	
	@Override
	public void update() {
		Timer.tick();
		if (timeTamp <= 0 && pause.isFinish() && !gameOver) {
			if (Input.getKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
				} else {
					pauseScreen = true;
					timer.pause();
					paused = true;
				}
				timeTamp = 10;
			}
		} else
			timeTamp--;
			
		if (gameOver || win) {
			if (rejouerButton.isClicked())
				onEnable();
		}
		
		if (pauseScreen) {
			if (reprendreButton.isClicked()) {
				pause.startPause(3);
				pauseScreen = false;
			}
		}
		
		if (pauseScreen || !pause.isFinish() || gameOver || win) {
			if (timer != null)
				timer.pause();
			return;
		}
		
		if (timer == null)
			timer = new Timer();
			
		if (paused) {
			timer.resume();
			paused = false;
		}
		
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
			// controle avant-arriere du tank + souris
			if (Input.isKeyDown(TankOptions.KEY_UP)) {
				movePlayer(false, true);
			} else if (Input.isKeyDown(TankOptions.KEY_DOWN)) {
				movePlayer(true, true);
			} else {
				movePlayer(false, false);
			}
		}
		// tir
		if (Input.isButtonDown(0)) {
			if (!tir && damaged == 0) {
				Ball.balles.add(new Ball(locPlayer.clone(), true, false));
				
				if (Ball.balles.size() - robotBallesNbr > maxBalles) {// tir alors qu'il y a trop de balles
					for (int i = 0; i < Ball.balles.size(); i++) {
						Ball b = Ball.balles.get(i);
						if (b.isPlayerBall()) {
							Ball.balles.remove(b);
							break;
						}
					}
				}
				tir = true;
			}
		} else {
			tir = false;
		}
		
		for (int i = Ball.balles.size() - 1; i >= 0; i--) {
			if (Ball.balles.size() > i)// Si suppr les balles
				Ball.balles.get(i).refreshPos();
		}
		
		playRobot();
	}
	
	@Override
	public void render() {
		
		ComponentsHelper.drawQuad(0, 0, defaultWidth, defaultHeight, new float[] { 0, 0.3f, 0.1f, 1 });
		
		if (!gameOver && !win && !pauseScreen && pause.isFinish())
			ComponentsHelper.drawText("Score: " + damage, defaultWidth / 2, defaultHeight / 2 + 35, PositionWidth.MILIEU, PositionHeight.HAUT, 40, new float[] { 0.7f, 0.7f, 0.7f, 1 });
			
		// empreintes du tank
		float f = 0.20f;
		for (Location loc : tankPrintP) {
			glColor4f(0.32f, 0.26f, 1, f);
			ComponentsHelper.renderTexture(Textures.GAME_TANK_TANKPRINT, loc.getX() - 32, loc.getY() - 28, 64, 56, loc.getDirection());
			f -= 0.02f;
		}
		
		f = 0.20f;
		for (Location loc : tankPrintR) {
			glColor4f(1, 0f, 0f, f);
			ComponentsHelper.renderTexture(Textures.GAME_TANK_TANKPRINT, loc.getX() - 32, loc.getY() - 28, 64, 56, loc.getDirection());
			f -= 0.02f;
		}
		
		glColor4f(1, 1, 1, 1);
		
		for (Ball b : Ball.balles) {
			if (b.isPlayerBall())
				ComponentsHelper.drawCircle(b.getLocation().getX(), b.getLocation().getY(), 5, 10, new float[] { 0f, 0f, 1, 1 });
			else
				ComponentsHelper.drawCircle(b.getLocation().getX(), b.getLocation().getY(), 5, 10, new float[] { 1, 0f, 0f, 1 });
				
		}
		if (smoke) {
			Random r = new Random();
			double xS = locPlayer.getX() + r.nextInt(tankSize + 5) * (r.nextBoolean() ? -1 : 1);
			double yS = locPlayer.getY() + r.nextInt(tankSize + 5) * (r.nextBoolean() ? -1 : 1);
			ComponentsHelper.renderTexture(Textures.GAME_TANK_SMOKE, xS, yS, 20, 20);
		}
		// Affiche le joueur
		Textures texturePlayer = tankAspectP <= 5 ? Textures.GAME_TANK_TANK2 : Textures.GAME_TANK_TANK1;
		if (tankAspectP == 10) tankAspectP = 0;
		
		ComponentsHelper.renderTexture(texturePlayer, locPlayer.getX() - 32, locPlayer.getY() - 28, 64, 56, locPlayer.getDirection());

		// Affiche le Bot
		Textures textureBot = tankAspectR <= 5 ? Textures.GAME_TANK_TANK2 : Textures.GAME_TANK_TANK1;
		if (tankAspectR == 10) tankAspectR = 0;
		
		if (damaged > 0) {// a été touché
			damaged--;
			glColor4f(1, 0f, 0f, 0.5f);
		}
		
		ComponentsHelper.renderTexture(textureBot, locRobot.getX() - 32, locRobot.getY() - 28, 64, 56, locRobot.getDirection());
		
		glColor4f(1, 1, 1, 1);

		Textures.GAME_TANK_WALL.renderBackground();// les murs
		
		if (!pause.isFinish()) {
			// debut du jeu
			if (pause.getTimePauseTotal() == 5) {
				Textures.GAME_STARTING_BG.renderBackground();
				
				int x = 1093;
				int y = 400;
				
				ComponentsHelper.drawText("CONTROLES", x, y - 30, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });
				ComponentsHelper.drawText("Avancer", x, y + 10, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Reculer", x, y + 140, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - 30, y + 45, 60, 60);
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - 30, y + 150 + 25, 60, 60);
				ComponentsHelper.drawText(Input.getKeyName(TankOptions.KEY_UP), x - 28 + 16, y + 38, 50, new float[] { 0, 0, 0, 1 });
				ComponentsHelper.drawText(Input.getKeyName(TankOptions.KEY_DOWN), x - 28 + 16, y + 150 + 18, 50, new float[] { 0, 0, 0, 1 });
				
				// if(Main.getPlayer().getLevel() <= GameList.CAR.getID()) { BATTRE SON RECORD :
				ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
				ComponentsHelper.drawText("Tuer plus de", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				ComponentsHelper.drawText(Main.getPlayer().getTankStats().getObjectif() + " fois le bot.", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				
				ComponentsHelper.drawText(pause.getPauseString(), 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, new float[] { 1, 1, 1, 1 });
			} else
				ComponentsHelper.drawText(pause.getPauseString(), defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.MILIEU, 60 * 2, new float[] { 0.3f, 0.3f, 0.3f, 1 });
				
		}
		
		if (pauseScreen)
			renderEchap(true);
			
		if (gameOver) {
			if (!stats) {
				stats = true;
				TankStats tankStats = Main.getPlayer().getTankStats();
				tankStats.addPartie();
				tankStats.addTemps((long) timer.getTime());
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
		for (Ball b : Ball.balles) {
			Location locB = b.getLocation();
			if (b.isPlayerBall() && locB.distance(locRobot) < 100) {
				Location loc = new Location(deplacedX(locB, Ball.speedBall), deplacedX(locB, Ball.speedBall));
				if (loc.distance(locRobot) < locB.distance(locRobot)) {
					nearBalleLoc = b.getLocation();
					break;
				}
				
			}
		}
		int lastDirection = locRobot.getDirection();
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
		// speed = speed / 2;=
		if (isTankCollision(deplacedX(locRobot, speedRobot), deplacedY(locRobot, speedRobot), locRobot.getDirection()) && isTankCollision(deplacedX(locRobot, speedRobot), deplacedY(locRobot, speedRobot), lastDirection)) {
			recul += 20;

		}
		
		double x = deplacedX(locRobot, (recul > 0 ? -1 : 1) * speedRobot);
		double y = deplacedY(locRobot, (recul > 0 ? -1 : 1) * speedRobot);
		tankAspectR++;
		
		if (!isTankCollision(x, y, locRobot.getDirection())) {
			locRobot.setPos(x, y);
		} else if (!isTankCollision(x, y, lastDirection)) {
			locRobot.setPos(x, y, lastDirection);

		} else if (!isTankCollision(x, locRobot.getY(), locRobot.getDirection())) {
			locRobot.setX(x);
		} else if (!isTankCollision(x, locRobot.getY(), lastDirection)) {
			locRobot.setPos(x, locRobot.getY(), lastDirection);

		} else if (!isTankCollision(locRobot.getX(), y, locRobot.getDirection())) {
			locRobot.setY(y);
		} else if (!isTankCollision(locRobot.getX(), y, lastDirection)) {
			locRobot.setPos(locRobot.getX(), y, lastDirection);

		} else if (!isTankCollision(locRobot.getX(), locRobot.getY(), lastDirection)) {
			locRobot.setDirection(lastDirection);

		}
		
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
				Ball.balles.add(new Ball(locRobot.clone(), false, false));
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
				
				int dist = tankCollisionNumber(deplacedX(locTo, speed), deplacedY(locTo, speed), direction);
				
				if (dist > bestGood) {
					bestDirection = direction;
					bestGood = dist;
					
				}
				
				if (i > 30 && bestGood > 60) {// dans un rayon de 30 + 30 ° sauf si mur
					
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
	
	private void movePlayer(final boolean backward, final boolean move) {
		
		// rotation du tank
		int rotationSpeed = 5;
		int directionM = getDirectionMouse();
		int directionPlayer = locPlayer.getDirection();
		int lastDirection = locPlayer.getDirection();
		
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
		smoke = false;
		if (move) {
			
			double x = deplacedX(locPlayer, backward ? -speedTank : speedTank);
			double y = deplacedY(locPlayer, backward ? -speedTank : speedTank);
			
			tankAspectP++;
			if (!isTankCollision(x, y, locPlayer.getDirection())) {
				locPlayer.setPos(x, y);
			} else {
				smoke = true;
				if (!isTankCollision(x, locPlayer.getY(), locPlayer.getDirection())) {
					locPlayer.setX(x);
				} else if (!isTankCollision(locPlayer.getX(), y, locPlayer.getDirection())) {
					locPlayer.setY(y);
				} else {
					if (isTankCollision(locPlayer.getX(), locPlayer.getY(), locPlayer.getDirection())) {
						// recul
						double x2 = deplacedX(locPlayer, -speedTank);
						double y2 = deplacedY(locPlayer, -speedTank);
						
						if (tankCollisionNumber(locPlayer.getX(), locPlayer.getY(), locPlayer.getDirection()) < tankCollisionNumber(x2, y2, locPlayer.getDirection())) {
							locPlayer.setPos(x2, y2);
						} else {
							x2 = deplacedX(locPlayer, speedTank);
							y2 = deplacedY(locPlayer, speedTank);
							
							if (tankCollisionNumber(locPlayer.getX(), locPlayer.getY(), locPlayer.getDirection()) < tankCollisionNumber(x2, y2, locPlayer.getDirection())) {
								
								locPlayer.setPos(x2, y2);
							}
						}
						smoke = true;
					}
				}
			}
		} else {
			if (isTankCollision(locPlayer.getX(), locPlayer.getY(), locPlayer.getDirection())) {
				// recul
				double x = deplacedX(locPlayer, -speedTank);
				double y = deplacedY(locPlayer, -speedTank);
				
				if (tankCollisionNumber(locPlayer.getX(), locPlayer.getY(), locPlayer.getDirection()) < tankCollisionNumber(x, y, locPlayer.getDirection())) {
					locPlayer.setPos(x, y);
				} else {
					x = deplacedX(locPlayer, speedTank);
					y = deplacedY(locPlayer, speedTank);
					
					if (tankCollisionNumber(locPlayer.getX(), locPlayer.getY(), locPlayer.getDirection()) < tankCollisionNumber(x, y, locPlayer.getDirection())) {
						
						locPlayer.setPos(x, y);
					}
				}
				smoke = true;
			}
		}
	}
	
	protected boolean isBallCollision(final int x, final int y) {
		return distanceBlocks[1920 * y + x] == 1;
	}

	private boolean isTankCollision(final double xT, final double yT, final int direction) {
		return tankCollisionNumber(xT, yT, direction) == 1;
	}
	
	private int tankCollisionNumber(final double xT, final double yT, final int direction) {
		int min = 1000000;
		int x;
		int y;

		int[] speed = new int[] { 35, 35, -40, -40, 31, -30, 29, 29 };
		int[] directionModif = new int[] { 47, -47, 42, -42, 0, 0, -90, 90 };

		for (int i = 0; i < 7; i++) {
			x = (int) (xT + speed[i] * Math.cos(Math.toRadians(direction + directionModif[i])));
			y = (int) (yT + speed[i] * Math.sin(Math.toRadians(direction + directionModif[i])));

			min = Math.min(distanceBlocks[1920 * y + x], min);

		}

		return min;

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
	
	static double deplacedX(final Location loc, final double speed) {
		return loc.getX() + speed * Math.cos(Math.toRadians(loc.getDirection()));
	}
	
	static double deplacedY(final Location loc, final double speed) {
		return loc.getY() + speed * Math.sin(Math.toRadians(loc.getDirection()));
	}

}
