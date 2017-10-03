package net.epopy.epopy.games.ping.menus;

import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
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

			ComponentsHelper.drawText(timer / 60 + "", defaultWidth / 2, 10, PositionWidth.MILIEU, PositionHeight.HAUT, 40, record ? colorTimer : new float[] { 1, 1, 1, 1 });
			
		}
		ComponentsHelper.drawQuad(ecartBordPaddle, (int) yPlayer, paddleWidth, paddleHeight);
		ComponentsHelper.drawQuad(defaultWidth - paddleWidth - ecartBordPaddle, (int) yRobot, paddleWidth, paddleHeight);
		ComponentsHelper.drawCircle((int) ballPos.getX() + 1, (int) ballPos.getY() + 1, ballSize, 20, new float[] { 0.5f, 0.5f, 0.5f, 1 });
		ComponentsHelper.drawCircle((int) ballPos.getX(), (int) ballPos.getY(), ballSize - 2, 20);

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
				int ecartement = 120;

				ComponentsHelper.drawText("CONTROLES", x, y - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });

				ComponentsHelper.drawText("Haut", x, y, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Bas", x, y + 150, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });

				if (PingOptions.MOUSE == 2) {
					ComponentsHelper.drawText("ou", x, y + 60, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 0.5f, 0, 1 });
					ComponentsHelper.drawText("ou", x, y + 150 + 60, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 0.5f, 0, 1 });
				}

				if (PingOptions.MOUSE == 2 || PingOptions.MOUSE == 1) {
					ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - (PingOptions.MOUSE == 2 ? ecartement : 30), y + 45, 60, 60);
					ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - (PingOptions.MOUSE == 2 ? ecartement : 30), y + 150 + 45, 60, 60);
					ComponentsHelper.drawText(Input.getKeyName(PingOptions.KEY_UP), x + 16 - (PingOptions.MOUSE == 2 ? ecartement : 30), y + 37, 50, new float[] { 0, 0, 0, 1 });
					ComponentsHelper.drawText(Input.getKeyName(PingOptions.KEY_DOWN), x + 16 - (PingOptions.MOUSE == 2 ? ecartement : 30), y + 150 + 37, 50, new float[] { 0, 0, 0, 1 });
				}

				if (PingOptions.MOUSE == 2 || PingOptions.MOUSE == 0) {
					ComponentsHelper.renderTexture(Textures.GAME_MOUSE_UP, x + (PingOptions.MOUSE == 2 ? ecartement / 2 : -30), y + 45, 60, 60);
					ComponentsHelper.renderTexture(Textures.GAME_MOUSE_DOWN, x + (PingOptions.MOUSE == 2 ? ecartement / 2 : -30), y + 150 + 45, 60, 60);
				}

				// if(Main.getPlayer().getLevel() <= GameList.SNAKE.getID()) { BATTRE SON RECORD :
				ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
				// ComponentsHelper.drawText(Main.getPlayer().getSnakeStats().getObjectifString(), 730, 600, PositionWidth.MILIEU,
				// PositionHeight.HAUT, 25, new float[]{0.8f, 0.8f, 0.8f, 1});
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
			PingStats pingStats = Main.getPlayer().getPingStats();
			String timeString = timer / 60 + "";
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

			speedBall += 10 * defaultWidth / AbstractGameMenu.defaultWidth / speedBall;
			speedPaddle = Math.abs(speedBall * Math.sin(Math.toRadians(50))) + 1;

		} else
			ballPos.setX(deplacedX());

		// Rebond si rencontre sol || plafond

		if (deplacedY() > defaultHeight - ballSize || deplacedY() < 0 + ballSize) {
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
