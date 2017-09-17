package net.epopy.network.games.tank.modules;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.imageio.ImageIO;

import net.epopy.epopy.display.Textures;

public class MapLoader {

	public int[] distanceBlocks;
	public int[] distanceZones;

	private static String MAP_PATH = "";
	private static int nbrOfPixels = -1;
	private static BufferedImage img = null;
	private static int width;
	public Map<String, Zone> ZONES = new TreeMap<>();

	//16, 82, 60
	public MapLoader(String path) {
		MAP_PATH = path;
		
		if(img == null) {
			try {
				img = ImageIO.read(MapLoader.class.getResource(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}	

		 width = img.getWidth();
		nbrOfPixels = img.getHeight() * img.getWidth();

		distanceBlocks = new int[nbrOfPixels];
		distanceZones = new int[nbrOfPixels];

		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int argb = img.getRGB(x, y);
				if ((argb >> 16 & 0xff) == 153 && (argb >> 8 & 0xff) == 153 && (argb & 0xff) == 102) 
					distanceBlocks[width * y + x] = 1;
				else if ((argb >> 16 & 0xff) == 255 && (argb >> 8 & 0xff) == 255 && (argb & 0xff) == 255) 
					distanceZones[width * y + x] = 1;
			}
		}
		add(distanceBlocks);
		add(distanceZones);
	}

	private int[] add(int[] dBlock) {
		while (true) {//TODO voir pour faire dans un runnable 
			int[] added = new int[nbrOfPixels];

			int modifs = 0;
			for (int n = width; n < nbrOfPixels - width; n++) {
				if (dBlock[n] == 0) {
					int max1 = Math.max(dBlock[n + 1], dBlock[n - 1]);
					int max2 = Math.max(dBlock[n + width], dBlock[n - width]);
					int maxTotal = Math.max(max1, max2);

					if (maxTotal > 0) {
						added[n] = maxTotal + 1;
						modifs++;
					}

				}
			}

			for (int n = width; n < nbrOfPixels - width; n++) {
				if (dBlock[n] == 0)
					dBlock[n] = added[n];
			}

			if (modifs == 0) break;
		}
		return dBlock;
	}

}