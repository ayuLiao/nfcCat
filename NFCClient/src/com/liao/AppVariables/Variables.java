package com.liao.AppVariables;

import android.app.Application;

public class Variables extends Application{
	private String ip = "192.168.1.103";
	@Override
	public void onCreate() {
		super.onCreate();
	}
	public String getIP(){
		return this.ip;
	}
	
	public void setIP(String ip){
		this.ip = ip;
	}
}
