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
	//删除
	ImageView user_del;
	ImageView password_del;
	
	//登录注册按钮
	Button login;
	Button reg;//注册
	
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
		init();//初始化
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
				String user_1 = usernumber.getText().toString();//自己输入时
				String user_2 = pref.getString("UserNumber", "");//跟数据库中储存的密码做对比
				String password_1 = userpassword.getText().toString();
				String password_2 = pref.getString("UserPassword", "");
				if(user_2.equals("")||password_2.equals("")){//数据库用户名为空，表示没有注册
					Toast.makeText(context, "用户不存在，请进行注册", Toast.LENGTH_SHORT).show();
				}else if(user_1.equals("")||password_1.equals("")){
					Toast.makeText(context, "请输入用户名或密码", Toast.LENGTH_SHORT).show();
				}else if(!user_1.equals(user_2)){//用户名不相同
					Toast.makeText(context, "用户名错误", Toast.LENGTH_SHORT).show();
				}else if(!password_1.equals(password_2)){
					Toast.makeText(context, "密码错误", Toast.LENGTH_SHORT).show();
				}else{
					startActivity(new Intent(context,MainActivity.class));
					finish();//不可返回
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
