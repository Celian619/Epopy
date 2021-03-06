package net.epopy.epopy.display.components;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.games.gestion.utils.Pause;
import net.epopy.network.utils.Callback;

public class NotificationGui {

	public static List<NotificationGui> notifs = new LinkedList<>();
	
	private static boolean reponseChoose;
	private static int x, w, removeTime, y = 30, h = 70;

	private final boolean error;
	private final int timeToStay;
	private final float[] color;
	private final String notification, information;
	private final Pause time = new Pause();
	private final ButtonGui accept, refuse;

	private Callback callback, reponse;

	public NotificationGui(final String notification, String information, final int timeToStay, final float[] color, final boolean error) {
		this.notification = notification;
		this.information = information;
		this.timeToStay = timeToStay;
		this.color = color;
		this.error = error;
		accept = new ButtonGui(Textures.MAIN_CONNEXION_NETWORK_BOX_CHECK, Textures.MAIN_CONNEXION_NETWORK_BOX_CHECK);
		refuse = new ButtonGui(Textures.GAME_MENU_QUITTER_OFF, Textures.GAME_MENU_QUITTER_OFF);
		if (information.equals("") || information == null)
			information = "null";

		if (!notifs.contains(this))
			notifs.add(this);
	}

	public void setCallbackRemove(final Callback callback) {
		this.callback = callback;
	}

	public void setCallbackReponse(final Callback callback) {
		reponse = callback;
	}

	public static void clear() {
		x = 0;
		w = 0;
		removeTime = 0;
		notifs = new LinkedList<>();
	}

	public static void render() {
		if (!notifs.isEmpty()) {
			NotificationGui notif = notifs.get(0);
			if (!notif.time.isStarted())
				notif.time.startPause(notif.timeToStay);
			else {
				int sizeNotif = 30;
				int sizeInformations = 20;

				if (removeTime == 0) {
					String message = notif.information.length() > notif.notification.length() ? notif.information : notif.notification;
					int size = notif.information.length() > notif.notification.length() ? sizeInformations : sizeNotif;
					FontUtils font = null;

					if (ComponentsHelper.fonts.containsKey(size))
						font = ComponentsHelper.fonts.get(size);
					else
						font = new FontUtils(size, "Impact");

					double msgWidth = 200;

					for (char c : message.toCharArray())
						msgWidth += font.getCharWidth(c);

					if (w < msgWidth)
						w += 10;

					x = AbstractGameMenu.defaultWidth - w / 2;
				}

				ComponentsHelper.renderTexture(Textures.MAIN_NOTIF, AbstractGameMenu.defaultWidth - w - 60, y, w + 60, h);

				ButtonGui button = notif.error ? notif.refuse : notif.accept;

				button.update(AbstractGameMenu.defaultWidth - w - 30, y + h / 4, null, null, 30, 30);
				button.setX(AbstractGameMenu.defaultWidth - w - 30);
				if (notif.error) GL11.glColor4f(1, 0, 0, 1);
				button.render();

				if (button.isClicked()) {
					removeTime = 60;
					if (notif.reponse != null) {
						reponseChoose = true;
					}
				}

				if (notif.refuse.isClicked() && notif.reponse != null) {
					removeTime = 60;
					reponseChoose = false;
				}

				if (notif.reponse != null) {
					notif.refuse.update(AbstractGameMenu.defaultWidth - w + 5, y + h / 4, null, null, 30, 30);
					notif.refuse.setX(AbstractGameMenu.defaultWidth - w + 5);
					GL11.glColor4f(1, 0, 0, 1);
					notif.refuse.render();
				}

				ComponentsHelper.drawText(notif.notification, x, y + (notif.information.equals("null") ? 13 : 5), PosWidth.MILIEU, PosHeight.HAUT, sizeNotif, notif.color);
				ComponentsHelper.drawText(notif.information.equals("null") ? "" : notif.information, x, y + sizeNotif + 10, PosWidth.MILIEU, PosHeight.HAUT, sizeInformations, notif.color);

				notif.time.getPauseString();

				if (notif.time.isFinish() && removeTime == 0)
					removeTime = 60;

				if (removeTime > 0) {
					if (removeTime == 1) {
						x = 0;
						w = 0;
						removeTime = 0;
						if (notif.reponse != null)
							notif.reponse.callback(reponseChoose);
						reponseChoose = false;
						if (notif.callback != null)
							notif.callback.callback(true);
						notifs.remove(notif);
						return;
					}
					w -= 10;
					x += 10;
					removeTime--;
				}
			}
		}

	}
}
