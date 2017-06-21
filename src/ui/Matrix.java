package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JPanel;

import data.StateMatrix;

public class Matrix extends JPanel {
	private static final long serialVersionUID = 1L;

	public Matrix() {
		this.setPreferredSize(new Dimension(710, 450));
		this.setBackground(Color.white);
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		g.setFont(new Font("宋体", Font.BOLD, 18));
		g.drawString("状态", 60, 50);
		StateMatrix stateMatrix = new StateMatrix();
		for(int i = 0; i < stateMatrix.getInCh().length; i++) {
			g.drawString(stateMatrix.getInCh()[i] + "", 10 + (i + 1) * 70, 50);
		}
		for(int i = 0; i < stateMatrix.getMatrix().length; i++) {
			for(int j = 0; j < stateMatrix.getMatrix()[i].length; j++) {
				if(stateMatrix.getMatrix()[i][j] == -1) {
					g.drawString("-", 10 + (j + 1) * 70, 50 + (i + 1) * 30);
				} else {
					g.drawString(stateMatrix.getMatrix()[i][j] + "", 10 + (j + 1) * 70, 50 + (i + 1) * 30);
				}
			}
		}
	}
}
