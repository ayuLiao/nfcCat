package com.liao.nfcclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class Login extends Activity{
	//ɾ��
	ImageView user_del;
	ImageView password_del;
	
	//��¼ע�ᰴť
	Button login;
	Button reg;//ע��
	
	EditText usernumber;
	EditText userpassword;
//-----------------
	Context context;
	SharedPreferences pref;
	Editor editor;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		this.context = this;
		init();//��ʼ��
		pref = getSharedPreferences("user", MODE_PRIVATE);
		editor = pref.edit();
		usernumber.setText(pref.getString("UserNumber", ""));
		userpassword.setText(pref.getString("UserPassword", ""));
		button_listener();
	}
	private void button_listener() {
		login.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String user_1 = usernumber.getText().toString();//�Լ�����ʱ
				String user_2 = pref.getString("UserNumber", "");//�����ݿ��д�����������Ա�
				String password_1 = userpassword.getText().toString();
				String password_2 = pref.getString("UserPassword", "");
				if(user_2.equals("")||password_2.equals("")){//���ݿ��û���Ϊ�գ���ʾû��ע��
					Toast.makeText(context, "�û������ڣ������ע��", Toast.LENGTH_SHORT).show();
				}else if(user_1.equals("")||password_1.equals("")){
					Toast.makeText(context, "�������û���������", Toast.LENGTH_SHORT).show();
				}else if(!user_1.equals(user_2)){//�û�������ͬ
					Toast.makeText(context, "�û�������", Toast.LENGTH_SHORT).show();
				}else if(!password_1.equals(password_2)){
					Toast.makeText(context, "�������", Toast.LENGTH_SHORT).show();
				}else{
					startActivity(new Intent(context,MainActivity.class));
					finish();//���ɷ���
				}
				
			}
			
			
		});
		
		reg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,registration.class));
//				finish();
			}
		});
	}
	private void init() {
		user_del = (ImageView) findViewById(R.id.user_del);
		password_del = (ImageView) findViewById(R.id.password_del);
		usernumber = (EditText) findViewById(R.id.usernumber);
		userpassword = (EditText) findViewById(R.id.userpassword);
		login = (Button) findViewById(R.id.id_login);
		reg = (Button) findViewById(R.id.id_zhuce);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
