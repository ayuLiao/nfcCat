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
	MydbHepler dhHepler;//���ݿ������
	SQLiteDatabase db;//���ݿ�
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);//�ޱ�����
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
				values.put("number", st);//time���ݱ��еı���
				db.insert("time", null, values);
				values.clear();
				Toast.makeText(context, st+"�Ѿ���ӳɹ�", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void init() {
		textView = (TextView) findViewById(R.id.time_add_textView);
		button = (Button) findViewById(R.id.time_add_button);
		editText = (EditText) findViewById(R.id.time_add_editText);
		dhHepler = new MydbHepler(context, "Time.db");//Time.db�����ݿ�����ƣ����������ݱ������
	}
}
