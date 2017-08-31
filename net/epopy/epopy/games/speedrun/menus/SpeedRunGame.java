package net.epopy.epopy.games.speedrun.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;

public class SpeedRunGame extends AbstractGameMenu {
	private static final int menSize = 100;

	int decors;
	int playerWalk;
	int playerSneak;
	int height;
	int propulsion;
	int newRobot;
	double level;
	int score;
	boolean sneak;
	List<Robot> robots = new ArrayList<Robot>();
	List<Integer> lampadairesX = new ArrayList<Integer>();

	@Override
	public void onEnable() {
		sneak = false;
		propulsion = 0;
		decors = 0;
		playerWalk = 1;
		height = 1;
		newRobot = 50;
		level = 12;
		playerSneak = 0;
		score = 0;
	}

	@Override
	public void update() {

		if (Main.getPlayer().hasSound()) {
			Audios.changeVolume(0.02f);
			Audios.SPEEDRUN.start(true);
		}

		level += 0.005;
		if (Input.isKeyDown(Keyboard.KEY_LSHIFT)) {
			sneak = true;
		} else {
			if (sneak) sneak = false;
			
			if (Input.isKeyDown(Keyboard.KEY_SPACE)) {
				if (height == 1) {// au sol
					height += 5;
					propulsion = 22;
				} else {
					propulsion++;
				}

			}
		}
		if (height > 1) {
			height += propulsion;
			propulsion -= 2;
		} else {
			height = 1;
		}
		
		decors -= level / 4;
		if (decors < 0) decors += 1920;
		
		playerWalk++;
		if (playerWalk > 80) playerWalk = 9;
		
		playerSneak++;
		if (playerSneak > 10) playerSneak = 0;
		
		for (int i = robots.size() - 1; i >= 0; i--) {
			Robot r = robots.get(i);
			
			if (r.x > 0 && r.x < 130) {
				if (r.normalORsneakORlampadaire == 0) {
					if (!sneak) {
						gameOver = true;
						System.out.println("GameOver");
					}
				} else if (r.normalORsneakORlampadaire == 1) {
					if (height < 60) {
						gameOver = true;
						System.out.println("GameOver");
					}
				} else {// lampadaire
					if (height > 130 || sneak) {
						gameOver = true;
						System.out.println("GameOver");
					}
				}

			}
			r.move();
		}
		
		if (newRobot > 0) newRobot--;
		else {
			robots.add(new Robot());
			newRobot = defaultWidth / (int) level;
		}
		
	}

	@Override
	public void render() {

		ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_BG, decors, 0, 1920, 1080);
		ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_BG, decors - 1920, 0, 1920, 1080);
		
		for (Robot r : robots) {

			if (r.normalORsneakORlampadaire == 2) {// lampadaire
				ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_LAMPADAIRE, r.x, defaultHeight / 2 - 180, 100, 300);
			} else if (r.normalORsneakORlampadaire == 1) {// sneak

				if ((r.walk - 5) / 10 == r.walk / 10)// l'unité n'est pas 1/2/3/4/5
					ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_OTHERMENSOL1, r.x, defaultHeight / 2 + 62, 140, 60);
				else ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_OTHERMENSOL2, r.x, defaultHeight / 2 + 62, 140, 60);

			} else {// normal
				Textures robotTexture;
				switch (r.walk / 10) {
					case 2:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN2;
						break;
					case 3:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN3;
						break;
					case 4:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN4;
						break;
					case 5:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN5;
						break;
					case 6:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN6;
						break;
					case 7:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN7;
						break;
					case 8:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN8;
						break;
					default:
					robotTexture = Textures.GAME_SPEEDRUN_OTHERMEN1;
						break;
				}
				ComponentsHelper.renderTexture(robotTexture, r.x, defaultHeight / 2 - 180, 200, 300);
			}

		}
		
		if (sneak) {
			if ((playerWalk - 5) / 10 == playerWalk / 10)// l'unité n'est pas 1/2/3/4/5
				ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_MANSOL1, 50, defaultHeight / 2 + 62, 140, 60);
			else ComponentsHelper.renderTexture(Textures.GAME_SPEEDRUN_MANSOL2, 50, defaultHeight / 2 + 62, 140, 60);
			
		} else {
			Textures playerTexture;
			switch (playerWalk / 10) {
				case 2:
				playerTexture = Textures.GAME_SPEEDRUN_MAN2;
					break;
				case 3:
				playerTexture = Textures.GAME_SPEEDRUN_MAN3;
					break;
				case 4:
				playerTexture = Textures.GAME_SPEEDRUN_MAN4;
					break;
				case 5:
				playerTexture = Textures.GAME_SPEEDRUN_MAN5;
					break;
				case 6:
				playerTexture = Textures.GAME_SPEEDRUN_MAN6;
					break;
				case 7:
				playerTexture = Textures.GAME_SPEEDRUN_MAN7;
					break;
				case 8:
				playerTexture = Textures.GAME_SPEEDRUN_MAN8;
					break;
				default:
				playerTexture = Textures.GAME_SPEEDRUN_MAN1;
					break;
			}
			ComponentsHelper.renderTexture(playerTexture, 50, defaultHeight / 2 - 27 - height, 100, 150);
			
		}

	}

	class Robot {

		int x;
		int speed;
		int walk;
		int normalORsneakORlampadaire;

		public Robot() {
			x = defaultWidth;
			Random r = new Random();
			normalORsneakORlampadaire = r.nextInt(3);
			speed = (int) level / 2;
			walk = r.nextInt(79) + 1;
		}

		public void move() {
			x -= speed;
			if (x < -menSize) {
				robots.remove(this);
				score++;
			}
			
			walk++;
			if (walk > 80) walk = 9;

		}
		
	}

}