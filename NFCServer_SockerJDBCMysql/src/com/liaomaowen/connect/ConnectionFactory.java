package com.liaomaowen.connect;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;


public class ConnectionFactory {
	//创建四个成员变量，用于读取从属性文件中读取到的配置信息，用于连接数据库
	private static String driver;
	private static String dburl;
	private static String user;
	private static String password;
	//这里使用了单例模式，保证了程序运行期间只有一个ConnectionFactory存在
	private static final ConnectionFactory factory = new ConnectionFactory();
	
	//与数据库的连接
	private Connection conn;
	
	//配置信息的读取代码
	/**
	 * 使用java的静态代码块，读取属性文件的信息，
	 * 并赋值给成员变量，static用于初始化值
	 * 
	 * 因为static是在类中执行的，所有静态代码块只会执行一次
	 */
	static{
		//Properties在java的util包中，可以对取属性文件中的键值对，专门用于处理属性文件中的键值对
		Properties prop = new Properties();
		try {
//对取属性文件中内容，首先通过该类的类加载器，再用getResourceAsStream方法读取属性文件中的内容，该方法会将属性文件信息读入到一个熟人流中
			InputStream in = ConnectionFactory.class.getClassLoader().
					getResourceAsStream("config.properties");
		//从输入流中读取键值对列表
			prop.load(in);
		} catch (Exception e) {
			System.out.println("==========配置信息读取错误===========");
		}
		//将从属性表中得到的值赋给成员变量
		driver = prop.getProperty("driver");
		dburl = prop.getProperty("dburl");
		user = prop.getProperty("user");
		password = prop.getProperty("password");
	}
	
	//定义一个构造函数
	private ConnectionFactory(){
		
	}
	
	//getInstance()方法获取一个ConnectionFactory实例
	public static ConnectionFactory getInstance(){
		return factory;
	}
	
	//获取数据库连接
	
	public Connection makeConnection(){
		try {
			Class.forName(driver);
			conn = DriverManager.getConnection(dburl, user, password);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("===========连接数据库失败============");
		}
		return conn;
	}
	
	
	
	
	
}
