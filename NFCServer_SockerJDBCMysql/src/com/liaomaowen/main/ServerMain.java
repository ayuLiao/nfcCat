package com.liaomaowen.main;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.text.html.HTMLDocument.Iterator;

import com.liaomaowen.SocketServer.ServerLister;
import com.liaomaowen.connect.ConnectionFactory;
import com.liaomaowen.dao.StudentDao;
import com.liaomaowen.dao.Implementation.StudentDaoImplementation;
import com.liaomaowen.entity.StudentEntity;

public class ServerMain {

//	public static void main(String[] args) {
//		//getInstance��������ȡһ��ʵ��
//		ConnectionFactory cf = ConnectionFactory.getInstance();
//		//makeConnection������ȡ���ݿ�����
//		Connection conn = cf.makeConnection();
//		//��ӡһ��connection������
//		try {
//			System.out.println(conn.getAutoCommit());
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	
	public static void main(String[] args){
//		Connection conn = null;
//		try {
//			conn = ConnectionFactory.getInstance().makeConnection();
//			conn.setAutoCommit(false);//�ر�������Զ��ύ
//			
//			StudentDao studentDao = new StudentDaoImplementation();
//			
//			StudentEntity liao = new StudentEntity();
//			liao.setStudentname("liaomaowen");
//			liao.setPassword("123456");
//			liao.setLateTime(0);
//			liao.setStudentnumber("14053060");
//			liao.setPhonenumber("13712167969");
//			liao.setSingIn(true);
//			
//			//��liao�����������ͨ��set�������Լ���Ҫ�����ݽ������룬������������
//			
////			studentDao.save(conn, liao);
//			
////			studentDao.update(conn,liao);//��װ��һ��liao��������tostring���������������ת��string������PreparedStatement������������뵽���ݱ���
////			studentDao.delete(conn, liao);
//		
//		
//			List<String> st = new ArrayList<String>();
//			st = studentDao.select_one(conn,"123456");
////			for(int i=0;i<st.size();i++){
////				System.out.print(st.get(i)+"\n");
////			}
//			System.out.print(st.get(1)+"\n");
//			System.out.print(st.get(2)+"\n");
//			conn.commit();//�ύһ������
//			
//		} catch (Exception e) {
//			try {
//				conn.rollback();//�����쳣����������ع����������ݿ��һ����
//			} catch (Exception e2) {
//				e2.printStackTrace();
//			}
//		}finally{
//			try {
//				conn.close();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		}
		

		new ServerLister().start();
		
	}
	
		
		
}
