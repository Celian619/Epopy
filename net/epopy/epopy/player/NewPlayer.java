package net.epopy.epopy.player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import net.epopy.epopy.Main;
import net.epopy.epopy.utils.FileUtils;
import net.epopy.sdk.security.Encryptor;

public class NewPlayer {
	
	public NewPlayer(final String name) {
		
		File profil = new File(FileUtils.PATH_FOLDER + name + ".txt");
		try {
			if (profil.exists()) {
				profil.delete();
				profil.createNewFile();
			}
			FileOutputStream is = new FileOutputStream(profil);
			OutputStreamWriter osw = new OutputStreamWriter(is);
			Writer w = new BufferedWriter(osw);

			String timeStamp = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());
			write(w, "account_create_at=" + timeStamp);
			
			write(w, "level=" + 1);
			
			write(w, "last_game=" + 1);
			
			/*
			 * Mario
			 */
			write(w, "mario_etoile=" + 0);
			write(w, "mario_xp=" + 0);
			
			/*
			 * Snake
			 */
			write(w, "snake_pts=" + 0);
			write(w, "snake_parties=" + 0);
			write(w, "snake_temps=" + 0);
			
			/*
			 * Pong
			 */
			write(w, "ping_record=" + 0);
			write(w, "ping_parties=" + 0);
			write(w, "ping_temps=" + 0);
			
			/*
			 * Car
			 */
			write(w, "car_record=" + 0);
			write(w, "car_parties=" + 0);
			write(w, "car_temps=" + 0);
			/*
			 * Tank
			 */
			write(w, "tank_record=" + 0);
			write(w, "tank_parties=" + 0);
			write(w, "tank_temps=" + 0);

			write(w, "plainv_pts=" + 0);
			write(w, "plainv_parties=" + 0);
			write(w, "plainv_temps=" + 0);

			write(w, "speedrun_record=" + 0);
			write(w, "speedrun_parties=" + 0);
			write(w, "speedrun_temps=" + 0);

			write(w, "sound_volume=5");
			write(w, "sound=true");

			w.close();
			
		} catch (Exception e) {
		
		}
	}
	
	private void write(final Writer w, final String s) {
		Encryptor encryptor = Main.getEncryptor();
		try {
			w.write(encryptor.encrypt(s));
			w.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
