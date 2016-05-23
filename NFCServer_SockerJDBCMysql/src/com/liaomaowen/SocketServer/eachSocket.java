/**
 * ÿ���ͻ��˵����Ӷ����γ�һ���̣߳����жϾ��ÿ���̼߳�����Ϊ��ͬ���ж�
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

	//��ͻ�����������
	//		public void out(String out){
	//			try {
	//				//��ȡsocket��������ſ��������������  socket.getOutputStream()
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
		//			out("www");//������ͻ��˷�����Ϣ
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
			conn.setAutoCommit(false);//�ر������Զ��ύ
			StudentDao studentDao = new StudentDaoImplementation();
			StudentEntity liao = new StudentEntity();
			//��ȡ�������ݣ��ͻ�����������������ݣ�
			BufferedReader br = 
					new BufferedReader(
							new InputStreamReader(
									socket.getInputStream(),"UTF-8"));


			String line = null;
			//			//��ȡ�ͻ�����������
			while((line = br.readLine())!=null){
				//JSON������
				JsonParser parser = new JsonParser();
				JsonObject object = (JsonObject) parser.parse(line);
				//�õ��Ǵ��ĸ�activity������
				String bool = object.get("bool").getAsString();

				if(bool.equals("registration")){//��Ϣ����ע��
					//������ڱ����У��ſ��Դ�������
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
					//���浽���ݿ�
					studentDao.save(conn, liao);
					conn.commit();
				}
				/**
				 * ��������
				 * �и���ʦ�ˣ���ʦ�˿�������ǩ��ʱ�䣬����ʦ���Լ�������ϼ�ʱ�Լ����漰��ʱ�䣬��ʱ��һ�����ͷ����ݸ�������
				 * �������ٸ�����������������ݿ��У�ÿһ���һ��Ԫ���Ƿ����ǩ����Ϊ���������ݿ⻹û����һ��´����
				 * ���������ٵ�ѧ��ǩ���ǾͲ�����ǩ���ˣ�ͬʱ�ٱ����ñ�һ�������һ���ࣨ����Ҫ�а�Ϳ���֮��Ĺ�ϵ�������ſ���
				 * �Կ����е�ǩ�������ݱ仯���ֵ���Ӧ�༶�ı��У�����û��ǩ���ģ�Ҳ����false�ĳٵ�����+1���ٴα�������true��Ϊfalse
				 * ����������ҵ��¸���ʦ����ʱ���ڽ���������ڸ�ʱ���Ӧ�İ༶���ǩ�����Ը�Ϊ��ǩ��
				 */
				if(bool.equals("Sing_In")){//����ǩ��,�������ѧ��������,������TimeIn�Ƿ�ʱ
					String studentnumber = object.get("Studentnumber").getAsString();
					boolean timein = studentDao.select_time(conn, studentnumber);
					if(timein){//����ǩ��
						liao.setSingIn(true);
						liao.setStudentnumber(studentnumber);
						studentDao.update(conn, liao);
						conn.commit();
					}
					
				}

				//				if (bool.equals("LateTime")) {//Ϊ��ѯ��ʱ�򣬷������ݸ���
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
					//����ѧ��������������
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

					object1.addProperty("name", st.get(0));//"name"+i//����
					object1.addProperty("studentnumber", st.get(1));//ѧ��
					object1.addProperty("latetime", st.get(2));//�ٵ�����
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
				
		

				
				if(bool.equals("time_start")){//��ʦ�˼�ʱ������
//					System.out.print("haha");
//					liao.setTimeIn(true);
//					studentDao.update_table_start(conn, liao);
					studentDao.update_table_start(conn);
					conn.commit();
				}
				if(bool.equals("time_over")){//��ʦ�˴�����ʱ�䵽�ˣ�ͬʱͳ��û��ǩ������
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
					//���µ����ݿ���
					studentDao.update_all(conn, liao);
					conn.commit();
				}
			}
			br.close();//�ر�


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
