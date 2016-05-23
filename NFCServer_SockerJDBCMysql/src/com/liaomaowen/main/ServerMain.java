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
//		//getInstance方法来获取一个实例
//		ConnectionFactory cf = ConnectionFactory.getInstance();
//		//makeConnection方法获取数据库连接
//		Connection conn = cf.makeConnection();
//		//打印一个connection的属性
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
//			conn.setAutoCommit(false);//关闭事务的自动提交
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
//			//用liao这个量的数据通过set方法将自己想要的数据进行输入，在用其进行配对
//			
////			studentDao.save(conn, liao);
//			
////			studentDao.update(conn,liao);//封装好一个liao，里面有tostring将我们输入的内容转成string，在用PreparedStatement方法，将其插入到数据表中
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
//			conn.commit();//提交一下事务
//			
//		} catch (Exception e) {
//			try {
//				conn.rollback();//若有异常，进行事务回滚，保持数据库的一致性
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
