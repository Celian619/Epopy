package net.epopy.launcher;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JWindow;

import net.epopy.launcher.utils.FileDownload;
import net.epopy.launcher.utils.Gif;
import net.epopy.launcher.utils.Version;
import net.epopy.sdk.security.Encryptor;

public class EpopyLauncher {
	
	private static String URL_JAR;
	private static String URL_VERSION;
	private static String PATH_FOLDER;
	private static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
	
	public static String VERSION = "Bêta-1.0.0.0";
	public static String URL_PARTENAIRES;
	
	// encryp
	private static String KEY = "E1BB465D57CAE7ACDBBE80919CE83DF";
	private static String ALGORITMO = "AES/CBC/PKCS5Padding";
	private static String CODIFICACION = "UTF-8";
	private static Encryptor encryptor = new Encryptor(KEY, ALGORITMO, CODIFICACION);;
	
	public static void main(final String[] args) {
		initFiles();
		
		new EpopyLauncher(PATH_FOLDER);
	}
	
	public EpopyLauncher(final String PATH_FOLDER) {
		
		/**
		 * Quand on aurra un server ou mettre sur une dropbox URL = "http://eroz.pe.hu/retrogames/"; URL_VERSION = URL + SYSTEM_NAME +
		 * "/version.txt"; URL_JAR = URL + SYSTEM_NAME + "/retrogames.jar";
		 */
		try {
			URL url = new URL("http://epopy.fr/urls.html");
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			String inputLine;
			System.out.println("----URLS----");
			while ((inputLine = in.readLine()) != null) {
				if (inputLine.contains("jar")) {
					inputLine = inputLine.replace("</br>", "");
					URL_JAR = inputLine.replace("jar=", "");
					System.out.println("Jar url = " + URL_JAR);
				} else if (inputLine.contains("version")) {
					inputLine = inputLine.replace("</br>", "");
					URL_VERSION = inputLine.replace("version=", "");
					System.out.println("Version url = " + URL_VERSION);
				} else if (inputLine.contains("partenaire")) {
					URL_PARTENAIRES = inputLine.replace("partenaire=", "");
					System.out.println("Partenaire url = " + URL_PARTENAIRES);
				}
			}
			in.close();
		} catch (MalformedURLException e) {
			URL_JAR = "No connetion";
		} catch (IOException e) {
			URL_JAR = "No connetion";
		}
		
		File jar = new File(PATH_FOLDER + "/epopy.jar");
		File downloadedFile = new File(PATH_FOLDER + "/download.txt");
		
		if (URL_JAR.equals("null") && !jar.exists()) {
			showWindowsError("url_null");
			return;
		}
		
		try {
			long startUpdate = System.currentTimeMillis();
			/**
			 * Current version
			 */
			
			Version currentVersionFile = new Version(PATH_FOLDER + "/version.txt");
			currentVersionFile.delete();
			String currentVersion = currentVersionFile.getVersion();
			
			/*
			 * Future version
			 */
			FileDownload.download(URL_VERSION, new File(PATH_FOLDER + "/version.txt"));
			// new FileDownload(URL_VERSION, PATH_FOLDER + "/version.txt");
			Version newVersionFile = new Version(PATH_FOLDER + "/version.txt");
			String newVersion = newVersionFile.getVersion();
			VERSION = newVersion;
			System.out.println("[VERSIONS]");
			System.out.println("  Current version: " + currentVersion);
			System.out.println("  Server version: " + newVersion);
			
			boolean needUpdate = !currentVersion.equals(newVersion);
			if (jar == null || !jar.exists())
				needUpdate = true;
			System.out.println("  Need update: " + needUpdate);
			System.out.println("");
			System.out.println("[UPDATE]");
			
			if (needUpdate) {
				if (jar.exists())
					jar.delete();
					
				if (!downloadedFile.exists())
					downloadedFile.createNewFile();
					
				Gif.frame();
				
				long start = System.currentTimeMillis();
				// new FileDownload(URL_JAR, PATH_FOLDER + "/epopy.jar");
				
				FileDownload.download(URL_JAR, new File(PATH_FOLDER + "/epopy.jar"));
				
				downloadedFile.delete();
				
				String time = timeFormat.format(Calendar.getInstance().getTimeInMillis() - start - 3600000);
				System.out.println("  Télechargement 'epopy.jar': " + time);
				jar = new File(PATH_FOLDER + "/epopy.jar");
			}
			String timeUpdate = timeFormat.format(Calendar.getInstance().getTimeInMillis() - startUpdate - 3600000);
			System.out.println("  Update: " + timeUpdate);
		} catch (Exception e) {
		}
		
		try {
			if (jar.exists()) {
				if (!downloadedFile.exists()) {
					Desktop.getDesktop().open(jar);
					System.exit(-1);
				} else {
					jar.delete();
					Gif.frame();
					FileDownload.download(URL_JAR, new File(PATH_FOLDER + "/epopy.jar"));
					downloadedFile.delete();
					Desktop.getDesktop().open(jar);
					System.exit(-1);
				}
			} else {
				showWindowsError("no_connexion");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private static void showWindowsError(final String image) {
		JWindow frame = new JWindow();
		frame.setSize(1000, 630);
		
		try {
			JButton button = new JButton(new ImageIcon(ImageIO.read(Gif.class.getResource("/net/epopy/launcher/quitter.png"))));
			button.setBounds(980, 0, 20, 20);
			button.setBorder(BorderFactory.createEmptyBorder());
			button.setContentAreaFilled(false);
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(final MouseEvent e) {
					System.exit(0);
				}
			});
			frame.add(button);
			
			// background
			frame.add(new JLabel(new ImageIcon(ImageIO.read(Gif.class.getResource("/net/epopy/launcher/" + image + ".png")))));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(EpopyLauncher.class.getResource("/net/epopy/launcher/logo.png")));
		
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public static FileOutputStream input;
	
	private static void initFiles() {
		String folderName = ".Epopy";
		String FileFolder = System.getenv("APPDATA") + "\\" + folderName;
		String os = System.getProperty("os.name").toUpperCase();
		
		String system = "no found !";
		if (os.contains("WIN")) {
			FileFolder = System.getenv("APPDATA") + "\\" + folderName;
			system = "Windows";
		} else if (os.contains("MAC")) {
			FileFolder = System.getProperty("user.home") + "/Library/Application " + "Support" + folderName;
			system = "Mac";
		} else if (os.contains("NUX")) {
			FileFolder = System.getProperty("user.dir") + "." + folderName;
			system = "Linux";
		}
		
		System.out.println("[SYSTEM] System name: " + system);
		
		File directory = new File(FileFolder);
		PATH_FOLDER = directory.getPath() + "/";
		
		if (directory.exists())
			System.out.println("[SYSTEM] Folder '.Epopy' was found !");
		else {
			directory.mkdir();
			System.out.println("[SYSTEM] Folder '.Epopy' has been created !");
		}
		
		/*
		 * Infos.txt
		 */
		File infos = null;
		try {
			infos = new File(PATH_FOLDER);
			if (infos.createNewFile()) {
				System.out.println("[SYSTEM] File 'infos.txt' has been created !");
				try {
					FileOutputStream is = new FileOutputStream(infos);
					OutputStreamWriter osw = new OutputStreamWriter(is);
					Writer w = new BufferedWriter(osw);
					w.write(encryptor.encrypt("keepConnexion=true"));
					w.close();
				} catch (IOException e) {
					System.err.println("Problem writing to the file info.txt");
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("[SYSTEM] File 'infos.txt' was found !");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			/*
			 * Lock.txt
			 */
			File lock = null;
			lock = new File(PATH_FOLDER + "lockLauncher.txt");
			lock.createNewFile();
			input = new FileOutputStream(lock);
			try {
				if (input.getChannel().tryLock() == null) {
					JOptionPane.showMessageDialog(null, "Une autre fenêtre est déjà lancée !", "Epopy", JOptionPane.WARNING_MESSAGE);
					System.out.println("\n\n\nAn other instance is ON ! (EXIT)");
					System.exit(0);
				}
			} catch (IOException ie) {
				ie.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(" ");
	}
}
