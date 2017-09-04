package net.epopy.epopy.games.ping.menus;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class PingOptions extends AbstractGameMenu {
	
	private static ButtonGui controlBas;
	private static ButtonGui controlHaut;
	private static boolean controlBasClicked;
	private boolean controlHautClicked;
	private static ButtonGui gauche;
	private static ButtonGui droite;
	
	public static int KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("ping_control_bas", String.valueOf(Keyboard.KEY_DOWN)));
	public static int KEY_UP = Integer.parseInt(Main.getPlayer().getConfig().getData("ping_control_haut", String.valueOf(Keyboard.KEY_UP)));
	public static int MOUSE = Integer.parseInt(Main.getPlayer().getConfig().getData("ping_control_mouse", "2"));// 0 = mouse | 1 = touches |
	// 2 = touches & mouse
	
	@Override
	public void onEnable() {
		controlBasClicked = false;
		controlHautClicked = false;
		gauche = new ButtonGui(Textures.GAME_MENU_GAUCHE_OFF, Textures.GAME_MENU_GAUCHE_ON);
		droite = new ButtonGui(Textures.GAME_MENU_DROITE_OFF, Textures.GAME_MENU_DROITE_ON);
		controlBas = new ButtonGui(Keyboard.getKeyName(KEY_DOWN), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlHaut = new ButtonGui(Keyboard.getKeyName(KEY_UP), new float[] { 0, 0.7f, 0, 1 }, 30);
	}
	
	@Override
	public void update() {
		gauche.update(1550 - 25 - 520, 723, PositionWidth.DROITE, PositionHeight.HAUT, 165 / 5, 148 / 5);
		droite.update(1550 + 185 - 530, 723, PositionWidth.GAUCHE, PositionHeight.HAUT, 165 / 5, 148 / 5);
		controlBas.update(1495 - 510, 530, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		controlHaut.update(1495 - 510, 350, PositionWidth.GAUCHE, PositionHeight.HAUT, 40 * 5, 30);
		
		if (gauche.isClicked()) {
			if (MOUSE > 0) {
				MOUSE--;
			} else MOUSE = 2;
			Main.getPlayer().getConfig().setValue("ping_control_mouse", String.valueOf(MOUSE));
		} else if (droite.isClicked()) {
			if (MOUSE < 2) {
				MOUSE++;
			} else MOUSE = 0;
			Main.getPlayer().getConfig().setValue("ping_control_mouse", String.valueOf(MOUSE));
		}
		
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
					Main.getPlayer().getConfig().setValue("ping_control_bas", String.valueOf(KEY_DOWN));
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
					Main.getPlayer().getConfig().setValue("ping_control_haut", String.valueOf(KEY_UP));
					controlHaut.setText(Keyboard.getKeyName(KEY_UP));
					break;
				}
			}
		}
	}
	
	@Override
	public void render() {
		
		gauche.render();
		droite.render();
		controlBas.render();
		controlHaut.render();
		
		String option1 = "Haut";
		String option2 = "Bas";
		String option3 = "Type de contrôle";
		
		String type = "";
		if (MOUSE == 0) type = "Souris";
		else if (MOUSE == 1) type = "Touches";
		else if (MOUSE == 2) type = "Touches & Souris";
		
		ComponentsHelper.drawText(type, 1580 - 475, 721, PositionWidth.MILIEU, PositionHeight.HAUT, 25, new float[] { 0, 0.7f, 0, 1 });
		
		ComponentsHelper.drawText(option1, 1420 - 528 - 10, 350, 30, new float[] { 1, 1f, 1, 1 });
		ComponentsHelper.drawText(option2, 1420 - 520, 530, 30, new float[] { 1, 1f, 1, 1 });
		ComponentsHelper.drawText(option3, 1280 - 535 - 20, 718, 30, new float[] { 1, 1f, 1, 1 });
		
	}
}