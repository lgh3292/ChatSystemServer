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
	// 颜色的名称
	public static final String initStyles[] = {
			"regular_0"
			};

	// 限定最多显示几行(目前不考虑)
//	private static final int maxRowCount = 20;

	// 限定是否显示 （默认为true）
	private static boolean isDisplay = true;

	// 当显示数据时，每一行的末尾需要用的字符(默认是回车符)
	private static final String lastChar = "\n";

	// 以下是窗体布局中所需要的成员
	private JButton btnStart;

	private JButton btnPause;

	private JButton btnExit;

	private JScrollPane scrollPane;

	private JTextPane txtMsg;

	private StyledDocument doc = null;

	public ServerFrame() {
		super();
		setTitle("Java聊天室-测试版-服务器-控制台");

		scrollPane = new JScrollPane();
		scrollPane.setBounds(0, 10, 892, 425);
		getContentPane().add(scrollPane);

		txtMsg = new JTextPane();
		txtMsg.setEditable(false);
		txtMsg.setFont(new Font("", Font.PLAIN, 5));
		scrollPane.setViewportView(txtMsg);

		btnPause = new JButton();
		btnPause.setText("暂停显示");
		btnPause.setBounds(403, 453, 106, 23);
		getContentPane().add(btnPause);

		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				isDisplay = false;
				changeState();
			}
		});

		btnStart = new JButton();
		btnStart.setText("开始显示");
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
		btnExit.setText("退出监控");
		btnExit.setBounds(568, 453, 106, 23);
		getContentPane().add(btnExit);

		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int choice=JOptionPane.showConfirmDialog(null,"是否确认退出?","确定?",JOptionPane.YES_NO_OPTION);
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

//		 this.setUndecorated(true);//去掉窗体修饰,包括最大化按钮
		this.setResizable(false); // 禁止改变窗体大小
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
				// 让文本框的焦点始终在最下面
				txtMsg.setCaretPosition(doc.getLength());

		} catch (BadLocationException ble) {
			System.err.println("Couldn't insert initial text into text pane.");
		}
	}
	// 初始化10种颜色的字体
	private void addStylesToDocument() {
		StyledDocument doc = txtMsg.getStyledDocument();
		// 试试简单方法
		Style def = StyleContext.getDefaultStyleContext().getStyle(
				StyleContext.DEFAULT_STYLE);
		for (int i = 0; i < initStyles.length; i++) {
			//font setting
			Style temp=doc.addStyle(initStyles[i], def);
			Font font=new Font("新宋体",Font.PLAIN,14);
			Color color=new Color(0,0,0);
			StyleConstants.setBold(temp, font.isBold());
			StyleConstants.setFontSize(temp, font.getSize());
			StyleConstants.setItalic(temp, font.isItalic());
			StyleConstants.setForeground(temp,color);
		}
	}

	// 更改开始显示和停止显示按纽的可用状态
	private void changeState() {
		this.btnStart.setEnabled(!isDisplay);
		this.btnPause.setEnabled(isDisplay);
	}
	
	public static void main(String args[]) {
		ServerFrame sc = new ServerFrame();
		sc.setMessageText("开始测试");
	}
}
