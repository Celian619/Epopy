package net.epopy.launcher.utils;

import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.imageio.ImageIO;

public class FileDownload {
	
	public static double pourcent = 0;
	
	public static Image getImage(final String filePath) {
		BufferedInputStream in = null;
		HttpURLConnection connection = null;
		
		try {
			final URL url = new URL(filePath);
			connection = (HttpURLConnection) url.openConnection();
			// connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			
			final int filesize = connection.getContentLength();
			float totalDataRead = 0;
			in = new BufferedInputStream(connection.getInputStream());
			final byte[] data = new byte[connection.getContentLength()];
			int i = 0;
			int oldPercent = 0;
			while ((i = in.read(data, 0, data.length)) >= 0) {
				totalDataRead = totalDataRead + i;
				final int percent = (int) (totalDataRead * 100 / filesize);
				if (percent - oldPercent >= 1) {
					System.out.println("Downloaded at " + percent + "%");
					pourcent = percent;
					oldPercent = percent;
				}
			}
			return ImageIO.read(new ByteArrayInputStream(data));
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (connection != null) {
				connection.disconnect();
			}
		}
		return null;
	}
	
	public static void download(final String urlString, final File downloaded) throws IOException {
		pourcent = 0;
		BufferedOutputStream bout = null;
		BufferedInputStream in = null;
		HttpURLConnection connection = null;
		
		if (downloaded.getParentFile() != null
				&& !downloaded.getParentFile().exists()) {
			downloaded.getParentFile().mkdirs();
		}
		
		if (downloaded.exists())
			downloaded.delete();
			
		try {
			final URL url = new URL(urlString);
			connection = (HttpURLConnection) url.openConnection();
			final int filesize = connection.getContentLength();
			float totalDataRead = 0;
			in = new BufferedInputStream(connection.getInputStream());
			final FileOutputStream fos = new FileOutputStream(downloaded);
			final byte[] data = new byte[1024];
			bout = new BufferedOutputStream(fos, data.length);
			int i = 0;
			int oldPercent = 0;
			while ((i = in.read(data, 0, data.length)) >= 0) {
				totalDataRead = totalDataRead + i;
				bout.write(data, 0, i);
				final int percent = (int) (totalDataRead * 100 / filesize);
				if (percent - oldPercent >= 1) {
					System.out.println("Downloaded at " + percent + "%");
					pourcent = percent;
					oldPercent = percent;
				}
			}
		} finally {
			if (bout != null) {
				bout.close();
			}
			if (in != null) {
				in.close();
			}
			if (connection != null) {
				connection.disconnect();
			}
		}
	}
}
