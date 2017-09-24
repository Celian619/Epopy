package net.epopy.epopy.games.speedrun;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGame;
import net.epopy.epopy.games.speedrun.menus.SpeedRunGame;
import net.epopy.epopy.games.speedrun.menus.SpeedRunOptions;

public class SpeedRun extends AbstractGame {

	@Override
	public void onEnable() {
		menuGame = new SpeedRunGame();
		menuOptions = new SpeedRunOptions();
		setStatus(false);
	}
	
	@Override
	public Textures getDefaultBackGround() {
		return Textures.SPEEDRUN_LEVELBG;
	}

	@Override
	public String getName() {
		return "SpeedRun";
	}

}
