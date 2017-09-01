package com.chat.server;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.metal.MetalLookAndFeel;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;

//import server.MessageSendingControl;

@SuppressWarnings("serial")
public class ServerFrame extends JFrame {
	// ��ɫ������
	public static final String initStyles[] = {
			"regular_0"
			};

	// �޶������ʾ����(Ŀǰ������)
//	private static final int maxRowCount = 20;

	// �޶��Ƿ���ʾ ��Ĭ��Ϊtrue��
	private static boolean isDisplay = true;

	// ����ʾ����ʱ��ÿһ�е�ĩβ��Ҫ�õ��ַ�(Ĭ���ǻس���)
	private static final String lastChar = "\n";

	// �����Ǵ��岼��������Ҫ�ĳ�Ա
	private JButton btnStart;

	private JButton btnPause;

	private JButton btnExit;

	private JScrollPane scrollPane;

	private JTextPane txtMsg;

	private StyledDocument doc = null;

	public ServerFrame() {
		super();
		setTitle("Java������-���԰�-������-����̨");

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 10, 892, 425);
		getContentPane().add(scrollPane);

		txtMsg = new JTextPane();
		txtMsg.setEditable(false);
		txtMsg.setFont(new Font("", Font.PLAIN, 5));
		scrollPane.setViewportView(txtMsg);

		btnPause = new JButton();
		btnPause.setText("��ͣ��ʾ");
		btnPause.setBounds(403, 453, 106, 23);
		getContentPane().add(btnPause);

		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDisplay = false;
				changeState();
			}
		});

		btnStart = new JButton();
		btnStart.setText("��ʼ��ʾ");
		btnStart.setEnabled(false);
		btnStart.setBounds(236, 453, 106, 23);
		getContentPane().add(btnStart);

		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDisplay = true;
				changeState();
			}
		});

		btnExit = new JButton();
		btnExit.setText("�˳����");
		btnExit.setBounds(568, 453, 106, 23);
		getContentPane().add(btnExit);

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice=JOptionPane.showConfirmDialog(null,"�Ƿ�ȷ���˳�?","ȷ��?",JOptionPane.YES_NO_OPTION);
				if(choice!=JOptionPane.YES_OPTION)return;
				dispose();
				System.exit(0);
//				MessageSendingControl.changeMessageSendingState(false);
//				System.exit(0);
			}
		});

		addStylesToDocument();
		doc = txtMsg.getStyledDocument();
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dispose();
				System.exit(0);
//				MessageSendingControl.changeMessageSendingState(false);
//				System.exit(0);
			}
		});
		getContentPane().setLayout(null);
		setSize(898, 532);

//		 this.setUndecorated(true);//ȥ����������,������󻯰�ť
		this.setResizable(false); // ��ֹ�ı䴰���С
		this.setLocationRelativeTo(null);
		this.setVisible(true);

		try {
			UIManager.setLookAndFeel(new MetalLookAndFeel());
		} catch (Exception e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(getContentPane());
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
	}

	public void setMessageText(String msg) {
		if (!isDisplay)return;
		try {
			// StyledDocument doc = txtMsg.getStyledDocument();
				doc.insertString(doc.getLength(), msg + lastChar, doc
						.getStyle(initStyles[0]));
				// ���ı���Ľ���ʼ����������
				txtMsg.setCaretPosition(doc.getLength());

		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}
	}
	// ��ʼ��10����ɫ������
	private void addStylesToDocument() {
		StyledDocument doc = txtMsg.getStyledDocument();
		// ���Լ򵥷���
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		for (int i = 0; i < initStyles.length; i++) {
			//font setting
			Style temp=doc.addStyle(initStyles[i], def);
			Font font=new Font("������",Font.PLAIN,14);
			Color color=new Color(0,0,0);
			StyleConstants.setBold(temp, font.isBold());
			StyleConstants.setFontSize(temp, font.getSize());
			StyleConstants.setItalic(temp, font.isItalic());
			StyleConstants.setForeground(temp,color);
		}
	}

	// ���Ŀ�ʼ��ʾ��ֹͣ��ʾ��Ŧ�Ŀ���״̬
	private void changeState() {
		this.btnStart.setEnabled(!isDisplay);
		this.btnPause.setEnabled(isDisplay);
	}
	
	public static void main(String args[]) {
		ServerFrame sc = new ServerFrame();
		sc.setMessageText("��ʼ����");
	}
}
