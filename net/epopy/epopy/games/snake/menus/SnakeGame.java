package net.epopy.epopy.games.snake.menus;

import java.util.ArrayList;
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
import net.epopy.epopy.games.gestion.GameList;
import net.epopy.epopy.player.stats.SnakeStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class SnakeGame extends AbstractGameMenu {
	
	private int snakeSize;
	private int timeFood = 0;
	private int timeSecond = 0;
	private final int dontMoveTime = 2;
	
	private int grilleSize;
	private final int maxFood = 10;
	
	private double cubeX;
	private double cubeY;
	
	private int ptsRecord;
	private List<Location> posSnake;
	private List<mouse> posMouse;
	
	private int eat;
	private int lastKey;
	
	private boolean addStats;
	private Timer timer;
	
	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound())
			Audios.SNAKE.start(true).setVolume(0.2f);
			
		posSnake = new LinkedList<Location>();
		posMouse = new ArrayList<mouse>(10);

		addStats = false;
		snakeSize = 1;
		eat = 0;
		lastKey = 0;
		
		gameOver = false;
		grilleSize = 100;
		cubeX = defaultWidth / (double) grilleSize;
		cubeY = 0.1 + defaultHeight / (double) grilleSize;

		Mouse.setGrabbed(true);
		
		posSnake.add(new Location(grilleSize / 2, grilleSize / 2, grilleSize, grilleSize));
		ptsRecord = Main.getPlayer().getSnakeStats().getRecord();
		
		paused = true;
		pauseScreen = false;
		timeTamp = 0;
		timer = new Timer();
		timer.pause();
		pause.startPause(5);
	}
	
	private int timeTamp;
	private boolean pauseScreen;
	private boolean paused = true;

	@Override
	public void update() {
		Timer.tick();
		
		if (timeTamp <= 0 && pause.isFinish() && !gameOver) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
				} else {
					pauseScreen = true;
					timer.pause();
					paused = true;
					Mouse.setGrabbed(false);
				}
				timeTamp = 10;
			}
		} else
			timeTamp--;
			
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
			Mouse.setGrabbed(true);
		}

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
		
		for (Location loc : posSnake)
			if (loc.getX() == x && loc.getY() == y) {
				Mouse.setGrabbed(false);
				gameOver = true;
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
		
		Textures.GAME_SNAKE_LEVEL_BG.renderBackground();
		
		for (mouse m : posMouse) {
			Location loc = m.loc;
			if (m.walk < 10)
				ComponentsHelper.renderTexture(Textures.GAME_SNAKE_SOURIS, (int) (cubeX * loc.getX() - 60), (int) (cubeY * loc.getY() - 33), 120, 65, loc.getDirection());
			else
				ComponentsHelper.renderTexture(Textures.GAME_SNAKE_SOURIS2, (int) (cubeX * loc.getX() - 60), (int) (cubeY * loc.getY() - 33), 120, 65, loc.getDirection());

		}
		int lastDirection = -1;
		for (int i = posSnake.size() - 1; i >= 0; i--) {
			Location loc = posSnake.get(i);
			
			if (i == 0) {
				ComponentsHelper.renderTexture(Textures.GAME_SNAKE_TETE, (int) (cubeX * loc.getX() - 59 / 2), (int) (cubeY * loc.getY() - 131 / 2), 59, 131, loc.getDirection());
			} else {
				
				ComponentsHelper.renderTexture(Textures.GAME_SNAKE_CORP, (int) (cubeX * loc.getX() - 45 / 2), (int) (cubeY * loc.getY() - 45 / 2), 45, 45, loc.getDirection());
				
			}

			lastDirection = loc.getDirection();

		}
		c++;
		if (c > 20) {
			Random random = new Random();
			color = new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), 1 };
			c = 0;
		}
		
		if (!gameOver && !paused) {
			if (snakeSize < ptsRecord)
				ComponentsHelper.drawText("Score: " + snakeSize, 0, 0, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, new float[] { 0.5f, 0.5f, 1, 1 });
			else {
				ComponentsHelper.drawText("Score: ", 0, 0, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, new float[] { 0.5f, 0.5f, 1, 1 });
				ComponentsHelper.drawText(snakeSize + "", 170, 0, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, color);
			}
		}
		
		if (!pause.isFinish()) {
			// debut du jeu
			if (pause.getTimePauseTotal() == 5) {
				Textures.GAME_STARTING_BG.renderBackground();
				
				int x = 1093;
				int y = 400;
				int ecartement = 120;
				
				ComponentsHelper.drawText("CONTROLES", x, y - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });
				
				ComponentsHelper.drawText("Haut", x - ecartement + 5, y, PositionWidth.GAUCHE, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Bas", x - ecartement + 10, y + 150, PositionWidth.GAUCHE, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Droite", x + ecartement / 2, y, PositionWidth.GAUCHE, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				ComponentsHelper.drawText("Gauche", x + ecartement / 2 - 10, y + 150, PositionWidth.GAUCHE, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
				
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - ecartement, y + 45, 60, 60);
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - ecartement, y + 150 + 45, 60, 60);
				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_UP), x + 16 - ecartement, y + 37, 50, new float[] { 0, 0, 0, 1 });
				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_DOWN), x + 16 - ecartement, y + 150 + 37, 50, new float[] { 0, 0, 0, 1 });
				
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x + ecartement / 2, y + 45, 60, 60);
				ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x + ecartement / 2, y + 150 + 45, 60, 60);
				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_RIGHT), x + 3 + ecartement / 2, y + 40, 50, new float[] { 0, 0, 0, 1 });
				ComponentsHelper.drawText(Input.getKeyName(SnakeOptions.KEY_LEFT), x + 3 + ecartement / 2, y + 150 + 40, 50, new float[] { 0, 0, 0, 1 });
				
				ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
				ComponentsHelper.drawText("Avoir plus", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				ComponentsHelper.drawText("de 100 points !", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				
				ComponentsHelper.drawText(pause.getPauseString(), 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, new float[] { 1, 1, 1, 1 });
			} else
				ComponentsHelper.drawText(pause.getPauseString(), defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.MILIEU, 60 * 2, new float[] { 1, 1, 1, 1 });
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
				Main.getPlayer().getSnakeStats().addTemps((long) timer.getTime());
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
		
		public mouse(final Location loc) {
			this.loc = loc;
			Random r = new Random();
			directionModif = (r.nextBoolean() ? 1 : -1) * (45 + r.nextInt(135));
			speed = r.nextDouble() / 2 + 0.2;
			walk = 0;
		}
		
		private void move() {
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
