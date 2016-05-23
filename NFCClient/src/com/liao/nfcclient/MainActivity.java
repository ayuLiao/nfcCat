package com.liao.nfcclient;

import com.liao.WatchInformation.LateTime;
import com.liao.nfcread.choose;
import com.liao.person.person;
import com.liao.time.MyCount;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity{

//	private GridView gridView;
//	private List<Map<String, Object>>dateList;
//	//-------------------------------
//	//数据源
//	private int[] icon = {R.drawable.sign_in,R.drawable.ic_launcher,
//			R.drawable.time,R.drawable.people};
//	private String[] iconName = {"签到","查看","时间范围","个人"};
//	private SimpleAdapter adapter;
	//--------------------------------
	Context context;
	
//---------------------------
	ImageButton sign_in;
	ImageButton watch;
	ImageButton time;
	ImageButton main_person;	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		this.context = this;
//		gridView = (GridView) findViewById(R.id.gridView);
//		dateList = new ArrayList<Map<String,Object>>();
//		adapter = new SimpleAdapter(this, getData(), R.layout.gridview_item,
//				new String[]{"image","text"}, new int[]{R.id.image,R.id.text});
//		//加载适配器
//		gridView.setAdapter(adapter);
//		gridView.setOnItemClickListener(this);
		
		//初始化
		init();
		
		//四个按钮的监听事件
		sign_in_listen();
		watch_listen();
		time_listen();
		persion_listen();
		
	}
	private void persion_listen() {
		main_person.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(context,person.class));
			}
		});
	}
	private void time_listen() {
		time.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(context,MyCount.class));
			}
		});
	}
	private void watch_listen() {
		watch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(context,LateTime.class));
			}
		});
	}
	private void sign_in_listen() {
		sign_in.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				startActivity(new Intent(context,choose.class));
			}
		});
	}
	private void init() {
		sign_in = (ImageButton) findViewById(R.id.sign_in);
		watch = (ImageButton) findViewById(R.id.watch);
		time = (ImageButton) findViewById(R.id.time);
		main_person = (ImageButton) findViewById(R.id.persion);
	}

//	private List<Map<String, Object>> getData() {
//		for(int i=0;i<icon.length;i++){
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("image", icon[i]);
//			map.put("text", iconName[i]);
//			dateList.add(map);
//		}
//		return dateList;
//	}
//
//	@Override
//	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//		Toast.makeText(this, iconName[position]+"  "+position, Toast.LENGTH_SHORT).show();
//		switch (position) {
//		case 0:
//			//签到
//			startActivity(new Intent(context,choose.class));
//			break;
//		case 1:
//			//查看
//			Log.i("main", "1111111111111111111111111111111");
//			startActivity(new Intent(context,LateTime.class));
//			break;
//		case 2:
//			//时间范围
//			startActivity(new Intent(context,choose.class));
//			break;
//		case 3:
//			//个人
//			startActivity(new Intent(context,choose.class));
//			break;
//
//		}
//	}

}
