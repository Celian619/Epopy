package net.epopy.epopy.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;

import net.epopy.epopy.Main;

public class FileUtils {

	public static String PATH_FOLDER;
	public static String PATH_INFOS;
	public static String SYSTEM_NAME;
	public static String version;

	public static FileOutputStream input;

	public static void checkFiles() {

		String folderName = ".Epopy";
		String FileFolder = System.getenv("APPDATA") + "\\" + folderName;
		String os = System.getProperty("os.name").toUpperCase();

		String system = "no found !";
		if (os.contains("WIN")) {
			SYSTEM_NAME = "Win";
			FileFolder = System.getenv("APPDATA") + "\\" + folderName;
			system = "Windows";
		} else if (os.contains("MAC")) {
			SYSTEM_NAME = "Mac";
			FileFolder = System.getProperty("user.home") + "/Library/Application " + "Support" + folderName;
			system = "Mac";
		} else if (os.contains("NUX")) {
			SYSTEM_NAME = "Linux";
			FileFolder = System.getProperty("user.dir") + "." + folderName;
			system = "Linux";
		}

		System.out.println("[SYSTEM] System name: " + system);

		File directory = new File(FileFolder);
		PATH_FOLDER = directory.getPath() + "/";
		PATH_INFOS = PATH_FOLDER + "infos.txt";

		/**try {
			File lock = new File(PATH_FOLDER + "lockEpopy.txt");
			lock.createNewFile();
			input = new FileOutputStream(lock);
			
			if (input.getChannel().tryLock() == null) {
				JOptionPane.showMessageDialog(null, "Une autre fenêtre est déjà lancée !", "Epopy", JOptionPane.WARNING_MESSAGE);
				System.out.println("\n\n\nAn other instance is ON ! (EXIT)");
				System.exit(0);
			}

		} catch (IOException e) {
		}*/
		
		File version = new File(PATH_FOLDER + "version.txt");
		if (version.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(PATH_FOLDER + "version.txt"))) {
				String ligne;
				while ((ligne = br.readLine()) != null) {
					Main.setVersion(ligne);
					break;
				}
				br.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else Main.setVersion("-1");

		if (directory.exists()) {
			System.out.println("[SYSTEM] Folder '.Epopy' was found !");
		} else {
			directory.mkdir();
			System.out.println("[SYSTEM] Folder '.Epopy' has been created !");
		}

	}

	public static void replaceSelected(final String replace, final String value, final String path) {
		try {
			BufferedReader file = new BufferedReader(new FileReader(path));
			String line;
			String lineR = "null";
			StringBuffer inputBuffer = new StringBuffer();

			while ((line = file.readLine()) != null) {
				inputBuffer.append(line);
				inputBuffer.append('\n');
				if (line.contains(replace))
					lineR = line;
			}
			String inputStr = inputBuffer.toString();
			file.close();

			inputStr = inputStr.replace(lineR, value);

			FileOutputStream fileOut = new FileOutputStream(path);
			fileOut.write(inputStr.getBytes());
			fileOut.close();
		} catch (Exception e) {
			System.out.println("Problem reading file.");
		}
	}

}