package net.epopy.epopy.games.ping.menus;

import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ButtonGui;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class PingOptions extends AbstractGameMenu {
	
	private static ButtonGui controlBas;
	private static ButtonGui controlHaut;
	private static boolean controlBasClicked;
	private boolean controlHautClicked;
	private static ButtonGui gauche;
	private static ButtonGui droite;
	
	public static int KEY_DOWN;
	public static int KEY_UP;
	public static int MOUSE;
	// 2 = touches & mouse
	
	@Override
	public void onEnable() {
		KEY_DOWN = Integer.parseInt(Main.getPlayer().getConfig().getData("ping_control_bas"));
		KEY_UP = Integer.parseInt(Main.getPlayer().getConfig().getData("ping_control_haut"));
		MOUSE = Integer.parseInt(Main.getPlayer().getConfig().getData("ping_control_mouse"));// 0 = mouse | 1 = touches |

		controlBasClicked = false;
		controlHautClicked = false;
		gauche = new ButtonGui(Textures.GAME_MENU_GAUCHE_OFF, Textures.GAME_MENU_GAUCHE_ON);
		droite = new ButtonGui(Textures.GAME_MENU_DROITE_OFF, Textures.GAME_MENU_DROITE_ON);
		controlBas = new ButtonGui(Keyboard.getKeyName(KEY_DOWN), new float[] { 0, 0.7f, 0, 1 }, 30);
		controlHaut = new ButtonGui(Keyboard.getKeyName(KEY_UP), new float[] { 0, 0.7f, 0, 1 }, 30);
	}
	
	@Override
	public void update() {
		gauche.update(1008, 723, PosWidth.DROITE, PosHeight.HAUT, 33, 30);
		droite.update(1203, 723, PosWidth.GAUCHE, PosHeight.HAUT, 33, 30);
		controlBas.update(985, 547, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		controlHaut.update(985, 367, PosWidth.GAUCHE, PosHeight.MILIEU, 200, 30);
		
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

		String type = "";
		if (MOUSE == 0) type = "Souris";
		else if (MOUSE == 1) type = "Touches";
		else if (MOUSE == 2) type = "Touches & Souris";
		
		drawText(type, 1105, 721, PosWidth.MILIEU, PosHeight.HAUT, 25, new float[] { 0, 0.7f, 0, 1 });
		
		float[] color = new float[] { 1, 1, 1, 1 };
		drawText("Haut", 935, 370, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Bas", 935, 550, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		drawText("Type de contrôle", 935, 738, PosWidth.DROITE, PosHeight.MILIEU, 30, color);
		
	}
}
