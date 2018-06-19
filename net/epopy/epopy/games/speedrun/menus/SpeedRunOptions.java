package net.epopy.epopy.games.speedrun.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class SpeedRunOptions extends AbstractGameMenu {
	
	private static boolean controlGaucheClicked, controlDroiteClicked;
	public static int KEY_SNEAK, KEY_JUMP;
	private static ButtonGui controlGauche, controlDroite;
	
	@Override
	public void onEnable() {
		KEY_SNEAK = Integer.parseInt(Main.getPlayer().getConfig().getData("speedrun_control_sneak", String.valueOf(Keyboard.KEY_DOWN)));
		KEY_JUMP = Integer.parseInt(Main.getPlayer().getConfig().getData("speedrun_control_jump", String.valueOf(Keyboard.KEY_UP)));
		
		controlDroiteClicked = false;
		controlGaucheClicked = false;
		controlGauche = new ButtonGui(Keyboard.getKeyName(KEY_SNEAK), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlDroite = new ButtonGui(Keyboard.getKeyName(KEY_JUMP), new float[] { 0, 0.7f, 0, 1 }, 30);
	}
	
	@Override
	public void update() {
		controlDroite.update(985, 367, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		controlGauche.update(985, 735, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
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

		float[] color = new float[] { 1, 1, 1, 1 };
		drawText("Jump", 935, 370, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Sneak", 935, 738, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
	}
}
