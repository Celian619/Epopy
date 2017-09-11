package net.epopy.epopy.display.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex2f;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import net.epopy.epopy.display.Textures;
import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.utils.Input;

public class ButtonGui {

	/**
	 * Quand on switch de button et qu'on reclique sur un autre
	 */
	public static int timeResetMenu = 90;
	public static int resetMenu = 0;

	private boolean isClicked;
	private int x;
	private int y;
	private int width;
	private int height;
	public int xx = -1;
	public int yy;
	private int ww;
	private int hh;
	public Textures textureOff;
	public Textures textureOn;
	private boolean isOn = false;

	public String text = null;
	private int textSize = 20;
	public float[] textColor = new float[] { 1, 1, 1, 1 };
	private boolean changeColor = true;
	private FontUtils font;

	public ButtonGui(final Textures textureOff, final Textures textureOn, int x, int y, final PositionWidth posWidth, final PositionHeight posHeight, final int width, final int height) {
		isClicked = false;

		if (posHeight == PositionHeight.BAS)
			y -= height;
		else if (posHeight == PositionHeight.MILIEU)
			y -= height / 2;

		if (posWidth == PositionWidth.DROITE)
			x -= width;
		else if (posWidth == PositionWidth.MILIEU)
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
		if (resetMenu == 0) {
			if (isClicked) {
				resetMenu = timeResetMenu;
				isClicked = false;
				return true;
			}
		}
		
		return false;
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
				if (Input.isButtonDown(0)) {
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

	public void update(int x, int y, final PositionWidth posWidth, final PositionHeight posHeight) {
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

			if (posHeight == PositionHeight.BAS)
				y -= textSize;
			else if (posHeight == PositionHeight.MILIEU)
				y -= textSize / 2;

			if (posWidth == PositionWidth.DROITE)
				x -= width;
			else if (posWidth == PositionWidth.MILIEU)
				x -= width / 2;

			xx = x;
			yy = y;
			ww = width;
			hh = textSize;
		}

		this.x = ComponentsHelper.getResponsiveX(xx);
		this.y = ComponentsHelper.getResponsiveY(yy);
		width = ComponentsHelper.getResponsiveX(ww);
		height = ComponentsHelper.getResponsiveY(hh);

		update();
	}

	public void update(int x, int y, final PositionWidth posWidth, final PositionHeight posHeight, final int width, final int height) {
		if (xx == -1) {
			if (posHeight == PositionHeight.BAS)
				y -= height;
			else if (posHeight == PositionHeight.MILIEU)
				y -= height / 2;

			if (posWidth == PositionWidth.DROITE)
				x -= width;
			else if (posWidth == PositionWidth.MILIEU)
				x -= width / 2;
			xx = x;
			yy = y;
			ww = width;
			hh = height;
		}

		this.x = ComponentsHelper.getResponsiveX(xx);
		this.y = ComponentsHelper.getResponsiveY(yy);
		this.width = ComponentsHelper.getResponsiveX(ww);
		this.height = ComponentsHelper.getResponsiveY(hh);

		update();
	}

	public void setX(final int x) {
		xx = x;
		this.x = ComponentsHelper.getResponsiveX(x);
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
				if (isOn)
					textColor[3] = 1;
				else
					textColor[3] = 0.6f;
				ComponentsHelper.drawText(text, xx, yy, isOn ? (int) (textSize * 1.02) : textSize, textColor);

			} else {
				if (isOn)
					textColor[3] = 0.7f;
				else
					textColor[3] = 1;
				ComponentsHelper.drawText(text, xx, yy, textSize, textColor);
			}
		}
	}

	public void setText(final String text) {
		this.text = text;
	}

}
