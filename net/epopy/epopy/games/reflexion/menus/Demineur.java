package net.epopy.epopy.games.reflexion.menus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;

public class Demineur extends AbstractGameMenu {

	private static int casesWidth = 15;
	private static int casesHeight = 15;
	private static int bombesNumber = 45;
	
	private List<Integer> mines;// 225
	private Map<Integer, Integer> opens;// -1 = mine; 0 = rien
	private final ButtonGui demineur = new ButtonGui(Textures.REFLEXION_DEMINEUR, Textures.REFLEXION_DEMINEUR_ON);
	private boolean inGame;

	@Override
	public void onEnable() {
		mines = new ArrayList<Integer>();
		Random r = new Random();
		for (int i = 0; i < bombesNumber; i++) {
			int num = r.nextInt(225);
			while (mines.contains(num))
				num = r.nextInt(225);
				
			mines.add(num);
		}
		inGame = false;
		
		opens = new HashMap<Integer, Integer>();
	}

	@Override
	public void update() {
		if (!inGame) {
			if (demineur.isClicked())
				inGame = true;
			else
				demineur.update(defaultWidth - 300, 169, PositionWidth.MILIEU, PositionHeight.MILIEU, 265, 40);
		} else {
			if (Input.getButtonDown(0)) {
				int x = (int) (Mouse.getX() / (double) Display.getWidth() * defaultWidth);
				int y = (int) ((Display.getHeight() - Mouse.getY()) / (double) Display.getHeight() * defaultHeight);
				x -= defaultWidth - 600;
				if (x > 0 && x < 600 && y > 0 && y < 600) {// clic dans la zone de jeu
					int line = x / 40;
					int column = y / 22;
					int caseNumber = column * 15 + line;
					System.out.println(caseNumber);
					if (mines.contains(caseNumber)) {
						gameOver = true;
						opens.put(caseNumber, -1);// bombe
						return;
					}
					int numero = 0;
					// fait les 9 case autour
					for (int i = -15; i < 15; i += 15) {// -15,0,15
						for (int i2 = -1; i2 < 1; i2++) {// -1,0,1
							if (i + i2 == 0) continue; // la case elle-même
							int caseC = caseNumber + i + i2;
							if (caseC > 0 && caseC < 225) {// pas hors jeu
								if (mines.contains(caseC)) numero++;
							}
						}
					}
					opens.put(caseNumber, numero);// bombe
				}
			}
		}
	}

	@Override
	public void render() {
		ComponentsHelper.drawQuad(defaultWidth - 600, 0, 600, 337, new float[] { 0.2f, 0.2f, 0.2f, 1 });

		for (Integer caseN : opens.keySet()) {
			int numero = opens.get(caseN);

			int lines = caseN / 15;

			int x = defaultWidth - 600 + (caseN - lines * 15) * 40;
			int y = lines * 22;

			if (numero == -1) {
				ComponentsHelper.renderTexture(Textures.REFLEXION_BOMB_EXPLODE, x, y, 22, 22);
			} else {
				ComponentsHelper.drawQuad(x, y, 40, 22);
			}
		}

		if (!inGame)
			demineur.render();

	}

}
