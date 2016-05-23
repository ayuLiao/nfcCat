/**
 * 定义了一个所有实体类的父类
 * 用于封装get方法和set方法
 * IdEntity是个抽象类
 * 因为student表中有一个没有实际意义的id表项
 * 该类就封装了id这个非业务的主键信息
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
