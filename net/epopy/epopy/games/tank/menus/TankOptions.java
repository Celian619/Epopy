package net.epopy.epopy.games.tank.menus;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class TankOptions extends AbstractGameMenu {

	private static ButtonGui controlBas;
	private static ButtonGui controlHaut;
	private static boolean controlBasClicked;
	private static boolean controlHautClicked;
	
	public static int KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("tank_control_bas", String.valueOf(Keyboard.KEY_DOWN)));
	public static int KEY_UP = Integer.parseInt(Main.getPlayer().getConfig().getData("tank_control_haut", String.valueOf(Keyboard.KEY_UP)));
	
	@Override
	public void onEnable() {
		controlBasClicked = false;
		controlHautClicked = false;
		controlBas = new ButtonGui(Keyboard.getKeyName(KEY_DOWN), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlHaut = new ButtonGui(Keyboard.getKeyName(KEY_UP), new float[] { 0, 0.7f, 0, 1 }, 30);
	}

	@Override
	public void update() {
		controlBas.update(1495 - 510, 718, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		controlHaut.update(1495 - 510, 350, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);

	
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

		ComponentsHelper.drawText("Avancer", 1420-575, 350, 30, new float[] { 1, 1f, 1, 1 });
		ComponentsHelper.drawText("Reculer", 1420-570, 718, 30, new float[] { 1,1f, 1, 1 });
	}
}
