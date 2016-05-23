/**
 * ѡ��鿴ʱ������ѡ��鿴�Լ���Ҳ����ѡ��鿴������
 * �鿴�Լ��ľ���Textview
 * �鿴ȫ��ֻ�ܲ鿴˭û��������ListView
 */

package com.liao.WatchInformation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.liao.AppVariables.Variables;
import com.liao.nfcclient.R;

public class LateTime extends Activity {
	Context context;
	private TextView name;
	private TextView student_number;
	private TextView lateTime;
	//-------------�����ݿ�����Ҫ�õı���-------------------
	Socket socket;
	BufferedReader reader;//�����ݿ��ж�ȡ����
	BufferedWriter writer;
	String ip;

	//�ӷ���˶�ȡ��������
	String line = null;
	String Name=" ";
	String Student_Number=" ";
	String Late_Time = " ";
	
	Button bt;
	//----------�������ݿ�-------
	SharedPreferences pref;
	
	boolean take;//��ť����û
	
	JsonParser parser;
	JsonObject object1;//��ȡ������
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latetime);
		ip = new Variables().getIP();//���ip
		this.context = this;
		take = false;
		init();
//		name.setText(Name);
//		student_number.setText(Student_Number);
//		lateTime.setText(Late_Time);
		connect();
		show();
		//		new Thread(runnable).start();
	}
	
	
	
	
	


	private void show() {
		bt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
//				Log.i("main", "3333333333333333333333333333333333333333333");
//				getLateTime re = new getLateTime();
//				re.execute();
				
				read();
				
				take = true;
				Json_Put();//������д������ˣ�����activity���ж�
//								Read();
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				name.setText(Name);
				student_number.setText(Student_Number);
				lateTime.setText(Late_Time);
			}
		});
	}


//	class getLateTime extends AsyncTask<Void, String, Void>{
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			JsonObject object = new JsonObject();
//			Log.i("main", "22222222222222222222222222222222222222222222222222");
//			object.addProperty("bool", "LateTime");
//			String studentnumber = pref.getString("UserNumber", "");
//			object.addProperty("Studentnumber",studentnumber );//����ѧ��������ѯ
//			String WriterLine = object.toString();
//			try {
//				writer.write(WriterLine.replace("\n", " ")+"\n");
//				writer.flush();
//				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			JsonParser parser = new JsonParser();
//			try {
//				Log.i("main", "0000000000000000000000000000000000000000000000000000");
//				while((line = reader.readLine()) != null){
//					Log.i("main", "-------------------------------11111111111111");
//					object1 = (JsonObject) parser.parse(line);
//					Log.i("main", "11111111111111111111111111111111111111111111111111111");
//					Student_Number = object1.get("studentnumber").getAsString();
//					Late_Time = object1.get("latetime").getAsString();
//					Name = object1.get("name").getAsString();
//					Log.i("main", "999999999999999999999999999999999999999999999");
////					Log.i("main", line);
//					take = true;
//				}
//				
////				if((line = reader.readLine()) == null){
////					Log.i("main", "88888888888888888888888888888888888888");
////					reader.close();
////				}
//				
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			return null;
//		}
//		
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//			name.setText(Name);
//			student_number.setText(Student_Number);
//			lateTime.setText(Late_Time);
//			Log.i("main", object1.get("name").getAsString()+"6");
//			Toast.makeText(context, Name+"onPostExecute",Toast.LENGTH_SHORT).show();
//		}
//		
//	}
	
	
	public void read(){
		AsyncTask<Void, String, Void> re = new AsyncTask<Void, String, Void>() {


			@Override
			protected Void doInBackground(Void... arg0) {
				
				JsonObject object = new JsonObject();
				object.addProperty("bool", "LateTime");
				String studentnumber = pref.getString("UserNumber", "");
//				Toast.makeText(context, studentnumber, Toast.LENGTH_SHORT).show();
				object.addProperty("Studentnumber",studentnumber );//����ѧ��������ѯ
				String WriterLine = object.toString();
				try {
					writer.write(WriterLine.replace("\n", " ")+"\n");
					writer.flush();
//					writer.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				parser = new JsonParser();
				try {
					while((line = reader.readLine()) != null){
						object1 = (JsonObject) parser.parse(line);
						Student_Number = object1.get("studentnumber").getAsString();
						Late_Time = object1.get("latetime").getAsString();
						Name = object1.get("name").getAsString();
//						Log.i("main", line);
					}
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			
				return null;
			}

			
	
			//AsyncTask<Params, Progress, Result>�رյ�ʱ��Ҳ���Ǻ�̨�����������һ��AsyncTask����һ��class���Ȼ������������岻��
//			@Override
//			protected void onPostExecute(Void result) {
//				super.onPostExecute(result);
//				name.setText(Name);
//				student_number.setText(Student_Number);
//				lateTime.setText(Late_Time);
//				Toast.makeText(context, "heheheheheheheh", Toast.LENGTH_SHORT).show();
//			}
		};
		re.execute();
	}
//

//public void read(){
//	AsyncTask<Void, String, Void> re = new AsyncTask<Void, String, Void>() {
//
//
//		@Override
//		protected Void doInBackground(Void... arg0) {
//			
//			JsonObject object = new JsonObject();
//			object.addProperty("bool", "LateTime");
//			String studentnumber = pref.getString("UserNumber", "");
////			Toast.makeText(context, studentnumber, Toast.LENGTH_SHORT).show();
//			object.addProperty("Studentnumber",studentnumber );//����ѧ��������ѯ
//			String WriterLine = object.toString();
//			try {
//				writer.write(WriterLine.replace("\n", " ")+"\n");
//				writer.flush();
////				writer.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			
//			parser = new JsonParser();
//			try {
//				while((line = reader.readLine()) != null){
//					object1 = (JsonObject) parser.parse(line);
//					Student_Number = object1.get("studentnumber").getAsString();
//					Late_Time = object1.get("latetime").getAsString();
//					Name = object1.get("name").getAsString();
////					Log.i("main", line);
//				}
//				reader.close();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		
//			return null;
//		}
//
//		
//		@Override
//		protected void onCancelled() {
//			super.onCancelled();
//		}
//		
//		@Override
//		protected void onPostExecute(Void result) {
//			super.onPostExecute(result);
//			name.setText(Name);
//			student_number.setText(Student_Number);
//			lateTime.setText(Late_Time);
//			Toast.makeText(context, "heheheheheheheh", Toast.LENGTH_SHORT).show();
//		}
//	};
//	re.execute();
//}



	private void connect() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {
			//�����ʱ����
			@Override
			protected Void doInBackground(Void... params) {

				try {
					socket = new Socket(ip,12345);
					reader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
					//�����ݸ�������������ȷ���ǲ�ѯ���ݵ�ҳ��
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				} catch (UnknownHostException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

//				JsonParser parser = new JsonParser();
//				try {
//					while((line = reader.readLine()) != null){
//						JsonObject object1 = (JsonObject) parser.parse(line);
//						Student_Number = object1.get("studentnumber").getAsString();
//						Late_Time = object1.get("latetime").getAsString();
//						Name = object1.get("name").getAsString();
////						Log.i("main", line);
//					}
////					reader.close();
//				} catch (IOException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				return null;
			}

//			@Override
//			protected void onPostExecute(Void result) {
//				super.onPostExecute(result);
//				name.setText(B.toString());
//				Log.i("main", B+"hhhhhhhhhhhhhhh");
//			}
		};
		//ִ��
		con.execute();
	}

	private void Json_Put() {
		JsonObject object = new JsonObject();
		object.addProperty("bool", "LateTime");
		String studentnumber = pref.getString("UserNumber", "");
//		Toast.makeText(context, studentnumber, Toast.LENGTH_SHORT).show();
		object.addProperty("Studentnumber",studentnumber );//����ѧ��������ѯ
		String WriterLine = object.toString();
		try {
			writer.write(WriterLine.replace("\n", " ")+"\n");
			writer.flush();
//			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		read();
	}
	private void init() {
		name = (TextView) findViewById(R.id.text_name);
		student_number = (TextView) findViewById(R.id.text_studentnumber);
		lateTime = (TextView) findViewById(R.id.text_latetime);
		bt = (Button) findViewById(R.id.latetimeshow);
		//��ʼ���������ݿ�
		pref = getSharedPreferences("user", MODE_PRIVATE);
		
		//����͸����
		View v = findViewById(R.id.latetime_background);
		v.getBackground().setAlpha(100);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i("mian", Student_Number+"66666666666666666666666666666666666666666666");
		Log.i("mian", Late_Time+"66666666666666666666666666666666666666666666");
		Log.i("mian", Name+"66666666666666666666666666666666666666666666");
		try {
			if(take){
				writer.close();
				reader.close();
			}
			
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	


}
