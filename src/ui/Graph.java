package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
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

		List<List<Integer>> coordList = paintTotalStates(g);
		paintTotalLinkedArrows(g, coordList);
//		paintArrow(g,true,
//				coordList.get(2).get(1),
//				coordList.get(2).get(2),
//				coordList.get(3).get(1),
//				coordList.get(3).get(2),
//				"a"
//				);
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
	 * 绘制连接箭头
	 * @param g
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param label
	 */
	private void paintArrow(Graphics g, boolean b, int x1, int y1, int x2, int y2, String label) {
		int angle1 = (int) (Math.atan(((double)(y1 - y2)) / ((double)( x2 - x1))) * 180 / Math.PI);
		int angle2 = angle1;
		if (b) {
			angle2 += 180;
		} else {
			angle1 += 180;
		}
		
		int xl1 = (int)(x1 + 18 + Math.cos(((double)angle1) / 180 * Math.PI) * 18);
		int yl1 = (int)(y1 + 18 - Math.sin(((double)angle1) / 180 * Math.PI) * 18);
		int xl2 = (int)(x2 + 18 + Math.cos(((double)angle2) / 180 * Math.PI) * 18);
		int yl2 = (int)(y2 + 18 - Math.sin(((double)angle2) / 180 * Math.PI) * 18);
		g.drawLine(xl1, yl1, xl2, yl2);
		
		Graphics2D g2d = (Graphics2D) g;
		g2d.translate(xl2, yl2); // 原点位置
		g2d.rotate((90 - angle2) * Math.PI / 180);
		int[] xs = { 0, -5, 5 };
		int[] ys = { 0, -12, -12 };
		g2d.fillPolygon(xs, ys, 3); // 实心箭头
		g2d.rotate((270 + angle2) * Math.PI / 180); // 恢复旋转角度和原点位置
		g2d.translate(-xl2, -yl2);
		
		g2d.drawString(label, (xl1 + xl2) / 2, (yl1 + yl2) / 2);  // 显示标签
		
//		GeneralPath path = new GeneralPath();
//		path.moveTo(xl1, yl1);
//		path.curveTo((xl1 + xl2) / 2-10, (yl1 + yl2) / 2, (xl1 + xl2) / 2, (yl1 + yl2) / 2, xl2, yl2);
//		Graphics2D g2d = (Graphics2D) g;
//		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//		g2d.draw(path);
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

	/**
	 * 绘制所有状态结点
	 * @param g
	 * @return
	 */
	private List<List<Integer>> paintTotalStates(Graphics g) {
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
		
		int max = 0;
		for(List<Integer> st: layerList) { // 获取各层次状态数的最大值
			if(st.size() > max) {
				max = st.size();
			}
		}
		
		List<List<Integer>> coordList = new ArrayList<List<Integer>>(); // 坐标列表
		paintState(g, "0", 60, max / 2 * 100 + 30);  // 绘制0号状态结点
		List<Integer> coord = new ArrayList<Integer>();
		coord.add(0);
		coord.add(60);
		coord.add(max / 2 * 100 + 30);
		coordList.add(coord);
		int layerNum = 1;
		for(List<Integer> st: layerList) {  // 绘制各状态结点
			int n = 0;
			for(int stateNum: st) {
				if(stateNum == 0) {
					continue;
				}
				paintState(g, String.valueOf(stateNum), 100 * layerNum - 40, n * 100 + 30);
				coord = new ArrayList<Integer>();
				coord.add(stateNum);
				coord.add(100 * layerNum - 40);
				coord.add(n * 100 + 30);
				coordList.add(coord);
				n++;
			}
			layerNum++;
		}
		return coordList;
	}
	
	private List<List<Map<String, Object>>> paintTotalLinkedArrows(Graphics g, List<List<Integer>> coordList) {
		List<List<Map<String, Object>>> arrowList = new ArrayList<List<Map<String, Object>>>();
		StateMatrix stateMatrix = new StateMatrix();
		
		int x1 = 0, x2 = 0, y1 = 0, y2 = 0;
		for(int state = 0; state < stateMatrix.stateTotal(); state++) {
			for(int p = 1; p < stateMatrix.getMatrix()[0].length; p++) {
				if(stateMatrix.getMatrix()[state][0] != stateMatrix.getMatrix()[state][p]) {
					int i;
					for(i = 0; i < coordList.size(); i++) {
						if(coordList.get(i).get(0) == state) {
							break;
						}
					}
					x1 = coordList.get(i).get(1);
					y1 = coordList.get(i).get(2);
					for(i = 0; i < coordList.size(); i++) {
						if(coordList.get(i).get(0) == p) {
							break;
						}
					}
					x2 = coordList.get(i).get(1);
					y2 = coordList.get(i).get(2);
					paintArrow(g, state < p, x1, y1, x2, y2, stateMatrix.getInCh()[p] + "");
				}
			}
		}
		return arrowList;
	}
}
