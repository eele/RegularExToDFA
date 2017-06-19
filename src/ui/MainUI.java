package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

public class MainUI {

	private static JButton button = null; // 转换按钮
	private static JTextField text = null; // 输入框
	private static JPanel tabPanel1 = null; // DFA状态转换图面板
	private static JPanel tabPanel2 = null; // DFA状态转换矩阵面板

	public static void main(String args[]) {
		setUI();
		setButtonAction();
	}

	private static void setUI() {
		JFrame frame = new JFrame("正规表达式到DFA转化工具");
		frame.setSize(710, 580); // 设置组件的大小
		frame.setBackground(Color.WHITE); // 将背景设置成白色
		frame.setLocation(100, 80); // 设置组件的显示位置
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel hPanel = new JPanel(); // 顶部面板
		hPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));
		hPanel.setPreferredSize(new Dimension(710, 80));
		JLabel blank = new JLabel(); // 顶部空白处
		blank.setPreferredSize(new Dimension(680, 20));
		JLabel label = new JLabel("   正则表达式： "); // 标签
		label.setFont(new Font("宋体", Font.BOLD, 15));
		text = new JTextField("a"); // 输入框
		text.setPreferredSize(new Dimension(430, 30));
		button = new JButton("转换"); // 转换按钮
		hPanel.add(blank);
		hPanel.add(label);
		hPanel.add(text);
		hPanel.add(button);

		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP); // 选项卡
		tabPane.setFont(new Font("宋体", Font.BOLD, 13));
		
		tabPanel1 = new JPanel();
		tabPanel2 = new JPanel();
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
		tabPanel1.add(msg1);
		tabPanel2.add(msg2);
		
		tabPane.addTab("DFA状态转换图", tabPanel1);
		tabPane.addTab("DFA状态转换矩阵", tabPanel2);
		tabPane.setSelectedIndex(0);

		frame.setLayout(new BorderLayout(3, 3));
		frame.add(hPanel, BorderLayout.NORTH);
		frame.add(tabPane, BorderLayout.CENTER);

		frame.setResizable(false);
		frame.setVisible(true); // 让组件可见
	}

	private static void setButtonAction() {
		AcListener acListener = new AcListener(button, text, tabPanel1, tabPanel2);
		button.addActionListener(acListener);  // 添加转换按钮动作
		
		 //设置快捷键为回车键
		button.registerKeyboardAction(acListener,
	      KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
	        JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
}
