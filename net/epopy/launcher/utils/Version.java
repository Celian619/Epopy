package net.epopy.launcher.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class Version {

	private final File file;
	private String version = "0.0.0";

	public Version(final String path) {
		File fileVersionCurrent = new File(path);
		if (fileVersionCurrent.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(fileVersionCurrent))) {
				version = br.readLine();
			} catch (Exception e) {
			}
		}
		
		file = fileVersionCurrent;
	}

	public String getVersion() {
		return version;
	}

	public void delete() {
		if (file != null && file.exists())
			file.delete();
	}
}
