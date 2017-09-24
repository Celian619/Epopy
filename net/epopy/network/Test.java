package net.epopy.network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

@SuppressWarnings("unused")
public class Test implements Runnable {

	private Socket socket;
	
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private Thread thread;

	static String urls = "http://puu.sh/xrmge/64e30ae6ed.txt";
	static File path = new File("C:\\Users\\SEVEN\\AppData\\Roaming\\.Epopy\\epopytest.txt");

	public static void main(final String[] args) throws IOException {
		
		// downloadFileFromUrlWithJavaIO(fileName, "https://puu.sh/xrmge/64e30ae6ed.txt");
		TrustManager[] trustAllCerts = new TrustManager[] {
				new X509TrustManager() {
					@Override
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}

					@Override
					public void checkClientTrusted(
							final java.security.cert.X509Certificate[] certs, final String authType) {
					}

					@Override
					public void checkServerTrusted(
							final java.security.cert.X509Certificate[] certs, final String authType) {
					}
				}
		};

		// Activate the new trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
		}

		// And as before now you can use URL and URLConnection
		URL url = new URL("https://www.catupload.com/files/a004c475919feb623fb60d9955c6b342.txt");
		URLConnection connection = url.openConnection();
		InputStream is = connection.getInputStream();

		int length = connection.getContentLength();
		System.out.println("start: " + is.available());

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

		}

		// Si on n'est pas arrivé à la fin du fichier, on lance une exception
		if (deplacement != length) { throw new IOException("Le fichier n'a pas été lu en entier (seulement "
				+ deplacement + " sur " + length + ")"); }
		// On crée un stream sortant vers la destination

		FileOutputStream destinationFile = new FileOutputStream("C:\\Users\\SEVEN\\AppData\\Roaming\\.Epopy\\epopytest.txt");

		destinationFile.write(data);

		destinationFile.flush();
		destinationFile.close();
		
	}

	public static void downloadFileFromUrlWithJavaIO(final String fileName, final String fileUrl)
			throws MalformedURLException, IOException {
		BufferedInputStream inStream = null;
		FileOutputStream outStream = null;
		try {
			URL fileUrlObj = new URL(fileUrl);
			inStream = new BufferedInputStream(fileUrlObj.openStream());
			outStream = new FileOutputStream(fileName);

			byte data[] = new byte[1024];
			int count;
			while ((count = inStream.read(data, 0, 1024)) != -1) {
				outStream.write(data, 0, count);
			}
		} finally {
			if (inStream != null)
				inStream.close();
			if (outStream != null)
				outStream.close();
		}
	}

	public Test() {
		// connect("192.168.1.15", 25565);

		Map<String, Integer> datasPoints = new HashMap<>();
		datasPoints.put("Celian", 1);
		datasPoints.put("Roro", 10);
		datasPoints.put("test", 5);
		datasPoints.put("qzdqzd", 14);
		ValueComparator comparateur = new ValueComparator(datasPoints);
		Map<String, Integer> mapTriee = new TreeMap<String, Integer>(comparateur);
		mapTriee.putAll(datasPoints);
		System.out.println(datasPoints);

		int i = 1;
		for (Entry<String, Integer> datas : mapTriee.entrySet()) {
			if (i > 3)
				break;
			System.out.println("#" + i + " " + datas.getKey() + " " + datas.getValue());
			i++;
		}

	}

	@Override
	public void run() {
		while (socket != null) {
			try {
				String reponse = dataInputStream.readUTF();
				System.out.println(reponse);
			} catch (Exception ex) {
			}
		}

	}

	class ValueComparator implements Comparator<String> {
		Map<String, Integer> base;

		public ValueComparator(final Map<String, Integer> base) {
			this.base = base;
		}

		@Override
		public int compare(final String a, final String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			}
		}
	}
}
