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
	private String mTagText;//�ж��Ƿ�Ҫ�������ݿ⣬�õ�nfc�е����ݾ�����

//===============================
	Context context;
	Socket socket;
	BufferedWriter writer;
	BufferedReader reader;
	String ip;
//----------�������ݿ�------------
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
		readNFCTag();//��ȡNFC Tag
		if(mTagText != null){
			//�򿪱������ݿ�
			pref = getSharedPreferences("user", MODE_PRIVATE);
			//��ȡ�������ݿ��е�ѧ������Ϣ������������У���Ϊǩ��ʱ�Ĳ���
			String value = pref.getString("UserNumber", "");
			JsonObject object = new JsonObject();
			object.addProperty("bool", "Sing_In");
			object.addProperty("Studentnumber", value);//��ѧ���Ŵ������˽��в�ѯ����
			
			final String WriterLine = object.toString();
			connect();//�������ݿ⣬��ǩ����Ϣ�������ݿ�
			new AlertDialog.Builder(context)
				.setTitle("ǩ��")
				.setMessage("���ǩ��")
				.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
					//�����ݴ���������
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
					Toast.makeText(context, "���������������ʧ��", Toast.LENGTH_SHORT).show();
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				return null;
			}
			
			//����UI
			@Override
			protected void onProgressUpdate(String... values) {
				super.onProgressUpdate(values);
			}
		};
		con.execute();
	}
	private void readNFCTag() {
		if(NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction())){
			//��ȡNFC��ǩ���ݣ�ԭʼ���ݣ�����һ���ֽ���
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
					//��NdefRecordת��TextRecord
					TextRecord textRecord = TextRecord.parse(record);
					//*********�õ����е�����*****************
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
