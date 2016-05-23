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

	//ʵ�ֽӿ��еķ���
	public void save(Connection conn, StudentEntity student)
			throws SQLException {
		//LateTime��int���ͣ����඼��String����
		String sql = "insert into student(phonenumber,studentnumber,studentname,password,LateTime,SingIn) values(?,?,?,?,?,?)";
		//preparedStatement��jdbc����ִ��sql��ѯ����api֮һ,����ִ�в������Ĳ�ѯ�����У��ʺ���ռλ�����������Ĳ���
		PreparedStatement ps = conn.prepareCall(sql);

		//�������ô��룬�ͽ�StudentEntity�д�������ݱ�������ݿ����
		ps.setString(1, student.getPhonenumber());
		ps.setString(2, student.getStudentnumber());
		ps.setString(3, student.getStudentname());
		ps.setString(4, student.getPassword());
		ps.setLong(5, student.getLateTime());
		ps.setBoolean(6, student.getSingIn());
		ps.execute();

	}

	//���·����������������³ٵ�����,����studentnumber����ѯ 
	public void update(Connection conn, StudentEntity student)
			throws SQLException {
		String sql = "update student set SingIn = ? where studentnumber = ?";
		PreparedStatement ps = conn.prepareStatement(sql);

		ps.setBoolean(1, student.getSingIn());
		ps.setString(2, student.getStudentnumber());
		ps.execute();
	}
	
//�ͻ��˸���ע����Ϣʱ���������ݿ�
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
	
	//ɾ���û���Ϣ,����ѧ����ɾ��
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
	

	//��ѯ�����ĳ����
	public List<String> select_one(Connection conn, String StudentNumber)
			throws SQLException {
		String sql = "select * from student where studentnumber = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, StudentNumber);
		ResultSet rs = ps.executeQuery();
		List<String> st = new ArrayList<String>();
		int col = rs.getMetaData().getColumnCount();
		while(rs.next()){
			st.add(rs.getString(4));//����
			st.add(rs.getString(3));//ѧ��
			st.add(rs.getString(6));//�ٵ�����
		}

		return st;
	}
	


	//��TimeInȫ����Ϊtrue
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
		//ûǩ���ģ��ٵ�������һ
		String sql_lateTime = "update student set LateTime = LateTime+1 where SingIn = false";
		PreparedStatement ps_lateTime = conn.prepareStatement(sql_lateTime);
		ps_lateTime.execute();
		//��ǩ�˵����ˣ���Ϊûǩ��
		String sql_SingIn = "update student set SingIn = false";
		PreparedStatement ps_SingIn = conn.prepareStatement(sql_SingIn);
		ps_SingIn.execute();
	}

	

	
	//ǩ��ʱ�������ݿ��в鿴TimeIn�Ƿ����ǩ��
	public boolean select_time(Connection conn, String StudentNumber)
			throws SQLException {
		String sql = "select * from student where studentnumber = ? ";
		PreparedStatement ps = conn.prepareStatement(sql);
		ps.setString(1, StudentNumber);
		ResultSet rs = ps.executeQuery();
		boolean timein = false;//Ĭ���ǲ�����ǩ���ģ�Ϊtrue�������ǩ
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
	////				System.out.print(rs.getString(i)+"\n");//�������ÿһ�е�����
	////			}
	//			System.out.print(rs.getString(4)+"\n");//����������ֵ�����
	//			
	//		}
	//	}



}
