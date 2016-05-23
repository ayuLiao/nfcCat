/**
 * ѡ��Ҫ��NFCǩ�������ö�ά��ǩ���Ľ���
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
		init();//��ʼ��
		//ѡ��nfc�İ�ť
		Log.i("main", "222222222222222222222222222222222222222222222222222222");
		nfc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//���뿿��nfc��ǩҳ
				Intent myIntent = new Intent(context, show.class);
				startActivity(myIntent);
			}
		});
		//ѡ���ά��İ�ť
		QR.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(choose.this,CaptureActivity.class);
				Log.i("main", "33333333333333333333333333333333333333333333333333333");
				startActivityForResult(intent, 1000);//Ϊ�˴Ӷ�ά��ɨ�����õ�����
			}
		});
		
	}
	//�õ�����
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

//		if(requestCode == 1000){
//			//ɨ��õ��Ľ��
//			String re = data.getExtras().getString("result");
//			//���������հ��У�Ҫ�жϵõ�����һ��������ַ����ſ���ǩ��������Ϊ�����飬���Ƕ�ά�붼����ǩ��
//			if (re != null) {
//				Log.i("main", "4444444444444444444444444444444444444444444444444444444");
//				connect();//���ӷ����
//				Log.i("main", "555555555555555555555555555555555555555555555555");
////				write();//����ǩ��
//				Log.i("main", "6666666666666666666666666666666666666666666");
//				
//			}
//		}
		
		if (resultCode == RESULT_OK) {
			//ɨ��õ��Ľ��
			String re = data.getExtras().getString("result");
			//���������հ��У�Ҫ�жϵõ�����һ��������ַ����ſ���ǩ��������Ϊ�����飬���Ƕ�ά�붼����ǩ��
			if (re != null) {
				connect();//���ӷ����
//				write();//����ǩ��
				
			}
		}
	}
	
		
	
	private void connect() {
		AsyncTask<Void, String, Void> read = new AsyncTask<Void, String, Void>() {
			
			//��ʱ����
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
				write();//����ǩ��
				return null;
			}
			
		
		};
		read.execute();//ִ��
	}
	
	private void write() {
		//�򿪱������ݿ⣬�õ�ѧ�ţ�����ǩ��ʱ�Ĳ�ѯ
		pref = getSharedPreferences("user", MODE_PRIVATE);
		String value = pref.getString("UserNumber", "").toString();
		JsonObject object = new JsonObject();
		object.addProperty("bool", "Sing_In");
		//������仰�ᱨ��ָ�����
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
