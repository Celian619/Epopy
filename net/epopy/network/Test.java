package net.epopy.network;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class Test implements Runnable {

	private Socket socket;
	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private Thread thread;

	public static void main(final String[] args) throws IOException, InterruptedException {
		/**	Process proc = Runtime.getRuntime().exec ("tasklist.exe");
		InputStream procOutput = proc.getInputStream ();
		BufferedReader in = new BufferedReader(new InputStreamReader(procOutput));
		String inputLine;
		System.out.println("----URLS----");
		while ((inputLine = in.readLine()) != null) {

			System.out.println(inputLine);
		}*/
		//new Test();

		//download("http://download1767.mediafire.com/3ti634my9lhg/wibhqj1482yvny8/epopy.jar", new File("C:\\Users\\SEVEN\\AppData\\Roaming\\.Epopy\\epopy.jar"));

	}
	public static void download(final String urlString, final File downloaded)
			throws IOException {
		BufferedOutputStream bout = null;
		BufferedInputStream in = null;
		HttpURLConnection connection = null;

		if (downloaded.getParentFile() != null
				&& !downloaded.getParentFile().exists()) {
			downloaded.getParentFile().mkdirs();
		}

		if (downloaded.exists()) {
			downloaded.delete();
		}
		System.out.println("start");
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
				final int percent = (int) ((totalDataRead * 100) / filesize);
				if (percent - oldPercent >= 10) {
					System.out.println("Downloaded at " + percent + "%");
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
		System.out.println("File " + urlString + " downloaded");
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

	private void connect(final String ip, final int port) {
		try {
			socket = new Socket(ip, port);
			socket.setTcpNoDelay(true);

			dataInputStream = new DataInputStream(socket.getInputStream());
			dataOutputStream = new DataOutputStream(socket.getOutputStream());

			thread = new Thread(this);
			thread.start();

		} catch (IOException e) {

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
