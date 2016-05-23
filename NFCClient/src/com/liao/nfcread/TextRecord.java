/**
 * ��NDEFת��TextRecord
 * ��ͨ������getText�õ��ַ���
 */
package com.liao.nfcread;

import java.util.Arrays;

import android.nfc.NdefRecord;

public class TextRecord {
	private String mText;
	private TextRecord(String text){
		mText = text;
	}
	
	public String getText(){
		return mText;
	}
	//��NDEF���ݷ�װ��TextRecord
	public static TextRecord parse(NdefRecord record){
		//�Ƿ�ΪNDEF����
		if(record.getTnf() != NdefRecord.TNF_WELL_KNOWN){
			return null;
		}
		if(!Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)){
			return null;
		}
		//��NDEF
		try {
			//��������ֽ���
			byte[] payload = record.getPayload();
			//�ж��ֽ�����������
			String textEncoding = ((payload[0] & 0x80) == 0)?"UTF-8":"UTF-16";
			int languageCodeLength = payload[0] & 0x3f;
			//�������
			String text = new String(payload, languageCodeLength+1, payload.length-languageCodeLength-1,textEncoding);
			return new TextRecord(text);
		} catch (Exception e) {
			throw new IllegalArgumentException();
		}
		
	}
}
