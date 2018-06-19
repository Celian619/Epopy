package net.epopy.epopy.games.tank.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class TankOptions extends AbstractGameMenu {
	
	public static int KEY_DOWN, KEY_UP;

	private static boolean controlBasClicked, controlHautClicked;
	private static ButtonGui controlBas, controlHaut;
	
	@Override
	public void onEnable() {
		KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("tank_control_bas", String.valueOf(Keyboard.KEY_DOWN)));
		KEY_UP = Integer.parseInt(Main.getPlayer().getConfig().getData("tank_control_haut", String.valueOf(Keyboard.KEY_UP)));

		controlBasClicked = false;
		controlHautClicked = false;
		controlBas = new ButtonGui(Keyboard.getKeyName(KEY_DOWN), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlHaut = new ButtonGui(Keyboard.getKeyName(KEY_UP), new float[] { 0, 0.7f, 0, 1 }, 30);
	}

	@Override
	public void update() {
		controlBas.update(985, 735, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		controlHaut.update(985, 367, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);

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
					Main.getPlayer().getConfig().setValue("tank_control_bas", String.valueOf(KEY_DOWN));
					controlBas.setText(Keyboard.getKeyName(KEY_DOWN));
					break;
				}
			}
		}
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
					Main.getPlayer().getConfig().setValue("tank_control_haut", String.valueOf(KEY_UP));
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

		float[] color = new float[] { 1, 1, 1, 1 };
		drawText("Avancer", 935, 370, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Reculer", 935, 738, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
	}
}
