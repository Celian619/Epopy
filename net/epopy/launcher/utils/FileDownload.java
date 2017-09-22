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

	/*public FileDownload(final String filePath, final String destination) {
		pourcent = 0;
		URLConnection connection = null;
		InputStream is = null;
		FileOutputStream destinationFile = null;

		try {
			// On crée l'URL
			URL url = new URL(filePath);

			// On crée une connection vers cet URL
			connection = url.openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

			// On récupère la taille du fichier
			int length = connection.getContentLength();

			// Si le fichier est inexistant, on lance une exception
			if (length == -1) {
				System.out.println("[ERROR] Pas de connexion / ou le fichier est vide (" + filePath + ")");
				return;
			}

			// On récupère le stream du fichier
			is = new BufferedInputStream(connection.getInputStream());

			// On prépare le tableau de bits pour les données du fichier
			byte[] data = new byte[length];

			// On déclare les variables pour se retrouver dans la lecture du fichier
			int currentBit = 0;
			int deplacement = 0;

			// Tant que l'on n'est pas à la fin du fichier, on récupère des données
			while (deplacement < length) {
				currentBit = is.read(data, deplacement, data.length - deplacement);
				if (currentBit == -1) break;
				deplacement += currentBit;
				if (pourcent < 90)
					pourcent += 0.020;
			}

			// Si on n'est pas arrivé à la fin du fichier, on lance une exception
			if (deplacement != length) { throw new IOException("Le fichier n'a pas été lu en entier (seulement "
					+ deplacement + " sur " + length + ")"); }
			if (pourcent < 100) pourcent = 94;
			// On crée un stream sortant vers la destination
			destinationFile = new FileOutputStream(destination);
			if (pourcent < 100) pourcent = 98;
			// On écrit les données du fichier dans ce stream
			destinationFile.write(data);
			if (pourcent < 100) pourcent = 99;
			// On vide le tampon et on ferme le stream
			destinationFile.flush();
			pourcent = 100;
		} catch (MalformedURLException e) {
			System.err.println("Problème avec l'URL : " + filePath);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				destinationFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}*/

	public static Image getImage(final String filePath) {
		BufferedInputStream in = null;
		HttpURLConnection connection = null;

		try {
			final URL url = new URL(filePath);
			connection = (HttpURLConnection) url.openConnection();
			//	connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");

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
