package com.liao.person;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.hardware.Camera.ShutterCallback;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liao.AppVariables.Variables;
import com.liao.nfcclient.R;

public class person extends Activity{


	private Context context;

	private EditText name;
	private EditText studentNumber;
	private EditText studentPhone;
	private EditText password;
	private Button cancel;
	private Button save;

	private String Name;
	private String StudentNumber;
	private String StudentPhone;
	private String Password;

	//-----轻量级数据库-------
	SharedPreferences pref;
	Editor editor;

	//----连接服务器-------
	Socket socket;
	BufferedWriter writer;
	private String ip;
	private String WriterLine;//写入的内容
	
	JsonObject object;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person);
		this.context = this;
		ip = new Variables().getIP();
		init();
		show();//从SharedPreferences中取出数据并显示出来
		watch();//EditText被编辑了
		
		bt_save();//点击保存，判断数据是否改变，将最少的数据写入服务器
		bt_cancel();//cancel，返回上一个Activity，并销毁这个activity
		change();//改变数据----重新将新的数据写入SharePredferences，更新服务器中数据库的数据
	}

	private void bt_save() {
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				is_save();//save，先判断有没有数据改变，将改变的数据发送到
				
			}

			
		});
		
	}
	
	
	//Activity关闭的时候
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		try {
//			writer.close();
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}

	private void watch() {
		name.addTextChangedListener(watcher);
		password.addTextChangedListener(watcher);
		studentNumber.addTextChangedListener(watcher);
		studentPhone.addTextChangedListener(watcher);
	}

	TextWatcher watcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			//edittext编辑框改变时，才可以点击
			save.setEnabled(true);
			//可以点击是，button中的文字改为白色
			ColorStateList whiteColor = getResources().getColorStateList(R.color.white);
			save.setTextColor(whiteColor);
		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
				int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub

		}
	};

	private void bt_cancel() {

	}

	private void is_save() {
		object = new JsonObject();
		object.addProperty("bool", "person");
		//接收改变的
		String change_name = name.getText().toString();
		String change_passwrod = password.getText().toString();
		String change_number = studentNumber.getText().toString();
		String change_phone = studentPhone.getText().toString();

		if(change_name.equals(Name)  && change_passwrod.equals(Password) &&
				change_number.equals(StudentNumber) && change_phone.equals(StudentPhone)){
			//没有改变
			Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
		}else{
			object.addProperty("Studentnumber", change_number);
			editor.putString("UserNumber", change_number);
			
			object.addProperty("Studentname", change_name);
			editor.putString("UserName", change_name);
			
			object.addProperty("Phonenumber", change_phone);
			editor.putString("UserPhone", change_phone);
			
			object.addProperty("Password", change_passwrod);
			editor.putString("UserPassword", change_passwrod);
			editor.commit();
			WriterLine = object.toString();
//			Toast.makeText(context, WriterLine, Toast.LENGTH_SHORT).show();
			Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
			connect();
		}
		
	}

	
	private void connect() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {

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
	//从SharedPreferences中取出数据并显示出来
	private void show() {
		name.setText(Name);//姓名
		studentNumber.setText(StudentNumber);
		studentPhone.setText(StudentPhone);
		password.setText(Password);
	}


	//改变数据----重新将新的数据写入SharePredferences，更新服务器中数据库的数据
	private void change() {

	}
	private void init() {
		name = (EditText) findViewById(R.id.person_name);
		studentNumber = (EditText) findViewById(R.id.person_studentNumber);
		studentNumber.setEnabled(false);//不可编辑,因为要根据学号查找
		studentPhone = (EditText) findViewById(R.id.person_phoneNumber);
		password = (EditText) findViewById(R.id.person_password);
		cancel = (Button) findViewById(R.id.person_cancel);
		pref = getSharedPreferences("user", MODE_PRIVATE);
		editor = pref.edit();
		save = (Button) findViewById(R.id.person_save);
		save.setEnabled(false);//一开始不可以点击
		//---出数据库中取出数据
		Name = pref.getString("UserName", "");
		StudentNumber = pref.getString("UserNumber", "");
		StudentPhone = pref.getString("UserPhone", "");
		Password = pref.getString("UserPassword", "");
	}

}
