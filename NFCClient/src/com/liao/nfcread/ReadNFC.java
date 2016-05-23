package com.liao.nfcread;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.liao.AppVariables.Variables;
import com.liao.nfcclient.R;

public class ReadNFC extends Activity{
	
	private TextView mTagContent;
	private Tag mDatectedTag;
	private String mTagText;//判断是否要连接数据库，得到nfc中的内容就连接

//===============================
	Context context;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	String ip;
//----------本地数据库------------
	SharedPreferences pref;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.readnfc);
		ip = new Variables().getIP();
		this.context = this;
		init();
	}
	private void init() {
		mTagContent = (TextView) findViewById(R.id.tv_nfc);
		mDatectedTag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
		Ndef ndef = Ndef.get(mDatectedTag);
		readNFCTag();//读取NFC Tag
		if(mTagText != null){
			//打开本地数据库
			pref = getSharedPreferences("user", MODE_PRIVATE);
			//读取本地数据库中的学生号信息，传入服务器中，作为签到时的查找
			String value = pref.getString("UserNumber", "");
			JsonObject object = new JsonObject();
			object.addProperty("bool", "Sing_In");
			object.addProperty("Studentnumber", value);//以学生号传入服务端进行查询更新
			
			final String WriterLine = object.toString();
			connect();//连接数据库，将签到信息传到数据库
			new AlertDialog.Builder(context)
				.setTitle("签到")
				.setMessage("点击签到")
				.setPositiveButton("确定",new DialogInterface.OnClickListener() {
					//将数据传到服务器
					@Override
					public void onClick(DialogInterface dialog, int which) {
						try {
							writer.write(WriterLine.replace("\n", " ")+"\n");
							writer.flush();
							writer.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
				} )
				.show();
		}
		mTagContent.setText(mTagText);
	}
	private void connect() {
		AsyncTask<Void, String, Void> con = new AsyncTask<Void, String, Void>() {

			@Override
			protected Void doInBackground(Void... params) {
				try {
					socket = new Socket(ip,12345);
					writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
				
				} catch (UnknownHostException e) {
					Toast.makeText(context, "与服务器建立连接失败", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
			
			//更新UI
			@Override
			protected void onProgressUpdate(String... values) {
				super.onProgressUpdate(values);
			}
		};
		con.execute();
	}
	private void readNFCTag() {
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
			//获取NFC标签数据，原始数据，就是一个字节流
			Parcelable[] rawMsgs = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage msgs[] = null;
			if(rawMsgs != null){
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			}
			try {
				if (msgs != null) {
					NdefRecord record = msgs[0].getRecords()[0];
					//将NdefRecord转成TextRecord
					TextRecord textRecord = TextRecord.parse(record);
					//*********得到其中的内容*****************
					mTagText = textRecord.getText();
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
	
	@Override
	protected void onDestroy() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		super.onDestroy();
	}
}
