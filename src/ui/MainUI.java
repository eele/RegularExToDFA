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

	private static JButton button = null; // ת����ť
	private static JTextField text = null; // �����
	private static JPanel tabPanel1 = null; // DFA״̬ת��ͼ���
	private static JPanel tabPanel2 = null; // DFA״̬ת���������

	public static void main(String args[]) {
		setUI();
		setButtonAction();
	}

	private static void setUI() {
		JFrame frame = new JFrame("������ʽ��DFAת������");
		frame.setSize(710, 580); // ��������Ĵ�С
		frame.setBackground(Color.WHITE); // ���������óɰ�ɫ
		frame.setLocation(100, 80); // �����������ʾλ��
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JPanel hPanel = new JPanel(); // �������
		hPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 2, 3));
		hPanel.setPreferredSize(new Dimension(710, 80));
		JLabel blank = new JLabel(); // �����հ״�
		blank.setPreferredSize(new Dimension(680, 20));
		JLabel label = new JLabel("   ������ʽ�� "); // ��ǩ
		label.setFont(new Font("����", Font.BOLD, 15));
		text = new JTextField("a"); // �����
		text.setPreferredSize(new Dimension(430, 30));
		button = new JButton("ת��"); // ת����ť
		hPanel.add(blank);
		hPanel.add(label);
		hPanel.add(text);
		hPanel.add(button);

		JTabbedPane tabPane = new JTabbedPane(JTabbedPane.TOP); // ѡ�
		tabPane.setFont(new Font("����", Font.BOLD, 13));
		
		tabPanel1 = new JPanel();
		tabPanel2 = new JPanel();
		JLabel msg1 = new JLabel("������������ʽ ");
		msg1.setFont(new Font("����", Font.BOLD, 16));
		JLabel msg2 = new JLabel("������������ʽ ");
		msg2.setFont(new Font("����", Font.BOLD, 16));
		msg1.setPreferredSize(new Dimension(710, 380));
		msg1.setVerticalAlignment(SwingConstants.CENTER);
		msg1.setHorizontalAlignment(SwingConstants.CENTER);
		msg2.setPreferredSize(new Dimension(710, 380));
		msg2.setVerticalAlignment(SwingConstants.CENTER);
		msg2.setHorizontalAlignment(SwingConstants.CENTER);
		tabPanel1.add(msg1);
		tabPanel2.add(msg2);
		
		tabPane.addTab("DFA״̬ת��ͼ", tabPanel1);
		tabPane.addTab("DFA״̬ת������", tabPanel2);
		tabPane.setSelectedIndex(0);

		frame.setLayout(new BorderLayout(3, 3));
		frame.add(hPanel, BorderLayout.NORTH);
		frame.add(tabPane, BorderLayout.CENTER);

		frame.setResizable(false);
		frame.setVisible(true); // ������ɼ�
	}

	private static void setButtonAction() {
		AcListener acListener = new AcListener(button, text, tabPanel1, tabPanel2);
		button.addActionListener(acListener);  // ���ת����ť����
		
		 //���ÿ�ݼ�Ϊ�س���
		button.registerKeyboardAction(acListener,
	      KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0),
	        JComponent.WHEN_IN_FOCUSED_WINDOW);
	}
}
