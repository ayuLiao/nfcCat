package com.liaomaowen.connect;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class ConnectionFactory {
	//�����ĸ���Ա���������ڶ�ȡ�������ļ��ж�ȡ����������Ϣ�������������ݿ�
	private static String driver;
	private static String dburl;
	private static String user;
	private static String password;
	//����ʹ���˵���ģʽ����֤�˳��������ڼ�ֻ��һ��ConnectionFactory����
	private static final ConnectionFactory factory = new ConnectionFactory();
	
	//�����ݿ������
	private Connection conn;
	
	//������Ϣ�Ķ�ȡ����
	/**
	 * ʹ��java�ľ�̬����飬��ȡ�����ļ�����Ϣ��
	 * ����ֵ����Ա������static���ڳ�ʼ��ֵ
	 * 
	 * ��Ϊstatic��������ִ�еģ����о�̬�����ֻ��ִ��һ��
	 */
	static{
		//Properties��java��util���У����Զ�ȡ�����ļ��еļ�ֵ�ԣ�ר�����ڴ��������ļ��еļ�ֵ��
		Properties prop = new Properties();
		try {
//��ȡ�����ļ������ݣ�����ͨ��������������������getResourceAsStream������ȡ�����ļ��е����ݣ��÷����Ὣ�����ļ���Ϣ���뵽һ����������
			InputStream in = ConnectionFactory.class.getClassLoader().
					getResourceAsStream("config.properties");
		//���������ж�ȡ��ֵ���б�
			prop.load(in);
		} catch (Exception e) {
			System.out.println("==========������Ϣ��ȡ����===========");
		}
		//�������Ա��еõ���ֵ������Ա����
		driver = prop.getProperty("driver");
		dburl = prop.getProperty("dburl");
		user = prop.getProperty("user");
		password = prop.getProperty("password");
	}
	
	//����һ�����캯��
	private ConnectionFactory(){
		
	}
	
	//getInstance()������ȡһ��ConnectionFactoryʵ��
	public static ConnectionFactory getInstance(){
		return factory;
	}
	
	//��ȡ���ݿ�����
	
	public Connection makeConnection(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dburl, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("===========�������ݿ�ʧ��============");
		}
		return conn;
	}
	
	
	
	
	
}
