package net.epopy.epopy.games.placeinvader.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class PlaceInvaderOptions extends AbstractGameMenu {
	
	public static int KEY_LEFT, KEY_RIGHT;

	private static boolean ctrlGaucheClicked, ctrlDroiteClicked;
	private static ButtonGui ctrlGauche, ctrlDroite;

	@Override
	public void onEnable() {
		KEY_LEFT = Integer.parseInt(Main.getPlayer().getConfig().getData("placeinvader_control_gauche", String.valueOf(Keyboard.KEY_LEFT)));
		KEY_RIGHT = Integer.parseInt(Main.getPlayer().getConfig().getData("placeinvader_control_droite", String.valueOf(Keyboard.KEY_RIGHT)));

		ctrlDroiteClicked = false;
		ctrlGaucheClicked = false;
		ctrlGauche = new ButtonGui(Keyboard.getKeyName(KEY_LEFT), new float[] { 0, 0.7f, 0, 1 }, 30);
		ctrlDroite = new ButtonGui(Keyboard.getKeyName(KEY_RIGHT), new float[] { 0, 0.7f, 0, 1 }, 30);
	}

	@Override
	public void update() {
		ctrlDroite.update(985, 367, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		ctrlGauche.update(985, 735, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		/*
		 * Gauche
		 */
		if (!ctrlGauche.isOn() && ctrlGaucheClicked) {
			ctrlGaucheClicked = false;
			ctrlGauche.setText(Keyboard.getKeyName(KEY_LEFT));
		}
		
		if (ctrlGauche.isClicked())
			ctrlGaucheClicked = true;

		if (ctrlGaucheClicked) {
			ctrlGauche.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_LEFT = i;
					ctrlGaucheClicked = false;
					Main.getPlayer().getConfig().setValue("placeinvader_control_gauche", String.valueOf(KEY_LEFT));
					ctrlGauche.setText(Keyboard.getKeyName(KEY_LEFT));
					break;
				}
			}
		}
		/*
		 * droite
		 */
		if (!ctrlDroite.isOn() && ctrlDroiteClicked) {
			ctrlDroiteClicked = false;
			ctrlDroite.setText(Keyboard.getKeyName(KEY_RIGHT));
		}
		
		if (ctrlDroite.isClicked())
			ctrlDroiteClicked = true;

		if (ctrlDroiteClicked) {
			ctrlDroite.setText("Touche ?");
			for (int i = 0; i < 209; i++) {
				if (Keyboard.isKeyDown(i)) {
					KEY_RIGHT = i;
					ctrlDroiteClicked = false;
					Main.getPlayer().getConfig().setValue("placeinvader_control_droite", String.valueOf(KEY_RIGHT));
					ctrlDroite.setText(Keyboard.getKeyName(KEY_RIGHT));
					break;
				}
			}
		}
		
	}

	@Override
	public void render() {
		ctrlGauche.render();
		ctrlDroite.render();

		float[] color = new float[] { 1, 1, 1, 1 };
		drawText("Gauche", 935, 738, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Droite", 935, 370, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
	}
}
