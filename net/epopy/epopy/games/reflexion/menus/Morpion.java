package net.epopy.epopy.games.reflexion.menus;

import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;

public class Morpion extends AbstractGameMenu {

	private Integer[] c;// 0 rien 1 player 5 robot
	private static int size = 100;
	private static int debutX = (int) (defaultWidth / 2 - 1.5 * size);
	private static int debutY = (int) (defaultHeight / 2 - 1.5 * size);
	private float[] color;
	
	@Override
	public void onEnable() {
		c = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		color = new float[] { 1, 1, 1, 1 };
		gameOver = false;
		win = false;
	}
	
	@Override
	public void update() {
		if (win) return;

		if (gameOver) {
			c = new Integer[] { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			color = new float[] { 1, 1, 1, 1 };
			gameOver = false;
			return;
		}

		if (Input.getButtonDown(0)) {
			int x = (int) (Mouse.getX() / (double) Display.getWidth() * defaultWidth);
			int y = (int) ((Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight);

			if (Math.abs(x - defaultWidth / 2) < 1.5 * size && Math.abs(y - defaultHeight / 2) < 1.5 * size) {

				x -= debutX;
				y -= debutY;

				int numCase = 0;
				numCase += x / size;

				numCase += 3 * (y / size);

				if (c[numCase] != 5 && c[numCase] != 1) {
					c[numCase] = 1;
					testWin(true);

				}

			}
		}
	}
	
	@Override
	public void render() {
		
		ComponentsHelper.drawLine(debutX + size, debutY, debutX + size, debutY + size * 3, 10, color);
		ComponentsHelper.drawLine(debutX + size * 2, debutY, debutX + size * 2, debutY + size * 3, 10, color);
		ComponentsHelper.drawLine(debutX, debutY + size, debutX + size * 3, debutY + size, 10, color);
		ComponentsHelper.drawLine(debutX, debutY + size * 2, debutX + size * 3, debutY + size * 2, 10, color);
		
		int caseN = 0;
		for (Integer i : c) {
			
			if (i == 1) {
				int y = caseN / 3;
				int x = caseN - y * 3;
				x = x * size + debutX;
				y = y * size + debutY;

				ComponentsHelper.drawLine(x + 20, y + size / 2, x + size - 20, y + size / 2, 8, color);
				ComponentsHelper.drawLine(x + size / 2, y + 20, x + size / 2, y + size - 20, 8, color);

			} else if (i == 5) {
				int y = caseN / 3;
				int x = caseN - y * 3;
				x = x * size + debutX;
				y = y * size + debutY;

				ComponentsHelper.drawCircle(x + size / 2, y + size / 2, size / 2 - 20, 20, color);
			}
			caseN++;
		}

	}

	private void testWin(final boolean isPlayer) {
		int n = (isPlayer ? 1 : 5) * 3;
		if (c[0] + c[1] + c[2] == n || c[3] + c[4] + c[5] == n || c[6] + c[7] + c[8] == n || c[0] + c[3] + c[6] == n || c[1] + c[4] + c[7] == n || c[2] + c[5] + c[8] == n || c[0] + c[4] + c[8] == n || c[2] + c[4] + c[6] == n) {
			if (isPlayer) {
				win = true;
				color = new float[] { 0.2f, 0.2f, 1, 1 };
			} else gameOver = true;
		} else {
			playRobot();
		}
	}
	
	private void playRobot() {
		if (isFull()) return;
		Random r = new Random();
		
		while (true) {
			int i = r.nextInt(9);
			if (c[i] == 0) {
				c[i] = 5;
				break;
			}
		}
		
	}

	private boolean isFull() {
		for (Integer i : c)
			if (i == 0) return false;
			
		return true;
	}
}
