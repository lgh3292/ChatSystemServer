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
			serverFrame.setMessageText("服务器已启动............");
			while (start) {
				Socket socket = serverSocket.accept();
				Client c = new Client(socket, serverFrame, clients, userId);
				clients.add(c);
				new Thread(c).start();
				serverFrame.setMessageText("目前连接了" + clients.size() + "个客户端!");
			}
		} catch (BindException e) {
			javax.swing.JOptionPane.showMessageDialog(null, "对不起,服务器已经启动或端口被占用,程序终止!");
			System.exit(0);
		} catch (Exception ee) {
			ee.printStackTrace();
			System.out.println("其他错误……");
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
			System.out.println("对方退出了……");
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
				// 如果接收到的是字符串，实际情况下是一个userId的字符串
				if (obj instanceof String) {
					String curUserId = null;
					// 如果此时是请求登陆
					if (obj.toString().indexOf(ServerConstants.LOGIN_REQUEST) > -1) {
						curUserId = obj.toString().split(
								ServerConstants.LOGIN_SPLIT)[1];
						// 如果帐号已经在别的地方登陆过
						if (this.userId.contains(curUserId)) {
							send(ServerConstants.LOGIN_REPEAT);
//							System.out.println("用户" + curUserId + "试图重复登陆!");
							serverFrame.setMessageText("用户" + curUserId + "试图重复登陆!");
							clients.remove(this);
						} else {
							send(ServerConstants.LOGIN_SUCCESS);
							this.userId.add(0, curUserId);
							serverFrame.setMessageText("用户" + curUserId + "登陆成功!");
						}
					}
					// 如果此时是注销
					if (obj.toString().indexOf(ServerConstants.LOGOUT) > -1) {
						curUserId = obj.toString().split(
								ServerConstants.LOGIN_SPLIT)[1];
						this.userId.remove(curUserId);
						serverFrame.setMessageText("用户" + curUserId + "成功下线!");
					}
					// System.out.println("上线提示成功接收"+obj+"！当前在线人数"+this.userId.size());
					List<String> list = transferList(this.userId);
					for (int i = 0; i < clients.size(); i++) {
						client = clients.get(i);
						client.send(list);
					}
					// System.out.println("向客户端传递在线具体人数成功");
				}
				// 服务器端如果接收到的是信息数据包
				if (obj instanceof MsgDTO) {
					this.msgDTO = (MsgDTO) obj;
//					System.out.println("剩余客户端数量：" + clients.size() + "个");
//					System.out.println(this.msgDTO.getContent());
					for (int i = 0; i < clients.size(); i++) {
						client = clients.get(i);
						client.send(this.msgDTO);
					}
					this.serverFrame.setMessageText(this.msgDTO.getUserId() + " 对 "
							+ this.msgDTO.getToWhere() + " 说: "
							+ this.msgDTO.getContent());
				}
			}
		} catch (Exception e) {
			// e.printStackTrace();
			clients.remove(this);
			System.out.println("客户端退出……!");
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
