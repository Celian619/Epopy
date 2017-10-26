package net.epopy.epopy.games.tetras.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class TetrasOptions extends AbstractGameMenu {

	private static ButtonGui controlBas;
	private static ButtonGui controlGauche;
	private static ButtonGui controlDroite;
	private static boolean controlBasClicked;
	private static boolean controlGaucheClicked;
	private static boolean controlDroiteClicked;
	
	public static int KEY_DOWN;
	public static int KEY_LEFT;
	public static int KEY_RIGHT;

	@Override
	public void onEnable() {
		KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("tetras_control_bas", String.valueOf(Keyboard.KEY_DOWN)));
		KEY_LEFT = Integer.parseInt(Main.getPlayer().getConfig().getData("tetras_control_gauche", String.valueOf(Keyboard.KEY_LEFT)));
		KEY_RIGHT = Integer.parseInt(Main.getPlayer().getConfig().getData("tetras_control_droite", String.valueOf(Keyboard.KEY_RIGHT)));

		controlBasClicked = false;
		controlDroiteClicked = false;
		controlGaucheClicked = false;
		
		controlBas = new ButtonGui(Keyboard.getKeyName(KEY_DOWN), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlGauche = new ButtonGui(Keyboard.getKeyName(KEY_LEFT), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlDroite = new ButtonGui(Keyboard.getKeyName(KEY_RIGHT), new float[] { 0, 0.7f, 0, 1 }, 30);
	}
	
	@Override
	public void update() {
		controlBas.update(985, 367, PositionWidth.GAUCHE, PositionHeight.MILIEU, 200, 30);

		controlGauche.update(985, 547, PositionWidth.GAUCHE, PositionHeight.MILIEU, 200, 30);
		controlDroite.update(985, 735, PositionWidth.GAUCHE, PositionHeight.MILIEU, 200, 30);
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
					Main.getPlayer().getConfig().setValue("tetras_control_gauche", String.valueOf(KEY_LEFT));
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
					Main.getPlayer().getConfig().setValue("tetras_control_droite", String.valueOf(KEY_RIGHT));
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
					Main.getPlayer().getConfig().setValue("tetras_control_bas", String.valueOf(KEY_DOWN));
					controlBas.setText(Keyboard.getKeyName(KEY_DOWN));
					break;
				}
			}
		}

	}
	
	@Override
	public void render() {
		controlBas.render();
		controlGauche.render();
		controlDroite.render();
		
		float[] color = new float[] { 1, 1, 1, 1 };
		drawText("Bas", 935, 370, PositionWidth.DROITE, PositionHeight.MILIEU, 30, color);
		drawText("Gauche", 935, 550, PositionWidth.DROITE, PositionHeight.MILIEU, 30, color);
		drawText("Droite", 935, 738, PositionWidth.DROITE, PositionHeight.MILIEU, 30, color);
	}
	
}
