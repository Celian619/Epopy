package net.epopy.epopy.games.car.menus;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class CarOptions extends AbstractGameMenu {
	
	private static ButtonGui controlGauche;
	private static ButtonGui controlDroite;
	private static boolean controlGaucheClicked;
	private static boolean controlDroiteClicked;
	
	public static int KEY_LEFT;
	public static int KEY_RIGHT;

	@Override
	public void onEnable() {
		KEY_LEFT = Integer.parseInt(Main.getPlayer().getConfig().getData("car_control_gauche"));
		KEY_RIGHT = Integer.parseInt(Main.getPlayer().getConfig().getData("car_control_droite"));

		controlDroiteClicked = false;
		controlGaucheClicked = false;
		controlGauche = new ButtonGui(Keyboard.getKeyName(KEY_LEFT), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlDroite = new ButtonGui(Keyboard.getKeyName(KEY_RIGHT), new float[] { 0, 0.7f, 0, 1 }, 30);
	}

	@Override
	public void update() {
		controlDroite.update(1495 - 520, 353, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		controlGauche.update(1495 - 520, 720, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
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
					Main.getPlayer().getConfig().setValue("car_control_gauche", String.valueOf(KEY_LEFT));
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
					Main.getPlayer().getConfig().setValue("car_control_droite", String.valueOf(KEY_RIGHT));
					controlDroite.setText(Keyboard.getKeyName(KEY_RIGHT));
					break;
				}
			}
		}
		
	}

	@Override
	public void render() {
		controlGauche.render();
		controlDroite.render();
		
		float[] color = new float[] { 1, 1, 1, 1 };
		ComponentsHelper.drawText("Gauche", 935, 370, PositionWidth.DROITE, PositionHeight.MILIEU, 30, color);
		ComponentsHelper.drawText("Droite", 935, 738, PositionWidth.DROITE, PositionHeight.MILIEU, 30, color);
	}
}
