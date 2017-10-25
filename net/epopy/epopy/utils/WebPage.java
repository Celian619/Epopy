package net.epopy.epopy.utils;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebPage {
	
	public static String WEB_PAGE_EPOPY = "http://www.epopy.fr";
	public static String WEB_PAGE_EPOPY_USER = "https://www.epopy.fr/login";
	public static String WEB_PAGE_TWITTER_EPOPY = "https://twitter.com/EpopyOfficiel";
	public static String WEB_PAGE_FACEBOOK_EPOPY = "https://www.facebook.com/EpopyOfficiel";

	public WebPage(final String url) {
		try {
			Desktop.getDesktop().browse(new URI(url));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
	
}
