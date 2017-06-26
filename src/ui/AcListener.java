package ui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EmptyStackException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.Convert;

/**
 * 按钮动作事件监听类
 * @author ele
 *
 */
public class AcListener implements ActionListener {

	private JButton button = null; // 按钮
	private JTextField text = null; // 输入框
	private JScrollPane tabPanel1 = null; // DFA状态转换图面板
	private JScrollPane tabPanel2 = null; // DFA状态转换矩阵面板

	public AcListener(JButton button, JTextField text, JScrollPane tabPanel12, JScrollPane tabPanel22) {
		this.button = button;
		this.text = text;
		this.tabPanel1 = tabPanel12;
		this.tabPanel2 = tabPanel22;
	}

	/**
	 * 执行转换按钮响应的具体动作事件
	 */
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if (obj == button) {
			if (text.getText().trim().equals("")) {  // 文本框为空时，显示输入提示
				JLabel msg1 = new JLabel("请输入正规表达式 ");
				msg1.setFont(new Font("宋体", Font.BOLD, 16));
				JLabel msg2 = new JLabel("请输入正规表达式 ");
				msg2.setFont(new Font("宋体", Font.BOLD, 16));
				msg1.setPreferredSize(new Dimension(690, 380));
				msg1.setVerticalAlignment(SwingConstants.CENTER);
				msg1.setHorizontalAlignment(SwingConstants.CENTER);
				msg2.setPreferredSize(new Dimension(690, 380));
				msg2.setVerticalAlignment(SwingConstants.CENTER);
				msg2.setHorizontalAlignment(SwingConstants.CENTER);
				tabPanel1.setViewportView(msg1);
				tabPanel2.setViewportView(msg2);
			} else {
				try {
					Convert convert = new Convert();
					convert.createTree(convert.reToPostfix(text.getText().trim()));  // 字符串输入检查，若输入有误则抛出异常
					
					Graph graph = new Graph(text.getText().trim());  // 绘制状态转换图
					tabPanel1.setViewportView(graph);
					
					Matrix matrix = new Matrix(text.getText().trim());  // 绘制状态转换图
					tabPanel2.setViewportView(matrix);
				} catch(EmptyStackException ex) {
					JLabel msg1 = new JLabel("正规表达式输入有误，请检查后重新输入");
					msg1.setFont(new Font("宋体", Font.BOLD, 16));
					JLabel msg2 = new JLabel("正规表达式输入有误，请检查后重新输入");
					msg2.setFont(new Font("宋体", Font.BOLD, 16));
					msg1.setPreferredSize(new Dimension(690, 380));
					msg1.setVerticalAlignment(SwingConstants.CENTER);
					msg1.setHorizontalAlignment(SwingConstants.CENTER);
					msg2.setPreferredSize(new Dimension(690, 380));
					msg2.setVerticalAlignment(SwingConstants.CENTER);
					msg2.setHorizontalAlignment(SwingConstants.CENTER);
					tabPanel1.setViewportView(msg1);
					tabPanel2.setViewportView(msg2);
				}
			}
		}
	}

}
