/**
 * 每个客户端的链接都会形成一个线程，有判断句对每个线程继续行为不同的判断
 */

package com.liaomaowen.SocketServer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liaomaowen.connect.ConnectionFactory;
import com.liaomaowen.dao.StudentDao;
import com.liaomaowen.dao.Implementation.StudentDaoImplementation;
import com.liaomaowen.entity.StudentEntity;
import com.mysql.jdbc.UpdatableResultSet;


public class eachSocket extends Thread {

	Socket socket;

	public eachSocket(Socket s){
		this.socket = s;
	}

	//向客户端输入数据
	//		public void out(String out){
	//			try {
	//				//获取socket输出流，才可以向外输出数据  socket.getOutputStream()
	//				socket.getOutputStream().write(out.getBytes("UTF-8"));
	//			} catch (UnsupportedEncodingException e) {
	//				e.printStackTrace();
	//			} catch (IOException e) {
	//				// TODO Auto-generated catch block
	//				e.printStackTrace();
	//			}
	//		}

	public void run() {



		Connection conn = null;
		//		while(true){
		//			out("www");//可以向客户端发送信息
		//			try {
		//				sleep(1000);
		//			} catch (InterruptedException e) {
		//				// TODO Auto-generated catch block
		//				e.printStackTrace();
		//			}
		//		}
		//				out("wwwwwww");




		try {

			conn = ConnectionFactory.getInstance().makeConnection();
			conn.setAutoCommit(false);//关闭事务自动提交
			StudentDao studentDao = new StudentDaoImplementation();
			StudentEntity liao = new StudentEntity();
			//获取输入数据（客户端向服务器输入数据）
			BufferedReader br = 
					new BufferedReader(
							new InputStreamReader(
									socket.getInputStream(),"UTF-8"));


			String line = null;
			//			//获取客户端输入数据
			while((line = br.readLine())!=null){
				//JSON解析器
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(line);
				//得到是从哪个activity传来的
				String bool = object.get("bool").getAsString();

				if(bool.equals("registration")){//信息来自注册
					//必须放在变量中，才可以存入数据
					String phonenumber = object.get("Phonenumber").getAsString();
					String studentname = object.get("Studentname").getAsString();
					String password = object.get("Password").getAsString();
					String studentnumber = object.get("Studentnumber").getAsString();
					int latetime = object.get("LateTime").getAsInt();
					boolean singIn = object.get("SingIn").getAsBoolean();
					liao.setPhonenumber(phonenumber);
					liao.setStudentname(studentname);
					liao.setPassword(password);
					liao.setLateTime(latetime);
					liao.setStudentnumber(studentnumber);
					liao.setSingIn(singIn);
					//保存到数据库
					studentDao.save(conn, liao);
					conn.commit();
				}
				/**
				 * 完整功能
				 * 有个老师端，老师端可以设置签到时间，在老师端自己的软件上计时自己所涉及的时间，当时间一到，就发数据给服务器
				 * 服务器再根据这个数据来将数据库中，每一项的一个元素是否可以签到改为否，现在数据库还没有这一项，下次添加
				 * 这样，当迟到学生签到是就不可以签到了，同时再遍历该表，一个表代表一个班（我们要有班和课室之间的关系，这样才可以
				 * 对课室中的签到将数据变化表现到对应班级的表中），将没有签到的，也就是false的迟到数据+1，再次遍历，将true改为false
				 * ，当这个课室的下个老师运用时，在将这个课室在该时间对应的班级表可签到属性改为可签到
				 */
				if(bool.equals("Sing_In")){//进行签到,更新这个学生的数据,查找其TimeIn是否超时
					String studentnumber = object.get("Studentnumber").getAsString();
					boolean timein = studentDao.select_time(conn, studentnumber);
					if(timein){//可以签到
						liao.setSingIn(true);
						liao.setStudentnumber(studentnumber);
						studentDao.update(conn, liao);
						conn.commit();
					}
					
				}

				//				if (bool.equals("LateTime")) {//为查询的时候，返回数据给它
				////					out("222222222222");
				//					BufferedWriter wt =
				//							new BufferedWriter(
				//									new OutputStreamWriter(
				//											socket.getOutputStream(),"UTF-8"));
				//					String WriterLine = "wowowowoow";
				//					wt.write(WriterLine.replace("\n", " ")+"\n");
				//					wt.flush();
				//				}
				if(bool.equals("LateTime")){
					//根据学生号来查找数据
					String StudentNumber = object.get("Studentnumber").getAsString();
//					System.out.print("LateTime");
					JsonObject object1 = new JsonObject();
					List<String> st = new ArrayList<String>();
					st = studentDao.select_one(conn,StudentNumber);
//					System.out.print(st);
					conn.commit();
					//										conn.commit();
					//					st = studentDao.select(conn);
					//					for(int i=0;i<st.size();i++){
					////						System.out.print(st.get(i)+"\n");
					//						object1.addProperty("name", st.get(0));
					//					}

					object1.addProperty("name", st.get(0));//"name"+i//姓名
					object1.addProperty("studentnumber", st.get(1));//学号
					object1.addProperty("latetime", st.get(2));//迟到次数
					String Writer = object1.toString();
					BufferedWriter wt;
//					System.out.print(Writer);
					try {
						wt = new BufferedWriter(
								new OutputStreamWriter(
										socket.getOutputStream(),"UTF-8"));
						wt.write(Writer.replace("\n", " ")+"\n");
						wt.flush();
					} catch (UnsupportedEncodingException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
				
		

				
				if(bool.equals("time_start")){//老师端计时触发器
//					System.out.print("haha");
//					liao.setTimeIn(true);
//					studentDao.update_table_start(conn, liao);
					studentDao.update_table_start(conn);
					conn.commit();
				}
				if(bool.equals("time_over")){//老师端触发器时间到了，同时统计没有签到的人
//					System.out.print("wowowow");
//					liao.setTimeIn(false);
//					studentDao.update_table_over(conn, liao);
					studentDao.update_table_over(conn);
					conn.commit();

				}
				
				if(bool.equals("person")){
//					System.out.print("lallalalalalalal");
					String studentnumber = object.get("Studentnumber").getAsString();
					String studentname = object.get("Studentname").getAsString();
					String phonenumber = object.get("Phonenumber").getAsString();
					String password = object.get("Password").getAsString();
					liao.setStudentnumber(studentnumber);
					liao.setStudentname(studentname);
					liao.setPhonenumber(phonenumber);
					liao.setPassword(password);
					//更新到数据库中
					studentDao.update_all(conn, liao);
					conn.commit();
				}
			}
			br.close();//关闭


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}catch (Exception e){
			try {
				conn.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}finally{
			try {
				conn.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}
}
