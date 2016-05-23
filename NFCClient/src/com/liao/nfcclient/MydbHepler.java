package com.liao.nfcclient;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MydbHepler extends SQLiteOpenHelper{

	public MydbHepler(Context context, String name) {
		super(context, name, null, 1);
	}
	//�״δ������ݿ�ʱ�Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		//����һ����Ϊtime�����ݱ�,number�Ǿ����ʱ�䣬��λ����,second �������˼
		db.execSQL("create table if not exists time(_id integer primary key autoincrement,"
				+ "number int not null)");
		db.execSQL("insert into time(number) values(60)"); //1����
		db.execSQL("insert into time(number) values(120)");//2����
		db.execSQL("insert into time(number) values(180)");//3����
		db.execSQL("insert into time(number) values(240)"); 
	}

	//�������ݿ���
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		
	}

}
