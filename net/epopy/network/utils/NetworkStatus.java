package net.epopy.network.utils;

public enum NetworkStatus {

	//server
	SERVER_FULL(),
	SERVER_MAINTENANCE(),
	SERVER_OFFLINE(),
	
	//user
	USER_NO_VALID(),
	USER_VALID(),
	USER_ALREADY_CONNECTED(),
	USER_WAITING_CONFIRMATION(),
	;
}
