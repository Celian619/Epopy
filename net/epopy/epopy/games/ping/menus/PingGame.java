package net.epopy.epopy.games.ping.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawCircle;
import static net.epopy.epopy.display.components.ComponentsHelper.drawQuad;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;
import static net.epopy.epopy.display.components.ComponentsHelper.renderTexture;

import java.util.Random;

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
import net.epopy.epopy.player.stats.PingStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class PingGame extends AbstractGameMenu {

	private static final int paddleWidth = 43;
	private static final int paddleHeight = 163;
	private static final int ballSize = 30;
	private static final int ecartBordPaddle = 15;

	// modifier pour change la difficulte
	private double speedPaddle = 10;
	private double speedBall = 8;

	private double yPlayer;
	private double yRobot;
	private double lastMouseY;

	private Location ballPos;
	private int direction;
	
	private int timer;

	// color
	private float[] colorTimer = new float[] { 1, 1, 1, 1 };
	private int colorTime;

	private boolean addStats;

	private boolean pauseScreen;

	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound() && !Audios.PING.isRunning())
			Audios.PING.start(true).setVolume(0.2f);
			
		speedBall = 8;
		speedPaddle = 10;
		addStats = pauseScreen = gameOver = false;
		
		Mouse.setCursorPosition(20, Display.getHeight() / 2);
		lastMouseY = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight;
		Mouse.setGrabbed(true);
		
		yPlayer = yRobot = defaultHeight / 2 - paddleHeight / 2;
		
		Random r = new Random();
		direction = (int) (180 + r.nextInt(45) * r.nextGaussian());
		ballPos = new Location(defaultWidth / 2, defaultHeight / 2);
		Textures.GAME_BACKGROUND_80OPACITY.renderBackground();

		timer = 0;
		pause.startPause(5);
	}

	@Override
	public void update() {
		if (!pauseScreen && !gameOver && pause.isFinish()) {
			timer++;
		}

		/*
		 * Random color
		 */
		colorTime++;
		if (colorTime > 20) {
			Random random = new Random();
			colorTimer = new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), 1 };
			colorTime = 0;
		}

		if (pause.isFinish() && !gameOver) {
			if (Input.getKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
					Mouse.setGrabbed(true);
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
				Mouse.setGrabbed(true);
			}
		}

		if (pauseScreen || !pause.isFinish() || gameOver) return;

		if (Input.isKeyDown(Keyboard.KEY_ESCAPE)) {
			Mouse.setGrabbed(false);
		}
		/*
		 * engine
		 */
		if (PingOptions.MOUSE == 1 || PingOptions.MOUSE == 2) {
			if (Input.isKeyDown(PingOptions.KEY_UP)) {
				yPlayer -= speedPaddle;
				if (yPlayer < 0) yPlayer = 0;
			} else if (Input.isKeyDown(PingOptions.KEY_DOWN)) {
				yPlayer += speedPaddle;
				if (yPlayer > defaultHeight - paddleHeight) yPlayer = defaultHeight - paddleHeight;
			}
		}
		if (PingOptions.MOUSE == 0 || PingOptions.MOUSE == 2) {
			double adaptedMouseY = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight;
			if (lastMouseY != adaptedMouseY) {
				lastMouseY = adaptedMouseY;
				if (adaptedMouseY < paddleHeight / 2)
					yPlayer = 0;
				else if (adaptedMouseY > defaultHeight - paddleHeight / 2)
					yPlayer = defaultHeight - paddleHeight;
				else
					yPlayer = lastMouseY - paddleHeight / 2;

			}
		}

		// rebond mur / plafond ou avance
		rebond();

		// IA du robot
		yRobot = ballPos.getY() - paddleHeight / 2;
		if (yRobot < 0) yRobot = 0;
		if (yRobot > defaultHeight - paddleHeight) yRobot = defaultHeight - paddleHeight;
		
	}

	@Override
	public void render() {
		Textures.GAME_PING_LEVEL_BG.renderBackground();
		if (!gameOver && !pauseScreen && pause.isFinish()) {

			boolean record = timer / (double) 60 >= Main.getPlayer().getPingStats().getRecord();

			drawText(timer / 60 + "", defaultWidth / 2, 10, PosWidth.MILIEU, PosHeight.HAUT, 40, record ? colorTimer : new float[] { 1, 1, 1, 1 });
			
		}
		drawQuad(ecartBordPaddle, (int) yPlayer, paddleWidth, paddleHeight);
		drawQuad(defaultWidth - paddleWidth - ecartBordPaddle, (int) yRobot, paddleWidth, paddleHeight);
		drawCircle((int) ballPos.getX() + 1, (int) ballPos.getY() + 1, ballSize, 20, new float[] { 0.5f, 0.5f, 0.5f, 1 });
		drawCircle((int) ballPos.getX(), (int) ballPos.getY(), ballSize - 2, 20);

		if (!pause.isFinish()) {
			if (Input.getKeyDown(Keyboard.KEY_RETURN)) {
				pause.stopPause();
				return;
			}
			// debut du jeu
			if (pause.getTimePauseTotal() == 5) {

				Textures.GAME_STARTING_BG.renderBackground();
				
				float[] orange = new float[] { 1, 0.5f, 0, 1 };
				drawText("CONTROLES", 1093, 350, PosWidth.MILIEU, PosHeight.MILIEU, 30, orange);
				float[] white = new float[] { 1, 1, 1, 1 };
				drawText("Haut", 1093, 400, PosWidth.MILIEU, PosHeight.HAUT, 25, white);
				drawText("Bas", 1093, 550, PosWidth.MILIEU, PosHeight.HAUT, 25, white);

				if (PingOptions.MOUSE == 2) {
					drawText("ou", 1093, 460, PosWidth.MILIEU, PosHeight.HAUT, 25, orange);
					drawText("ou", 1093, 610, PosWidth.MILIEU, PosHeight.HAUT, 25, orange);
				}

				if (PingOptions.MOUSE == 2 || PingOptions.MOUSE == 1) {
					drawText(Input.getKeyName(PingOptions.KEY_UP), 1109 - (PingOptions.MOUSE == 2 ? 120 : 30), 157, 50, white);
					drawText(Input.getKeyName(PingOptions.KEY_DOWN), 1109 - (PingOptions.MOUSE == 2 ? 120 : 30), 307, 50, white);
				}

				if (PingOptions.MOUSE == 2 || PingOptions.MOUSE == 0) {
					renderTexture(Textures.GAME_MOUSE_UP, 1093 + (PingOptions.MOUSE == 2 ? 60 : -30), 165, 60, 60);
					renderTexture(Textures.GAME_MOUSE_DOWN, 1093 + (PingOptions.MOUSE == 2 ? 60 : -30), 315, 60, 60);
				}
				
				drawText("OBJECTIF", 660, 495, PosWidth.GAUCHE, PosHeight.HAUT, 30, orange);
				float[] gray = new float[] { 0.8f, 0.8f, 0.8f, 1 };
				drawText("Tenir plus d'", 710, 600, PosWidth.MILIEU, PosHeight.HAUT, 25, gray);
				drawText("1 minute 20", 710, 630, PosWidth.MILIEU, PosHeight.HAUT, 25, gray);

				drawText(pause.getPauseString(), 660, 335, PosWidth.GAUCHE, PosHeight.HAUT, 100, white);
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
			PingStats pingStats = Main.getPlayer().getPingStats();
			String timeString = timer / 60 + "s";
			boolean record = timer / 60 > pingStats.getRecord();
			renderEchap(false, timeString, record);
			if (!addStats) {
				Main.getPlayer().getPingStats().addPartie();
				Main.getPlayer().getPingStats().addTemps(timer / 60);
				// set best score
				if (record)
					Main.getPlayer().getPingStats().setRecord(timer / 60);

				if (timer / 60 > pingStats.getObjectif()) {
					if (Main.getPlayer().getLevel() <= GameList.PING.getID())
						Main.getPlayer().setLevel(GameList.PING.getID() + 1);
				}
				addStats = true;
			}
		}
		
	}

	private void rebond() {

		// rebond murs
		int epaisseur = paddleWidth + ballSize - 5 + ecartBordPaddle;
		if (deplacedX() > defaultWidth - ballSize || deplacedX() < ballSize) {
			direction = 540 - direction;

			if (direction > 360)
				direction -= 360;
			gameOver = true;
		} else if ((direction < 90 || direction > 270) && deplacedX() > AbstractGameMenu.defaultWidth - epaisseur && Math.abs(ballPos.getY() - (yRobot + paddleHeight / 2)) <= (double) paddleHeight / 2) {

			direction = 540 - direction;

			direction += (ballPos.getY() - yRobot - paddleHeight / 2) / 2;

			if (direction > 360)
				direction -= 360;

			speedBall += 10 * defaultWidth / AbstractGameMenu.defaultWidth / speedBall;
			speedPaddle = Math.abs(speedBall * Math.sin(Math.toRadians(50))) + 1;

		} else if (deplacedX() < epaisseur && Math.abs(ballPos.getY() - (yPlayer + paddleHeight / 2)) <= (double) paddleHeight / 2 && direction > 90 && direction < 270) {

			direction = 540 - direction;

			direction += (ballPos.getY() - yPlayer - paddleHeight / 2) / 2;

			direction += new Random().nextGaussian();// - 1 ou + 1

			if (direction > 90 && direction < 100) {
				direction = 88;
			} else if (direction < 270) {
				direction = 272;
			}

			if (direction > 360)
				direction -= 360;

			speedBall += 10 / speedBall;
			speedPaddle = Math.abs(speedBall * Math.sin(Math.toRadians(50))) + 1;

		} else
			ballPos.setX(deplacedX());

		// Rebond si rencontre sol || plafond

		if (deplacedY() > defaultHeight - ballSize || deplacedY() < ballSize) {
			direction = 360 - direction;

			int directionBis = direction;
			while (directionBis > 90)
				directionBis -= 90;

			// alonge les rebond pour que la balle ne face pas trop de rebonds
			if (directionBis < 45)
				direction += 10;
			else
				direction -= 10;

			// verifie si le +/- 10 a pas fait depasser les bornes
			if (direction > 360) direction -= 360;
			else if (direction < 0) direction += 360;

		} else
			ballPos.setY(deplacedY());

	}

	private double deplacedX() {
		return ballPos.getX() + speedBall * Math.cos(Math.toRadians(direction));
	}

	private double deplacedY() {
		// - car + fait decendre
		return ballPos.getY() + speedBall * Math.sin(Math.toRadians(direction));
	}
	
}
