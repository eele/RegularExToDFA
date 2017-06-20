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

		List<List<Integer>> coordList = paintTotalStates(g);
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

		g2d.rotate((360 - angle) * Math.PI / 180); // �ָ���ת�ǶȺ�ԭ��λ��
		g2d.translate(-(x + 18), -(y + 18));
	}

	private List<List<Integer>> paintTotalStates(Graphics g) {
		List<List<Integer>> layerList = new ArrayList<List<Integer>>();
		StateMatrix stateMatrix = new StateMatrix();

		List<Integer> stateList = new ArrayList<Integer>();
		stateList.add(0);
		layerList.add(stateList);
		int[] hArray = new int[100]; // �ظ��������
		for (int i = 0; i < 100; i++) {
			hArray[i] = 0;
		}
		int count = 0, state = 0, layer = 0; // ��ǰ�����״̬������һ��ĸ�״̬��š���һ���
		while (count < stateMatrix.stateTotal() - 1) {  // ����״̬���Ĳ������
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
		for(List<Integer> st: layerList) { // ��ȡ�����״̬�������ֵ
			if(st.size() > max) {
				max = st.size();
			}
		}
		
		List<List<Integer>> coordList = new ArrayList<List<Integer>>(); // �����б�
		paintState(g, "0", 60, max / 2 * 75 + 30);  // ����0��״̬���
		List<Integer> coord = new ArrayList<Integer>();
		coord.add(0);
		coord.add(60);
		coord.add(max / 2 * 75 + 30);
		coordList.add(coord);
		int layerNum = 1;
		for(List<Integer> st: layerList) {  // ���Ƹ�״̬���
			int n = 0;
			for(int stateNum: st) {
				if(stateNum == 0) {
					continue;
				}
				paintState(g, String.valueOf(stateNum), 80 * layerNum - 20, n * 75 + 30);
				coord.add(stateNum);
				coord.add(80 * layerNum - 20);
				coord.add(n * 75 + 30);
				coordList.add(coord);
				n++;
			}
			layerNum++;
		}
		return coordList;
	}
}
