package net.epopy.launcher.utils;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class FileDownload {

	public static double pourcent = 0;
	
	public FileDownload(String filePath, String destination) { 
		pourcent = 0;
		URLConnection connection = null;
		InputStream is = null;
		FileOutputStream destinationFile = null;

		try { 
			//On crée l'URL
			URL url = new URL(filePath);

			//On crée une connection vers cet URL
			connection = url.openConnection();
			connection.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
			connection.setRequestProperty("Accept-Encoding", "identity"); 
			connection.setRequestProperty("Accept-Language", "zh-CN");
			connection.setRequestProperty("Charset", "UTF-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setConnectTimeout(100000);
			connection.setReadTimeout(10000);
			
			//On récupère la taille du fichier
			int length = connection.getContentLength();

			//Si le fichier est inexistant, on lance une exception
			if(length == -1){
				System.out.println("[ERROR] Pas de connexion / ou le fichier est vide (" + filePath+")");
				return;
			}

			//On récupère le stream du fichier
			is = new BufferedInputStream(connection.getInputStream());

			//On prépare le tableau de bits pour les données du fichier
			byte[] data = new byte[length];

			//On déclare les variables pour se retrouver dans la lecture du fichier
			int currentBit = 0;
			int deplacement = 0;

			//Tant que l'on n'est pas à la fin du fichier, on récupère des données
			while(deplacement < length){
				currentBit = is.read(data, deplacement, data.length-deplacement);	
				if(currentBit == -1)break;	
				deplacement += currentBit;
				if(pourcent < 90)
					pourcent+=0.020;
			}

			//Si on n'est pas arrivé à la fin du fichier, on lance une exception
			if(deplacement != length){
				throw new IOException("Le fichier n'a pas été lu en entier (seulement " 
						+ deplacement + " sur " + length + ")");
			}		
			if(pourcent < 100)pourcent=94;
			//On crée un stream sortant vers la destination
			destinationFile = new FileOutputStream(destination); 
			if(pourcent < 100)pourcent=98;
			//On écrit les données du fichier dans ce stream
			destinationFile.write(data);
			if(pourcent < 100)pourcent=99;
			//On vide le tampon et on ferme le stream
			destinationFile.flush();
			pourcent = 100;
		} catch (MalformedURLException e) { 
			System.err.println("Problème avec l'URL : " + filePath); 
		} catch (IOException e) { 
			e.printStackTrace();
		} finally{
			try {
				is.close();
				destinationFile.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
