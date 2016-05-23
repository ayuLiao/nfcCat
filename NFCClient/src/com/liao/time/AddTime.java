package com.liao.time;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;







import android.widget.Toast;

import com.liao.nfcclient.MydbHepler;
import com.liao.nfcclient.R;

public class AddTime extends Activity{

	private Context context;
	private TextView textView;
	private Button button;
	private EditText editText;
	MydbHepler dhHepler;//数据库相关类
	SQLiteDatabase db;//数据库
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//无标题栏
		setContentView(R.layout.addtime);
		
		this.context = this;
		init();
		AddTime();
	}

	private void AddTime() {
		db = dhHepler.getWritableDatabase();
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String st = editText.getText().toString();
				ContentValues values = new ContentValues();
				values.put("number", st);//time数据表中的表项
				db.insert("time", null, values);
				values.clear();
				Toast.makeText(context, st+"已经添加成功", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void init() {
		textView = (TextView) findViewById(R.id.time_add_textView);
		button = (Button) findViewById(R.id.time_add_button);
		editText = (EditText) findViewById(R.id.time_add_editText);
		dhHepler = new MydbHepler(context, "Time.db");//Time.db是数据库的名称，而不是数据表的名称
	}
}
