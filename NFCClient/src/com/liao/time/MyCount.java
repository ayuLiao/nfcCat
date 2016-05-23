package com.liao.time;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liao.AppVariables.Variables;
import com.liao.nfcclient.MydbHepler;
import com.liao.nfcclient.R;
import com.liao.time.ArcMenu.OnMenuItemClikListener;


public class MyCount extends Activity implements OnItemClickListener{

	private ArcMenu mArcMenu;//�˵��ؼ�
	private ListView listView;
	private Context context;
	private TextView data;//ʱ��
	private String time_data;//ʱ��
	//--------�������ݿ�---------
	SQLiteDatabase db;//���ݿ�
	MydbHepler dbHepler;//���ݿ������
	Cursor c;//SQLite��ѯ
	SimpleCursorAdapter adapter;//Cursor������

	String[] Alist = {"��ʼ��ʱ","ɾ��"};
	
	private final int requestCode = 1500;//��ת��һ��Activity�Ļش�ֵ
	
	//-------------��ʱ---------------
	private Count clock;
	private long millisInFuture;
	
	//------------------���ӷ�����--------
	Socket socket;
	BufferedWriter writer;//�������д����
	private String ip;
	
	//��ʱ����ʼʱ����������������Ϣ��time_start
	JsonObject object; 
	String WriterLine;
	//��ʱ������ʱ���͸�����������Ϣ��time_over
	JsonObject object_over;
	String WriterLine_over;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.time_show);

		this.context = this;
		ip = new Variables().getIP();
		Log.i("main","111111111111111111111111111111111111111111111111111");
		init();//��ʼ��
		Log.i("main","222222222222222222222222222222222222222222");
		initEvent();//����listView�����¼�����������ʱ���˵����Ӳ˵�����
		Log.i("main","33333333333333333333333333333333333333333333333333");
		//SQLite���ݿ��ʼ��,Time.dbΪ���ݿ�����
		dbHepler = new MydbHepler(context, "Time.db");
		Log.i("main","44444444444444444444444444444444444444444");
		ListQuery();//���ݿ��У����ݱ������չʾ��listview��
		Log.i("main","55555555555555555555555555555555555555555555555555");
		listView.setOnItemClickListener(this);//ListView��item����ļ���
		Log.i("main","6666666666666666666666666666666666666666666666666666666");
	}
	private void ListQuery() {
		db = dbHepler.getWritableDatabase();
		c = db.rawQuery("select * from time", null);
		adapter = new SimpleCursorAdapter(this, R.layout.item, c, new String[]{"number"}, new int[]{R.id.time_item});
		listView.setAdapter(adapter);
	}
	private void init() {
		listView = (ListView) findViewById(R.id.time_list);
		mArcMenu = (ArcMenu) findViewById(R.id.time_menu);
		/**
		 * ���ñ���ͼƬ��͸����
		 * �ҵ�Ҫ���ñ�����layout��id
		 * 0~255͸����ֵ
		 */
		View v = findViewById(R.id.time_back);
		v.getBackground().setAlpha(100);
	}

	private void initEvent() {

		listView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView arg0, int arg1) {
				// TODO Auto-generated method stub
				if(mArcMenu.isOpen())
				{
					mArcMenu.toggleMenu(600);

				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, 
					int visibleItem, int totalItemCount) {
			}
		});

		//ʹ����ArcMenu�Ļص��ӿ�
		//����Ӳ˵��ᷢ��������
		mArcMenu.setOnMenuItemClikListener(new OnMenuItemClikListener() {

			

			@Override
			public void onClick(View view, int pos) {
				switch (pos) {
				case 1://���ʱ�䰴ť
					Intent intent = new Intent(MyCount.this,AddTime.class);
					//startActivityForResult��ش�requestCode,�����ض���Activity���Ը��߲�ͬ��requestCode�����в�ͬ�Ĵ������
					startActivityForResult(intent, requestCode);
					break;

				case 2://���ʱ�䰴ť
					
					new AlertDialog.Builder(context)
					 .setTitle("���")
					 .setMessage("ȷ�����ʱ���?")
					 .setNegativeButton("ȡ��", null)
					 .setPositiveButton("���", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) {
							db = dbHepler.getWritableDatabase();
							db.delete("time", null, null);
							ListQuery();
						}
					}).show();

					
					
					break;
				}
			}
		});

	}
	/**
     * ���е�Activity����ķ���ֵ�������������������
     * requestCode:    ��ʾ��������һ��Activityʱ����ȥ��requestCodeֵ
     * resultCode����ʾ�����������Activity�ش�ֵʱ��resultCodeֵ
     * data����ʾ�����������Activity�ش�������Intent����
     */
	
	//���յ���ǰActivity��ת��Ŀ��Activity�رպ�Ļش�ֵ
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if(requestCode == 1500){
			ListQuery();
		}
		
	}
	
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		data = (TextView) view.findViewById(R.id.time_item);
		time_data = (String) data.getText();
		
//		long l = Long.parseLong(time_data);
//		Toast.makeText(context, l+"", Toast.LENGTH_SHORT).show();
		
		millisInFuture = Long.parseLong(time_data)*1000;
		Toast.makeText(context, millisInFuture+"", Toast.LENGTH_SHORT).show();
		choose();//ѡ�񵯿�ɾ������ʼ��ʱ
	}
	private void choose() {
		//�����onClickListenerҪ��DialogInterface����Ȼ���button��onClickListener��ͻ������
		//Alist---����Դ
		Dialog alertDialog = new AlertDialog.Builder(this).setTitle("ѡ��")
				.setItems(Alist, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							//��ʱʱ�䵽�����ӷ��������������д�����ݣ��ı����ݿ��һ������
							connect();//��ʼʱ��������ǩ����Ϊ����ǩ��
							time();//��ʼ��ʱ
							break;
						case 1:
							dele();//����洢ʱ��ı������ݿ�
							break;
						}

					}

				}).create();
		alertDialog.show();
	}
	//��ʱ
	private void time() {
		//��ʼ��ʱ����ʱʱ��ΪmillisInFuture
		clock = new Count(millisInFuture, 1000);//onTick������ʱ�䣬1��
		clock.start();
	}
	//��ձ������ݿ�
	private void dele() {
		db = dbHepler.getWritableDatabase();
		Cursor del = db.query("time", null, "number = ?", new String[]{time_data}, null, null, null);
		if(del != null){
			String[] COL = del.getColumnNames();
			while (del.moveToNext()) {
				for(String COLS : COL){
					String Dele = del.getString(del.getColumnIndex(COLS));
					db.delete("time", "number = ?", new String[]{Dele});
				}
			}
		}
		ListQuery();
	}
	
	
	//��ʱ��
	class Count extends CountDownTimer{

		public Count(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}
		//��ʱ������ʱҪ��������
		@Override
		public void onFinish() {
			//ʱ�䵽���ر�ǩ������
//			connect();//��ʱʱ�䵽�����ӷ��������������д�����ݣ��ı����ݿ��һ������
//			time_writer();
			connect1();
		}

		//����ʱ��ʼʱҪ�������飬����m��ֱ����ɵ�ʱ��
		@Override
		public void onTick(long m) {
//			Toast.makeText(context, m / 1000+"", Toast.LENGTH_SHORT).show();
		}

	}
	//�����������
	private void connect() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {

			//��ʱ����
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
				
				 object = new JsonObject();
				object.addProperty("bool", "time_start");
				 WriterLine = object.toString();
				
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

	private void connect1() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {

			//��ʱ����
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
				
				 object = new JsonObject();
				object.addProperty("bool", "time_over");
				 WriterLine = object.toString();
				
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
	
	
//	public void time_writer(){
//		object_over = new JsonObject();
//		object_over.addProperty("bool", "time_over");
//		WriterLine_over = object_over.toString();
//		try {
//		writer.write(WriterLine_over.replace("\n", " ")+"\n");
//		writer.flush();
//		writer.close();
//	} catch (IOException e) {
//		e.printStackTrace();
//	}
//	}
	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		try {
//			socket.close();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
