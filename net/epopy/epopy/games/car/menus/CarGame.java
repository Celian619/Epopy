package net.epopy.epopy.games.car.menus;

import static org.lwjgl.opengl.GL11.GL_FRONT;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glReadBuffer;
import static org.lwjgl.opengl.GL11.glReadPixels;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.nio.ByteBuffer;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.BufferUtils;
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
import net.epopy.epopy.player.stats.CarStats;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class CarGame extends AbstractGameMenu {
	
	private final int grilleWidth = 20;
	private final int grilleHeight = 10;
	private final int bord = grilleWidth / 10;
	private final double cubeWidth = defaultWidth / (double) grilleWidth;
	private final double cubeHeight = defaultHeight / (double) grilleHeight;
	
	private final int middleWidth = grilleWidth / 2;
	private final int middleHeight = grilleHeight / 2;
	
	private boolean creating;
	Textures map = null;
	
	private List<Location> pointsInt;
	private List<Location> waitingPoints;
	
	private Location locCar;
	private double speed;
	private int direction;
	private boolean start;
	private static Timer timer;
	
	private boolean win;
	private boolean contreSens;
	private boolean addStats;
	
	private static boolean pauseScreen;
	private int timeTamp;
	
	@Override
	public void onEnable() {
		if (Main.getPlayer().hasSound())
			Audios.CAR.start(true).setVolume(0.2f);
			
		Mouse.setGrabbed(true);
		pauseScreen = false;
		addStats = false;
		win = false;
		contreSens = false;
		start = false;
		creating = true;
		timeTamp = 0;
		direction = 0;
		speed = 0.1;
		timer = new Timer();
		map = null;
		locCar = new Location(middleWidth * cubeWidth - 17.5, middleHeight * cubeHeight + cubeHeight / 2);
		
		pointsInt = new LinkedList<Location>();//
		waitingPoints = new LinkedList<Location>();
		for (int x = middleWidth - 5; x < middleWidth + 5; x++) {
			pointsInt.add(new Location(x, middleHeight));
			if (x != middleWidth)
				waitingPoints.add(new Location(x, middleHeight));
		}
	}
	
	@Override
	public void update() {
		if (timeTamp <= 0 && pause.isFinish() && !win) {
			if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
				if (pauseScreen) {
					pauseScreen = false;
					pause.startPause(3);
					start = false;
					Mouse.setGrabbed(true);
				} else {
					pauseScreen = true;
					timer.pause();
					Mouse.setGrabbed(false);
				}
				timeTamp = 20;
			}
		} else
			timeTamp--;
			
		// fin de la creation
		if (waitingPoints.size() == 0 && creating) {
			creating = false;
			pointsInt.clear();
			waitingPoints.clear();
			saveScreen();
			pause.startPause(5);
		}
		
		if (creating)
			upgradeMap();
		else if (win || pauseScreen) {
			if (win) {
				if (Mouse.isGrabbed())
					Mouse.setGrabbed(false);
				if (rejouerButton.isClicked())
					onEnable();
			} else if (reprendreButton.isClicked()) {
				pauseScreen = false;
				pause.startPause(3);
				Mouse.setGrabbed(true);
				start = false;
			}
			return;
		} else if (pause.isFinish()) {
			if (!start) {
				start = true;
				timer.resume();
			}
			movePlayer();
		}
		Timer.tick();
		
	}
	
	@Override
	public void render() {
		
		if (creating) {
			List<Location> pointsExt = new LinkedList<Location>();// exterieur
			
			for (int x = 0; x <= grilleWidth; x++) {
				for (int y = 0; y <= grilleWidth; y++) {
					Location loc = new Location(x, y);
					if (loc.getNearest(pointsInt).distance(loc) > 0) pointsExt.add(loc);
				}
			}
			
			// la ligne de départ :
			ComponentsHelper.drawLine((int) (middleWidth * cubeWidth), (int) (middleHeight *
					cubeHeight), (int) (middleWidth * cubeWidth), (int) ((middleHeight + 1) * cubeHeight), 8, new float[] { 0f, 0f, 1, 1 });
					
			paintLiaisons(pointsInt);
			paintLiaisons(pointsExt);
			
		} else {
			
			map.renderBackground();
			
			if (win) {
				CarStats carStats = Main.getPlayer().getCarStats();
				renderEchap(false, (int) timer.getTime() + " sec", timer.getTime() < carStats.getRecord() || carStats.getRecord() == 0);
				if (!addStats) {
					addStats = true;
					if (timer.getTime() < carStats.getRecord() || carStats.getRecord() < 1)
						carStats.setRecord((int) timer.getTime());
						
					if (carStats.getRecord() <= carStats.getObjectif()) {
						if (Main.getPlayer().getLevel() <= GameList.CAR.getID())
							Main.getPlayer().setLevel(GameList.CAR.getID() + 1);
					}
					carStats.addPartie();
					carStats.addTemps((long) timer.getTime());
				}
				return;
			} else if (pauseScreen) {
				renderEchap(true);
				return;
			}
			
			ComponentsHelper.renderTexture(Textures.GAME_CAR_VOITURE, locCar.getX() - 17.5, locCar.getY() - 8, 35, 16, direction);
			
			if (contreSens) {
				contreSens = false;
				ComponentsHelper.drawText("Tu es serieux pourquoi tu roules à contre-sens ?", defaultWidth / 2, defaultHeight - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 40, new float[] { 1, 0, 0, 1 });
			}
			if (!pause.isFinish()) {
				if (pause.getTimePauseTotal() == 5) {
					Textures.GAME_STARTING_BG.renderBackground();
					
					int x = 1093;
					int y = 400;
					
					ComponentsHelper.drawText("CONTROLES", x, y - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 30, new float[] { 1, 0.5f, 0, 1 });
					ComponentsHelper.drawText("Droite", x, y, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
					ComponentsHelper.drawText("Gauche", x, y + 150, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 1, 1, 1, 1 });
					
					ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - 30, y + 45, 60, 60);
					ComponentsHelper.renderTexture(Textures.GAME_TOUCHE_VIERGE, x - 30, y + 150 + 45, 60, 60);
					ComponentsHelper.drawText(Input.getKeyName(CarOptions.KEY_RIGHT), x, y + 75, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 0, 0, 0, 1 });
					ComponentsHelper.drawText(Input.getKeyName(CarOptions.KEY_LEFT), x, y + 220, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 0, 0, 0, 1 });
					
					// if(Main.getPlayer().getLevel() <= GameList.CAR.getID()) { BATTRE SON RECORD :
					ComponentsHelper.drawText("OBJECTIF", 660, 495, PositionWidth.GAUCHE, PositionHeight.HAUT, 30, new float[] { 1, 0.5f, 0, 1 });
					ComponentsHelper.drawText("Finir en moins", 710, 600, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
					ComponentsHelper.drawText("d'une minute !", 710, 630, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0.8f, 0.8f, 0.8f, 1 });
					
					ComponentsHelper.drawText(pause.getPauseString(), 660, 335, PositionWidth.GAUCHE, PositionHeight.HAUT, 100, new float[] { 1, 1, 1, 1 });
				} else
					ComponentsHelper.drawText(pause.getPauseString(), defaultWidth / 2, defaultHeight / 2 - 100, PositionWidth.MILIEU, PositionHeight.MILIEU, 60 * 2, new float[] { 0.3f, 0.3f, 0.3f, 1 });
				return;
			}
			if (!pauseScreen) {
				if (!pause.isFinish())
					ComponentsHelper.drawText(pause.getPauseString(), 1920 / 2, 10, PositionWidth.MILIEU, PositionHeight.HAUT, 40, new float[] { 1, 1, 1, 1 });
				else
					ComponentsHelper.drawText((int) timer.getTime() + "", 1920 / 2, 10, PositionWidth.MILIEU, PositionHeight.HAUT, 40, new float[] { 1, 1, 1, 1 });
			}
		}
		
	}
	
	/*
	 *
	 * methods
	 *
	 *
	 *
	 *
	 *
	 */
	
	private void movePlayer() {
		if (Input.isKeyDown(CarOptions.KEY_RIGHT) && timer.getTime() > 0.1) {
			speed -= speed / 5 - 0.5;// freine dans les virage a grande vitesse
			direction += 2 + speed;
			if (direction > 360) direction -= 360;
		} else if (Input.isKeyDown(CarOptions.KEY_LEFT) && timer.getTime() > 0.1) {
			speed -= speed / 5 - 0.5;
			direction -= 2 + speed;
			if (direction < 0) direction += 360;
		} else {
			speed += 0.2 / speed;
		}
		
		if ((isLine((int) deplacedX(15), (int) deplacedY(15)) || isLine((int) deplacedX(), (int) deplacedY())) && timer.getTime() > 0.3 && locCar.getY() >= middleHeight * cubeHeight) {// arrivee
			if (direction > 270 || direction < 90)
				win = true;
			else {
				contreSens = true;
				speed = 0;
			}
			
		} else {
			if (!isCircuit((int) deplacedX(15), (int) deplacedY(15)) || !isCircuit((int) deplacedX(), (int) deplacedY())) // crash
				speed = 0;
				
			locCar.setPos(deplacedX(), deplacedY());
		}
		
	}
	
	private void upgradeMap() {
		for (int i = waitingPoints.size() - 1; i >= 0; i--) {
			Location loc = waitingPoints.get(i);
			if (middleHeight <= Math.abs((int) loc.getY() - middleHeight) + bord || middleWidth <= Math.abs((int) loc.getX() - middleWidth) + bord) {
				waitingPoints.remove(loc);
				continue;
			}
			
			if (new Random().nextInt(waitingPoints.size()) == 0) {
				
				List<Location> nears = loc.getNears(1);
				for (Location testLoc : nears) {
					if (testLoc.getNearestDistance(pointsInt) > 0 && goodDistanceOtherInt(testLoc)) {
						pointsInt.add(testLoc);
						waitingPoints.add(testLoc);
						return;
					}
				}
				
				waitingPoints.remove(loc);
			}
		}
	}
	
	private boolean isLine(final int x, final int y) {
		BufferedImage img = map.getBuffImage();
		ColorModel cm = img.getColorModel();
		
		return cm.getRed(img.getRGB(x, y)) + cm.getGreen(img.getRGB(x, y)) == 0 && cm.getBlue(img.getRGB(x, y)) == 255;
	}
	
	private boolean isCircuit(final int x, final int y) {
		BufferedImage img = map.getBuffImage();
		ColorModel cm = img.getColorModel();
		
		return cm.getRed(img.getRGB(x, y)) + cm.getGreen(img.getRGB(x, y)) + cm.getBlue(img.getRGB(x, y)) >= 10;
		
	}
	
	private void paintLiaisons(final List<Location> points) {
		float[] color = new float[] { 0f, 0f, 0f, 1 };
		
		// draw all the lines
		for (Location loc : points) {
			List<Location> near = new LinkedList<>();
			for (Location locat : points) {
				if (distanceDiag(locat, loc) == 1) {
					ComponentsHelper.drawLine((int) (loc.getX() * cubeWidth), (int) (loc.getY() *
							cubeHeight), (int) (locat.getX() * cubeWidth), (int) (locat.getY() * cubeHeight), 8, color);
					near.add(locat);
				}
			}
			
			if (near.size() >= 2) {
				
				for (Location locat : near) {
					for (Location locat2 : near) {
						if (distanceDiag(locat, locat2) == 1) {
							glColor4f(color[0], color[1], color[2], color[3]);
							glBegin(GL_TRIANGLES);
							glVertex2f(getImageX(loc), getImageY(loc));
							glVertex2f(getImageX(locat), getImageY(locat));
							glVertex2f(getImageX(locat2), getImageY(locat2));
							glColor4f(1, 1, 1, 1);
							glEnd();
							
						}
					}
				}
			}
		}
	}
	
	private void saveScreen() {
		glReadBuffer(GL_FRONT);
		int width = Display.getWidth();
		int height = Display.getHeight();
		int bpp = 4; // Assuming a 32-bit display with a byte each for red, green, blue, and alpha.
		ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * bpp);
		glReadPixels(0, 0, width, height, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		
		BufferedImage circuit = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int i = (x + width * y) * bpp;
				int r = buffer.get(i) & 0xFF;
				int g = buffer.get(i + 1) & 0xFF;
				int b = buffer.get(i + 2) & 0xFF;
				circuit.setRGB(x, height - (y + 1), 0xFF << 24 | r << 16 | g << 8 | b);
			}
		}
		
		BufferedImage newImage = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_RGB);
		
		Graphics g = newImage.createGraphics();
		g.drawImage(circuit, 0, 0, 1920, 1080, null);
		g.dispose();
		
		map = new Textures(newImage);
	}
	
	private int getImageX(final Location loc) {
		return ComponentsHelper.getResponsiveX(loc.getX() * cubeWidth);
	}
	
	private int getImageY(final Location loc) {
		return ComponentsHelper.getResponsiveY(loc.getY() * cubeHeight);
	}
	
	private int distanceDiag(final Location loc1, final Location loc2) {
		return (int) Math.max(Math.abs(loc1.getX() - loc2.getX()), Math.abs(loc1.getY() - loc2.getY()));
	}
	
	private boolean goodDistanceOtherInt(final Location loc) {
		Location near = loc.getNearest(pointsInt);
		int i = 100000; // a reduire
		for (Location locat : pointsInt) {
			int diff = distanceDiag(locat, loc);
			
			if (locat.distance(near) > 1 && diff < i)
				i = diff;
				
		}
		return i >= 2;
	}
	
	private double deplacedX() {
		return deplacedX(speed);
	}
	
	private double deplacedX(final double size) {
		double locX = locCar.getX() + size * Math.cos(Math.toRadians(direction));
		if (locX < 10) locX = 10;
		if (locX >= defaultWidth - 10) locX = defaultWidth - 10;
		return locX;
	}
	
	private double deplacedY() {
		return deplacedY(speed);
	}
	
	private double deplacedY(final double size) {
		double locY = locCar.getY() + size * Math.sin(Math.toRadians(direction));
		if (locY < 10) locY = 10;
		else if (locY >= defaultHeight - 10) locY = defaultHeight - 10;
		return locY;
	}
}
