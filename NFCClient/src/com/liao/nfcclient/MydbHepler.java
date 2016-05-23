package com.liao.nfcclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MydbHepler extends SQLiteOpenHelper{

	public MydbHepler(Context context, String name) {
		super(context, name, null, 1);
	}
	//首次创建数据库时才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		//创建一个名为time的数据表,number是具体的时间，单位是秒,second 是秒的意思
		db.execSQL("create table if not exists time(_id integer primary key autoincrement,"
				+ "number int not null)");
		db.execSQL("insert into time(number) values(60)"); //1分钟
		db.execSQL("insert into time(number) values(120)");//2分钟
		db.execSQL("insert into time(number) values(180)");//3分钟
		db.execSQL("insert into time(number) values(240)"); 
	}

	//更新数据库用
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
