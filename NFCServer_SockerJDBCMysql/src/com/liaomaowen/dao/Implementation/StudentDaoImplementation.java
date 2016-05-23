package com.liaomaowen.dao.Implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.liaomaowen.dao.StudentDao;
import com.liaomaowen.entity.StudentEntity;
import com.mysql.fabric.xmlrpc.base.Array;

public class StudentDaoImplementation implements StudentDao {

	//实现接口中的方法
	public void save(Connection conn, StudentEntity student)
			throws SQLException {
		//LateTime是int类型，其余都是String类型
		String sql = "insert into student(phonenumber,studentnumber,studentname,password,LateTime,SingIn) values(?,?,?,?,?,?)";
		//preparedStatement是jdbc用于执行sql查询语句的api之一,用于执行参数化的查询，其中？问号是占位符，代表具体的参数
		PreparedStatement ps = conn.prepareCall(sql);

		//参数设置代码，就将StudentEntity中传入的数据保存带数据库表中
		ps.setString(1, student.getPhonenumber());
		ps.setString(2, student.getStudentnumber());
		ps.setString(3, student.getStudentname());
		ps.setString(4, student.getPassword());
		ps.setLong(5, student.getLateTime());
		ps.setBoolean(6, student.getSingIn());
		ps.execute();

	}

	//更新方法，可以用来更新迟到人数,根据studentnumber来查询 
	public void update(Connection conn, StudentEntity student)
			throws SQLException {
		String sql = "update student set SingIn = ? where studentnumber = ?";
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setBoolean(1, student.getSingIn());
		ps.setString(2, student.getStudentnumber());
		ps.execute();
	}
	
//客户端更改注册信息时，更新数据库
	public void update_all(Connection conn, StudentEntity student)
			throws SQLException {
		String sql = "update student set phonenumber = ?,studentname = ?,password = ? where studentnumber = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, student.getPhonenumber());
		ps.setString(2, student.getStudentname());
		ps.setString(3, student.getPassword());
		ps.setString(4, student.getStudentnumber());
		ps.execute();
	}
	
	//删除用户信息,根据学号来删除
	public void delete(Connection conn, StudentEntity student) throws SQLException{
		String sql = "delete from student where studentnumber = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, student.getStudentnumber());
		ps.execute();
	}

	public List<String> select(Connection conn) throws SQLException {
		String sql = "select * from student";
		PreparedStatement ps = conn.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();
		int col = rs.getMetaData().getColumnCount();
		//		List<String> st_name = new ArrayList<String>();
		//		List<String> st_number = new ArrayList<String>();
		//		List<String> st_LateTime = new ArrayList<String>();
		List<String> st = new ArrayList<String>();
		while (rs.next()) {
			//			st_name.add(rs.getString(4));
			//			st_number.add(rs.getString(3));
			//			st_LateTime.add(rs.getString(6));
			st.add(rs.getString(4));
		}
		return st;
	}
	

	//查询具体的某个人
	public List<String> select_one(Connection conn, String StudentNumber)
			throws SQLException {
		String sql = "select * from student where studentnumber = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, StudentNumber);
		ResultSet rs = ps.executeQuery();
		List<String> st = new ArrayList<String>();
		int col = rs.getMetaData().getColumnCount();
		while(rs.next()){
			st.add(rs.getString(4));//姓名
			st.add(rs.getString(3));//学号
			st.add(rs.getString(6));//迟到次数
		}

		return st;
	}
	


	//将TimeIn全部改为true
	public void update_table_start(Connection conn)
			throws SQLException {
		String sql = "update student set TimeIn = true";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
	}

	public void update_table_over(Connection conn)
			throws SQLException {
		String sql = "update student set TimeIn = false";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.execute();
		//没签到的，迟到次数加一
		String sql_lateTime = "update student set LateTime = LateTime+1 where SingIn = false";
		PreparedStatement ps_lateTime = conn.prepareStatement(sql_lateTime);
		ps_lateTime.execute();
		//将签了到的人，该为没签到
		String sql_SingIn = "update student set SingIn = false";
		PreparedStatement ps_SingIn = conn.prepareStatement(sql_SingIn);
		ps_SingIn.execute();
	}

	

	
	//签到时，从数据库中查看TimeIn是否可以签到
	public boolean select_time(Connection conn, String StudentNumber)
			throws SQLException {
		String sql = "select * from student where studentnumber = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, StudentNumber);
		ResultSet rs = ps.executeQuery();
		boolean timein = false;//默认是不可以签到的，为true，则可以签
		int col = rs.getMetaData().getColumnCount();
		while(rs.next()){
			timein = rs.getBoolean(8);
		}
		return timein;
	}

	


	//	public void select(Connection conn)
	//			throws SQLException {
	//		StudentEntity liao = new StudentEntity();
	//		
	//		String sql = "select * from student";
	//		PreparedStatement ps = conn.prepareStatement(sql);
	//		ResultSet rs = ps.executeQuery();
	//		int col = rs.getMetaData().getColumnCount();
	//		while (rs.next()) {
	////			for(int i=1;i<=col;i++){
	////				System.out.print(rs.getString(i)+"\n");//输出表中每一行的内容
	////			}
	//			System.out.print(rs.getString(4)+"\n");//输入表中名字的内容
	//			
	//		}
	//	}



}
