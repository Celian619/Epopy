package net.epopy.network;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class Test implements Runnable {

	private Socket socket;

	private DataOutputStream dataOutputStream;
	private DataInputStream dataInputStream;
	private Thread thread;

	static String urls = "http://puu.sh/xrmge/64e30ae6ed.txt";
	static File path = new File("C:\\Users\\SEVEN\\AppData\\Roaming\\.Epopy\\epopytest.txt");

	public static void main(final String[] args) throws IOException {
		String c = "maxplayer 5";
		System.out.println(c.length() + " = " +  c.charAt(c.length()-3) + c.charAt(c.length()-2) + c.charAt(c.length()-1));
	}
	
	
	public static String getDurationBreakdown(long millis)
	{
		if(millis < 0)
		{
			throw new IllegalArgumentException("Duration must be greater than zero!");
		}

		//  long days = TimeUnit.MILLISECONDS.toDays(millis);
		// millis -= TimeUnit.DAYS.toMillis(days);
		long hours = TimeUnit.MILLISECONDS.toHours(millis);
		millis -= TimeUnit.HOURS.toMillis(hours);
		long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
		millis -= TimeUnit.MINUTES.toMillis(minutes);
		long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);

		StringBuilder sb = new StringBuilder(64);

		sb.append(hours);
		sb.append("h et ");
		sb.append(minutes);
		sb.append("min");

		return(sb.toString());
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
