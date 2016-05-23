/**
 * 选择要用NFC签到还是用二维码签到的界面
 */
package com.liao.nfcread;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liao.AppVariables.Variables;
import com.liao.nfcclient.R;
import com.zxing.activity.CaptureActivity;



public class choose extends Activity{

	private ImageButton nfc;
	private ImageButton QR;
	private Context context;
//-----------------------------------
	String ip;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
//--------------------------------
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose);
		this.context = this;
		Log.i("main", "1111111111111111111111111111111111111111111111111111");
		init();//初始化
		//选择nfc的按钮
		Log.i("main", "222222222222222222222222222222222222222222222222222222");
		nfc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//进入靠近nfc标签页
				Intent myIntent = new Intent(context, show.class);
				startActivity(myIntent);
			}
		});
		//选择二维码的按钮
		QR.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(choose.this,CaptureActivity.class);
				Log.i("main", "33333333333333333333333333333333333333333333333333333");
				startActivityForResult(intent, 1000);//为了从二维码扫描界面得到内容
			}
		});
		
	}
	//得到内容
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		if(requestCode == 1000){
//			//扫描得到的结果
//			String re = data.getExtras().getString("result");
//			//这里在最终版中，要判断得到的是一个特殊的字符，才可以签到，这里为了试验，凡是二维码都可以签到
//			if (re != null) {
//				Log.i("main", "4444444444444444444444444444444444444444444444444444444");
//				connect();//连接服务端
//				Log.i("main", "555555555555555555555555555555555555555555555555");
////				write();//进行签到
//				Log.i("main", "6666666666666666666666666666666666666666666");
//				
//			}
//		}
		
		if (resultCode == RESULT_OK) {
			//扫描得到的结果
			String re = data.getExtras().getString("result");
			//这里在最终版中，要判断得到的是一个特殊的字符，才可以签到，这里为了试验，凡是二维码都可以签到
			if (re != null) {
				connect();//连接服务端
//				write();//进行签到
				
			}
		}
	}
	
		
	
	private void connect() {
		AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
			
			//耗时操作
			@Override
			protected Void doInBackground(Void... arg0) {
				try {
					socket = new Socket(ip,12345);
//					socket = new Socket();
//					socket.connect(new InetSocketAddress(ip,12345),5000);
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				write();//进行签到
				return null;
			}
			
		
		};
		read.execute();//执行
	}
	
	private void write() {
		//打开本地数据库，得到学号，进行签到时的查询
		pref = getSharedPreferences("user", MODE_PRIVATE);
		String value = pref.getString("UserNumber", "").toString();
		JsonObject object = new JsonObject();
		object.addProperty("bool", "Sing_In");
		//不加这句话会报空指针错误
//		Toast.makeText(context, value, Toast.LENGTH_SHORT);
		object.addProperty("Studentnumber", value);
		String WriterLine = object.toString();
		try {
			writer.write(WriterLine.replace("\n", " ")+"\n");
			writer.flush();
			writer.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private void init() {
		nfc = (ImageButton) findViewById(R.id.image_NFC);
		QR = (ImageButton) findViewById(R.id.image_QR);
		ip = new Variables().getIP();
	}

	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		try {
//			writer.close();
//			socket.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
}
