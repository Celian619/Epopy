package net.epopy.epopy.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Input {

	public static final int KEYBOARD_SIZE = Keyboard.getKeyCount(), MOUSE_SIZE = Mouse.getButtonCount();

	private static boolean[] lastKeys = new boolean[KEYBOARD_SIZE], lastButtons = new boolean[MOUSE_SIZE];
	private static List<Integer> letters;
	private static List<Integer> lettersUTF8;

	public static void update() {
		for (int i = 0; i < KEYBOARD_SIZE; i++)
			lastKeys[i] = isKeyDown(i);

		for (int i = 0; i < MOUSE_SIZE; i++)
			lastButtons[i] = isButtonDown(i);
	}

	public static boolean isAnyKeyDown() {
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
			boolean alt = Keyboard.isKeyDown(29);
			switch (i) {

				case 2:
				return "&";
				case 3:
				return alt ? "~" : "é";
				case 4:
				return alt ? "#" : "\"";
				case 5:
				return alt ? "{" : "'";
				case 6:
				return alt ? "[" : "(";
				case 7:
				return alt ? "|" : "-";
				case 8:
				return alt ? "`" : "è";
				case 9:
				return alt ? "\\" : "_";
				case 10:
				return alt ? "^" : "ç";
				case 11:
				return alt ? "@" : "à";
				case 13:
				return alt ? "}" : "=";
				case 26:
				return alt ? "]" : ")";
				case 27:
				return "^";
				case 39:
				return alt ? "¤" : "$";
				case 41:
				return "ù";
				case 43:
				return "*";

			}

		} else {
			switch (i) {
				case 26:
				return "°";
				case 13:
				return "+";
				case 27:
				return "¨";
				case 39:
				return "£";
				case 41:
				return "%";
				case 43:
				return "*";
			}
		}
		return Keyboard.getKeyName(i);
	}

	public static String getNumpadInput(final Boolean isMaj, final boolean utf8, final int i) {
		switch (i) {
			case Keyboard.KEY_NUMPAD0:
			return "0";
			case Keyboard.KEY_NUMPAD1:
			return "1";
			case Keyboard.KEY_NUMPAD2:
			return "2";
			case Keyboard.KEY_NUMPAD3:
			return "3";
			case Keyboard.KEY_NUMPAD4:
			return "4";
			case Keyboard.KEY_NUMPAD5:
			return "5";
			case Keyboard.KEY_NUMPAD6:
			return "6";
			case Keyboard.KEY_NUMPAD7:
			return "7";
			case Keyboard.KEY_NUMPAD8:
			return "8";
			case Keyboard.KEY_NUMPAD9:
			return "9";

		}

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
		if (lettersUTF8 != null)
			return lettersUTF8;
		if (letters == null)
			getLetters();
		lettersUTF8 = new LinkedList<>(Arrays.asList(new Integer[] { 52, 83, 26, 13, 27, 39, 41, 43 }));
		lettersUTF8.addAll(letters);
		return lettersUTF8;
	}

	public static List<Integer> getLetters() {
		if (letters != null)
			return letters;
		letters = new LinkedList<>(Arrays.asList(new Integer[] { Keyboard.KEY_A, Keyboard.KEY_Z, Keyboard.KEY_E, Keyboard.KEY_R, Keyboard.KEY_T, Keyboard.KEY_Y, Keyboard.KEY_U, Keyboard.KEY_I,
				Keyboard.KEY_O, Keyboard.KEY_P, Keyboard.KEY_Q, Keyboard.KEY_S, Keyboard.KEY_D, Keyboard.KEY_F, Keyboard.KEY_G, Keyboard.KEY_H, Keyboard.KEY_J, Keyboard.KEY_K, Keyboard.KEY_L,
				Keyboard.KEY_M, Keyboard.KEY_W, Keyboard.KEY_X, Keyboard.KEY_C, Keyboard.KEY_V, Keyboard.KEY_B, Keyboard.KEY_N, Keyboard.KEY_1, Keyboard.KEY_2, Keyboard.KEY_3, Keyboard.KEY_4,
				Keyboard.KEY_5, Keyboard.KEY_6, Keyboard.KEY_7, Keyboard.KEY_8, Keyboard.KEY_9, Keyboard.KEY_0, Keyboard.KEY_NUMPAD0, Keyboard.KEY_NUMPAD1, Keyboard.KEY_NUMPAD2, Keyboard.KEY_NUMPAD3,
				Keyboard.KEY_NUMPAD4, Keyboard.KEY_NUMPAD5, Keyboard.KEY_NUMPAD6, Keyboard.KEY_NUMPAD7, Keyboard.KEY_NUMPAD8, Keyboard.KEY_NUMPAD9, 11 }));
				
		return letters;
	}
}
