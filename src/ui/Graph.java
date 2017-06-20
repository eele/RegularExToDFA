package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.JPanel;

import data.StateMatrix;

/**
 * 状态转换图类
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
		getStateCoordinate();

		for (int i = 0; i < 360; i = i + 10)
			paintLoop(g, 200, 100, i, "a");
	}

	/**
	 * 绘制状态结点
	 * 
	 * @param g
	 * @param num
	 * @param x
	 * @param y
	 */
	private void paintState(Graphics g, String num, int x, int y) {
		g.drawOval(x, y, 36, 36);
		g.setFont(new Font("宋体", Font.BOLD, 18));
		g.drawString(num, x + 13, y + 25);
	}

	/**
	 * 绘制环箭头
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param angle
	 * @param label
	 */
	private void paintLoop(Graphics g, int x, int y, int angle, String label) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(x + 18, y + 18); // 原点位置

		g2d.drawString(label, (int) (Math.sin(angle * Math.PI / 180) * 60),
				(int) (-Math.cos(angle * Math.PI / 180) * 60));

		g2d.rotate(angle * Math.PI / 180);
		g2d.drawArc(-11, -46, 20, 35, -45, 180 + 90); // 圆弧

		int[] xs = { 4, 15, 6 };
		int[] ys = { -29, -26, -15 };
		g2d.fillPolygon(xs, ys, 3); // 实心箭头

		g2d.rotate((360 - angle) * Math.PI / 180); // 恢复旋转角度和原点位置
		g2d.translate(-(x + 18), -(y + 18));
	}

	private List<Map<String, String>> getStateCoordinate() {
		List<List<Integer>> layerList = new ArrayList<List<Integer>>();
		StateMatrix stateMatrix = new StateMatrix();

		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(0);
		layerList.add(stateList);
		int[] hArray = new int[100]; // 重复检测数组
		for (int i = 0; i < 100; i++) {
			hArray[i] = 0;
		}
		int count = 0, state = 0, layer = 0; // 当前加入的状态数、上一层的各状态编号、上一层号
		while (count < stateMatrix.stateTotal() - 1) {  // 生成状态结点的层次排列
			stateList = new ArrayList<Integer>();
			for (int i = 0; i < layerList.get(layer).size(); i++) {
				state = layerList.get(layer).get(i);
				for (int m = 1; m < stateMatrix.getMatrix()[state].length; m++) {
					if ((stateMatrix.getMatrix()[state][m] > state) && (hArray[stateMatrix.getMatrix()[state][m]] == 0)) {
						stateList.add(stateMatrix.getMatrix()[state][m]);
						hArray[stateMatrix.getMatrix()[state][m]] = 1;
						count++;
					}
				}
			}
			layerList.add(stateList);
			layer++;
		}
		
		
		return null;
	}
}
