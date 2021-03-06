package net.epopy.epopy.player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.lwjgl.input.Keyboard;

import net.epopy.epopy.Main;
import net.epopy.epopy.utils.FileUtils;

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
			write(w, "display_fullscreen=false");
			
			write(w, "display_x=" + 0);
			write(w, "display_y=" + 0);
			write(w, "display_width=" + (int) (1920 / 1.5));
			write(w, "display_height=" + (int) (1080 / 1.5));
			
			write(w, "configUpgrade=" + 1);
			
			write(w, "level=" + 10);// TODO Modifier ceci !
			
			write(w, "last_game=" + 1);
			
			for (String str : new String[] { "snake", "ping", "car", "tank", "plainv", "speedrun", "tetras" }) {
				write(w, str + "_record=0");
				write(w, str + "_parties=0");
				write(w, str + "_temps=0");
			}
			
			write(w, "ping_control_bas=" + String.valueOf(Keyboard.KEY_DOWN));
			write(w, "ping_control_haut=" + String.valueOf(Keyboard.KEY_UP));
			write(w, "ping_control_mouse=2");
			
			write(w, "car_control_gauche=" + String.valueOf(Keyboard.KEY_LEFT));
			write(w, "car_control_droite=" + String.valueOf(Keyboard.KEY_RIGHT));
			
			write(w, "snake_control_bas=" + String.valueOf(Keyboard.KEY_DOWN));
			write(w, "snake_control_haut=" + String.valueOf(Keyboard.KEY_UP));
			write(w, "snake_control_gauche=" + String.valueOf(Keyboard.KEY_LEFT));
			write(w, "snake_control_droite=" + String.valueOf(Keyboard.KEY_RIGHT));
			
			write(w, "tank_control_bas=" + String.valueOf(Keyboard.KEY_DOWN));
			write(w, "tank_control_haut=" + String.valueOf(Keyboard.KEY_UP));
			
			write(w, "placeinvader_control_gauche=" + String.valueOf(Keyboard.KEY_LEFT));
			write(w, "placeinvader_control_droite=" + String.valueOf(Keyboard.KEY_RIGHT));
			
			write(w, "speedrun_control_sneak=" + String.valueOf(Keyboard.KEY_DOWN));
			write(w, "speedrun_control_jump=" + String.valueOf(Keyboard.KEY_UP));
			
			write(w, "tetras_control_bas=" + String.valueOf(Keyboard.KEY_DOWN));
			write(w, "tetras_control_gauche=" + String.valueOf(Keyboard.KEY_LEFT));
			write(w, "tetras_control_droite=" + String.valueOf(Keyboard.KEY_RIGHT));
			
			write(w, "sound_volume=5");
			write(w, "sound=true");
			
			w.close();
			
		} catch (Exception e) {
		
		}
	}
	
	private void write(final Writer w, final String s) {
		try {
			w.write(Main.getEncryptor().encrypt(s));
			w.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
