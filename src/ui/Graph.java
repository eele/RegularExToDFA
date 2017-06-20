package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

/**
 * ״̬ת��ͼ��
 * 
 * @author ele
 *
 */
public class Graph extends JPanel {
	private static final long serialVersionUID = 1L;

	public Graph() {
		this.setPreferredSize(new Dimension(710, 300));
		this.setBackground(Color.white);
	}

	public void paint(Graphics g) {
		super.paint(g);

		paintState(g, "0", 100, 100);
		paintState(g, "1", 200, 100);

		for(int i=0;i<360;i=i+10)
		paintLoop(g, 200, 100, i, "a");
	}

	/**
	 * ����״̬���
	 * 
	 * @param g
	 * @param num
	 * @param x
	 * @param y
	 */
	private void paintState(Graphics g, String num, int x, int y) {
		g.drawOval(x, y, 36, 36);
		g.setFont(new Font("����", Font.BOLD, 18));
		g.drawString(num, x + 13, y + 25);
	}

	/**
	 * ���ƻ���ͷ
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param angle
	 * @param label
	 */
	private void paintLoop(Graphics g, int x, int y, int angle, String label) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(x + 18, y + 18); // ԭ��λ��

		g2d.drawString(label, (int) (Math.sin(angle * Math.PI / 180) * 60),
				(int) (-Math.cos(angle * Math.PI / 180) * 60));

		g2d.rotate(angle * Math.PI / 180);
		g2d.drawArc(-11, -46, 20, 35, -45, 180 + 90); // Բ��

		int[] xs = { 4, 15, 6 };
		int[] ys = { -29, -26, -15 };
		g2d.fillPolygon(xs, ys, 3); // ʵ�ļ�ͷ

		g2d.rotate((360 - angle) * Math.PI / 180);  // �ָ���ת�ǶȺ�ԭ��λ��
		g2d.translate(-(x + 18), -(y + 18));
	}
}
