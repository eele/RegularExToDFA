package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * 按钮动作监听类
 * @author ele
 *
 */
public class AcListener implements ActionListener {

	private JButton button = null; // 按钮
	private JTextField text = null; // 输入框
	private JPanel tabPanel1 = null; // DFA状态转换图面板
	private JPanel tabPanel2 = null; // DFA状态转换矩阵面板

	public AcListener(JButton button, JTextField text, JPanel tabPanel1, JPanel tabPanel2) {
		this.button = button;
		this.text = text;
		this.tabPanel1 = tabPanel1;
		this.tabPanel2 = tabPanel2;
	}

	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if (obj == button) {
			if (text.getText().trim().equals("")) {  // 文本框为空时，显示输入提示
				JLabel msg1 = new JLabel("请输入正则表达式 ");
				msg1.setFont(new Font("宋体", Font.BOLD, 16));
				JLabel msg2 = new JLabel("请输入正则表达式 ");
				msg2.setFont(new Font("宋体", Font.BOLD, 16));
				msg1.setPreferredSize(new Dimension(710, 380));
				msg1.setVerticalAlignment(SwingConstants.CENTER);
				msg1.setHorizontalAlignment(SwingConstants.CENTER);
				msg2.setPreferredSize(new Dimension(710, 380));
				msg2.setVerticalAlignment(SwingConstants.CENTER);
				msg2.setHorizontalAlignment(SwingConstants.CENTER);
				tabPanel1.removeAll();   // 清空面板
				tabPanel2.removeAll();   // 清空面板
				tabPanel1.add(msg1);
				tabPanel2.add(msg2);
				tabPanel1.updateUI();
				tabPanel1.repaint();
				tabPanel2.updateUI();
				tabPanel2.repaint();
			} else {
				tabPanel1.removeAll();   // 清空面板
				tabPanel1.updateUI();
				tabPanel1.repaint();
				tabPanel2.removeAll();
				tabPanel2.updateUI();
				tabPanel2.repaint();
				
				Graph graph = new Graph();  // 绘制状态转换图
				tabPanel1.add(graph);
				tabPanel1.updateUI();
				tabPanel1.repaint();
			}
		}
	}

}
