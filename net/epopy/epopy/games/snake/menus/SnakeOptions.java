package net.epopy.epopy.games.snake.menus;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class SnakeOptions extends AbstractGameMenu {
	
	private static ButtonGui controlBas;
	private static ButtonGui controlHaut;
	private static ButtonGui controlGauche;
	private static ButtonGui controlDroite;
	private static boolean controlBasClicked;
	private static boolean controlHautClicked;
	private static boolean controlGaucheClicked;
	private static boolean controlDroiteClicked;
	
	public static int KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_bas", String.valueOf(Keyboard.KEY_DOWN)));
	public static int KEY_UP = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_haut", String.valueOf(Keyboard.KEY_UP)));
	public static int KEY_LEFT = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_gauche", String.valueOf(Keyboard.KEY_LEFT)));
	public static int KEY_RIGHT = Integer.parseInt(Main.getPlayer().getConfig().getData("snake_control_droite", String.valueOf(Keyboard.KEY_RIGHT)));

	@Override
	public void onEnable() {
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
		controlHaut.update(1495 - 520, 353, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		controlBas.update(1495 - 520, 720, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		
		controlGauche.update(1495 - 520, 353 + 100, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		controlDroite.update(1495 - 520, 720 - 100, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
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
		
		ComponentsHelper.drawText("Haut", 1420 - 528, 353, 30, new float[] { 1, 1, 1, 1 });
		ComponentsHelper.drawText("Bas", 1420 - 520, 720, 30, new float[] { 1, 1, 1, 1 });
		ComponentsHelper.drawText("Gauche", 1400 - 543, 353 + 100, 30, new float[] { 1, 1, 1, 1 });
		ComponentsHelper.drawText("Droite", 1400 - 525, 720 - 100, 30, new float[] { 1, 1, 1, 1 });
	}
}
