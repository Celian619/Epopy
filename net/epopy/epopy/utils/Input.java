package net.epopy.epopy.utils;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Input {

	public static final int KEYBOARD_SIZE = Keyboard.getKeyCount();
	public static final int MOUSE_SIZE = Mouse.getButtonCount();

	private static boolean[] lastKeys = new boolean[KEYBOARD_SIZE];
	private static boolean[] lastButtons = new boolean[MOUSE_SIZE];

	public static void update() {
		for (int i = 0; i < KEYBOARD_SIZE; i++)
			lastKeys[i] = isKeyDown(i);

		for (int i = 0; i < MOUSE_SIZE; i++)
			lastButtons[i] = isButtonDown(i);
	}

	public static boolean getAnyKeyDown() {
		for (boolean down : lastKeys)
			if (down) return true;
		return false;
	}

	public static boolean isKeyDown(final int key) {
		return Keyboard.isKeyDown(key);
	}

	public static boolean getKeyDown(final int key) {
		return isKeyDown(key) && !lastKeys[key];
	}

	public static boolean getKeyUp(final int key) {
		return !isKeyDown(key) && lastKeys[key];
	}

	public static boolean isButtonDown(final int button) {
		return Mouse.isButtonDown(button);
	}

	public static boolean getButtonDown(final int button) {
		return isButtonDown(button) && !lastButtons[button];
	}

	public static boolean getButtonUp(final int button) {
		return !isButtonDown(button) && lastButtons[button];
	}

	private static List<Integer> letters = null;
	private static List<Integer> lettersUTF8 = null;

	public static String getKeyName(final int key) {
		if (Keyboard.getKeyName(key).equals("DOWN"))
			return "↓";
		else if (Keyboard.getKeyName(key).equals("UP"))
			return "↑";
		else if (Keyboard.getKeyName(key).equals("RIGHT"))
			return "→";
		else if (Keyboard.getKeyName(key).equals("LEFT"))
			return "←";
		return Keyboard.getKeyName(key);
	}

	public static String getUTF8(final Boolean isMaj, final int i) {
		if (!isMaj) {
			if (2 == i)
				return "&";
			if (3 == i)
				return Keyboard.isKeyDown(29) ? "~" : "é";
			if (4 == i)
				return Keyboard.isKeyDown(29) ? "#" : "\"";
			if (5 == i)
				return Keyboard.isKeyDown(29) ? "{" : "'";
			if (6 == i)
				return Keyboard.isKeyDown(29) ? "[" : "(";
			if (7 == i)
				return Keyboard.isKeyDown(29) ? "|" : "-";
			if (8 == i)
				return Keyboard.isKeyDown(29) ? "`" : "è";
			if (9 == i)
				return Keyboard.isKeyDown(29) ? "\\" : "_";
			if (10 == i)
				return Keyboard.isKeyDown(29) ? "^" : "ç";
			if (11 == i)
				return Keyboard.isKeyDown(29) ? "@" : "à";
			if (26 == i)
				return Keyboard.isKeyDown(29) ? "]" : ")";
			if (13 == i)
				return Keyboard.isKeyDown(29) ? "}" : "=";
			if (27 == i)
				return "^";
			if (39 == i)
				return Keyboard.isKeyDown(29) ? "¤" : "$";
			if (41 == i)
				return "ù";
			if (43 == i)
				return "*";
		} else {
			if (26 == i)
				return "°";
			if (13 == i)
				return "+";
			if (27 == i)
				return "¨";
			if (39 == i)
				return "£";
			if (41 == i)
				return "%";
			if (43 == i)
				return "*";
		}
		return Keyboard.getKeyName(i);
	}

	public static String getNumpadInput(final Boolean isMaj, final boolean utf8, final int i) {

		if (Keyboard.KEY_NUMPAD0 == i)
			return "0";
		if (Keyboard.KEY_NUMPAD1 == i)
			return "1";
		if (Keyboard.KEY_NUMPAD2 == i)
			return "2";
		if (Keyboard.KEY_NUMPAD3 == i)
			return "3";
		if (Keyboard.KEY_NUMPAD4 == i)
			return "4";
		if (Keyboard.KEY_NUMPAD5 == i)
			return "5";
		if (Keyboard.KEY_NUMPAD6 == i)
			return "6";
		if (Keyboard.KEY_NUMPAD7 == i)
			return "7";
		if (Keyboard.KEY_NUMPAD8 == i)
			return "8";
		if (Keyboard.KEY_NUMPAD9 == i)
			return "9";

		// return getUTF8(isMaj, i);
		return utf8 ? getUTF8(isMaj, i) : Keyboard.getKeyName(i);
	}

	public static void checkInputFullscreen() {
		if (Input.isKeyDown(Keyboard.KEY_F11)) {
			try {
				if (Display.isFullscreen()) {
					Display.setDisplayMode(new DisplayMode((int) (1920 / 1.5), (int) (1080 / 1.5)));
					Display.setFullscreen(false);
				} else {
					Display.setDisplayModeAndFullscreen(Display.getDesktopDisplayMode());
					
					Display.setFullscreen(true);
				}
			} catch (LWJGLException e) {
				e.printStackTrace();
			}
		}
	}

	public static List<Integer> getLettersUTF8() {
		if (lettersUTF8 != null && letters != null)
			return lettersUTF8;
		if (letters == null)
			getLetters();
		lettersUTF8 = new LinkedList<>();
		lettersUTF8.add(52);
		lettersUTF8.add(83);
		lettersUTF8.add(26);
		lettersUTF8.add(13);
		lettersUTF8.add(27);
		lettersUTF8.add(39);
		lettersUTF8.add(41);
		lettersUTF8.add(43);
		lettersUTF8.addAll(letters);
		return lettersUTF8;
	}

	public static List<Integer> getLetters() {
		if (letters != null)
			return letters;
		letters = new LinkedList<>();
		letters.add(Keyboard.KEY_A);
		letters.add(Keyboard.KEY_Z);
		letters.add(Keyboard.KEY_E);
		letters.add(Keyboard.KEY_R);
		letters.add(Keyboard.KEY_T);
		letters.add(Keyboard.KEY_Y);
		letters.add(Keyboard.KEY_U);
		letters.add(Keyboard.KEY_I);
		letters.add(Keyboard.KEY_O);
		letters.add(Keyboard.KEY_P);
		letters.add(Keyboard.KEY_Q);
		letters.add(Keyboard.KEY_S);
		letters.add(Keyboard.KEY_D);
		letters.add(Keyboard.KEY_F);
		letters.add(Keyboard.KEY_G);
		letters.add(Keyboard.KEY_H);
		letters.add(Keyboard.KEY_J);
		letters.add(Keyboard.KEY_K);
		letters.add(Keyboard.KEY_L);
		letters.add(Keyboard.KEY_M);
		letters.add(Keyboard.KEY_W);
		letters.add(Keyboard.KEY_X);
		letters.add(Keyboard.KEY_C);
		letters.add(Keyboard.KEY_V);
		letters.add(Keyboard.KEY_B);
		letters.add(Keyboard.KEY_N);
		letters.add(Keyboard.KEY_1);
		letters.add(Keyboard.KEY_2);
		letters.add(Keyboard.KEY_3);
		letters.add(Keyboard.KEY_4);
		letters.add(Keyboard.KEY_5);
		letters.add(Keyboard.KEY_6);
		letters.add(Keyboard.KEY_7);
		letters.add(Keyboard.KEY_8);
		letters.add(Keyboard.KEY_9);
		letters.add(Keyboard.KEY_0);
		letters.add(Keyboard.KEY_NUMPAD0);
		letters.add(Keyboard.KEY_NUMPAD1);
		letters.add(Keyboard.KEY_NUMPAD2);
		letters.add(Keyboard.KEY_NUMPAD3);
		letters.add(Keyboard.KEY_NUMPAD4);
		letters.add(Keyboard.KEY_NUMPAD5);
		letters.add(Keyboard.KEY_NUMPAD6);
		letters.add(Keyboard.KEY_NUMPAD7);
		letters.add(Keyboard.KEY_NUMPAD8);
		letters.add(Keyboard.KEY_NUMPAD9);
		letters.add(11);

		/*
		 * letters.add(52);
		 *
		 * letters.add(83); letters.add(26); letters.add(13); letters.add(27); letters.add(39); letters.add(41); letters.add(43);
		 **/
		return letters;
	}
}
