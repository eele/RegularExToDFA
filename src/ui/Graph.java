package ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * ×´Ì¬×ª»»Í¼Àà
 * 
 * @author ele
 *
 */
public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;
	
	public Graph() {
		this.setPreferredSize(new Dimension(680, 200));
	}

	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2D = (Graphics2D) g;
		
		paintState(g2D);
		
	}
	
	private void paintState(Graphics2D g2D) {
		g2D.drawOval(100, 100, 30, 30);
	}
}
