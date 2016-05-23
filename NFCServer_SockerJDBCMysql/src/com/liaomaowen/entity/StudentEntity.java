/**
 * student的实例
 */

package com.liaomaowen.entity;

public class StudentEntity extends IdEntity {
	//添加成员属性,与student表中的数据项一一对应
	//在数据库中这些成员的类型为varchar，在io中就为string类型
	private String phonenumber;
	private String studentnumber;
	private String studentname;
	private String password;
	private long LateTime;
	private boolean SingIn;
	private boolean TimeIn;
	/**
	 * 创建成员变量的get方法和set方法
	 * 让eclipse自动补全get和set方法
	 * 右键选择Source-->Generate Getters and Setters……-->右侧的Select All-->OK
	 */
	public String getPhonenumber() {
		return phonenumber;
	}
	public void setPhonenumber(String phonenumber) {
		this.phonenumber = phonenumber;
	}
	public String getStudentnumber() {
		return studentnumber;
	}
	public void setStudentnumber(String studentnumber) {
		this.studentnumber = studentnumber;
	}
	public String getStudentname() {
		return studentname;
	}
	public void setStudentname(String studentname) {
		this.studentname = studentname;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public long getLateTime() {
		return LateTime;
	}
	public void setLateTime(long lateTime) {
		LateTime = lateTime;
	}
	public boolean getSingIn() {
		return SingIn;
	}
	public void setSingIn(boolean singIn) {
		SingIn = singIn;
	}
	
	public boolean getTimeIn(){
		return TimeIn;
	}
	public void setTimeIn(boolean timeIn){
		TimeIn = timeIn;
	}
	//右键Source-->选择Generate toString()-->展开继承的字段Inherited Fields选id-->确定
	@Override
	public String toString() {
		return "StudentEntity [phonenumber=" + phonenumber + ", studentnumber="
				+ studentnumber + ", studentname=" + studentname
				+ ", password=" + password + ", LateTime=" + LateTime
				+ ", SingIn=" + SingIn + ", TimeIn=" + TimeIn + ", id=" + id
				+ "]";
	}
	
	



}
