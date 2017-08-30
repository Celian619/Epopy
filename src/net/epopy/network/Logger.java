package net.epopy.network;

public class Logger {
	

	private static String name = "Epopy - Network";
	
    public static void info(String message) {
        System.out.println("[" + name + "] " + message);
    }

    public static void warning(String message) {
        System.out.println((char)27 + "[31m[" + name + "] " + message + (char)27 + "[0m");
    }

}
