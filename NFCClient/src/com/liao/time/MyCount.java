package com.liao.time;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liao.AppVariables.Variables;
import com.liao.nfcclient.MydbHepler;
import com.liao.nfcclient.R;
import com.liao.time.ArcMenu.OnMenuItemClikListener;


public class MyCount extends Activity implements OnItemClickListener{

	private ArcMenu mArcMenu;//菜单控件
	private ListView listView;
	private Context context;
	private TextView data;//时间
	private String time_data;//时间
	//--------本地数据库---------
	SQLiteDatabase db;//数据库
	MydbHepler dbHepler;//数据库相关类
	Cursor c;//SQLite查询
	SimpleCursorAdapter adapter;//Cursor适配器

	String[] Alist = {"开始计时","删除"};
	
	private final int requestCode = 1500;//跳转至一个Activity的回传值
	
	//-------------计时---------------
	private Count clock;
	private long millisInFuture;
	
	//------------------连接服务器--------
	Socket socket;
	BufferedWriter writer;//向服务器写数据
	private String ip;
	
	//计时器开始时发生给服务器的消息，time_start
	JsonObject object; 
	String WriterLine;
	//计时器结束时发送给服务器的消息，time_over
	JsonObject object_over;
	String WriterLine_over;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_show);

		this.context = this;
		ip = new Variables().getIP();
		Log.i("main","111111111111111111111111111111111111111111111111111");
		init();//初始化
		Log.i("main","222222222222222222222222222222222222222222");
		initEvent();//监听listView下来事件，当其下拉时，菜单的子菜单收缩
		Log.i("main","33333333333333333333333333333333333333333333333333");
		//SQLite数据库初始化,Time.db为数据库名称
		dbHepler = new MydbHepler(context, "Time.db");
		Log.i("main","44444444444444444444444444444444444444444");
		ListQuery();//数据库中，数据表的数据展示在listview中
		Log.i("main","55555555555555555555555555555555555555555555555555");
		listView.setOnItemClickListener(this);//ListView中item点击的监听
		Log.i("main","6666666666666666666666666666666666666666666666666666666");
	}
	private void ListQuery() {
		db = dbHepler.getWritableDatabase();
		c = db.rawQuery("select * from time", null);
		adapter = new SimpleCursorAdapter(this, R.layout.item, c, new String[]{"number"}, new int[]{R.id.time_item});
		listView.setAdapter(adapter);
	}
	private void init() {
		listView = (ListView) findViewById(R.id.time_list);
		mArcMenu = (ArcMenu) findViewById(R.id.time_menu);
		/**
		 * 设置背景图片的透明度
		 * 找到要设置背景的layout的id
		 * 0~255透明度值
		 */
		View v = findViewById(R.id.time_back);
		v.getBackground().setAlpha(100);
	}

	private void initEvent() {

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if(mArcMenu.isOpen())
				{
					mArcMenu.toggleMenu(600);

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, 
					int visibleItem, int totalItemCount) {
			}
		});

		//使用了ArcMenu的回调接口
		//点击子菜单会发生的事情
		mArcMenu.setOnMenuItemClikListener(new OnMenuItemClikListener() {

			

			@Override
			public void onClick(View view, int pos) {
				switch (pos) {
				case 1://添加时间按钮
					Intent intent = new Intent(MyCount.this,AddTime.class);
					//startActivityForResult会回传requestCode,这样特定的Activity可以更具不同的requestCode来进行不同的代码操作
					startActivityForResult(intent, requestCode);
					break;

				case 2://清空时间按钮
					
					new AlertDialog.Builder(context)
					 .setTitle("清空")
					 .setMessage("确定清空时间表?")
					 .setNegativeButton("取消", null)
					 .setPositiveButton("清空", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							db = dbHepler.getWritableDatabase();
							db.delete("time", null, null);
							ListQuery();
						}
					}).show();

					
					
					break;
				}
			}
		});

	}
	/**
     * 所有的Activity对象的返回值都是由这个方法来接收
     * requestCode:    表示的是启动一个Activity时传过去的requestCode值
     * resultCode：表示的是启动后的Activity回传值时的resultCode值
     * data：表示的是启动后的Activity回传过来的Intent对象
     */
	
	//接收到当前Activity跳转后，目标Activity关闭后的回传值
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 1500){
			ListQuery();
		}
		
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		data = (TextView) view.findViewById(R.id.time_item);
		time_data = (String) data.getText();
		
//		long l = Long.parseLong(time_data);
//		Toast.makeText(context, l+"", Toast.LENGTH_SHORT).show();
		
		millisInFuture = Long.parseLong(time_data)*1000;
		Toast.makeText(context, millisInFuture+"", Toast.LENGTH_SHORT).show();
		choose();//选择弹框，删除，开始计时
	}
	private void choose() {
		//这里的onClickListener要加DialogInterface，不然会跟button的onClickListener冲突，报错
		//Alist---数据源
		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("选择")
				.setItems(Alist, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							//计时时间到就连接服务器，向服务器写入数据，改变数据库的一个表项
							connect();//开始时，将不可签到改为可以签到
							time();//开始计时
							break;
						case 1:
							dele();//情况存储时间的本地数据库
							break;
						}

					}

				}).create();
		alertDialog.show();
	}
	//计时
	private void time() {
		//开始计时，计时时间为millisInFuture
		clock = new Count(millisInFuture, 1000);//onTick的运行时间，1秒
		clock.start();
	}
	//清空本地数据库
	private void dele() {
		db = dbHepler.getWritableDatabase();
		Cursor del = db.query("time", null, "number = ?", new String[]{time_data}, null, null, null);
		if(del != null){
			String[] COL = del.getColumnNames();
			while (del.moveToNext()) {
				for(String COLS : COL){
					String Dele = del.getString(del.getColumnIndex(COLS));
					db.delete("time", "number = ?", new String[]{Dele});
				}
			}
		}
		ListQuery();
	}
	
	
	//计时类
	class Count extends CountDownTimer{

		public Count(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		//计时器结束时要做的事情
		@Override
		public void onFinish() {
			//时间到，关闭签到功能
//			connect();//计时时间到就连接服务器，向服务器写入数据，改变数据库的一个表项
//			time_writer();
			connect1();
		}

		//倒计时开始时要做的事情，参数m是直到完成的时间
		@Override
		public void onTick(long m) {
//			Toast.makeText(context, m / 1000+"", Toast.LENGTH_SHORT).show();
		}

	}
	//与服务器交互
	private void connect() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {

			//耗时操作
			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					socket = new Socket(ip,12345);
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				 object = new JsonObject();
				object.addProperty("bool", "time_start");
				 WriterLine = object.toString();
				
				try {
					writer.write(WriterLine.replace("\n", " ")+"\n");
					writer.flush();
					writer.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		};
		con.execute();
	}

	private void connect1() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {

			//耗时操作
			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					socket = new Socket(ip,12345);
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				 object = new JsonObject();
				object.addProperty("bool", "time_over");
				 WriterLine = object.toString();
				
				try {
					writer.write(WriterLine.replace("\n", " ")+"\n");
					writer.flush();
					writer.close();
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
		};
		con.execute();
	}
	
	
//	public void time_writer(){
//		object_over = new JsonObject();
//		object_over.addProperty("bool", "time_over");
//		WriterLine_over = object_over.toString();
//		try {
//		writer.write(WriterLine_over.replace("\n", " ")+"\n");
//		writer.flush();
//		writer.close();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//	}
	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		try {
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
