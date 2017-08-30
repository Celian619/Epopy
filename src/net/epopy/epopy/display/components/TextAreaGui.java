package net.epopy.epopy.display.components;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2f;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import net.epopy.epopy.display.components.ComponentsHelper.PositionHeight;
import net.epopy.epopy.display.components.ComponentsHelper.PositionWidth;
import net.epopy.epopy.games.gestion.AbstractGameMenu;
import net.epopy.epopy.utils.Input;

public class TextAreaGui {

	private StringBuilder text;
	private int x;
	private int y;
	private int width;
	private int height;
	private int xx = -1;
	private int yy;
	private int ww;
	private int hh;
	public int textSize = 30;
	private boolean enter;

	private float[] color0;
	private float[] color1;
	private float[] color2;
	private int letters = 0;
	private String infos;
	private boolean isCap = Toolkit.getDefaultToolkit().getLockingKeyState(KeyEvent.VK_CAPS_LOCK);
	private int removeTime;
	private boolean password;
	private boolean cartereSpecial;
	public boolean isOn;

	public TextAreaGui(int x, int y) {
		text = new StringBuilder("");
		this.x = x;
		this.y = y;
		color0 = new float[] { 1, 1, 1, 0.2f };
		color1 = new float[] { 1, 1, 1, 0.4f };
		color2 = new float[] { 0.658824f, 0.658824f, 0.658824f, 1 };
		enter = false;
	}

	public TextAreaGui(int x, int y, boolean trasparent, String infos) {
		text = new StringBuilder("");
		this.x = x;
		this.y = y;
		this.infos = infos;

		if (trasparent) {
			color0 = new float[] { 1, 1, 1, 0 };
			color1 = new float[] { 1, 1, 1, 0.0f };
			color2 = new float[] { 0.658824f, 0.658824f, 0.658824f, 0 };
		} else {
			color0 = new float[] { 1, 1, 1, 0.2f };
			color1 = new float[] { 1, 1, 1, 0.4f };
			color2 = new float[] { 0.658824f, 0.658824f, 0.658824f, 1 };
		}
		enter = false;
	}
	public TextAreaGui(int x, int y, boolean trasparent, String infos, boolean password) {
		text = new StringBuilder("");
		this.password = password;
		this.x = x;
		this.y = y;
		this.infos = infos;

		if (trasparent) {
			color0 = new float[] { 1, 1, 1, 0 };
			color1 = new float[] { 1, 1, 1, 0.0f };
			color2 = new float[] { 0.658824f, 0.658824f, 0.658824f, 0 };
		} else {
			color0 = new float[] { 1, 1, 1, 0.2f };
			color1 = new float[] { 1, 1, 1, 0.4f };
			color2 = new float[] { 0.658824f, 0.658824f, 0.658824f, 1 };
		}
		enter = false;
	}
	public String getText() {
		return text.toString();
	}

	public TextAreaGui setAccesCaratereSpecial(boolean cartereSpecial) {
		this.cartereSpecial = cartereSpecial;
		return this;
	}

	public TextAreaGui setText(String text) {
		this.text = new StringBuilder(text);
		return this;
	}

	public TextAreaGui addText(String text) {
		this.text.append(text);
		letters++;
		return this;
	}

	public TextAreaGui setEnter(boolean enter) {
		this.enter = enter;
		return this;
	}

	public boolean isEnter() {
		return enter;
	}

	public void update(int x, int y, int width, int height, float[] c0, float[] c1, float[] c2, int maxletters) {
		int mx = Mouse.getX();
		int my = Display.getHeight() - Mouse.getY();

		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;

		color0 = c0;
		color1 = c1;
		color2 = c2;

		float x0 = x - 4 - 16 * 4;
		float y0 = y - 3;
		float x1 = x + 16 * 4 + 10 * 16 *2 ;
		float y1 = y + 32;

		if (width != -1 && height != -1) {
			x0 = x - 4 - width;
			y0 = y - 3;
			x1 = x + width + 10 * width + 20 + 32;
			y1 = y + height;
		}
		//ComponentsHelper.renderTexture(Textures.GAME_CAR_BG, (Mouse.getX() * 1920 / Display.getWidth()),( (Display.getHeight() - Mouse.getY()) * 1080 / Display.getHeight()), 50, 50);

		if (mx >= x0 && mx < x1 && my >= y0 && my < y1) {
		/**
			int cx = (Mouse.getX() * 1920 / Display.getWidth());
			int cy = ((Display.getHeight() - Mouse.getY()) * 1080 / Display.getHeight());
			ComponentsHelper.drawLine(cx, cy-10, cx, cy+20, 1);
		*/
			if (Mouse.isButtonDown(0))
				enter = true;
		} else {
			if (Mouse.isButtonDown(0))
				enter = false;
		}

		boolean isMaj = isCap && !Input.isKeyDown(54) && !Input.isKeyDown(42)
				|| !isCap && Input.isKeyDown(54) && !Input.isKeyDown(42) ||
				!isCap && !Input.isKeyDown(54) && Input.isKeyDown(42);

		if (removeTime > 0) removeTime--;

		if (enter) {
			if (Input.getKeyDown(Keyboard.KEY_RETURN))
				enter = false;
			else if (Keyboard.isKeyDown(Keyboard.KEY_BACK) && removeTime == 0) {
				if (text.length() != 0) {
					text.replace(0, text.length(), text.substring(0, text.length() - 1));
					letters--;
					removeTime = 7;
				}
			} else if (Input.getKeyDown(58)) {
				isCap = !isCap;
				Toolkit.getDefaultToolkit().setLockingKeyState(KeyEvent.VK_CAPS_LOCK, isCap);
			} else {

				if (getText().length() <= maxletters) {
					for (int i = 0; i < Keyboard.getKeyCount(); i++) {
						if (Input.getKeyDown(i)) {
							if (Input.isKeyDown(29) && i == 11) 
								addText("@");
							else if (isMaj && i == 52 || i == 83) 
								addText(".");
							else if(i == 57 & cartereSpecial)
								addText(" ");
							else if(i == 52 & cartereSpecial)
								addText(";");
							else if(i == 51 & cartereSpecial) 
								addText(isMaj ? "?" : ",");
							else if(i == 0 & cartereSpecial) 
								addText(isMaj ? ">" : "<");
							else if(!cartereSpecial) {
								for (int l : Input.getLetters()) {
									if (l == i) {
										addText(isMaj ? Input.getNumpadInput(isMaj, false , i).toUpperCase() : Input.getNumpadInput(isMaj, false , i).toLowerCase());
										break;
									}
								}
							} else {
								for (int l : Input.getLettersUTF8()) {
									if (l == i) {
										addText(isMaj ? Input.getNumpadInput(isMaj, true , i).toUpperCase() : Input.getNumpadInput(isMaj, true , i).toLowerCase());
										break;
									}
								}
							}
						}
					}
				} else {
					glColor4f(1, 0, 0, 1);
					ComponentsHelper.drawText("Max " + maxletters + " caractères !", AbstractGameMenu.defaultWidth / 2, AbstractGameMenu.defaultHeight / 2 - 50, PositionWidth.MILIEU, PositionHeight.MILIEU, 50, new float[] { 1, 0, 0, 1 });
					glColor4f(1, 1, 1, 1);
				}
			}
		}
	}

	public void update(int maxletters) {

		if (xx == -1) {
			xx = x;
			yy = y;
			ww = width;
			hh = height;
		}

		x = ComponentsHelper.getResponsiveX(xx);
		y = ComponentsHelper.getResponsiveY(yy);
		width = ComponentsHelper.getResponsiveX(ww);
		height = ComponentsHelper.getResponsiveY(hh);

		update(x, y, 16, -1, color0, color1, color2, maxletters);
	}

	int i = 0;

	// pour la couleur du text
	float c = 0.02f;
	boolean moins = true;
	boolean up = false;
	int r = 0;

	public void render() {

		int x0 = xx - 4 - 16;
		int y0 = yy;
		int x1 = xx + 16 + 10 * 16 + 20 + 32;
		int y1 = yy + 32;

		if (width != -1 && height != -1) {
			x0 = xx - 4 - ww;
			y0 = yy - 3;
			x1 = xx + ww + 10 * ww + 20 + 32;
			y1 = yy + hh;
		}

		if (up) {
			if (moins) {
				if (c >= 1.0f)
					moins = false;
				else
					c += 0.02;
			} else {
				if (c <= 0.2f) {
					moins = true;
				} else {
					c -= 0.02f;
				}
			}
			up = false;
		} else {
			if (r == 2) {
				r = 0;
				up = true;
			} else
				r++;
		}

		if (i == 30) {
			i = 0;
		} else
			i++;

		if (letters == 0) {
			GL11.glColor4f(1, 1, 1, c);
			ComponentsHelper.drawText(infos, x0, yy+5, 30);
			GL11.glColor4f(1, 1, 1, 1);
		}
		int xText =  getText().length() != 0 ?  x0+4 : x0;

		String text = getText();

		if(password) {
			StringBuilder str = new StringBuilder();
			for(int i = 0; i < text.length(); i++)
				str.append("*");
			text = str.toString();
		}
		if (enter) {
			ComponentsHelper.drawQuad(x0, y0, x1 - xx, y1 - yy, color2);
			ComponentsHelper.drawQuad(x0 + 2, y0 + 2, x1 - 4 - xx, y1 - 4 - yy, color1);

			int lastX = ComponentsHelper.drawText(text, xText, yy+7, password ? 60 : textSize, new float[]{0.7f, 0.7f, 0.7f, 1});

			if (i > 20 && i < 35) {
				int x = lastX;
				int width = x + ComponentsHelper.getResponsiveX(2);
				int height = y + ComponentsHelper.getResponsiveY(49);
				glColor4f(1, 1, 1, 1);
				glBegin(GL_QUADS);
				glVertex2f(x, y+3);
				glVertex2f(width, y+3);
				glVertex2f(width, height);
				glVertex2f(x, height);
				glColor4f(1, 1, 1, 1);
				glEnd();
			}

		} else {
			ComponentsHelper.drawQuad(x0, y0, x1 - xx, y1 - yy, color0);
			ComponentsHelper.drawQuad(x0 + 2, y0 + 2, x1 - 4 - xx, y1 - 4 - yy, color1);
			ComponentsHelper.drawText(text, xText, yy+7, password ? 60 : textSize, new float[]{0.7f, 0.7f, 0.7f, 1});
		}
	}

}
