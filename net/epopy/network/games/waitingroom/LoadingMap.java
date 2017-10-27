package net.epopy.network.games.waitingroom;

import static net.epopy.epopy.display.components.ComponentsHelper.drawLine;
import static net.epopy.epopy.display.components.ComponentsHelper.drawText;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.network.games.AbstractGameNetwork;
import net.epopy.network.games.tank.Tank;
import net.epopy.network.games.tank.modules.MapLoader;

public class LoadingMap extends AbstractGameNetwork {

	@Override
	public void onEnable() {
		loding = false;
	}

	@Override
	public void update() {

	}

	double x = 0;
	boolean loding;
	boolean droite = true;
	private static float[] blackColor = new float[] { 0, 0, 0, 1 };

	private int i = 0;
	private int iValue = 0;

	@Override
	public void render() {
		getDefaultBackGround().renderBackground();

		int milieu = 1920 / 2;
		
		if (iValue == 20)
			i = 1;
		else if (iValue == 40)
			i = 2;
		else if (iValue == 60)
			i = 3;
		else if (iValue == 80)
			i = 2;
		else if (iValue == 100)
			i = 1;
		else if (iValue == 120) {
			i = 0;
			iValue = 0;
		}
		iValue++;
		
		String point = "";
		if (i == 1)
			point = ".";
		else if (i == 2)
			point = ". .";
		else if (i == 3)
			point = ". . .";

		drawText("Chargement en cours ", milieu, 800, PosWidth.MILIEU, PosHeight.HAUT, 50, blackColor);
		drawText(point, 1210, 800, 50, blackColor);

		drawLine(milieu + x, 880, milieu - x, 880, 2, blackColor);
		if (droite) {
			if (x < 300)
				x += 5;
			else
				droite = false;
		} else {
			if (x > 0)
				x -= 5;
			else
				droite = true;
		}

		if (!MapLoader.LOADING && !loding) {
			Tank.MAP = new MapLoader("/net/epopy/epopy/display/res/network/games/map-tank-default-game.png");
			loding = true;
		}
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_WAITING_LODING_MAP_TANK;
	}

}
