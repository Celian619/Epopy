package net.epopy.epopy.games.pong.menus;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

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
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class PongGame extends AbstractGameMenu {

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

	// timer
	private long startGame;
	private long startPause;
	private String timer = "00:00:00";
	private final SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

	// color
	private float[] colorTimer = new float[] { 1f, 1f, 1f, 1f };
	private int colorTime;

	// pour la pause
	private boolean pause;
	private int reset;
	private String timeRependre;
	private long startReprendre;
	private boolean addStats;
	private boolean wasStart;

	@Override
	public void onEnable() {

		if (Main.getPlayer().hasSound()) {
			Audios.changeVolume(0.02f);
			Audios.PONG.start(true);
		}

		timeRependre = "??";
		speedBall = 8;
		speedPaddle = 10;
		addStats = false;
		pause = false;
		gameOver = false;
		reset = -1;
		wasStart = false;
		Mouse.setCursorPosition(20, Display.getHeight() / 2);
		lastMouseY = (Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight;
		Mouse.setGrabbed(true);

		yPlayer = defaultHeight / 2 - paddleHeight / 2;
		yRobot = defaultHeight / 2 - paddleHeight / 2;

		Random r = new Random();

		direction = (int) (180 + r.nextInt(45) * r.nextGaussian());

		ballPos = new Location(defaultWidth / 2, defaultHeight / 2);
		startGame = 0;
		startPause = -1;
		timer = "00:00:00";

		Textures.GAME_BACKGROUND_80OPACITY.renderBackground();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void update() {

		/*
		 * Random color
		 */
		colorTime++;
		if (colorTime > 20) {
			Random random = new Random();
			colorTimer = new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), 1 };
			colorTime = 0;
		}

		if (Input.isKeyDown(Keyboard.KEY_ESCAPE) && !gameOver) {
			if (reset == 0) {
				if (pause) {
					colorTime = 0;
					pause = false;
					reset = -1;
				} else {
					reset = 1;
					pause = true;
					if (startPause == -1)
						startPause = System.currentTimeMillis();
					Mouse.setGrabbed(false);
					Mouse.setCursorPosition(Display.getWidth() / 2, Display.getHeight() / 2);
				}
			}
		}

		if (reset >= 1 && reset < 20)
			reset++;

		if (reset >= 20)
			reset = 0;

		if (gameOver || pause) {
			if (gameOver) {
				if (rejouerButton.isClicked())
					onEnable();

			} else if (reprendreButton.isClicked()) {
				pause = false;
				reset = -1;
				colorTime = 0;
			}
		} else {
			if (reset >= 0) {
				/*
				 * Timer
				 */

				if (startGame == 0)
					startGame = System.currentTimeMillis();

				timer = timeFormat.format(Calendar.getInstance().getTimeInMillis() - startGame - 3600000);

				if (Input.isKeyDown(Keyboard.KEY_ESCAPE)) {
					Mouse.setGrabbed(false);
				}
				/*
				 * engine
				 */
				if (PongOptions.MOUSE == 1 || PongOptions.MOUSE == 2) {
					if (Input.isKeyDown(PongOptions.KEY_UP)) {
						yPlayer -= speedPaddle;
						if (yPlayer < 0) yPlayer = 0;
					} else if (Input.isKeyDown(PongOptions.KEY_DOWN)) {
						yPlayer += speedPaddle;
						if (yPlayer > defaultHeight - paddleHeight) yPlayer = defaultHeight - paddleHeight;
					}
				}
				if (PongOptions.MOUSE == 0 || PongOptions.MOUSE == 2) {
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

			} else {
				if (startReprendre == 0)
					startReprendre = System.currentTimeMillis();

				Mouse.setCursorPosition(20, (int) (Display.getHeight() - lastMouseY / defaultHeight * Display.getHeight()));
				Mouse.setGrabbed(true);
				Date timeDiff = new Date(Calendar.getInstance().getTimeInMillis() - startReprendre - 3600000);

				if (startReprendre != 0)
					timeRependre = "0" + ((timer.equals("00:00:00") && !wasStart ? 5 : 3) - timeDiff.getSeconds());

				if (timeRependre.equals("00"))
					timeRependre = "GO";

				if (timeRependre.equals("0-1")) {
					reset = 1;
					timeRependre = "??";
					startReprendre = 0;
					wasStart = true;
					if (startPause != -1) {
						long timePause = Calendar.getInstance().getTimeInMillis() - startPause - 3600000;
						Date date = new Date(timePause);
						long newTime = startGame + TimeUnit.SECONDS.toMillis(date.getSeconds())
								+ TimeUnit.MINUTES.toMillis(-date.getMinutes()) +
								TimeUnit.HOURS.toMillis(-date.getHours());
						startGame = newTime;
						startPause = -1;
					}
				}

			}
		}
	}

	@Override
	public void render() {
		Textures.GAME_PONG_LEVEL_BG.renderBackground();
		boolean record = System.currentTimeMillis() - startGame > Main.getPlayer().getPongStats().getRecord();
		if (timeRependre.equals("??") && !gameOver && !pause)
			ComponentsHelper.drawText(timer, defaultWidth / 2, 10, PositionWidth.MILIEU, PositionHeight.HAUT, 40, record ? colorTimer : new float[] { 1, 1, 1, 1 });
		ComponentsHelper.drawQuad(ecartBordPaddle, (int) yPlayer, paddleWidth, paddleHeight);
		ComponentsHelper.drawQuad(defaultWidth - paddleWidth - ecartBordPaddle, (int) yRobot, paddleWidth, paddleHeight);
		ComponentsHelper.drawCircle((int) ballPos.getX() + 1, (int) ballPos.getY() + 1, ballSize, 20, new float[] { 0.5f, 0.5f, 0.5f, 1f });
		ComponentsHelper.drawCircle((int) ballPos.getX(), (int) ballPos.getY(), ballSize - 2, 20);

		if (!timeRependre.equals("??")) {

			// debut du jeu
			if (timer.equals("00:00:00") && !wasStart) {

				Textures.GAME_STARTING_BG.renderBackground();

				int x = 1093;
				int y = 400;
				int ecartement = 120;

				ComponentsHelper.drawText("CONTROLE", x, y - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });

				ComponentsHelper.drawText("Haut", x, y, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Bas", x, y + 150, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				
				if (PongOptions.MOUSE == 2) {
					ComponentsHelper.drawText("ou", x, y + 60, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 0.5f, 0, 1 });
					ComponentsHelper.drawText("ou", x, y + 150 + 60, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 0.5f, 0, 1 });
				}
				
				if (PongOptions.MOUSE == 2 || PongOptions.MOUSE == 1) {
					ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - (PongOptions.MOUSE == 2 ? ecartement : 30), y + 45, 60, 60);
					ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - (PongOptions.MOUSE == 2 ? ecartement : 30), y + 150 + 45, 60, 60);
					ComponentsHelper.drawText(Input.getKeyName(PongOptions.KEY_UP), x + 16 - (PongOptions.MOUSE == 2 ? ecartement : 30), y + 37, 50, new float[] { 0, 0, 0, 1 });
					ComponentsHelper.drawText(Input.getKeyName(PongOptions.KEY_DOWN), x + 16 - (PongOptions.MOUSE == 2 ? ecartement : 30), y + 150 + 37, 50, new float[] { 0, 0, 0, 1 });
				}

				if (PongOptions.MOUSE == 2 || PongOptions.MOUSE == 0) {
					ComponentsHelper.renderTexture(Textures.GAME_MOUSE_UP, x + (PongOptions.MOUSE == 2 ? ecartement / 2 : -30), y + 45, 60, 60);
					ComponentsHelper.renderTexture(Textures.GAME_MOUSE_DOWN, x + (PongOptions.MOUSE == 2 ? ecartement / 2 : -30), y + 150 + 45, 60, 60);
				}

				// if(Main.getPlayer().getLevel() <= GameList.SNAKE.getID()) { BATTRE SON RECORD :
				ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
				// ComponentsHelper.drawText(Main.getPlayer().getSnakeStats().getObjectifString(), 730, 600, PositionWidth.MILIEU,
				// PositionHeight.HAUT, 25, new float[]{0.8f, 0.8f, 0.8f, 1});
				ComponentsHelper.drawText("Tenir plus d'une", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				ComponentsHelper.drawText("1min et 15secs", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });

				ComponentsHelper.drawText(timeRependre, 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, new float[] { 1, 1, 1, 1 });
			} else
				ComponentsHelper.drawText(timeRependre, defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.MILIEU, 60 * 2, new float[] { 1, 1, 1, 1 });
		}
		if (gameOver || pause) {

			if (Mouse.isGrabbed())
				Mouse.setGrabbed(false);

			if (gameOver) {
				renderEchap(false, timer, System.currentTimeMillis() - startGame > Main.getPlayer().getPongStats().getRecord());
				if (!addStats) {
					Main.getPlayer().getPongStats().addPartie();
					Main.getPlayer().getPongStats().addTemps(startGame);
					// set best score
					if (System.currentTimeMillis() - startGame > Main.getPlayer().getPongStats().getRecord())
						Main.getPlayer().getPongStats().setRecord(System.currentTimeMillis() - startGame);

					if (System.currentTimeMillis() - startGame > Main.getPlayer().getPongStats().getObjectif()) {
						if (Main.getPlayer().getLevel() <= GameList.PONG.getID())
							Main.getPlayer().setLevel(GameList.PONG.getID() + 1);
					}
					addStats = true;
				}
			} else {
				renderEchap(true);
			}

			/**
			 * if (System.currentTimeMillis() - startGame > Main.getPlayer().getPongStats().getRecord()) ComponentsHelper.drawText(
			 * "Nouveau record !", defaultWidth / 2, defaultHeight / 2 - 300, PositionWidth.MILIEU, PositionHeight.MILIEU, 80, colorTimer);
			 */
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
