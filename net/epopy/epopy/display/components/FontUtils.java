package net.epopy.epopy.display.components;

import static org.lwjgl.opengl.GL11.GL_LINEAR;
import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL11.glVertex3f;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

import net.epopy.epopy.utils.FileUtils;

public class FontUtils {
	
	// Constants
	private final Map<Integer, String> CHARS = new HashMap<Integer, String>() {
		private static final long serialVersionUID = 1L;

		{
			put(0, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
			put(1, "abcdefghijklmnopqrstuvwxyzéàè»«ê♥Êûô↓↑←→â©Φ•");
			put(2, "0123456789");
			put(3, "ÄÖÜäöüß");
			put(4, " $+-*/=%\"'#@&_(),.;:?!\\|<>[]§`^~°{}¨£¤$ùç");
		}
	};
	
	// Variables
	private java.awt.Font font;
	private final FontMetrics fontMetrics;
	private final BufferedImage bufferedImage;
	private final int fontTextureId;
	
	// Getters
	public int getFontImageWidth() {
		return (int) CHARS.values().stream().mapToDouble(e -> fontMetrics.getStringBounds(e, null).getWidth()).max().getAsDouble();
	}
	
	public int getFontImageHeight() {
		return CHARS.keySet().size() * getCharHeight();
	}
	
	public int getCharX(final char c) {
		String originStr = CHARS.values().stream().filter(e -> e.contains("" + c)).findFirst().orElse("" + c);
		return (int) fontMetrics.getStringBounds(originStr.substring(0, originStr.indexOf(c)), null).getWidth();
	}
	
	public int getCharY(final char c) {
		int lineId = CHARS.keySet().stream().filter(i -> CHARS.get(i).contains("" + c)).findFirst().orElse(0);
		return getCharHeight() * lineId;
	}
	
	public int getCharWidth(final char c) {
		return fontMetrics.charWidth(c);
	}
	
	public int getCharHeight() {
		return fontMetrics.getMaxAscent() + fontMetrics.getMaxDescent();
	}
	
	// Constructors
	public FontUtils(final int size, final String police) {
		if (FileUtils.SYSTEM_NAME.equals("Linux"))
			font = new Font("sans-serif", Font.BOLD, size);
		else
			font = new Font(police, police.equals("Impact") ? Font.BOLD : Font.PLAIN, size);
			
		// Generate buffered image
		GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		Graphics2D graphics = gc.createCompatibleImage(1, 1, Transparency.TRANSLUCENT).createGraphics();
		graphics.setFont(font);
		
		fontMetrics = graphics.getFontMetrics();
		bufferedImage = graphics.getDeviceConfiguration().createCompatibleImage(getFontImageWidth(), getFontImageHeight(), Transparency.TRANSLUCENT);
		
		// Generate textures
		fontTextureId = glGenTextures();
		glEnable(GL_TEXTURE_2D);
		glBindTexture(GL_TEXTURE_2D, fontTextureId);
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, getFontImageWidth(), getFontImageHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, asByteBuffer());
		
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
	}
	
	// Functions
	public int drawText(final String text, final int x, final int y) {
		glBindTexture(GL_TEXTURE_2D, fontTextureId);
		glBegin(GL_QUADS);
		
		int xTmp = x;
		for (char c : text.toCharArray()) {
			
			float width = getCharWidth(c);
			float height = getCharHeight();
			float cw = 1f / getFontImageWidth() * width;
			float ch = 1f / getFontImageHeight() * height;
			float cx = 1f / getFontImageWidth() * getCharX(c);
			float cy = 1f / getFontImageHeight() * getCharY(c);
			
			glTexCoord2f(cx, cy);
			glVertex3f(xTmp, y, 0);
			
			glTexCoord2f(cx + cw, cy);
			glVertex3f(xTmp + width, y, 0);
			
			glTexCoord2f(cx + cw, cy + ch);
			glVertex3f(xTmp + width, y + height, 0);
			
			glTexCoord2f(cx, cy + ch);
			glVertex3f(xTmp, y + height, 0);
			
			xTmp += width;
		}
		
		glEnd();
		glBindTexture(GL_TEXTURE_2D, 0);
		return xTmp;
	}
	
	// Conversions
	public ByteBuffer asByteBuffer() {
		
		ByteBuffer byteBuffer;
		
		// Draw the characters on our image
		Graphics2D imageGraphics = (Graphics2D) bufferedImage.getGraphics();
		imageGraphics.setFont(font);
		imageGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		imageGraphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		// draw every CHAR by line...
		imageGraphics.setColor(java.awt.Color.WHITE);
		CHARS.keySet().stream().forEach(i -> imageGraphics.drawString(CHARS.get(i), 0, fontMetrics.getMaxAscent() + getCharHeight() * i));
		
		// Generate textures data
		int[] pixels = new int[bufferedImage.getWidth() * bufferedImage.getHeight()];
		bufferedImage.getRGB(0, 0, bufferedImage.getWidth(), bufferedImage.getHeight(), pixels, 0, bufferedImage.getWidth());
		byteBuffer = ByteBuffer.allocateDirect(pixels.length * 4);
		
		for (int y = 0; y < bufferedImage.getHeight(); y++) {
			for (int x = 0; x < bufferedImage.getWidth(); x++) {
				
				int pixel = pixels[y * bufferedImage.getWidth() + x];
				byteBuffer.put((byte) (pixel >> 16 & 0xFF)); // Red component
				byteBuffer.put((byte) (pixel >> 8 & 0xFF)); // Green component
				byteBuffer.put((byte) (pixel & 0xFF)); // Blue component
				byteBuffer.put((byte) (pixel >> 24 & 0xFF)); // Alpha component. Only for RGBA
			}
		}
		
		byteBuffer.flip();
		
		return byteBuffer;
	}
	
}