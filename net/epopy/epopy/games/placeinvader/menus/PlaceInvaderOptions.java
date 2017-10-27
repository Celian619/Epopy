package net.epopy.epopy.games.placeinvader.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class PlaceInvaderOptions extends AbstractGameMenu {

	private static ButtonGui controlGauche;
	private static ButtonGui controlDroite;
	private static boolean controlGaucheClicked;
	private static boolean controlDroiteClicked;

	public static int KEY_LEFT;
	public static int KEY_RIGHT;
	
	@Override
	public void onEnable() {
		KEY_LEFT = Integer.parseInt(Main.getPlayer().getConfig().getData("placeinvader_control_gauche", String.valueOf(Keyboard.KEY_LEFT)));
		KEY_RIGHT = Integer.parseInt(Main.getPlayer().getConfig().getData("placeinvader_control_droite", String.valueOf(Keyboard.KEY_RIGHT)));
		
		controlDroiteClicked = false;
		controlGaucheClicked = false;
		controlGauche = new ButtonGui(Keyboard.getKeyName(KEY_LEFT), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlDroite = new ButtonGui(Keyboard.getKeyName(KEY_RIGHT), new float[] { 0, 0.7f, 0, 1 }, 30);
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
					Main.getPlayer().getConfig().setValue("placeinvader_control_gauche", String.valueOf(KEY_LEFT));
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
					Main.getPlayer().getConfig().setValue("placeinvader_control_droite", String.valueOf(KEY_RIGHT));
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
		drawText("Gauche", 935, 738, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Droite", 935, 370, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
	}
}
