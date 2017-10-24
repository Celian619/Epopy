package net.epopy.epopy.games.reflexion.menus;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class Demineur extends AbstractGameMenu {
	
	private static int caseWidth = 15;
	private static int caseHeight = 15;
	private static int bombesNumber = 45;

	private final List<Integer> mines = new ArrayList<Integer>();// 225
	
	@Override
	public void onEnable() {
		Random r = new Random();
		for (int i = 0; i < bombesNumber; i++) {
			int num = r.nextInt(225);
			while (mines.contains(num))
				num = r.nextInt(225);
				
			mines.add(num);
		}
		System.out.println("Mines : " + mines);
	}
	
	@Override
	public void update() {
	
	}
	
	@Override
	public void render() {
	
	}
	
}
