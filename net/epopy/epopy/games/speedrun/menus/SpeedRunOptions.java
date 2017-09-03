package net.epopy.epopy.games.speedrun.menus;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class SpeedRunOptions extends AbstractGameMenu {

	private static ButtonGui controlGauche;
	private static ButtonGui controlDroite;
	private static boolean controlGaucheClicked;
	private static boolean controlDroiteClicked;

	public static int KEY_SNEAK = Integer.parseInt(Main.getPlayer().getConfig().getData("speedrun_control_sneak", String.valueOf(Keyboard.KEY_DOWN)));
	public static int KEY_JUMP = Integer.parseInt(Main.getPlayer().getConfig().getData("speedrun_control_jump", String.valueOf(Keyboard.KEY_UP)));
	
	@Override
	public void onEnable() {
		controlDroiteClicked = false;
		controlGaucheClicked = false;
		controlGauche = new ButtonGui(Keyboard.getKeyName(KEY_SNEAK), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlDroite = new ButtonGui(Keyboard.getKeyName(KEY_JUMP), new float[] { 0, 0.7f, 0, 1 }, 30);
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
			controlGauche.setText(Keyboard.getKeyName(KEY_SNEAK));
		}

		if (controlGauche.isClicked())
			controlGaucheClicked = true;
			
		if (controlGaucheClicked) {
			controlGauche.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_SNEAK = i;
					controlGaucheClicked = false;
					Main.getPlayer().getConfig().setValue("speedrun_control_sneak", String.valueOf(KEY_SNEAK));
					controlGauche.setText(Keyboard.getKeyName(KEY_SNEAK));
					break;
				}
			}
		}
		/*
		 * droite
		 */
		if (!controlDroite.isOn() && controlDroiteClicked) {
			controlDroiteClicked = false;
			controlDroite.setText(Keyboard.getKeyName(KEY_JUMP));
		}

		if (controlDroite.isClicked())
			controlDroiteClicked = true;
			
		if (controlDroiteClicked) {
			controlDroite.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_JUMP = i;
					controlDroiteClicked = false;
					Main.getPlayer().getConfig().setValue("speedrun_control_jump", String.valueOf(KEY_JUMP));
					controlDroite.setText(Keyboard.getKeyName(KEY_JUMP));
					break;
				}
			}
		}

	}
	
	@Override
	public void render() {
		controlGauche.render();
		controlDroite.render();

		ComponentsHelper.drawText("Jump", 1400 - 525, 350, 30, new float[] { 1, 1, 1, 1 });
		ComponentsHelper.drawText("Sneak", 1400 - 540, 717, 30, new float[] { 1, 1, 1, 1 });
	}
}
