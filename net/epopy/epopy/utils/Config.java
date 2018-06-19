package net.epopy.epopy.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import net.epopy.epopy.Main;
import net.epopy.sdk.security.Encryptor;

public class Config {
	
	private final File file;
	private final Map<String, String> infos = new TreeMap<String, String>(), infosCrypt = new TreeMap<String, String>();
	private final Encryptor encryptor;
	
	public Config(final File file) {
		this.file = file;
		encryptor = Main.getEncryptor();
		if (file.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String ligne;
				while ((ligne = br.readLine()) != null) {
					String ligneDecrypt = encryptor.decrypt(ligne);
					infos.put(ligneDecrypt.split("=")[0], ligneDecrypt.split("=")[1]);
					infosCrypt.put(ligneDecrypt.split("=")[0], ligne);
				}
				System.out.println("[CONFIG] Config has been loaded");
			} catch (Exception e) {
				e.printStackTrace();
				if (file.getName().equals("infos.txt")) {
					System.out.println("[CONFIG][ERROR] File : " + file.getPath() + " can't load ! (Le fichier à été modifé)");
					file.delete();
					System.exit(-1);
				}
			}
		} else
			System.out.println("[CONFIG][ERROR] File : " + file.getPath() + " not exist !");
	}
	
	public boolean contains(final String key) {
		return infos.containsKey(key);
	}
	
	public String getData(final String key) {
		if (contains(key))
			return infos.get(key);
		else
			setValue(key, "0");
		return getData(key);
	}

	public String getData(final String key, final String defaultValue) {
		if (contains(key))
			return infos.get(key);
		else
			setValue(key, defaultValue);
		return getData(key);
	}
	
	public void setValue(final String key, final String value) {
		String replace = key + "=" + value;
		String newValue = encryptor.encrypt(replace);
		if (contains(key) && file.exists()) {
			try {
				FileUtils.replaceSelected(infosCrypt.get(key), newValue, getFile().getPath());
				infos.put(key, value);
				infosCrypt.put(key, newValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				infos.put(key, value);
				infosCrypt.put(key, newValue);
				
				FileOutputStream is = new FileOutputStream(getFile().getPath());
				OutputStreamWriter osw = new OutputStreamWriter(is);
				Writer w = new BufferedWriter(osw);
				for (Entry<String, String> info : infos.entrySet()) {
					w.write(encryptor.encrypt(info.getKey() + "=" + info.getValue()));
					w.write("\n");
				}
				System.out.println("[CONFIG] Rajout: " + replace);
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public File getFile() {
		return file;
	}
	
}
