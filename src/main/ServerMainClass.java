package main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.chat.server.Server;



@SuppressWarnings("serial")
public class ServerMainClass extends JFrame {

	private JComboBox cbxPort;
	/**
	 * Launch the application
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			ServerMainClass frame = new ServerMainClass();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the frame
	 */
	public ServerMainClass() {
		super();
		getContentPane().setLayout(null);
		setResizable(false);
		setTitle("Java������-���԰�-������-��½");
		setBounds(100, 100, 368, 199);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JButton btnExit = new JButton();
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		btnExit.setText("�˳�(E)");
		btnExit.setBounds(255, 136, 77, 25);
		getContentPane().add(btnExit);

		final JButton btnStart = new JButton();
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(cbxPort.getItemCount()==0 || cbxPort.getSelectedItem().toString().trim().equals("")){
					javax.swing.JOptionPane.showMessageDialog(null,"������˿ں�!");
					return;
				}
				String port[]={cbxPort.getSelectedItem().toString().trim()};
				if(port[0].length()!=4){
					javax.swing.JOptionPane.showMessageDialog(null,"�˿ںų��ȱ�������λ!");
					return;
				}
				if(!base.util.StringTransform.isDigit(port[0])){
					javax.swing.JOptionPane.showMessageDialog(null,"�˿ںű�������Ч����!");
					return;
				}
				dispose();
				Server.main(port);
			}
		});
		btnStart.setText("����(S)");
		btnStart.setBounds(158, 136, 77, 25);
		getContentPane().add(btnStart);

		cbxPort = new JComboBox();
		cbxPort.setBounds(127, 47, 134, 23);
		cbxPort.setEditable(true);
		cbxPort.addItem("8888");
		cbxPort.addItem("9000");
		getContentPane().add(cbxPort);

		final JLabel label = new JLabel();
		label.setText("�˿ں�:");
		label.setBounds(52, 46, 57, 25);
		getContentPane().add(label);

		final JLabel label_1 = new JLabel();
		label_1.setFont(new Font("", Font.PLAIN, 15));
		label_1.setText("�����ڱ�������������,������˿ں�(����ʹ��Ĭ��)");
		label_1.setBounds(10, 0, 380, 41);
		getContentPane().add(label_1);
		//
	}

}
