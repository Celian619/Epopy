package net.epopy.epopy.games.snake.menus;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

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
	private final List<Location> posSnake = new LinkedList<Location>();
	private final List<Location> posFood = new LinkedList<Location>();
	
	private int eat;
	private int lastKey;
	
	private int reset;
	private boolean pause;
	private boolean wasStart;
	
	// stats
	private long startGame;
	private boolean addStats;
	
	@Override
	public void onEnable() {

		if (Main.getPlayer().hasSound()) {
			Audios.changeVolume(0.02f);
			Audios.SNAKE.start(true);
		}

		addStats = false;
		startGame = 0;
		reset = -1;
		pause = false;
		snakeSize = 1;
		eat = 0;
		lastKey = 0;
		wasStart = false;
		gameOver = false;
		grilleSize = 100;
		cubeX = defaultWidth / (double) grilleSize;
		cubeY = 0.1 + defaultHeight / (double) grilleSize;
		
		posSnake.clear();
		posFood.clear();
		
		Mouse.setGrabbed(true);

		posSnake.add(new Location(grilleSize / 2, grilleSize / 2, grilleSize, grilleSize));
		ptsRecord = Main.getPlayer().getSnakeStats().getRecord();
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void update() {
		if (Input.isKeyDown(Keyboard.KEY_ESCAPE) && !gameOver) {
			if (reset == 0) {
				if (pause) {
					pause = false;
					reset = -1;
					Mouse.setGrabbed(true);
				} else {
					reset = 1;
					pause = true;
					Mouse.setGrabbed(false);
				}
			}
		}
		if (reset >= 1 && reset < 20)
			reset++;
			
		if (reset >= 20)
			reset = 0;
			
		if (!gameOver && !pause) {
			if (reset >= 0) {
				if (startGame == 0)
					startGame = System.currentTimeMillis();
					
				if (timeSecond != 0) {
					timeSecond--;
					return;
				}
				
				// head of the snake
				
				double x = posSnake.get(0).getX();
				double y = posSnake.get(0).getY();
				// move
				if (Input.isKeyDown(SnakeOptions.KEY_UP)) {
					if (lastKey == 1) {
						y++;
					} else {
						y--;
						lastKey = 0;
					}
				} else if (Input.isKeyDown(SnakeOptions.KEY_DOWN)) {
					if (lastKey == 0) {
						y--;
					} else {
						y++;
						lastKey = 1;
					}
				} else if (Input.isKeyDown(SnakeOptions.KEY_RIGHT)) {
					if (lastKey == 3) {
						x--;
					} else {
						x++;
						lastKey = 2;
					}
				} else if (Input.isKeyDown(SnakeOptions.KEY_LEFT)) {
					if (lastKey == 2) {
						x++;
					} else {
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
							break;
						case 2:
						x++;
							break;
						case 3:
						x--;
							break;
					}
				}
				
				for (Location loc : posSnake)
					if (loc.getX() == x && loc.getY() == y) {
						Mouse.setGrabbed(false);
						gameOver = true;
					}
				for (Location loc : posFood)
					if (Math.abs(loc.getX() - x) < 4 && Math.abs(loc.getY() - y) < 5) {
						eat += 5;
						snakeSize++;
						posFood.remove(loc);
						break;
					}
				if (eat == 0) {
					Location loc = posSnake.get(posSnake.size() - 1);
					posSnake.remove(posSnake.size() - 1);
					loc.setPos(x, y);
					posSnake.add(0, loc);
					
				} else {
					posSnake.add(0, new Location(x, y, grilleSize, grilleSize));
					eat--;
				}
				
				if (posSnake.size() + posFood.size() < Math.pow(grilleSize, 2) && timeFood == 0 && posFood.size() < maxFood) {
					Boolean foodOk = false;
					int xf;
					int yf;
					Location locFood;
					int i = grilleSize * grilleSize;
					Random r = new Random();
					while (!foodOk) {
						xf = r.nextInt(grilleSize);
						yf = r.nextInt(grilleSize);
						locFood = new Location(xf, yf);
						foodOk = true;
						for (Location locSnake : posSnake)
							if (locSnake.equals(locFood)) {
								foodOk = false;
								break;
							}
						for (Location locOldFood : posFood)
							if (locOldFood.equals(locFood)) {
								foodOk = false;
								break;
							}
						if (foodOk)
							posFood.add(locFood);
							
						i--;// si la grille est trop complete
						if (i == 0) break;
					}
					timeFood = 10;
				}
				if (timeFood > 0)
					timeFood--;
				timeSecond = dontMoveTime;
			} else {
				
				if (startReprendre == 0)
					startReprendre = System.currentTimeMillis();
				Date timeDiff = new Date(Calendar.getInstance().getTimeInMillis() - startReprendre - 3600000);
				
				if (startReprendre != 0)
					timeRependre = "0" + (3 - timeDiff.getSeconds());
					
				if (timeRependre.equals("00"))
					timeRependre = "GO";
					
				if (timeRependre.equals("0-1")) {
					reset = 1;
					wasStart = true;
					timeRependre = "??";
					startReprendre = 0;
				}
			}
		}
		if (pause || gameOver) {
			if (gameOver) {
				if (rejouerButton.isClicked())
					onEnable();
					
			} else {
				if (reprendreButton.isClicked() && pause) {
					pause = false;
					reset = -1;
					Mouse.setGrabbed(true);
				}
			}
		}
		if (snakeSize > ptsRecord) {
			Main.getPlayer().getSnakeStats().setRecord(snakeSize);
		}
		if (snakeSize >= Main.getPlayer().getSnakeStats().getObjectif()) {
			if (Main.getPlayer().getLevel() <= GameList.SNAKE.getID())
				Main.getPlayer().setLevel(GameList.SNAKE.getID() + 1);
		}
	}
	
	private String timeRependre = "??";
	private long startReprendre;
	private int c = 0;
	private float[] color = new float[] { 1f, 0f, 0f, 1f };
	private int rotationFood = 0;
	
	@Override
	public void render() {
		rotationFood++;
		if (rotationFood > 360) rotationFood -= 360;

		Textures.GAME_SNAKE_LEVEL_BG.renderBackground();
		
		for (Location loc : posFood) {
			int rotationModif = rotationFood + (int) loc.getX();
			if (rotationModif > 360) rotationModif -= 360;
			// rotation differente en sens et en emplacement
			ComponentsHelper.drawCircle((int) (cubeX * loc.getX() + cubeX / 2), (int) (cubeY * loc.getY() + cubeY / 2), (int) (cubeX * 1), 5,
					new float[] { 1, 1, 1, 1f }, loc.getX() > loc.getY() ? 360 - rotationModif : rotationModif);
		}
		
		double pos = 0;
		for (Location loc : posSnake) {
			pos++;
			ComponentsHelper.drawCircle((int) (cubeX * loc.getX() + cubeX / 2), (int) (cubeY * loc.getY() + cubeX / 2), (int) (cubeX * 2), 10, new float[] { 0f, (float) (pos / posSnake.size()), 0f, 1f });
		}
		c++;
		if (c > 20) {
			Random random = new Random();
			color = new float[] { random.nextFloat(), random.nextFloat(), random.nextFloat(), 1 };
			c = 0;
		}

		if (!gameOver && !pause) {
			if (snakeSize < ptsRecord)
				ComponentsHelper.drawText("Score: " + snakeSize, 0, 0, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, new float[] { 0.5f, 0.5f, 1f, 1f });
			else {
				ComponentsHelper.drawText("Score: ", 0, 0, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, new float[] { 0.5f, 0.5f, 1f, 1f });
				ComponentsHelper.drawText(snakeSize + "", 170, 0, PositionWidth.GAUCHE, PositionHeight.HAUT, 50, color);
			}
		}

		if (!timeRependre.equals("??")) {
			if (!timeRependre.equals("GO") && !wasStart) {
				Textures.GAME_STARTING_BG.renderBackground();

				int x = 1093;
				int y = 400;
				int ecartement = 120;

				ComponentsHelper.drawText("CONTROLE", x, y - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });

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

				// if(Main.getPlayer().getLevel() <= GameList.SNAKE.getID()) { BATTRE SON RECORD :
				ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
				// ComponentsHelper.drawText(Main.getPlayer().getSnakeStats().getObjectifString(), 730, 600, PositionWidth.MILIEU,
				// PositionHeight.HAUT, 25, new float[]{0.8f, 0.8f, 0.8f, 1});
				ComponentsHelper.drawText("Avoir plus", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
				ComponentsHelper.drawText("de 100 points !", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });

				ComponentsHelper.drawText(timeRependre, 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, new float[] { 1, 1, 1, 1 });
			} else
				ComponentsHelper.drawText(timeRependre, defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.MILIEU, 60 * 2, new float[] { 1, 1, 1, 1 });
		}

		if (gameOver || pause) {

			if (!pause && gameOver) {
				char[] recordC = String.valueOf(snakeSize).toCharArray();
				int n = recordC.length;
				String newRecord = "";

				for (char chars : recordC) {

					if (n / 3 == n / (double) 3 && n != recordC.length)
						newRecord += ",";

					newRecord += chars;
					n--;
				}

				renderEchap(false, newRecord + " pts", snakeSize > ptsRecord);
				if (!addStats) {
					Main.getPlayer().getSnakeStats().addTemps(startGame);
					Main.getPlayer().getSnakeStats().addPartie();
					addStats = true;
				}
			}

			if (pause)
				renderEchap(true);

		}
	}
}
