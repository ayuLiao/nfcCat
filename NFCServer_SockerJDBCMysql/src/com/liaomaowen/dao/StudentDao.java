//定义数据库方法的接口
package com.liaomaowen.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.liaomaowen.entity.StudentEntity;


//接口中的方法具体实现要在具体的class中，接口是内部的变化外部都不会因此改变
public interface StudentDao {
//添加方法的定义
	
	//student信息保存方法save的定义
	public void save(Connection conn, StudentEntity student) throws SQLException;
	//根据用户传人的studentnumber来查询更新
	public void update(Connection conn,StudentEntity student) throws SQLException;
	
	//更新一个用户的所有信息，除studentnumber
	public void update_all(Connection conn,StudentEntity student) throws SQLException;
	
	public void update_table_start(Connection conn) throws SQLException;
	
	public void update_table_over(Connection conn) throws SQLException;
	
	public void delete(Connection conn,StudentEntity student) throws SQLException;
	
	//查询迟到的所有人
	public List<String> select(Connection conn) throws SQLException;
	
	//查收迟到的特定某个人
	public List<String> select_one(Connection conn,String StudentNumber) throws SQLException;
	
	public boolean select_time(Connection conn,String StudentNumber) throws SQLException;
	
}
