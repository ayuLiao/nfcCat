package com.liaomaowen.SocketServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerLister extends Thread {
	public void run() {
		try {
			ServerSocket serverSocket = new ServerSocket(12345);
			while (true) {
				//����
				Socket socket = serverSocket.accept();
				//��socket�����µ��߳�
				new eachSocket(socket).start();
			}
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
