//�������ݿⷽ���Ľӿ�
package com.liaomaowen.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.liaomaowen.entity.StudentEntity;


//�ӿ��еķ�������ʵ��Ҫ�ھ����class�У��ӿ����ڲ��ı仯�ⲿ��������˸ı�
public interface StudentDao {
//��ӷ����Ķ���
	
	//student��Ϣ���淽��save�Ķ���
	public void save(Connection conn, StudentEntity student) throws SQLException;
	//�����û����˵�studentnumber����ѯ����
	public void update(Connection conn,StudentEntity student) throws SQLException;
	
	//����һ���û���������Ϣ����studentnumber
	public void update_all(Connection conn,StudentEntity student) throws SQLException;
	
	public void update_table_start(Connection conn) throws SQLException;
	
	public void update_table_over(Connection conn) throws SQLException;
	
	public void delete(Connection conn,StudentEntity student) throws SQLException;
	
	//��ѯ�ٵ���������
	public List<String> select(Connection conn) throws SQLException;
	
	//���ճٵ����ض�ĳ����
	public List<String> select_one(Connection conn,String StudentNumber) throws SQLException;
	
	public boolean select_time(Connection conn,String StudentNumber) throws SQLException;
	
}
