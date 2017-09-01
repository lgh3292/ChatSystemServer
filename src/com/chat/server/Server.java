package com.chat.server;

import java.io.*;
import java.net.*;
import java.util.*;

import com.chat.constants.ServerConstants;
import com.chat.dto.MsgDTO;

public class Server implements java.lang.Runnable{
	private static ServerFrame serverFrame = new ServerFrame();

	private static List<Client> clients = new ArrayList<Client>();

	private static List<String> userId = new ArrayList<String>();

	private static ServerSocket serverSocket;

	private static boolean start = true;

	private static int port=8888;
	
	public static void main(String[] args) {
		if(args.length!=0){
			port=Integer.parseInt(args[0]);
		}
		new Thread(new Server()).start();
	}

	public void run() {
		try {
			serverSocket = new ServerSocket(port);
			serverFrame.setMessageText("������������............");
			while (start) {
				Socket socket = serverSocket.accept();
				Client c = new Client(socket, serverFrame, clients, userId);
				clients.add(c);
				new Thread(c).start();
				serverFrame.setMessageText("Ŀǰ������" + clients.size() + "���ͻ���!");
			}
		} catch (BindException e) {
			javax.swing.JOptionPane.showMessageDialog(null, "�Բ���,�������Ѿ�������˿ڱ�ռ��,������ֹ!");
			System.exit(0);
		} catch (Exception ee) {
			ee.printStackTrace();
			System.out.println("�������󡭡�");
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}

class Client implements Runnable {
	private ServerFrame serverFrame;

	private List<Client> clients;

	private List<String> userId;

	private Socket socket;

	private InputStream in;

	private OutputStream out;

	private ObjectInputStream ois;

	private ObjectOutputStream oos;

	private boolean bConnect = true;

	private MsgDTO msgDTO;

	public Client(Socket socket, ServerFrame serverFrame, List<Client> clients,
			List<String> userId) {
		this.serverFrame = serverFrame;
		this.clients = clients;
		this.userId = userId;
		this.socket = socket;
		try {
			if (this.in == null)
				this.in = this.socket.getInputStream();
			if (this.ois == null)
				this.ois = new ObjectInputStream(this.in);

			if (this.out == null)
				this.out = this.socket.getOutputStream();
			if (this.oos == null)
				this.oos = new ObjectOutputStream(this.out);
			this.bConnect = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(Object obj) {
		try {
			this.oos.writeObject(obj);
			this.oos.flush();
		} catch (IOException e) {
			// e.printStackTrace();
			System.out.println("�Է��˳��ˡ���");
		}
	}

	private List<String> transferList(List<String> tempList) {
		List<String> list = new ArrayList<String>();
		for (String userId : tempList) {
			list.add(userId);
		}
		return list;
	}

	public void run() {
		Client client;
		try {
			while (bConnect) {
				Object obj = ois.readObject();
				// ������յ������ַ�����ʵ���������һ��userId���ַ���
				if (obj instanceof String) {
					String curUserId = null;
					// �����ʱ�������½
					if (obj.toString().indexOf(ServerConstants.LOGIN_REQUEST) > -1) {
						curUserId = obj.toString().split(
								ServerConstants.LOGIN_SPLIT)[1];
						// ����ʺ��Ѿ��ڱ�ĵط���½��
						if (this.userId.contains(curUserId)) {
							send(ServerConstants.LOGIN_REPEAT);
//							System.out.println("�û�" + curUserId + "��ͼ�ظ���½!");
							serverFrame.setMessageText("�û�" + curUserId + "��ͼ�ظ���½!");
							clients.remove(this);
						} else {
							send(ServerConstants.LOGIN_SUCCESS);
							this.userId.add(0, curUserId);
							serverFrame.setMessageText("�û�" + curUserId + "��½�ɹ�!");
						}
					}
					// �����ʱ��ע��
					if (obj.toString().indexOf(ServerConstants.LOGOUT) > -1) {
						curUserId = obj.toString().split(
								ServerConstants.LOGIN_SPLIT)[1];
						this.userId.remove(curUserId);
						serverFrame.setMessageText("�û�" + curUserId + "�ɹ�����!");
					}
					// System.out.println("������ʾ�ɹ�����"+obj+"����ǰ��������"+this.userId.size());
					List<String> list = transferList(this.userId);
					for (int i = 0; i < clients.size(); i++) {
						client = clients.get(i);
						client.send(list);
					}
					// System.out.println("��ͻ��˴������߾��������ɹ�");
				}
				// ��������������յ�������Ϣ���ݰ�
				if (obj instanceof MsgDTO) {
					this.msgDTO = (MsgDTO) obj;
//					System.out.println("ʣ��ͻ���������" + clients.size() + "��");
//					System.out.println(this.msgDTO.getContent());
					for (int i = 0; i < clients.size(); i++) {
						client = clients.get(i);
						client.send(this.msgDTO);
					}
					this.serverFrame.setMessageText(this.msgDTO.getUserId() + " �� "
							+ this.msgDTO.getToWhere() + " ˵: "
							+ this.msgDTO.getContent());
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			clients.remove(this);
			System.out.println("�ͻ����˳�����!");
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (oos != null)
					oos.close();
				if (socket != null)
					socket.close();
			} catch (IOException ex) {
				// ex.printStackTrace();
			}
		}
	}
}
