package net.epopy.network;

public class Test {

	public static void main(String[] args) {
		String myName = "$2y$10$kb2wu";
		myName = myName.substring(0,2)+'a'+myName.substring(3);  
		System.out.print(myName);
	}
}
