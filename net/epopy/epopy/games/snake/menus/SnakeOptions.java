package net.epopy.epopy.games.snake.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class SnakeOptions extends AbstractGameMenu {
	
	private static boolean controlBasClicked, controlHautClicked, controlGaucheClicked, controlDroiteClicked;
	public static int KEY_DOWN, KEY_UP, KEY_LEFT, KEY_RIGHT;
	private static ButtonGui controlBas, controlHaut, controlGauche, controlDroite;
	
	@Override
	public void onEnable() {
		KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_bas", String.valueOf(Keyboard.KEY_DOWN)));
		KEY_UP = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_haut", String.valueOf(Keyboard.KEY_UP)));
		KEY_LEFT = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_gauche", String.valueOf(Keyboard.KEY_LEFT)));
		KEY_RIGHT = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_droite", String.valueOf(Keyboard.KEY_RIGHT)));
		
		controlBasClicked = false;
		controlHautClicked = false;
		controlDroiteClicked = false;
		controlGaucheClicked = false;
		controlBas = new ButtonGui(Keyboard.getKeyName(KEY_DOWN), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlHaut = new ButtonGui(Keyboard.getKeyName(KEY_UP), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlGauche = new ButtonGui(Keyboard.getKeyName(KEY_LEFT), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlDroite = new ButtonGui(Keyboard.getKeyName(KEY_RIGHT), new float[] { 0, 0.7f, 0, 1 }, 30);

	}

	@Override
	public void update() {
		controlHaut.update(985, 367, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		controlBas.update(985, 735, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		
		controlGauche.update(985, 467, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		controlDroite.update(985, 637, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		/*
		 * Gauche
		 */
		if (!controlGauche.isOn() && controlGaucheClicked) {
			controlGaucheClicked = false;
			controlGauche.setText(Keyboard.getKeyName(KEY_LEFT));
		}
		
		if (controlGauche.isClicked())
			controlGaucheClicked = true;

		if (controlGaucheClicked) {
			controlGauche.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_LEFT = i;
					controlGaucheClicked = false;
					Main.getPlayer().getConfig().setValue("snake_control_gauche", String.valueOf(KEY_LEFT));
					controlGauche.setText(Keyboard.getKeyName(KEY_LEFT));
					break;
				}
			}
		}
		/*
		 * droite
		 */
		if (!controlDroite.isOn() && controlDroiteClicked) {
			controlDroiteClicked = false;
			controlDroite.setText(Keyboard.getKeyName(KEY_RIGHT));
		}
		
		if (controlDroite.isClicked())
			controlDroiteClicked = true;

		if (controlDroiteClicked) {
			controlDroite.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_RIGHT = i;
					controlDroiteClicked = false;
					Main.getPlayer().getConfig().setValue("snake_control_droite", String.valueOf(KEY_RIGHT));
					controlDroite.setText(Keyboard.getKeyName(KEY_RIGHT));
					break;
				}
			}
		}
		/**
		 * Bas
		 */
		if (!controlBas.isOn() && controlBasClicked) {
			controlBasClicked = false;
			controlBas.setText(Keyboard.getKeyName(KEY_DOWN));
		}
		
		if (controlBas.isClicked())
			controlBasClicked = true;

		if (controlBasClicked) {
			controlBas.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_DOWN = i;
					controlBasClicked = false;
					Main.getPlayer().getConfig().setValue("snake_control_bas", String.valueOf(KEY_DOWN));
					controlBas.setText(Keyboard.getKeyName(KEY_DOWN));
					break;
				}
			}
		}
		
		/**
		 * Haut
		 */
		
		if (!controlHaut.isOn() && controlHautClicked) {
			controlHautClicked = false;
			controlHaut.setText(Keyboard.getKeyName(KEY_UP));
		}
		if (controlHaut.isClicked())
			controlHautClicked = true;

		if (controlHautClicked) {
			controlHaut.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_UP = i;
					controlHautClicked = false;
					Main.getPlayer().getConfig().setValue("snake_control_haut", String.valueOf(KEY_UP));
					controlHaut.setText(Keyboard.getKeyName(KEY_UP));
					break;
				}
			}
		}
	}

	@Override
	public void render() {
		controlBas.render();
		controlHaut.render();
		controlGauche.render();
		controlDroite.render();

		float[] color = new float[] { 1, 1, 1, 1 };
		drawText("Haut", 935, 370, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Bas", 935, 738, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Gauche", 935, 472, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Droite", 935, 640, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
	}
}
