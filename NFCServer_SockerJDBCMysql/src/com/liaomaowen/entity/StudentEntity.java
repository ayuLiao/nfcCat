/**
 * student��ʵ��
 */

package com.liaomaowen.entity;

public class StudentEntity extends IdEntity {
	//��ӳ�Ա����,��student���е�������һһ��Ӧ
	//�����ݿ�����Щ��Ա������Ϊvarchar����io�о�Ϊstring����
	private String phonenumber;
	private String studentnumber;
	private String studentname;
	private String password;
	private long LateTime;
	private boolean SingIn;
	private boolean TimeIn;
	/**
	 * ������Ա������get������set����
	 * ��eclipse�Զ���ȫget��set����
	 * �Ҽ�ѡ��Source-->Generate Getters and Setters����-->�Ҳ��Select All-->OK
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
	//�Ҽ�Source-->ѡ��Generate toString()-->չ���̳е��ֶ�Inherited Fieldsѡid-->ȷ��
	@Override
	public String toString() {
		return "StudentEntity [phonenumber=" + phonenumber + ", studentnumber="
				+ studentnumber + ", studentname=" + studentname
				+ ", password=" + password + ", LateTime=" + LateTime
				+ ", SingIn=" + SingIn + ", TimeIn=" + TimeIn + ", id=" + id
				+ "]";
	}
	
	



}
