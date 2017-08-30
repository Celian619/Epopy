package net.epopy.network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
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
		  Process process = new ProcessBuilder("tasklist.exe", "/fo", "csv", "/nh").start();
		    new Thread(() -> {
		        Scanner sc = new Scanner(process.getInputStream());
		        if (sc.hasNextLine()) sc.nextLine();
		        while (sc.hasNextLine()) {
		            String line = sc.nextLine();
		            String[] parts = line.split(",");
		            String unq = parts[0].substring(1).replaceFirst(".$", "");
		            String pid = parts[1].substring(1).replaceFirst(".$", "");
		            
		            	System.out.println(unq + "      " + pid + "      " + parts[5]);
		        }
		    }).start();
		    process.waitFor();
		    System.out.println("Done");
		//new Test();
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
