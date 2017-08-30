package net.epopy.epopy.games.gestion;

public class GameLogger {

	private String name;
	
	public GameLogger(String name) {
		this.name = name;
	}
	
	public void info(String infos) {
		System.out.println("["+name +"] " + infos);
	}
	
	public void warning(String warning) {
		System.out.println("[WARNING] " + "[" + name + "] " + warning);
	}
	
	public void error(String error) {
		System.out.println("[ERROR] " + "[" + name + "] " + error);
	}
	
}
