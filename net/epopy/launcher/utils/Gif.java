package net.epopy.launcher.utils;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class Gif extends JPanel {
	Image image;
	Image image2;

	public Gif() {
		image = Toolkit.getDefaultToolkit().createImage(Gif.class.getResource("/net/epopy/launcher/update.png"));
		image2 = Toolkit.getDefaultToolkit().createImage(Gif.class.getResource("/net/epopy/launcher/update.gif"));
	}

	@Override
	public void paintComponent(final Graphics g) {
		super.paintComponent(g);
		if (image != null) {
			g.drawImage(image, 0, 0, this);
			g.drawImage(image2, 148, 95, 2800 / 4, 1575 / 4 , this);
			g.setFont(new Font("Mirosoft Tai Le", Font.BOLD, 15));
			g.setColor(Color.WHITE);
			g.drawString((int) FileDownload.pourcent + "%", (int) FileDownload.pourcent < 10 ? 1000 / 2 - 12 : 1000 / 2 - 17, 300);
		}
	}

	private static Window frame;

	public static void frame() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				frame = new JWindow();
				frame.add(new Gif());
				frame.setSize(1000, 630);
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
			}
		});
	}

	public static void remove() {
		frame.dispose();
		frame = null;
	}
}


