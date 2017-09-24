package net.epopy.epopy.games.tetras.menus;

import static org.lwjgl.opengl.GL11.glColor4f;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;
import net.epopy.epopy.utils.Location;

public class TetrasGame extends AbstractGameMenu {

	private static final int grilleSectionX = 40;
	private static final int grilleSectionY = 30;
	private static final double grilleWidth = defaultWidth / grilleSectionX;
	private static final double grilleHeight = defaultHeight / grilleSectionY;
	private double fallSpeed = 0.075;
	
	private boolean[] isBlock;
	private boolean right;
	private boolean left;
	private boolean down;
	private boolean rotateL;
	private boolean rotateR;
	
	private boolean changeStopDown;
	
	private int score;

	private block movingBlock;
	
	float[] color;
	float[] lastColor;
	
	@Override
	public void onEnable() {
		score = 0;
		changeStopDown = false;
		Random r = new Random();
		color = new float[] { (float) (0.5 + r.nextFloat() / 2), (float) (0.5 + r.nextFloat() / 2), (float) (0.5 + r.nextFloat() / 2), 1 };
		lastColor = new float[] { (float) (0.5 + r.nextFloat() / 2), (float) (0.5 + r.nextFloat() / 2), (float) (0.5 + r.nextFloat() / 2), 1 };
		
		isBlock = new boolean[grilleSectionX * grilleSectionY];
		movingBlock = new block(new Location(grilleSectionX / 2, 2));

	}

	@Override
	public void update() {
		
		right = false;
		left = false;
		down = false;
		rotateL = false;
		rotateR = false;

		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			right = true;

		}

		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			left = true;
			
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
			if (!changeStopDown)
				down = true;
		} else if (changeStopDown) {
			changeStopDown = false;
		}

		if (Input.getButtonDown(0)) {
			rotateL = true;
		}

		if (Input.getButtonDown(1)) {
			
			rotateR = true;
			
		}

		movingBlock.move();

	}

	@Override
	public void render() {
		
		Textures.TETRAS_BG.renderBackground();
		Location locat = movingBlock.loc;
		
		for (Location loc : movingBlock.locs) {
			double x = loc.getX() + locat.getX();
			double y = loc.getY() + locat.getY();
			
			x *= grilleWidth;
			y *= grilleHeight;

			glColor4f(color[0], color[1], color[2], color[3]);
			ComponentsHelper.renderTexture(Textures.TETRAS_BLOCK, x - 1, y - 1, (int) grilleWidth + 2, (int) grilleHeight + 2);

			glColor4f(1, 1, 1, 1);
			/*
			 * ComponentsHelper.drawQuad((int) x - 1, (int) y - 1, (int) grilleWidth + 2, (int) grilleHeight + 2, lastColor);
			 * ComponentsHelper.drawQuad((int) x + 1, (int) y + 1, (int) grilleWidth - 2, (int) grilleHeight - 2, color);
			 */
		}
		int i = 0;
		for (Boolean b : isBlock) {
			if (b) {
				int y = i / grilleSectionX;
				int x = i - y * grilleSectionX;
				
				x *= grilleWidth;
				y *= grilleHeight;

				glColor4f(lastColor[0], lastColor[1], lastColor[2], lastColor[3]);

				ComponentsHelper.renderTexture(Textures.TETRAS_BLOCK, x - 1, y - 1, (int) grilleWidth + 2, (int) grilleHeight + 2);

				glColor4f(1, 1, 1, 1);
				/*
				 * ComponentsHelper.drawQuad(x - 1, y - 1, (int) grilleWidth + 2, (int) grilleHeight + 2, color);
				 * ComponentsHelper.drawQuad(x + 1, y + 1, (int) grilleWidth - 2, (int) grilleHeight - 2, lastColor);
				 */
				
			}
			
			i++;
		}
		
		ComponentsHelper.drawText("Score : " + score, 10, 50, 40);
		
	}

	private boolean deathTest() {
		for (int i = 0; i < grilleSectionX * 4; i++) {
			if (isBlock[i]) return true;
		}
		return false;
	}
	
	private void deleteLines() {
		for (int y = 3; y < grilleSectionY; y++) {
			boolean fullLine = true;
			for (int x = 0; x < grilleSectionX; x++) {
				if (!isBlock[y * grilleSectionX + x]) {
					fullLine = false;
					continue;
				}
			}
			if (fullLine) {
				fallSpeed -= 0.01;

				boolean clearLine = true;
				for (int yUp = y; yUp > 0; yUp--) {

					for (int xUp = grilleSectionX - 1; xUp >= 0; xUp--) {
						if (isBlock[yUp * grilleSectionX + xUp] && clearLine) {
							isBlock[yUp * grilleSectionX + xUp] = false;
						} else if (isBlock[yUp * grilleSectionX + xUp]) {
							isBlock[yUp * grilleSectionX + xUp] = false;
							isBlock[(yUp + 1) * grilleSectionX + xUp] = true;
						}
					}
					clearLine = false;
				}
			}
		}

	}

	class block {

		Location loc;
		List<Location> locs;

		private block(final Location loc) {
			this.loc = loc;
			Random r = new Random();
			locs = new ArrayList<Location>(4);
			Location locat = new Location(0, 0);
			locs.add(locat);

			for (int i = 0; i < 3; i++) {
				Location locR = locs.get(r.nextInt(locs.size()));
				Location newLoc;
				if (r.nextBoolean())
					newLoc = locR.clone().add(r.nextInt(3) - 1, 0);
				else
					newLoc = locR.clone().add(0, r.nextInt(3) - 1);

				boolean same = false;
				for (Location location : locs) {
					if (location.distance(newLoc) == 0) {
						i--;
						same = true;
						continue;
					}
				}
				if (same) continue;

				locs.add(newLoc);
			}
		}

		private void move() {

			if (rotateL) {
				List<Location> newLocations = new ArrayList<Location>(4);
				Boolean rotate = true;
				for (Location locat : locs) {
					double x = locat.getX();
					double y = locat.getY();

					if (isCollision(new Location(-y + loc.getX(), x + loc.getY()))) {
						rotate = false;
						break;
					}
					newLocations.add(new Location(-y, x));
				}

				if (rotate) {
					locs = newLocations;
				}

			} else if (rotateR) {

				List<Location> newLocations = new ArrayList<Location>(4);
				Boolean rotate = true;
				for (Location locat : locs) {
					double x = locat.getX();
					double y = locat.getY();

					if (isCollision(new Location(y + loc.getX(), -x + loc.getY()))) {
						rotate = false;
						break;
					}
					newLocations.add(new Location(y, -x));
				}

				if (rotate) {
					locs = newLocations;
				}

			} else if (left) {
				Location newLoc = new Location(loc.getX() - 0.2, loc.getY());
				if (!isTetrisLeft(locs, newLoc)) {
					loc = newLoc;
				}
			} else if (right) {
				Location newLoc = new Location(loc.getX() + 0.2, loc.getY());
				if (!isTetrisRight(locs, newLoc)) {
					loc = newLoc;
				}
			} else if (down) {
				Location newLoc = new Location(loc.getX(), loc.getY() + 1);
				if (!isTetrisInGround(locs, newLoc)) {
					loc = newLoc;
				}
			}

			if (!left && !right && (int) loc.getX() != loc.getX()) {
				if (loc.getX() - (int) loc.getX() > 0.5)
					loc.setX((int) loc.getX() + 1);
				else
					loc.setX((int) loc.getX());
			}

			// System.out.println(testTetrisCollision(locs, loc));

			Location newLoc = new Location(loc.getX(), loc.getY() + fallSpeed);
			if (!isTetrisInGround(locs, newLoc)) {// peut descendre
				loc = newLoc;
			} else {// transformation en bloc
				for (Location locat : locs) {
					int x = (int) (locat.getX() + loc.getX());
					int y = (int) (locat.getY() + loc.getY());

					isBlock[y * grilleSectionX + x] = true;

				}
				score++;
				fallSpeed += 0.001;
				deleteLines();
				if (deathTest()) {
					System.out.println("Tu es mort !");
					gameOver = true;
				} else {
					movingBlock = new block(new Location(grilleSectionX / 2, 2));
					Random r = new Random();
					lastColor = color;
					color = new float[] { (float) (0.5 + r.nextFloat() / 2), (float) (0.5 + r.nextFloat() / 2), (float) (0.5 + r.nextFloat() / 2), 1 };
					changeStopDown = true;
				}

			}

		}

		private boolean isTetrisLeft(final List<Location> locations, final Location locBase) {
			
			for (Location locs : locations) {
				if (loc.getX() + locs.getX() < 0.2) return true;
				
				int testCase = (int) (loc.getY() + locs.getY()) * grilleSectionX + (int) (loc.getX() + locs.getX() - 0.2);
				if (testCase > 0 && testCase < grilleSectionX * grilleSectionY && isBlock[testCase])
					return true;
			}
			return false;
		}

		private boolean isTetrisRight(final List<Location> locations, final Location locBase) {
			
			for (Location locs : locations) {
				if (loc.getX() + locs.getX() + 1 >= grilleSectionX) return true;

				int testCase = (int) (loc.getY() + locs.getY()) * grilleSectionX + (int) (loc.getX() + locs.getX() + 1);
				if (testCase > 0 && testCase < grilleSectionX * grilleSectionY && isBlock[testCase])
					return true;
			}
			return false;
		}

		private boolean isTetrisInGround(final List<Location> locations, final Location locBase) {
			
			for (Location locs : locations) {
				if (loc.getY() + locs.getY() + 1 >= grilleSectionY) return true;

				int testCase = (int) (loc.getY() + locs.getY() + 1) * grilleSectionX + (int) (loc.getX() + locs.getX());
				if (testCase > 0 && testCase < grilleSectionX * grilleSectionY && isBlock[testCase])
					return true;
			}
			return false;
		}

		private boolean isCollision(final Location loca) {// y a t'il un block ?
			int xTest = (int) loca.getX();
			int yTest = (int) loca.getY();
			// hors limite
			if (xTest <= 0 || xTest >= grilleSectionX || yTest <= 0 || yTest >= grilleSectionY) return true;

			return isBlock[yTest * grilleSectionX + xTest];

		}
	}
}
