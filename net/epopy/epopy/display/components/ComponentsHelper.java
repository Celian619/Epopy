package net.epopy.epopy.display.components;

import static org.lwjgl.opengl.GL11.GL_LINES;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLineWidth;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glPopMatrix;
import static org.lwjgl.opengl.GL11.glPushMatrix;
import static org.lwjgl.opengl.GL11.glRotatef;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTranslatef;
import static org.lwjgl.opengl.GL11.glVertex2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.games.gestion.AbstractGameMenu;

public class ComponentsHelper {

	private static String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "0123456789.;,:=+-'►✔✖*/(\\()!?@ ";

	public static void drawQuadData(int x, int y, int width, int height, final Vector4f color) {
		x = getResponsiveX(x);
		y = getResponsiveY(y);
		width = getResponsiveX(width);
		height = getResponsiveY(height);

		glColor4f(color.x, color.y, color.z, color.w);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glColor4f(1, 1, 1, 1);
		glEnd();
	}

	public static void drawQuadData(int x, int y, int width, int height, final float[] color) {
		x = getResponsiveX(x);
		y = getResponsiveY(y);
		width = getResponsiveX(width);
		height = getResponsiveY(height);

		glColor4f(color[0], color[1], color[2], color[3]);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glColor4f(1, 1, 1, 1);
		glEnd();
	}

	public static void drawCircle(final double x, final double y, final int radius, final int cote) {
		drawCircle(x, y, radius, cote, new float[] { 1, 1, 1, 1 }, 0);
	}
	
	public static void drawCircle(final double x, final double y, final int radius, final int cote, final float[] color) {
		drawCircle(x, y, radius, cote, color, 0);
	}

	public static void drawCircle(final double x, final double y, final int radius, final int cote, final int rotation) {
		drawCircle(x, y, radius, cote, new float[] { 1, 1, 1, 1 }, rotation);
	}

	public static void drawCircle(final double x, final double y, int radius, final int cote, final float[] color, final int rotation) {
		radius = getResponsiveX(radius);

		glColor4f(color[0], color[1], color[2], color[3]);
		glPushMatrix();
		glTranslatef(getResponsiveX(x), getResponsiveY(y), 0);
		glScalef(radius, radius, 1);
		glRotatef(rotation, 0, 0, 1);

		glBegin(GL11.GL_TRIANGLE_FAN);
		glVertex2f(0, 0);
		for (int i = 0; i <= cote; i++) { // NUM_PIZZA_SLICES decides how round the circle looks.
			double angle = Math.PI * 2 * i / cote;
			glVertex2f((float) Math.cos(angle), (float) Math.sin(angle));
		}
		glEnd();
		glPopMatrix();
		glColor4f(1, 1, 1, 1);
	}

	public static void drawString(String msg, final double x, final double y, int size, final float[] color) {
		size = getResponsiveString(size);
		msg = msg.toUpperCase();
		// TextureUtils.FONT.bind();
		glColor4f(color[0], color[1], color[2], color[3]);
		glBegin(GL_QUADS);
		for (int i = 0; i < msg.length(); i++)
			drawChar(msg.charAt(i), getResponsiveX(x) + i * (int) (size * (7.0f / 8.0f)), getResponsiveY(y), size);
		glColor4f(1, 1, 1, 1);
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
	}

	public static Map<Integer, FontUtils> fonts = new HashMap<>(10);
	
	public static int drawText(final String msg, final double x, final double y, final PositionWidth posWidth, final PositionHeight posHeight, final int size) {
		return drawText(msg, x, y, posWidth, posHeight, size, new float[] { 1, 1, 1, 1 });
	}

	public static int drawText(final String msg, final double x, final double y, final int size, final float[] color) {
		return drawText(msg, x, y, PositionWidth.GAUCHE, PositionHeight.HAUT, size, color);
	}

	public static int drawText(final String msg, final double x, final double y, final int size) {
		return drawText(msg, x, y, PositionWidth.GAUCHE, PositionHeight.HAUT, size, new float[] { 1, 1, 1, 1 });
	}

	public static int drawText(final String msg, final double x, final double y, final PositionWidth posWidth, final PositionHeight posHeight, final int size, final float[] color) {
		return drawText(msg, x, y, posWidth, posHeight, size, color, true);
	}

	public static int drawText(final String msg, double x, double y, final PositionWidth posWidth, final PositionHeight posHeight, int size, final float[] color, final boolean resize) {
		if (resize) {
			x = getResponsiveX(x);
			y = getResponsiveY(y);
		}
		size = getResponsiveX(size);

		if (!fonts.containsKey(size))
			fonts.put(size, new FontUtils(size, "Impact"));

		FontUtils font = fonts.get(size);

		double msgHeight = font.getCharHeight();
		double msgWidth = 0;

		for (char c : msg.toCharArray())
			msgWidth += font.getCharWidth(c);

		if (posHeight == PositionHeight.BAS)
			y -= msgHeight;
		else if (posHeight == PositionHeight.MILIEU)
			y -= msgHeight / 2;

		if (posWidth == PositionWidth.DROITE)
			x -= msgWidth;
		else if (posWidth == PositionWidth.MILIEU)
			x -= msgWidth / 2;

		glColor4f(color[0], color[1], color[2], color[3]);
		int lastX = font.drawText(msg, (int) x, (int) y);
		glColor4f(1, 1, 1, 1);

		return lastX;
	}

	private static void drawChar(final int character, int x, int y, final int size) {
		x = getResponsiveX(x);
		y = getResponsiveY(y);

		int xo = chars.indexOf(character) % 26;
		int yo = chars.indexOf(character) / 26;

		glTexCoord2f(xo / 26.0f, yo / 6.0f);
		glVertex2f(x, y);
		glTexCoord2f((xo + 1) / 26.0f, yo / 6.0f);
		glVertex2f(x + size, y);
		glTexCoord2f((xo + 1) / 26.0f, (yo + 1) / 6.0f);
		glVertex2f(x + size, y + size);
		glTexCoord2f(xo / 26.0f, (yo + 1) / 6.0f);
		glVertex2f(x, y + size);
	}

	public static void drawQuad(final int x, final int y, final int width, final int height) {
		drawQuad(x, y, width, height, new float[] { 1, 1, 1, 1 });

	}

	public static void drawQuad(int x, int y, int width, int height, final float[] color) {

		x = getResponsiveX(x);
		y = getResponsiveY(y);
		width = getResponsiveX(width);
		height = getResponsiveY(height);

		glColor4f(color[0], color[1], color[2], color[3]);
		glBegin(GL_QUADS);
		glVertex2f(x, y);
		glVertex2f(x + width, y);
		glVertex2f(x + width, y + height);
		glVertex2f(x, y + height);
		glEnd();
		glColor4f(1, 1, 1, 1);
	}

	public static void renderTexture(final Textures textureUtils, final double x, final double y, final int width, final int height) {
		renderTexture(textureUtils, x, y, width, height, 0, false);
	}

	public static void renderTexture(final Textures textureUtils, final double x, final double y, final int width, final int height, final int rotation) {
		renderTexture(textureUtils, x, y, width, height, rotation, false);
	}

	public static void renderTexture(final Textures textureUtils, double x, double y, int width, int height, final int rotation, final boolean loopBorder) {
		x = getResponsiveX(x);
		y = getResponsiveY(y);
		width = getResponsiveX(width);
		height = getResponsiveY(height);
		boolean other = false;
		boolean otherX = false;
		boolean otherY = false;
		do {
			glTranslatef((int) (x + width / 2), (int) (y + height / 2), 0);
			glRotatef(rotation, 0, 0, 1);
			
			textureUtils.bind();
			glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex3f(-width / 2, -height / 2, 0);
			glTexCoord2f(1, 0);
			glVertex3f(width / 2, -height / 2, 0);
			glTexCoord2f(1, 1);
			glVertex3f(width / 2, height / 2, 0);
			glTexCoord2f(0, 1);
			glVertex3f(-width / 2, height / 2, 0);
			glEnd();
			glBindTexture(GL_TEXTURE_2D, 0);
			glLoadIdentity();
			
			other = false;
			if (!otherX) {
				if (x + width > AbstractGameMenu.defaultWidth) {
					x -= AbstractGameMenu.defaultWidth;
					otherX = other = true;
					
				} else if (x - width < 0) {
					x += AbstractGameMenu.defaultWidth;
					otherX = other = true;
				}
			}
			
			if (!otherY) {
				if (y + height > AbstractGameMenu.defaultHeight) {
					y -= AbstractGameMenu.defaultHeight;
					otherY = other = true;
				} else if (y - height < 0) {
					y += AbstractGameMenu.defaultHeight;
					otherY = other = true;
				}
			}
			if (loopBorder && (otherX || otherY)) {
				System.out.println(otherX + "   " + otherY);
			}
		} while (other && loopBorder);
		
	}

	public static void drawLine(final int x1, final int y1, final int x2, final int y2, final float width) {
		drawLine(x1, y1, x2, y2, width, new float[] { 1, 1, 1, 1 });
	}

	public static void drawLine(double x1, double y1, double x2, double y2, float width, final float[] color) {
		x2 = getResponsiveX(x2);
		y2 = getResponsiveY(y2);
		x1 = getResponsiveX(x1);
		y1 = getResponsiveY(y1);
		width = getResponsiveString(width);
		glColor4f(color[0], color[1], color[2], color[3]);
		glLineWidth(width);
		glBegin(GL_LINES);
		glVertex2f((int) x1, (int) y1);
		glVertex2f((int) x2, (int) y2);
		glColor4f(1, 1, 1, 1);
		glEnd();
	}

	public static int getResponsiveString(final double size) {// Height /2 car - modifie par la size
		return (int) (size / (AbstractGameMenu.defaultWidth * (AbstractGameMenu.defaultHeight / 2)) * Display.getWidth() * (Display.getHeight() / 2) * 1.33);
	}

	public static int getResponsiveX(final double size) {
		return (int) (size / AbstractGameMenu.defaultWidth * Display.getWidth());
	}

	public static int getResponsiveX(final double size, final int w) {
		return (int) (size / w * Display.getWidth());
	}

	public static int getResponsiveY(final double size) {
		return (int) (size / AbstractGameMenu.defaultHeight * Display.getHeight());
	}

	public enum PositionHeight {
		BAS(),
		HAUT(),
		MILIEU();
	}

	public enum PositionWidth {
		DROITE(),
		GAUCHE(),
		MILIEU();
	}

}
