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

	//-----���������ݿ�-------
	SharedPreferences pref;
	Editor editor;

	//----���ӷ�����-------
	Socket socket;
	BufferedWriter writer;
	private String ip;
	private String WriterLine;//д�������
	
	JsonObject object;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.person);
		this.context = this;
		ip = new Variables().getIP();
		init();
		show();//��SharedPreferences��ȡ�����ݲ���ʾ����
		watch();//EditText���༭��
		
		bt_save();//������棬�ж������Ƿ�ı䣬�����ٵ�����д�������
		bt_cancel();//cancel��������һ��Activity�����������activity
		change();//�ı�����----���½��µ�����д��SharePredferences�����·����������ݿ������
	}

	private void bt_save() {
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				is_save();//save�����ж���û�����ݸı䣬���ı�����ݷ��͵�
				
			}

			
		});
		
	}
	
	
	//Activity�رյ�ʱ��
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
			//edittext�༭��ı�ʱ���ſ��Ե��
			save.setEnabled(true);
			//���Ե���ǣ�button�е����ָ�Ϊ��ɫ
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
		//���ոı��
		String change_name = name.getText().toString();
		String change_passwrod = password.getText().toString();
		String change_number = studentNumber.getText().toString();
		String change_phone = studentPhone.getText().toString();

		if(change_name.equals(Name)  && change_passwrod.equals(Password) &&
				change_number.equals(StudentNumber) && change_phone.equals(StudentPhone)){
			//û�иı�
			Toast.makeText(context, "����ɹ�", Toast.LENGTH_SHORT).show();
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
			Toast.makeText(context, "����ɹ�", Toast.LENGTH_SHORT).show();
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
	//��SharedPreferences��ȡ�����ݲ���ʾ����
	private void show() {
		name.setText(Name);//����
		studentNumber.setText(StudentNumber);
		studentPhone.setText(StudentPhone);
		password.setText(Password);
	}


	//�ı�����----���½��µ�����д��SharePredferences�����·����������ݿ������
	private void change() {

	}
	private void init() {
		name = (EditText) findViewById(R.id.person_name);
		studentNumber = (EditText) findViewById(R.id.person_studentNumber);
		studentNumber.setEnabled(false);//���ɱ༭,��ΪҪ����ѧ�Ų���
		studentPhone = (EditText) findViewById(R.id.person_phoneNumber);
		password = (EditText) findViewById(R.id.person_password);
		cancel = (Button) findViewById(R.id.person_cancel);
		pref = getSharedPreferences("user", MODE_PRIVATE);
		editor = pref.edit();
		save = (Button) findViewById(R.id.person_save);
		save.setEnabled(false);//һ��ʼ�����Ե��
		//---�����ݿ���ȡ������
		Name = pref.getString("UserName", "");
		StudentNumber = pref.getString("UserNumber", "");
		StudentPhone = pref.getString("UserPhone", "");
		Password = pref.getString("UserPassword", "");
	}

}
