package net.epopy.epopy.games.deco.menus;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.audio.Audios;
import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;

public class DecoGame extends AbstractGameMenu {

	private static int voiePlayer;// Voies : 0 gauche 1 milieu 2 droite
	private static int score;
	private double speed;
	private boolean right;
	private boolean left;

	private final List<Prise> prises = new LinkedList<Prise>(); // Position , Voie

	@Override
	public void onEnable() {

		if (Main.getPlayer().hasSound()) {
			Audios.changeVolume(0.02f);
			Audios.DECO.start(true);
		}

		right = false;
		left = false;
		speed = 1;
		voiePlayer = 1;
	}

	@Override
	public void update() {
		Random r = new Random();
		if (prises.size() < 5 && getNearestPos() > 200 + r.nextInt(100)) {
			
			prises.add(new Prise(0, r.nextInt(3), r.nextBoolean()));
			
		}
		
		if (Input.isKeyDown(Keyboard.KEY_RIGHT)) {
			if (voiePlayer < 2 && !right) {

				voiePlayer++;
				right = true;

			}

		} else if (right) {
			right = false;
		}
		
		if (Input.isKeyDown(Keyboard.KEY_LEFT)) {
			if (voiePlayer > 0 && !left) {

				voiePlayer--;
				left = true;

			}

		} else if (left) {
			left = false;
		}

		for (int i = prises.size() - 1; i >= 0; i--) {
			Prise p = prises.get(i);
			p.position += speed;
			if (p.position > defaultHeight - 100 && p.good) {

				prises.remove(i);
				gameOver = true;
			} else if (p.position > defaultHeight - 175 && !p.good) {
				prises.remove(i);
			} else if (p.position >= defaultHeight - 200 && p.voie == voiePlayer) {
				prises.remove(i);
				if (p.good)
					score++;
				else
					gameOver = true;
			}
		}

		speed += 0.001;
	}

	@Override
	public void render() {
		
		Textures.GAME_DECO_LEVEL_BG.renderBackground();
		float[] color = new float[] { 0, 1f, 0, 1 };
		for (Prise p : prises) {
			if (p.good)
				ComponentsHelper.drawText("1", defaultWidth / 2 + 300 * (p.voie - 1), p.position, PositionWidth.MILIEU, PositionHeight.MILIEU, 60, color);
			else
				ComponentsHelper.drawText("0", defaultWidth / 2 + 300 * (p.voie - 1), p.position, PositionWidth.MILIEU, PositionHeight.MILIEU, 60, color);
				
		}
		
		ComponentsHelper.drawText("_", defaultWidth / 2 + 300 * (voiePlayer - 1), defaultHeight - 280, PositionWidth.MILIEU, PositionHeight.MILIEU, 200, color);

		ComponentsHelper.drawText("Score : " + score, 10, 10, 40);
		
		if (gameOver) {
			ComponentsHelper.drawText("GAME OVER", defaultWidth - 200, 0, 40);
		}
		
	}

	/*
	 *
	 * methods
	 *
	 *
	 *
	 *
	 *
	 */

	private double getNearestPos() {
		double i = 10000;
		for (Prise p : prises) {
			if (p.position < i) i = p.position;
		}
		
		return i;
	}
	
	class Prise {
		private final boolean good;
		private final int voie;
		private double position;
		
		public Prise(final int position, final int voie, final boolean good) {
			this.position = position;
			this.voie = voie;
			this.good = good;
		}

	}
	
}
