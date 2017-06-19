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

public class SymListener implements ActionListener {

	private JButton button = null; // ��ť
	private JTextField text = null; // �����
	private JPanel tabPanel1 = null; // DFA״̬ת��ͼ���
	private JPanel tabPanel2 = null; // DFA״̬ת���������

	public SymListener(JButton button, JTextField text, JPanel tabPanel1, JPanel tabPanel2) {
		this.button = button;
		this.text = text;
		this.tabPanel1 = tabPanel1;
		this.tabPanel2 = tabPanel2;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		Object obj = e.getSource();
		if (obj == button) {
			if (text.getText().trim().equals("")) {  // �ı���Ϊ��ʱ
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
				tabPanel1.updateUI();
				tabPanel1.repaint();
				tabPanel2.updateUI();
				tabPanel2.repaint();
			} else {
				tabPanel1.removeAll();
				tabPanel1.updateUI();
				tabPanel1.repaint();
				tabPanel2.removeAll();
				tabPanel2.updateUI();
				tabPanel2.repaint();
			}
		}
	}

}
