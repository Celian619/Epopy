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
	
	private static float[] blackColor = new float[] { 0, 0, 0, 1 };

	private boolean loading, droite = true;
	private int i, iValue;
	private double x;

	@Override
	public void onEnable() {
		loading = false;
	}

	@Override
	public void update() {

	}

	@Override
	public void render() {
		getDefaultBackGround().renderBackground();

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

		String point = new String(new char[i]).replace("\0", ".");// . ou .. ou ...
		
		int milieu = 960;

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

		if (!MapLoader.LOADING && !loading) {
			Tank.MAP = new MapLoader("/net/epopy/epopy/display/res/network/games/map-tank-default-game.png");
			loading = true;
		}
	}

	@Override
	public Textures getDefaultBackGround() {
		return Textures.NETWORK_WAITING_LODING_MAP_TANK;
	}

}
