/**
 * ������һ������ʵ����ĸ���
 * ���ڷ�װget������set����
 * IdEntity�Ǹ�������
 * ��Ϊstudent������һ��û��ʵ�������id����
 * ����ͷ�װ��id�����ҵ���������Ϣ
 */

package com.liaomaowen.entity;

public abstract class IdEntity {

	protected long id;
	
	public long getId(){
		return id;
	}
	
	public void setId(long id){
		this.id = id;
	}
}
