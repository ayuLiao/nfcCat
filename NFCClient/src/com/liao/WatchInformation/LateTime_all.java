package com.liao.WatchInformation;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.liao.nfcclient.R;

public class LateTime_all extends Activity {
	
	Context context;
	private ListView listView;
	private ArrayAdapter<String> adapter;
	String[] re= {"haha","haha","hahah","hahah"};//สýพÝิด
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.latetime_all);
		this.context = this;
		init();
	}
	private void init() {
		listView = (ListView) findViewById(R.id.listView);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, re);
		listView.setAdapter(adapter);
	}
}
