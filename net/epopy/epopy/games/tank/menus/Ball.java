package net.epopy.epopy.games.tank.menus;

import java.util.List;

import net.epopy.epopy.utils.Location;

class Ball {
	static final int speedBall = 8;
	private final Location loc;
	private final boolean playerOwn;
	
	static List<Ball> balles;
	private final boolean testBall;
	boolean isTestFinish;
	boolean testResult;
	private int rebonds = 0;
	
	public Ball(final Location location, final boolean playerOwn, final boolean isTest) {
		loc = location;
		this.playerOwn = playerOwn;
		testBall = isTest;
		if (isTest) {
			isTestFinish = false;
			testResult = false;
		}
	}
	
	public Location getLocation() {
		return loc;
	}
	
	public void refreshPos() {
		
		double x = TankGame.deplacedX(loc, speedBall);
		double y = TankGame.deplacedY(loc, speedBall);
		
		if (!TankGame.game.isBallCollision((int) x, (int) y)) {
			loc.setPos(x, y);
		} else if (!TankGame.game.isBallCollision((int) x, (int) loc.getY())) {// plafond
			int direction = 360 - loc.getDirection();
			if (direction >= 180) direction -= 360;
			loc.setDirection(direction);
			rebonds++;
		} else if (!TankGame.game.isBallCollision((int) loc.getX(), (int) y)) {// mur
			int direction = 540 - loc.getDirection();
			if (direction >= 180) direction -= 360;
			loc.setDirection(direction);
			rebonds++;
		} else {
			int direction = loc.getDirection() - 180;
			if (direction <= -180) direction += 360;
			loc.setDirection(direction);
		}
		
		if (playerOwn) {
			if (loc.distance(TankGame.game.locRobot) < TankGame.tankSize) {
				if (TankGame.game.damaged == 0) {
					TankGame.game.damage++;
					TankGame.game.damaged = 500;
				}
				balles.remove(this);
			} else if (loc.distance(TankGame.game.locPlayer) < TankGame.tankSize && rebonds > 0)
				balles.remove(this);
			else if (rebonds > TankGame.maxRebonds)
				balles.remove(this);
				
		} else {
			if (loc.distance(TankGame.game.locPlayer) < TankGame.tankSize) {
				if (testBall) {
					isTestFinish = true;
					testResult = true;
					return;
				}
				
				TankGame.game.gameOver = true;
			} else if (loc.distance(TankGame.game.locRobot) < TankGame.tankSize && rebonds > 0) {
				
				if (testBall) {
					isTestFinish = true;
					testResult = false;
					return;
				}
				
				balles.remove(this);
				TankGame.game.robotBallesNbr--;
			} else if (rebonds > TankGame.maxRebonds) {
				
				if (testBall) {
					isTestFinish = true;
					testResult = false;
					
					return;
				}
				
				balles.remove(this);
				TankGame.game.robotBallesNbr--;
			}
		}
		
		if (balles.contains(this) && balles.size() > 1 && !testBall) {// pas encore supprimé
			for (int i = balles.size() - 1; i >= 0; i--) {
				Ball b = balles.get(i);
				if (b != this) {
					if (b.getLocation().distance(loc) < 10) {
						balles.remove(this);
						balles.remove(b);
						if (!playerOwn) TankGame.game.robotBallesNbr--;
						if (!b.playerOwn) TankGame.game.robotBallesNbr--;
						
						break;
					}
				}
			}
		}
		
	}
	
	public boolean isPlayerBall() {
		return playerOwn;
	}
	
}
