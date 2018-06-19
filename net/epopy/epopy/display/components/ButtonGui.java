package net.epopy.epopy.display.components;

import static org.lwjgl.opengl.EXTFramebufferObject.glGenerateMipmapEXT;
import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_LINEAR_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glScalef;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper.PosHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PosWidth;
import net.epopy.epopy.utils.Input;

public class ButtonGui {

	/**
	 * Quand on switch de button et qu'on reclique sur un autre
	 */
	public static int timeResetMenu = 40, resetMenu = 0;
	
	public boolean canOver = true;
	public int xx = -1, yy;
	public String text;
	public float[] textColor = new float[] { 1, 1, 1, 1 };

	public Textures textureOff, textureOn;
	
	private boolean isClicked, isOn, changeColor = true;
	private int x, y, width, height, ww, hh, textSize = 20;

	private FontUtils font;

	public ButtonGui(final Textures textureOff, final Textures textureOn, int x, int y, final PosWidth posWidth, final PosHeight posHeight, final int width, final int height) {
		isClicked = false;

		if (posHeight == PosHeight.BAS)
			y -= height;
		else if (posHeight == PosHeight.MILIEU)
			y -= height / 2;

		if (posWidth == PosWidth.DROITE)
			x -= width;
		else if (posWidth == PosWidth.MILIEU)
			x -= width / 2;

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.textureOff = textureOff;
		this.textureOn = textureOn;
	}

	public ButtonGui(final Textures textureOff, final Textures textureOn) {
		height = width = y = x = 0;
		this.textureOff = textureOff;
		this.textureOn = textureOn;
	}

	public ButtonGui(final String text, final float[] color, final int textSize) {
		x = y = -1;
		width = text.length() * textSize;
		height = textSize;
		this.text = text;
		this.textSize = textSize;
		textColor = color;
		changeColor = true;
	}

	public ButtonGui(final String text, final float[] color, final int textSize, final boolean change) {
		x = y = -1;
		width = text.length() * textSize;
		height = textSize;
		this.text = text;
		this.textSize = textSize;
		textColor = color;
		changeColor = change;
	}

	public boolean isClicked() {
		if (!canOver)
			return false;
		if (resetMenu == 0) {
			if (isClicked) {
				resetMenu = timeResetMenu;
				isClicked = false;
				return true;
			}
		}

		return false;
	}

	public void setOver(final boolean over) {
		canOver = over;
	}

	public void update() {
		if (resetMenu > 0)
			resetMenu--;

		int mx = Mouse.getX();
		int my = Display.getHeight() - Mouse.getY();
		int x2 = x + width;
		int y2 = y + height;

		if (mx >= x && mx < x2 && my >= y && my < y2) {
			isOn = true;

			if (resetMenu == 0) {
				if (Input.getButtonDown(0)) {
					isClicked = true;
				}
			}
			if (Input.getButtonUp(0)) {
				if (isClicked)
					isClicked = false;
			}
		} else {
			isOn = false;
			isClicked = false;
		}
	}

	public boolean isOn() {
		return isOn;
	}

	public void update(int x, int y, final PosWidth posWidth, final PosHeight posHeight) {
		if (font == null) {
			if (ComponentsHelper.fonts.containsKey(textSize))
				font = ComponentsHelper.fonts.get(textSize);
			else
				font = new FontUtils(textSize, "Impact");
		}
		if (xx == -1) {
			int width = 0;
			for (char c : text.toCharArray())
				width += font.getCharWidth(c);

			if (posHeight == PosHeight.BAS)
				y -= textSize;
			else if (posHeight == PosHeight.MILIEU)
				y -= textSize / 2;

			if (posWidth == PosWidth.DROITE)
				x -= width;
			else if (posWidth == PosWidth.MILIEU)
				x -= width / 2;

			xx = x;
			yy = y;
			ww = width;
			hh = textSize;
		}

		this.x = (int) ComponentsHelper.getResponsiveX(xx);
		this.y = (int) ComponentsHelper.getResponsiveY(yy);
		width = (int) ComponentsHelper.getResponsiveX(ww);
		height = (int) ComponentsHelper.getResponsiveY(hh);

		update();
	}

	public void update(int x, int y, final PosWidth posWidth, final PosHeight posHeight, final int width, final int height) {
		if (posHeight == PosHeight.BAS)
			y -= height;
		else if (posHeight == PosHeight.MILIEU)
			y -= height / 2;

		if (posWidth == PosWidth.DROITE)
			x -= width;
		else if (posWidth == PosWidth.MILIEU)
			x -= width / 2;
		if (xx == -1 || xx != x || yy != y || ww != width || hh != height) {
			xx = x;
			yy = y;
			ww = width;
			hh = height;
		}

		this.x = (int) ComponentsHelper.getResponsiveX(xx);
		this.y = (int) ComponentsHelper.getResponsiveY(yy);
		this.width = (int) ComponentsHelper.getResponsiveX(ww);
		this.height = (int) ComponentsHelper.getResponsiveY(hh);
		update();
	}

	public void setX(final int x) {
		xx = x;
		this.x = (int) ComponentsHelper.getResponsiveX(x);
	}

	public void setClicked(final boolean clicked) {
		isClicked = clicked;
	}

	public void render() {
		if (xx == -1)
			return;

		if (text == null) {
			if (isOn)
				textureOn.bind();
			else
				textureOff.bind();
			glGenerateMipmapEXT(GL_TEXTURE_2D);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
			glMatrixMode(GL_TEXTURE);
			glLoadIdentity();
			glScalef(1, 1, 1);
			glMatrixMode(GL_MODELVIEW);

			glBegin(GL_QUADS);
			glTexCoord2f(0, 0);
			glVertex2f(x, y);
			glTexCoord2f(1, 0);
			glVertex2f(x + width, y);
			glTexCoord2f(1, 1);
			glVertex2f(x + width, y + height);
			glTexCoord2f(0, 1);
			glVertex2f(x, y + height);
			glEnd();
			glBindTexture(GL_TEXTURE_2D, 0);
		} else {
			if (changeColor) {
				if (canOver) {
					if (isOn)
						textColor[3] = 1;
					else
						textColor[3] = 0.6f;
				}
				ComponentsHelper.drawText(text, xx, yy, isOn ? (int) (textSize * 1.02) : textSize, textColor);

			} else {
				if (canOver) {
					if (isOn)
						textColor[3] = 0.7f;
					else
						textColor[3] = 1;
				}
				ComponentsHelper.drawText(text, xx, yy, textSize, textColor);
			}
		}
	}

	public void setText(final String text) {
		this.text = text;
	}

}
