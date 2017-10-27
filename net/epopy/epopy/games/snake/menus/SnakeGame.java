package net.epopy.epopy.games.snake.menus;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.player.stats.SnakeStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class SnakeGame extends AbstractGameMenu {
	
	private final int maxFood = 10;
	private final int dontMoveTime = 2;
	
	private int timer;

	private List<Location> posSnake;
	private List<mouse> posMouse;
	
	private double cubeX;
	private double cubeY;
	
	private int grilleSize;
	private int snakeSize;
	private int ptsRecord;
	private int timeSecond;
	private int timeFood;
	private int eat;
	private int lastKey;

	private boolean pauseScreen;
	private boolean addStats;
	
	@Override
	public void onEnable() {
		
		// Lancement de la musique si le joueur l'a activé
		if (Main.getPlayer().hasSound() && !Audios.SNAKE.isRunning())
			Audios.SNAKE.start(true).setVolume(0.3f);
		song = Audios.SNAKE;
		pause.startPause(5);

		/*
		 * Initialisation de toute les variable a chaque lancement du jeu
		 */
		timer = 0;

		posSnake = new LinkedList<Location>();
		
		posMouse = new ArrayList<mouse>(10);
		
		grilleSize = 100;

		cubeX = defaultWidth / (double) grilleSize;
		cubeY = 0.1 + defaultHeight / (double) grilleSize;

		snakeSize = 1;
		ptsRecord = Main.getPlayer().getSnakeStats().getRecord();
		timeSecond = timeFood = eat = lastKey = 0;

		pauseScreen = addStats = gameOver = false;
		
		Mouse.setGrabbed(true);
		
		for (int i = 0; i < 30; i++) {
			posSnake.add(new Location(grilleSize / 2, grilleSize / 2 + i, grilleSize, grilleSize));// pos tete du snake
		}
		
	}

	@Override
	public void update() {
		// update le Timer
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
		}
		
		if (pauseScreen) {
			if (reprendreButton.isClicked()) {
				pause.startPause(3);
				pauseScreen = false;
				Mouse.setGrabbed(true);
			}
		}
		
		if (pauseScreen || !pause.isFinish() || gameOver) return;
		
		if (timeSecond != 0) {
			timeSecond--;
			return;
		}
		// head of the snake
		double x = posSnake.get(0).getX();
		double y = posSnake.get(0).getY();
		int direction = 0;
		// move
		if (Input.isKeyDown(SnakeOptions.KEY_UP)) {
			if (lastKey == 1) {
				y++;
				direction = 180;
			} else {
				y--;
				lastKey = 0;
			}
		} else if (Input.isKeyDown(SnakeOptions.KEY_DOWN)) {
			if (lastKey == 0) {
				y--;
			} else {
				direction = 180;
				y++;
				lastKey = 1;
			}
		} else if (Input.isKeyDown(SnakeOptions.KEY_RIGHT)) {
			if (lastKey == 3) {
				x--;
				direction = 270;
			} else {
				direction = 90;
				x++;
				lastKey = 2;
			}
		} else if (Input.isKeyDown(SnakeOptions.KEY_LEFT)) {
			if (lastKey == 2) {
				x++;
				direction = 90;
			} else {
				direction = 270;
				x--;
				lastKey = 3;
			}
		} else {
			switch (lastKey) {
				case 0:
				y--;
					break;
				case 1:
				y++;
				direction = 180;
					break;
				case 2:
				direction = 90;
				x++;
					break;
				case 3:
				direction = 270;
				x--;
					break;
			}
		}
		int n = 1;
		
		for (Location loc : posSnake) {
			
			int distance = 2;
			if (n < 10) {
				distance = 0;
				n++;
			}
			
			if (Math.abs(loc.getX() - x) < distance && Math.abs(loc.getY() - y) < distance) {
				Mouse.setGrabbed(false);
				gameOver = true;
			}
		}
		for (mouse mouse : posMouse) {
			Location loc = mouse.loc;
			if (Math.abs(loc.getX() - x) < 4 && Math.abs(loc.getY() - y) < 5) {
				eat += 5;
				snakeSize++;
				posMouse.remove(mouse);
				break;
			}
		}
		if (eat == 0) {
			Location loc = posSnake.get(posSnake.size() - 1);
			posSnake.remove(posSnake.size() - 1);
			loc.setPos(x, y, direction);
			posSnake.add(0, loc);
			
		} else {
			posSnake.add(0, new Location(x, y, direction, grilleSize, grilleSize));
			eat--;
		}
		
		if (posSnake.size() + posMouse.size() < Math.pow(grilleSize, 2) && timeFood == 0 && posMouse.size() < maxFood) {
			Boolean foodOk = false;
			int xf;
			int yf;
			Location locFood;
			int i = grilleSize * grilleSize;
			Random r = new Random();
			while (!foodOk) {
				xf = r.nextInt(grilleSize);
				yf = r.nextInt(grilleSize);
				locFood = new Location(xf, yf, (r.nextBoolean() ? 1 : -1) * r.nextInt(180), grilleSize, grilleSize);
				foodOk = true;
				for (Location locSnake : posSnake)
					if (locSnake.distance(locFood) < 3) {
						foodOk = false;
						break;
					}
					
				if (foodOk)
					posMouse.add(new mouse(locFood));
					
				i--;// si la grille est trop complete
				if (i == 0) break;
			}
			timeFood = 10;
		}
		if (timeFood > 0)
			timeFood--;
		timeSecond = dontMoveTime;
		
		for (mouse mouse : posMouse)
			mouse.move();
	}

	private int c = 0;
	private float[] color = new float[] { 1, 0f, 0f, 1 };
	private int rotationFood = 0;

	@Override
	public void render() {
		rotationFood++;
		if (rotationFood > 360) rotationFood -= 360;

		Textures.SNAKE_LEVEL_BG.renderBackground();

		for (mouse m : posMouse) {
			Location loc = m.loc;
			double width = 24 * m.size;
			double height = 7 * m.size;

			if (m.walk < 5)
				ComponentsHelper.renderTexture(Textures.SNAKE_FOOD, (int) (cubeX * loc.getX() - width / 2), (int) (cubeY * loc.getY() - height / 2), width, height, loc.getDirection(), true);
			else if (m.walk < 10 || m.walk > 15)
				ComponentsHelper.renderTexture(Textures.SNAKE_FOOD2, (int) (cubeX * loc.getX() - width / 2), (int) (cubeY * loc.getY() - height / 2), width, height, loc.getDirection(), true);
			else
				ComponentsHelper.renderTexture(Textures.SNAKE_FOOD3, (int) (cubeX * loc.getX() - width / 2), (int) (cubeY * loc.getY() - height / 2), width, height, loc.getDirection(), true);

		}

		for (int i = posSnake.size() - 1; i >= 0; i--) {
			Location loc = posSnake.get(i);

			if (i == 0) {
				ComponentsHelper.renderTexture(Textures.SNAKE_TETE, (int) (cubeX * loc.getX() - 50 / 2), (int) (cubeY * loc.getY() - 100 / 2), 50, 100, loc.getDirection());
			} else {
				int sizeX = 45;
				int sizeY = 45;

				int directionSoon = posSnake.get(i - 1).getDirection();

				if (i > posSnake.size() - 30) {
					sizeX -= 30 - (posSnake.size() - i);
					if (directionSoon != loc.getDirection()) {
						sizeY = sizeX;
					}
				}

				ComponentsHelper.renderTexture(Textures.SNAKE_CORP, (int) (cubeX * loc.getX() - sizeX / 2), (int) (cubeY * loc.getY() - sizeY / 2), sizeX, sizeY, loc.getDirection());
			}

		}
		c++;
		if (c > 20) {
			Random random = new Random();
			color = new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), 1 };
			c = 0;
		}

		if (!gameOver && pause.isFinish() && !pauseScreen) {
			if (snakeSize < ptsRecord)
				ComponentsHelper.drawText("Score: " + snakeSize, 0, 0, PosWidth.GAUCHE, PosHeight.HAUT, 50, new float[] { 0.5f, 0.5f, 1, 1 });
			else {
				ComponentsHelper.drawText("Score: ", 0, 0, PosWidth.GAUCHE, PosHeight.HAUT, 50, new float[] { 0.5f, 0.5f, 1, 1 });
				ComponentsHelper.drawText(snakeSize + "", 170, 0, PosWidth.GAUCHE, PosHeight.HAUT, 50, color);
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

				float[] orange = new float[] { 1, 0.5f, 0, 1 };
				float[] white = new float[] { 1, 1, 1, 1 };
				float[] grey = new float[] { 0.8f, 0.8f, 0.8f, 1 };

				ComponentsHelper.drawText("CONTROLES", 1093, 370, PosWidth.MILIEU, PosHeight.MILIEU, 30, orange);

				ComponentsHelper.drawText("Haut", 978, 410, PosWidth.GAUCHE, PosHeight.HAUT, 25, white);
				ComponentsHelper.drawText("Bas", 983, 545, PosWidth.GAUCHE, PosHeight.HAUT, 25, white);
				ComponentsHelper.drawText("Droite", 1153, 410, PosWidth.GAUCHE, PosHeight.HAUT, 25, white);
				ComponentsHelper.drawText("Gauche", 1143, 545, PosWidth.GAUCHE, PosHeight.HAUT, 25, white);

				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_UP), 989, 437, 50, white);
				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_DOWN), 989, 567, 50, white);

				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_RIGHT), 1156, 440, 50, white);
				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_LEFT), 1156, 570, 50, white);

				ComponentsHelper.drawText("OBJECTIF", 660, 495, PosWidth.GAUCHE, PosHeight.HAUT, 30, orange);
				ComponentsHelper.drawText("Avoir plus", 710, 600, PosWidth.MILIEU, PosHeight.HAUT, 25, grey);
				ComponentsHelper.drawText("de 100 points !", 710, 630, PosWidth.MILIEU, PosHeight.HAUT, 25, grey);

				ComponentsHelper.drawText(pause.getPauseString(), 660, 335, PosWidth.GAUCHE, PosHeight.HAUT, 100, white);
			} else
				pause.showRestartChrono();
		}
		if (pauseScreen)
			renderEchap(true);

		if (gameOver) {
			char[] recordC = String.valueOf(snakeSize).toCharArray();
			int n = recordC.length;
			String newRecord = "";

			for (char chars : recordC) {

				if (n / 3 == n / (double) 3 && n != recordC.length)
					newRecord += ",";

				newRecord += chars;
				n--;
			}
			SnakeStats snakeStats = Main.getPlayer().getSnakeStats();
			if (snakeSize > ptsRecord)
				snakeStats.setRecord(snakeSize);

			if (snakeSize >= snakeStats.getObjectif()) {
				if (Main.getPlayer().getLevel() <= GameList.SNAKE.getID())
					Main.getPlayer().setLevel(GameList.SNAKE.getID() + 1);
			}

			renderEchap(false, newRecord + " pts", snakeSize > ptsRecord);
			if (!addStats) {
				Main.getPlayer().getSnakeStats().addTemps(timer / 60);
				Main.getPlayer().getSnakeStats().addPartie();
				addStats = true;
			}
		}
	}
	
	class mouse {

		Location loc;
		int directionModif;
		Double speed;
		int walk;
		double size;

		public mouse(final Location loc) {
			this.loc = loc;
			Random r = new Random();
			directionModif = (r.nextBoolean() ? 1 : -1) * (45 + r.nextInt(135));
			speed = r.nextDouble() / 2 + 0.2;
			walk = 0;
			size = 0;
		}

		private void move() {
			if (size < 6)
				size += 0.05;
			walk++;
			if (walk >= 20) {
				walk = 0;

			}
			Random r = new Random();
			if (directionModif <= 5 && directionModif >= -5) {
				loc.setDirection(loc.getDirection() + directionModif);
				
				directionModif = (r.nextBoolean() ? 1 : -1) * (45 + r.nextInt(135));

			} else {
				
				int i = r.nextInt(6);
				if (directionModif < 0) i *= -1;
				
				loc.setDirection(loc.getDirection() + i);
				directionModif -= i;
			}
			double xAdd = speed * Math.cos(Math.toRadians(loc.getDirection()));
			double yAdd = speed * Math.sin(Math.toRadians(loc.getDirection()));
			
			Location locModif = loc.clone().add(xAdd, yAdd);

			double nearestDistS = loc.getNearestDistance(posSnake);
			if (nearestDistS > 5 || locModif.getNearestDistance(posSnake) > nearestDistS) {

				loc.add(xAdd, yAdd);
			}
			
			speed += (r.nextBoolean() ? 1 : -1) * r.nextDouble() / 10;
			
			if (speed > 0.7) speed = 0.7;
			if (speed < 0.2) speed = 0.2;
			
			if (loc.getDirection() > 180) loc.setDirection(loc.getDirection() - 360);
			
			if (loc.getDirection() < -180) loc.setDirection(loc.getDirection() + 360);
		}
		
	}
}
