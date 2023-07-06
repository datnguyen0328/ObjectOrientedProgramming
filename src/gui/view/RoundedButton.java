package gui.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JButton;

public class RoundedButton extends JButton {
	private Color backgroundColor;
	private Color hoverColor;
	private Color pressedColor;
	private int cornerRadius;

	public RoundedButton(String text) {
		super(text);
		backgroundColor = new Color(0, 0, 0, 0);
		hoverColor = new Color(108, 125, 253);
		pressedColor = Color.LIGHT_GRAY;
		cornerRadius = 30;
		
		setFont(new Font("SansSerif", Font.BOLD, 12));
		
		setPreferredSize(new Dimension(35, 35));
		setBorder(null);
		setBorderPainted(false);
		setContentAreaFilled(false);
		setOpaque(false);
		setFocusPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		if (getModel().isPressed()) {
			g2.setColor(pressedColor);
		} else if (getModel().isRollover()) {
			g2.setColor(hoverColor);
		} else {
			g2.setColor(backgroundColor);
		}

		g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

		super.paintComponent(g2);
		g2.dispose();
	}
}
